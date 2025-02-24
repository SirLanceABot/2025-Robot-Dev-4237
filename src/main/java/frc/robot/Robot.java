package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.CommandsManager;
import frc.robot.controls.DriverBindings;
import frc.robot.controls.OperatorBindings;
import frc.robot.elastic.AutonomousTab;
import frc.robot.loggers.DataLogFile;
import frc.robot.motors.MotorControllerLance;


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
    private Command autonomousCommand = null;
    private TestMode testMode = null;
    private boolean isRedAlliance;
    // private CommandSwerveDrivetrain drivetrain;



    /** 
     * Uses the default access modifier so that the Robot object can only be constructed in this same package.
     */
    Robot() 
    {
        //Configure the loggers
        DataLogFile.config();

        //Configure RobotContainer
        robotContainer = new RobotContainer();
        // drivetrain = robotContainer.getCommandSwerveDrivetrain();
        //Configure commands
        CommandsManager.createCommands(robotContainer);

        //Configure Bindings
        DriverBindings.createBindings(robotContainer);
        OperatorBindings.createBindings(robotContainer);

        // TODO - Add these PathPlanner warmup commands
        // FollowPathCommand.warmupCommand().schedule();
        // PathfindingCommand.warmupCommand().schedule();
    }

    @Override
    public void robotPeriodic() 
    {
        // Run periodic tasks
        

        // // SmartDashboard.putNumber("Speed", drivetrain.getState().Speeds.getVelocity());
        // if(robotContainer.getCommandSwerveDrivetrain() != null)
        //     SmartDashboard.putNumber("Speed", robotContainer.getCommandSwerveDrivetrain().getState().Speeds.vxMetersPerSecond);

        // System.out.println(robotContainer.getCommandSwerveDrivetrain().getPigeon2().getYaw().getValueAsDouble());
        // isRedAlliance = robotContainer.getCommandSwerveDrivetrain().isRedAllianceSupplier().getAsBoolean();
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
    {}

    /**
     * This method runs periodically (20ms) during disabled mode.
     */
    @Override
    public void disabledPeriodic() 
    {
        robotContainer.resetRobot();
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
        DataLogManager.start();
        autonomousCommand = robotContainer.getAutonomousCommand();

        if(autonomousCommand != null) 
        {
            autonomousCommand.schedule();
        }

        robotContainer.resetRobot();
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
    {}

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
    }

    /**
     * This method runs periodically (20ms) during teleop mode.
     */
    @Override
    public void teleopPeriodic() 
    {
        System.out.print("Axis : " + robotContainer.getDriverController().getRawAxis(1) + "      ");
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
