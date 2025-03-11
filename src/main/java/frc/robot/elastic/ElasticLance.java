package frc.robot.elastic;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.util.ElementScanner14;

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
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
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
import frc.robot.pathplanner.PathPlannerLance;
import frc.robot.sensors.Camera;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs.ColorPattern;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.PoseEstimator;

public class ElasticLance 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    private static Color allianceColor = new Color();
    private static Color validReefTagColor = new Color();
    private static Color validAutoColor = new Color();
 
    // Elastic.Notification AutoNotification = new Elastic.Notification();
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    // private static SendableChooser < Command > leftWall;
    // private static SendableChooser < Command > middle;
    // private static SendableChooser < Command > rightWall;

    private static Field2d autofield = new Field2d();
    private static Field2d field = new Field2d();

    private static Trajectory trajectory;

    private static Camera scoringSideCamera;
    private static Camera climbSideCamera;
    private static PoseEstimator poseEstimator;
    private static LEDs leds;
    private static Elevator elevator;
    private static Pivot pivot;
    private static Drivetrain drivetrain;
    private static boolean useFullRobot;

    private static Alert autoAlert = new Alert("Invalid Auto", AlertType.kWarning);
    private static Alert useFullRobotAlert = new Alert("NOT using Full Robot!", AlertType.kError);

    
    private ElasticLance()
    {}

    public static void configElastic(RobotContainer robotContainer)
    {
        leds = robotContainer.getLEDs();
        // configAutoChooser();
        
        elevator = robotContainer.getElevator();
        drivetrain = robotContainer.getDrivetrain();
        pivot = robotContainer.getPivot();
        scoringSideCamera = robotContainer.getScoringSideCamera();
        climbSideCamera = robotContainer.getClimbSideCamera();
        useFullRobot = robotContainer.useFullRobot();
        // poseEstimator = robotContainer.getPoseEstimator();

        //configTeleopField();
        //createAutoField();
        
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
        // SmartDashboard.putNumber("Elevator Position", elevator.getPosition());
        // SmartDashboard.putNumber("Pivot Position", pivot.getPosition());
        // updateTeleopField();
        // updateAutoField();
        updateReefTagBox();
        updateValidAutoBox();

        updateAllianceColorBox();

        if(!useFullRobot)
        {
            useFullRobotAlert.set(true);
            leds.setColorSolidCommand(Color.kTurquoise).ignoringDisable(true).schedule();
        }
    }

    public static void updateAllianceColorBox()
    {
        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red)
        {
            allianceColor = Color.kRed;
        }
        else if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)
        {
            allianceColor = Color.kBlue;
        }
        else 
        {
            allianceColor = Color.kGray;
        }

        SmartDashboard.putString("Alliance Color", allianceColor.toHexString());
    }

    public static void updateReefTagBox()
    {
        int scoreTagID = 0;
        int climbTagID = 0;

        if(scoringSideCamera != null)
        {
            scoreTagID = (int) scoringSideCamera.getTagId();
        }

        if(climbSideCamera != null)
        {
            climbTagID = (int) climbSideCamera.getTagId();
        }

        if(scoringSideCamera == null && climbSideCamera == null)
        {
            validReefTagColor = Color.kGray;
        }
        else if((scoreTagID >= 6 && scoreTagID <= 11) || (scoreTagID >= 17 && scoreTagID <= 22) || 
           (climbTagID >= 6 && climbTagID <= 11) || (climbTagID >= 17 && climbTagID <= 22))
        {
            validReefTagColor = Color.kGreen;
        }
        else 
        {
            validReefTagColor = Color.kRed;
        }

        SmartDashboard.putString("Is Reef Tag", validReefTagColor.toHexString());
    }

    public static void updateValidAutoBox()
    {
        System.out.println(PathPlannerLance.getAutonomousCommand().getName());
        if(PathPlannerLance.getAutonomousCommand().getName().equalsIgnoreCase("InstantCommand"))
        {
            validAutoColor = Color.kRed;
            leds.setColorSolidCommand(Color.kRed).ignoringDisable(true).schedule();
            autoAlert.set(true);
        }
        else 
        {
            validAutoColor = Color.kGreen;
            leds.setColorSolidCommand(Color.kGreen).ignoringDisable(true).schedule();
            autoAlert.set(false);
        }
        SmartDashboard.putString("Is Auto Valid", validAutoColor.toHexString());

        // Elastic.Notification notification = new Elastic.Notification(Elastic.Notification.NotificationLevel.ERROR, "Error Notification", "This is an example error notification.");
    }

        /**
     * Configures the Sendable Choosers for left, middle, and right autonomous paths.
     */
    // private static void configAutoChooser()
    // {
    //     boolean aBotAutoChoosers = true;
    //     if (AutoBuilder.isConfigured())
    //     {
    //         leftWall = AutoBuilder.buildAutoChooserWithOptionsModifier(
    //             (stream) -> aBotAutoChoosers ?
    //             stream.filter(auto -> auto.getName().startsWith("Left")) :
    //             stream
    //         );

    //         middle = AutoBuilder.buildAutoChooserWithOptionsModifier(
    //             (stream) -> aBotAutoChoosers ?
    //             stream.filter(auto -> auto.getName().startsWith("Middle")) :
    //             stream
    //         );

    //         rightWall = AutoBuilder.buildAutoChooserWithOptionsModifier(
    //             (stream) -> aBotAutoChoosers ?
    //             stream.filter(auto -> auto.getName().startsWith("Right")) :
    //             stream
    //         );
    //     }
    //     else
    //     {
    //         leftWall = new SendableChooser < Command > ();
    //         leftWall.setDefaultOption("None", Commands.none());

    //         middle = new SendableChooser < Command > ();
    //         middle.setDefaultOption("None", Commands.none());

    //         rightWall = new SendableChooser < Command > ();
    //         rightWall.setDefaultOption("None", Commands.none());
    //     }

    //     SmartDashboard.putData("Left-Wall", leftWall);
    //     SmartDashboard.putData("Middle", middle);
    //     SmartDashboard.putData("Right-Wall", rightWall);
    // }

    // public static Command getLeftWall()
    // {
    //     return leftWall.getSelected();
    // }

    // public static Command getRightWall()
    // {
    //     return rightWall.getSelected();
    // }

    // public static Command getMiddle()
    // {
    //     return middle.getSelected();
    // }
    /**
     * Retrieves the selected autonomous command from the Sendable Choosers.
     *
     * @return The selected autonomous command.
     */
    // public static Command getAutonomousCommand()
    // {
    //     if (leftWall != null && middle != null && rightWall != null)
    //     {
    //         int counter = 0;
    //         Command command = null;
    //         if (leftWall.getSelected().getName().contains("Left_Wall"))
    //         {
    //             counter++;
    //             command = leftWall.getSelected();
    //             //return leftWall.getSelected();
    //         }
    //         if (middle.getSelected().getName().contains("Middle"))
    //         {
    //             counter++;
    //             command = middle.getSelected();
    //             //return middle.getSelected();
    //         }
    //         if (rightWall.getSelected().getName().contains("Right_Wall"))
    //         {
    //             counter++;
    //             command = rightWall.getSelected();
    //             //return rightWall.getSelected();
    //         }
    //         if ( counter == 1)
    //         {
    //             SmartDashboard.putString("ERROR", "Valid Selection, good job");
    //             autoName = command.getName();;
    //             //leds.setColorSolidCommand(color.kGreen).schedule();
    //             return command;
    //         }
    //         else
    //         {
    //             SmartDashboard.putString("ERROR", "Invalid Selection: Pick ONE Autonomous");
    //             //leds.setColorBlinkCommand(Color.kRed).schedule();
    //             return Commands.none();
    //         }
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }

    // }

    // public static void resetRobot(Pigeon2 gyro)
    // {   
    //     if(gyro != null)
    //     {
    //         boolean isRed = DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red;
        
    //         Command leftWall = getLeftWall();
    //         Command middle = getMiddle();
    //         Command rightWall = getRightWall();

    //         if(isRed)
    //         {
    //             if(leftWall != Commands.none())
    //             {
    //                 gyro.setYaw(Constants.Gyro.RED_LEFT_YAW);
    //             }
    //             else if(middle != Commands.none())
    //             {
    //                 gyro.setYaw(Constants.Gyro.RED_MIDDLE_YAW);
    //             }
    //             else if(rightWall != Commands.none())
    //             {
    //                 gyro.setYaw(Constants.Gyro.RED_RIGHT_YAW);
    //             }
    //         }
    //         else
    //         {
    //             if(leftWall != Commands.none())
    //             {
    //                 gyro.setYaw(Constants.Gyro.BLUE_LEFT_YAW);
    //             }
    //             else if(middle != Commands.none())
    //             {
    //                 gyro.setYaw(Constants.Gyro.BLUE_MIDDLE_YAW);
    //             }
    //             else if(rightWall != Commands.none())
    //             {
    //                 gyro.setYaw(Constants.Gyro.BLUE_RIGHT_YAW);
    //             }
    //         }
    //     }
    // }

    // private static void createAutoField()
    // {
    //     //Create and push Field2d to SmartDashboard.
    //     SmartDashboard.putData("AutoField", autofield);
    //     Pose2d pose = drivetrain.getPose();
    //     autofield.setRobotPose(pose);
    // }

    // public static void updateAutoField() 
    // {
    //     List<PathPlannerPath> pathPlannerPaths = null;
    //     try 
    //     {
    //         pathPlannerPaths = getPathPlannerPaths(autoName);
    //                         } catch (IOException | ParseException | org.json.simple.parser.ParseException e) 
    //                         {
    //                             e.printStackTrace();
    //                         }
                    
    //                         if (pathPlannerPaths != null) 
    //                         {
    //                             List<Pose2d> poses = extractPosesFromPaths(pathPlannerPaths);
    //                             autofield.getObject("path").setPoses(poses);
    //                         }    
                    
    //                     }                        
                            
                         
    //                     private static List<PathPlannerPath>getPathPlannerPaths(String autoName) throws IOException, ParseException, org.json.simple.parser.ParseException
    //                     {
    //                         return PathPlannerAuto.getPathGroupFromAutoFile(autoName);
    //                     } 
    
    

    // private static List<Pose2d>extractPosesFromPaths(List<PathPlannerPath> pathPlannerPaths)
    // {
    // List<Pose2d> poses = new ArrayList<>();
    // for (PathPlannerPath path : pathPlannerPaths) 
    // {
    //     poses.addAll(path.getAllPathPoints().stream()
    //                 .map(
    //                     point -> new Pose2d(point.position.getX(), point.position.getY(), new Rotation2d()))
    //                 .collect(Collectors.toList()));
    // }
    // return poses;
    // }
        
    // private static void configTeleopField()
    // {
    //     SmartDashboard.putData("FieldT", field);
    // }

    // private static void updateTeleopField()
    // {
    //     var robotPose = drivetrain.getPose();
    //     field.setRobotPose(robotPose);
    // }

}