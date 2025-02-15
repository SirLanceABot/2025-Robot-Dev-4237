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
import frc.robot.subsystems.Claw;
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
    private static Claw claw;
    private static Climb climb;
    private static LEDs leds;
    private static GyroLance gyro;
    private static Proximity intakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity clawProximity;
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
        claw = robotContainer.getClaw();
        climb = robotContainer.getClimb();
        leds = robotContainer.getLEDs();
        gyro = robotContainer.getGyro();
        intakeProximity = robotContainer.getCoralIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        clawProximity = robotContainer.getClawProximity();
        commandSwerveDrivetrain = robotContainer.getCommandSwerveDrivetrain();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * Moves the scorer to the position passed to the command  **USE moveScorerTo(insert position here) instead, uses logic to make sure we don't assassinate our claw on our source intake**
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

    public static Command moveScorerToL1Command()
    {
        if(elevator != null && pivot != null)
        {
            return
            Commands.either(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL1).getAsBoolean() && pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kL1),
                    pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)),

                    Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition))
                    .andThen(
                        Commands.waitUntil(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                        .deadlineFor(
                            pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)))
                    .andThen(
                        Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kL1))
                        .deadlineFor(
                            elevator.moveToSetPositionCommand(ElevatorPosition.kL1))),

                () -> (elevator.isAtPosition(ElevatorPosition.kHoldingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kFlippedPosition).getAsBoolean()))
                .withName("Move Scorer to L1 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command moveScorerToL2Commmand()
    {
        if(elevator != null && pivot != null)
        {
            return
            Commands.either(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL2).getAsBoolean() && pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kL2),
                    pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)),

                    Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition))
                    .andThen(
                        Commands.waitUntil(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                        .deadlineFor(
                            pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)))
                    .andThen(
                        Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kL2))
                        .deadlineFor(
                            elevator.moveToSetPositionCommand(ElevatorPosition.kL2))),

                () -> (elevator.isAtPosition(ElevatorPosition.kHoldingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kFlippedPosition).getAsBoolean()))
                .withName("Move Scorer to L2 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command moveScorerToL3Command()
    {
        if(elevator != null && pivot != null)
        {
            return
            Commands.either(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL3).getAsBoolean() && pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kL3),
                    pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)),

                    Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition))
                    .andThen(
                        Commands.waitUntil(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                        .deadlineFor(
                            pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)))
                    .andThen(
                        Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kL3))
                        .deadlineFor(
                            elevator.moveToSetPositionCommand(ElevatorPosition.kL3))),

                () -> (elevator.isAtPosition(ElevatorPosition.kHoldingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kFlippedPosition).getAsBoolean()))
                .withName("Move Scorer to L3 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command moveScorerToL4Command()
    {
        if(elevator != null && pivot != null)
        {
            return
            Commands.either(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL4).getAsBoolean() && pivot.isAtPosition(PivotPosition.kL4).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kL4),
                    pivot.moveToSetPositionCommand(PivotPosition.kL4)),

                    Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL4).getAsBoolean() && pivot.isAtPosition(PivotPosition.kL4).getAsBoolean()))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kL4),
                        Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                        .deadlineFor(
                            pivot.moveToSetPositionCommand(PivotPosition.kL4))),

                () -> (elevator.isAtPosition(ElevatorPosition.kHoldingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kFlippedPosition).getAsBoolean()))
                .withName("Move Scorer to L4 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command moveScorerToIntakingPositionCommand()
    {
        if(elevator != null && pivot != null)
        {
            return
            Commands.either(
                Commands.waitUntil(pivot.isAtPosition(PivotPosition.kDownPosition))
                .deadlineFor(
                    pivot.moveToSetPositionCommand(PivotPosition.kDownPosition))
                .andThen(
                    Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kGrabCoralPosition))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition))),

                    Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition))
                    .andThen(
                        Commands.waitUntil(pivot.isAtPosition(PivotPosition.kDownPosition))
                        .deadlineFor(
                            pivot.moveToSetPositionCommand(PivotPosition.kDownPosition)))
                    .andThen(
                        Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kGrabCoralPosition))
                        .deadlineFor(
                            elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition))),

                () -> (elevator.getLeftPosition() > 40.0)) // Checks if elevator is higher than the designated "Safe Swing" position
                .withName("Move Scorer to Intaking Position Command");  
        }
        else
        {
            return Commands.none();
        }
    }
    
    /**
     * Command that only turns on kicker motor to score coral, done here so it can be registered for PathPlanner
     * @return BTYG
     * @author Logan Bellinger
     */
    public static Command scoreCoralOnlyCommand()
    {
        if(claw != null)
        {
            return 
            claw.placeCoralCommand().withTimeout(0.5).withName("Score Coral Only Command");
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
                elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition),
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
                elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition),
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
                                    new Pose2d(currentPose.getTranslation(), currentPose.getRotation()),
                                    new Pose2d(targetPose.getTranslation(), targetPose.getRotation()));          


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
