package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandsManager.TargetPosition;
import frc.robot.sensors.GyroLance;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Pivot.PivotPosition;

public final class GeneralCommands
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    // Put all inner enums and inner classes here



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private static Intake intake;
    private static IntakeWrist intakeWrist;
    private static Pivot pivot;
    private static Elevator elevator;
    private static Grabber grabber;
    private static Climb climb;
    private static LEDs leds;
    private static GyroLance gyro;
    private static Proximity intakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity grabberProximity;
    private static PoseEstimator poseEstimate;
    private static CommandSwerveDrivetrain commandSwerveDrivetrain;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private GeneralCommands()
    {}

    public static void createCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        intake = robotContainer.getIntake();
        intakeWrist = robotContainer.getIntakeWrist();
        pivot = robotContainer.getPivot();
        elevator = robotContainer.getElevator();
        grabber = robotContainer.getGrabber();
        climb = robotContainer.getClimb();
        leds = robotContainer.getLEDs();
        gyro = robotContainer.getGyro();
        intakeProximity = robotContainer.getCoralIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        grabberProximity = robotContainer.getGrabberProximity();
        commandSwerveDrivetrain = robotContainer.getCommandSwerveDrivetrain();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * Moves the scorer to the position passed to the command
     * @param targetPosition position to move scorer to
     * @return the command to do the thing
     * @author Logan Bellinger
     */
    public static Command moveScorerToSetPositionCommand(TargetPosition targetPosition)
    {
        if(elevator != null && pivot != null && leds != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(targetPosition.elevator).getAsBoolean() && pivot.isAtPosition(targetPosition.pivot).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(targetPosition.elevator),
                pivot.moveToSetPositionCommand(targetPosition.pivot))
            .withName("Move Scorer to Set Position Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to climb up the cage
     * @return the command to climb up cage
     * @author Logan Bellinger
     */
    public static Command climbUpCageCommand()
    {
        if(elevator != null && pivot != null && intakeWrist != null &&climb != null && leds != null)
        {
            return
            Commands.waitUntil(intakeWrist.isAtPosition(Position.kRestingPosition))
            .deadlineFor(
                leds.setColorRainbowCommand(),
                elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition),
                intakeWrist.moveToSetPositionCommand(Position.kRestingPosition))
            .andThen(
                climb.climbToUpPositionCommand())
            .withName("Climb Up Cage");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * The command to climb down the cage to the down set position
     * @return The command to go down
     * @author Logan Bellinger
     */
    public static Command climbDownCageCommand()
    {
        if(elevator != null && pivot != null && intakeWrist != null && climb != null && leds != null)
        {
            return
            Commands.waitUntil(intakeWrist.isAtPosition(Position.kRestingPosition))
            .deadlineFor(
                leds.setColorRainbowCommand(),
                elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition),
                intakeWrist.moveToSetPositionCommand(Position.kRestingPosition))
            .andThen(
                climb.climbToDownPositionCommand())
            .withName("Climb Down Cage");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Resets the gyro to 0 degrees
     * @return the command to reset gyro
     * @author Logan Bellinger
     */
    public static Command resetGyroCommand()
    {
        if(gyro != null)
        {
            return
            gyro.resetYawCommand().withName("Reset Yaw Command");
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Comamand determineLocationCommand()
    // {

    // }

    public static Command driveToPositionCommand(Pose2d targetPose, Pose2d currentPose)
    {

        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                                    new Pose2d(currentPose.getX(), currentPose.getY(), currentPose.getRotation()),
                                    new Pose2d(targetPose.getX(), targetPose.getY(), targetPose.getRotation()));          


        PathPlannerPath path = new PathPlannerPath(
                                    waypoints,
                                    PathConstraints.unlimitedConstraints(12.0),
                                    null,
                                    new GoalEndState(0.0, targetPose.getRotation()));
        path.preventFlipping = true;


        return AutoBuilder.followPath(path);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    // public static Command exampleCommand()
    // {
    //     if(subsystems != null)
    //     {
    //         return someCompoundCommand;
    //     }
    //     else
    //         return Commands.none();
    // }
}
