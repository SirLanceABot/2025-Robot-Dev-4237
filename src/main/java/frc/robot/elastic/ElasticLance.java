package frc.robot.elastic;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.struct.parser.ParseException;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs.ColorPattern;
import frc.robot.subsystems.Pivot;

public class ElasticLance 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    private static Color color = new Color();
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private static SendableChooser < Command > leftWall;
    private static SendableChooser < Command > middle;
    private static SendableChooser < Command > rightWall;

    private static Field2d autofield = new Field2d();
    private static Field2d field = new Field2d();

    private static Trajectory trajectory;

    private static LEDs leds;
    private static Elevator elevator;
    private static Pivot pivot;
    private static Drivetrain drivetrain;
    private final Field2d m_field = new Field2d();
    public static String autoName;

    public String newAutoName;

    
    private ElasticLance()
    {}

    public static void configElastic(RobotContainer robotContainer)
    {
        leds = robotContainer.getLEDs();
        configAutoChooser();
        configTeleopField();
        elevator = robotContainer.getElevator();
        drivetrain = robotContainer.getDrivetrain();
        
    }

    // public static void createWidgets()
    // {
    //     updateAllianceColorBox();
    // }

    public static void sendDataToSmartDashboard()
    {
        SmartDashboard.putNumber("Voltage", RobotController.getBatteryVoltage());
        SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());
        SmartDashboard.putNumber("CAN Utilization %", RobotController.getCANStatus().percentBusUtilization * 100.00);
        SmartDashboard.putNumber("CPU Temperature", RobotController.getCPUTemp());
        //SmartDashboard.putNumber("Elevator Position", elevator.getPosition());
        //SmartDashboard.putNumber("Pivot Position", pivot.getPosition());

        updateAllianceColorBox();
    }

    public static void updateAllianceColorBox()
    {
        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red)
        {
            color = Color.kRed;
        }
        else if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)
        {
            color = Color.kBlue;
        }
        else 
        {
            color = Color.kGray;
        }

        SmartDashboard.putString("Alliance Color", color.toHexString());
    }

        /**
     * Configures the Sendable Choosers for left, middle, and right autonomous paths.
     */
    private static void configAutoChooser()
    {
        boolean aBotAutoChoosers = true;
        if (AutoBuilder.isConfigured())
        {
            leftWall = AutoBuilder.buildAutoChooserWithOptionsModifier(
                (stream) -> aBotAutoChoosers ?
                stream.filter(auto -> auto.getName().startsWith("Left")) :
                stream
            );

            middle = AutoBuilder.buildAutoChooserWithOptionsModifier(
                (stream) -> aBotAutoChoosers ?
                stream.filter(auto -> auto.getName().startsWith("Middle")) :
                stream
            );

            rightWall = AutoBuilder.buildAutoChooserWithOptionsModifier(
                (stream) -> aBotAutoChoosers ?
                stream.filter(auto -> auto.getName().startsWith("Right")) :
                stream
            );
        }
        else
        {
            leftWall = new SendableChooser < Command > ();
            leftWall.setDefaultOption("None", Commands.none());

            middle = new SendableChooser < Command > ();
            middle.setDefaultOption("None", Commands.none());

            rightWall = new SendableChooser < Command > ();
            rightWall.setDefaultOption("None", Commands.none());
        }

        SmartDashboard.putData("Left-Wall", leftWall);
        SmartDashboard.putData("Middle", middle);
        SmartDashboard.putData("Right-Wall", rightWall);
    }

    public static Command getLeftWall()
    {
        return leftWall.getSelected();
    }

    public static Command getRightWall()
    {
        return rightWall.getSelected();
    }

    public static Command getMiddle()
    {
        return middle.getSelected();
    }
    /**
     * Retrieves the selected autonomous command from the Sendable Choosers.
     *
     * @return The selected autonomous command.
     */
    public static Command getAutonomousCommand()
    {
        if (leftWall != null && middle != null && rightWall != null)
        {
            int counter = 0;
            Command command = null;
            if (leftWall.getSelected().getName().contains("Left_Wall"))
            {
                counter++;
                command = leftWall.getSelected();
                //return leftWall.getSelected();
            }
            if (middle.getSelected().getName().contains("Middle"))
            {
                counter++;
                command = middle.getSelected();
                //return middle.getSelected();
            }
            if (rightWall.getSelected().getName().contains("Right_Wall"))
            {
                counter++;
                command = rightWall.getSelected();
                //return rightWall.getSelected();
            }
            if ( counter == 1)
            {
                SmartDashboard.putString("ERROR", "Valid Selection, good job");
                configAutoField();
                leds.setColorSolidCommand(color.kGreen).schedule();
                return command;
            }
            else
            {
                SmartDashboard.putString("ERROR", "Invalid Selection: Pick ONE Autonomous");
                leds.setColorBlinkCommand(Color.kRed).schedule();
                return Commands.none();
            }
        }
        else
        {
            return Commands.none();
        }

    }

    public static void resetRobot(Pigeon2 gyro)
    {   
        if(gyro != null)
        {
            boolean isRed = DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red;

            
        
            Command leftWall = getLeftWall();
            Command middle = getMiddle();
            Command rightWall = getRightWall();

            if(isRed)
            {
                if(leftWall != Commands.none())
                {
                    gyro.setYaw(Constants.Gyro.RED_LEFT_YAW);
                }
                else if(middle != Commands.none())
                {
                    gyro.setYaw(Constants.Gyro.RED_MIDDLE_YAW);
                }
                else if(rightWall != Commands.none())
                {
                    gyro.setYaw(Constants.Gyro.RED_RIGHT_YAW);
                }
            }
            else
            {
                if(leftWall != Commands.none())
                {
                    gyro.setYaw(Constants.Gyro.BLUE_LEFT_YAW);
                }
                else if(middle != Commands.none())
                {
                    gyro.setYaw(Constants.Gyro.BLUE_MIDDLE_YAW);
                }
                else if(rightWall != Commands.none())
                {
                    gyro.setYaw(Constants.Gyro.BLUE_RIGHT_YAW);
                }
            }
        }
    }

    private static void configAutoField()
    {
        // var robotPose = drivetrain.getPose();
        // //Create and push Field2d to SmartDashboard.
        // SmartDashboard.putData(autofield);
        // autofield.setRobotPose(robotPose);
        
        // autoName = getAutonomousCommand().getName();
        // //List<PathPlannerPath> pathPlannerPaths = PathPlannerAuto.getPathGroupFromAutoFile(autoName);
        //     List<Pose2d> poses = new ArrayList<>();
        //     for (PathPlannerPath path : pathPlannerPaths) {
        //         poses.addAll(path.getAllPathPoints().stream().map(point -> new Pose2d(point.position.getX(), point.position.getY(), new Rotation2d())).collect(Collectors.toList()));
        //     }
        //     autofield.getObject("path").setPoses(poses);
        }
      
          
           
                //     // Create the trajectory to follow in autonomous. It is best to initialize
            //     // trajectories here to avoid wasting time in autonomous.
            //     trajectory = TrajectoryGenerator.generateTrajectory(
            //         new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            //         List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
            //         new Pose2d(3, 0, Rotation2d.fromDegrees(0)),
            //         new TrajectoryConfig(Units.feetToMeters(3.0), Units.feetToMeters(3.0))
            //     );
          
        
        
            //     // Push the trajectory to Field2d.
            //     autofield.getObject("traj").setTrajectory(trajectory);
            
        
        
        
           

    private static void configTeleopField()
    {
        SmartDashboard.putData("Field", field);
    }

}