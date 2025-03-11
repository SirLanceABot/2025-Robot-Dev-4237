package frc.robot;

import java.lang.invoke.MethodHandles;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.commands.PathfindingCommand;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.CommandsManager;
import frc.robot.controls.DriverBindings;
import frc.robot.controls.OperatorBindings;
//import frc.robot.elastic.AutonomousTab;
import frc.robot.elastic.ElasticLance;
import frc.robot.loggers.DataLogFile;
import frc.robot.motors.MotorControllerLance;
import frc.robot.pathplanner.PathPlannerLance;
import frc.robot.subsystems.LEDs;


public class Robot extends TimedRobot 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final RobotContainer robotContainer;
    private final LEDs leds;
    private Command autonomousCommand = null;
    private TestMode testMode = null;
    private boolean isRedAlliance;
    private Command currentCommand = null;
    private Command previousCommand = null;
    private boolean autonomousRun = false;
    // private Drivetrain drivetrain;



    /** 
     * Uses the default access modifier so that the Robot object can only be constructed in this same package.
     */
    Robot() 
    {
        //Configure the loggers
        DataLogFile.config();

        //Configure RobotContainer
        robotContainer = new RobotContainer();
        leds = robotContainer.getLEDs();
        // drivetrain = robotContainer.getDrivetrain();
        //Configure commands
        CommandsManager.createCommands(robotContainer);

        //Configure Bindings
        DriverBindings.createBindings(robotContainer);
        OperatorBindings.createBindings(robotContainer);

        PathPlannerLance.configPathPlanner(robotContainer);
        ElasticLance.configElastic(robotContainer);
        // TODO - Add these PathPlanner warmup commands
        FollowPathCommand.warmupCommand().schedule();
        PathfindingCommand.warmupCommand().schedule();
    }

    @Override
    public void robotPeriodic() 
    {
        // Run periodic tasks
        PeriodicTask.runAllPeriodicTasks();
        ElasticLance.sendDataToSmartDashboard();

        // // SmartDashboard.putNumber("Speed", drivetrain.getState().Speeds.getVelocity());
        // if(robotContainer.getDrivetrain() != null)
        //     SmartDashboard.putNumber("Speed", robotContainer.getDrivetrain().getState().Speeds.vxMetersPerSecond);

        // System.out.println(robotContainer.getDrivetrain().getPigeon2().getYaw().getValueAsDouble());
        // isRedAlliance = robotContainer.getDrivetrain().isRedAllianceSupplier().getAsBoolean();
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();

        //Display poseEstimator pose
        // System.out.println("X = " + ((int)(robotContainer.getPoseEstimator().getEstimatedPose().getX() * 100)) / 100.0 + "   Y = " + ((int)(robotContainer.getPoseEstimator().getEstimatedPose().getY() * 100)) / 100.0);
    }

    /**
     * This method runs one time after the driver station connects.
     */
    @Override
    public void driverStationConnected()
    {}

    /**
     * This method runs one time when the robot enters disabled mode.
     */
    @Override
    public void disabledInit() 
    {
        if (!autonomousRun)
        {
            previousCommand = ElasticLance.getAutonomousCommand();
            
        }
    }

    /**
     * This method runs periodically (20ms) during disabled mode.
     */
    @Override
    public void disabledPeriodic() 
    {
        if (!autonomousRun && robotContainer.getDrivetrain() != null)
        {
            // robotContainer.getDrivetrain().seedFieldCentric();
            currentCommand = ElasticLance.getAutonomousCommand();
            if(currentCommand != previousCommand)
            {
                ElasticLance.resetRobot(robotContainer.getDrivetrain().getPigeon2());
                autonomousCommand = currentCommand;
                // ElasticLance.updateAutoField();
            }
            previousCommand = currentCommand;
            
            // leds.setColorSolidCommand(Color.kBlue);

            // System.out.println(currentCommand.getName());
        }    
    }

    /**
     * This method runs one time when the robot exits disabled mode.
     */
    @Override
    public void disabledExit() 
    {}

    /**
     * This method runs one time when the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() 
    {
        // DataLogManager.start();
        //autonomousCommand = ElasticLance.getAutonomousCommand();

        // PathPlannerAuto path = new PathPlannerAuto("Testing");
        Command path = AutoBuilder.buildAuto("Straight Middle");
        try {
            PathPlannerPath ppPath = PathPlannerPath.fromPathFile("Straight Middle");
            Pose2d initialPose = ppPath.getStartingHolonomicPose().orElse(new Pose2d());
            robotContainer.getPoseEstimator().resetPose(initialPose);
        }
        catch (Exception e)
        {
            System.out.println("Path planner loading file error");
        }


        path.schedule();
        System.out.println("Scheduled Auto Command");

        // if(autonomousCommand != null) 
        // {
        //     autonomousCommand.schedule();
        // }

    }

    /**
     * This method runs periodically (20ms) during autonomous mode.
     */
    @Override
    public void autonomousPeriodic() 
    {}

    /**
     * This method runs one time when the robot exits autonomous mode.
     */
    @Override
    public void autonomousExit() 
    {
        autonomousRun = true;
    }

    /**
     * This method runs one time when the robot enters teleop mode.
     */
    @Override
    public void teleopInit() 
    {
        DataLogManager.start();
        if(autonomousCommand != null) 
        {
            autonomousCommand.cancel();
            autonomousCommand = null;
        }

        autonomousRun = false;
    }

    /**
     * This method runs periodically (20ms) during teleop mode.
     */
    @Override
    public void teleopPeriodic() 
    {
        // System.out.print("Axis : " + robotContainer.getDriverController().getRawAxis(1) + "      ");

        // leds.setColorBlinkCommand(Color.kRed);
    }

    /**
     * This method runs one time when the robot exits teleop mode.
     */
    @Override
    public void teleopExit() 
    {
        MotorControllerLance.logAllStickyFaults();
        DataLogManager.stop();
    }

    /**
     * This method runs one time when the robot enters test mode.
     */
    @Override
    public void testInit() 
    {
        CommandScheduler.getInstance().cancelAll();

        // Create a TestMode object to test one team members code.
        testMode = new TestMode(robotContainer);

        testMode.init();
    }

    /**
     * This method runs periodically (20ms) during test mode.
     */
    @Override
    public void testPeriodic() 
    {
        testMode.periodic();
    }
    
    /**
     * This method runs one time when the robot exits test mode.
     */
    @Override
    public void testExit() 
    {
        testMode.exit();

        // Set the TestMode object to null so that garbage collection will remove the object.
        testMode = null;
    }
}
