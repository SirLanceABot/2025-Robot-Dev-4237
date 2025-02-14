package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandsManager.TargetPosition;
import frc.robot.sensors.GyroLance;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Pivot.PivotPosition;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.PoseEstimator;
import frc.robot.subsystems.Elevator.ElevatorPosition;

public final class ScoringCommands
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
    private static CommandSwerveDrivetrain drivetrain;
    private static Intake intake;
    private static IntakeWrist intakeWrist;
    private static Pivot pivot;
    private static Elevator elevator;
    private static Claw claw;
    private static LEDs leds;
    private static GyroLance gyro;
    private static PoseEstimator poseEstimator;
    private static Proximity algaeIntakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity clawProximity;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private ScoringCommands()
    {}

    public static void createCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        drivetrain = robotContainer.getCommandSwerveDrivetrain();
        intake = robotContainer.getIntake();
        intakeWrist = robotContainer.getIntakeWrist();
        pivot = robotContainer.getPivot();
        elevator = robotContainer.getElevator();
        claw = robotContainer.getClaw();
        leds = robotContainer.getLEDs();
        gyro = robotContainer.getGyro();
        algaeIntakeProximity = robotContainer.getAlgaeIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        clawProximity = robotContainer.getClawProximity();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    /**
     * Command to score coral, moves elevator and pivot up, ejects coral, and moves elevator and pivot back down
     * @param targetPosition the target position to score the coral at
     * @return the command to score
     * @author Logan Bellinger
     */
    public static Command scoreCoralCommand(TargetPosition targetPosition)
    {
        if(elevator != null && pivot != null && claw != null && leds != null && clawProximity != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(targetPosition.elevator).getAsBoolean() && pivot.isAtPosition(targetPosition.pivot).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(targetPosition.elevator),
                pivot.moveToSetPositionCommand(targetPosition.pivot))
            .andThen(
                Commands.waitUntil(() -> (!(clawProximity.isDetectedSupplier().getAsBoolean())))
                .deadlineFor(
                    claw.placeCoralCommand()))
            .andThen(
                Commands.waitUntil(() -> (elevator.isAtPosition(targetPosition.elevator).getAsBoolean() && pivot.isAtPosition(targetPosition.pivot).getAsBoolean()))
                .deadlineFor(
                    claw.stopCommand(),
                    pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition),
                    elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition)))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Score Coral on Reef Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to score the algae in the processor
     * @return the command to score in the processor
     * @author Logan Bellinger
     */
    public static Command scoreProcessorWithIntakeCommand()
    {
        if(intake != null && intakeWrist != null && leds != null && algaeIntakeProximity != null)
        {
            return
            Commands.waitUntil(() -> (!algaeIntakeProximity.isDetectedSupplier().getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                intakeWrist.moveToSetPositionCommand(Position.kManipAlgaePosition),
                intake.ejectAlgaeCommand())
            .andThen(
                Commands.waitUntil(intakeWrist.isAtPosition(Position.kRestingPosition))
                .deadlineFor(
                    intake.stopCommand(),
                    intakeWrist.moveToSetPositionCommand(Position.kRestingPosition)))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Score Processor With Intake");   
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to score Algae in the barge
     * @return the command to score in barge
     * @author Logan Bellinger
     */
    public static Command scoreAlgaeInBargeCommand()
    {
        if(elevator != null && pivot != null && claw != null && leds != null && clawProximity != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL4).getAsBoolean() && pivot.isAtPosition(PivotPosition.kScoreBargePosition).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(ElevatorPosition.kL4),
                pivot.moveToSetPositionCommand(PivotPosition.kScoreBargePosition))
            .andThen(
                Commands.waitUntil(() -> (!(clawProximity.isDetectedSupplier()).getAsBoolean()))
                .deadlineFor(
                    claw.ejectAlgaeCommand()))
            .andThen(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kRestingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kRestingPosition).getAsBoolean()))
                .deadlineFor(
                    claw.stopCommand(),
                    elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition),
                    pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition)))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Score Algae In Barge Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to finish scoring coral on the reef
     * @return the command to finish scoring
     * @author Logan Bellinger
     */
    public static Command finishScoringCoralCommand()
    {
        if(elevator != null && pivot != null && claw != null && leds != null && clawProximity != null)
        {
            return
            Commands.waitUntil(() -> (!(clawProximity.isDetectedSupplier()).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                claw.ejectAlgaeCommand())
            .andThen(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kRestingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kRestingPosition).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition),
                    pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition)))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Finish Scoring Coral Command");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command flipScorerCommand()
    {
        if(elevator != null && pivot != null)
        {
            return
            Commands.waitUntil(pivot.isAtPosition(PivotPosition.kFlippedPosition))
            .deadlineFor(pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition))
            .andThen(
                Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kHoldingPosition))
                .deadlineFor(elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)))
            .withName("Flip Scorer");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * This command will spit out the algae into the processor with the claw and then move the elevator and pivot back to resting position
     * @return the command to move elevator and pivot and release algae
     * @author Logan Bellinger
     */
    public static Command scoreProcessorWithClawCommand()
    {
        if(elevator != null && pivot != null && claw != null && leds != null && clawProximity != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kScoreProcessorPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kScoreProcessorPosition).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(ElevatorPosition.kScoreProcessorPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kScoreProcessorPosition))
            .andThen(
                Commands.waitUntil(() -> (!(clawProximity.isDetectedSupplier()).getAsBoolean()))
                .deadlineFor(
                    claw.ejectAlgaeCommand()))
                .andThen(
                    Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kRestingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kRestingPosition).getAsBoolean()))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition),
                        pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition)))
                .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Finish Scoring in Processor with Claw Command");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(boolean isRight, TargetPosition targetPosition)
    {
        // TODO: fix this.  idk why owen wrote this the choose closest branch like this.  feels wrong at best
        if(drivetrain != null && gyro != null && elevator != null && pivot != null && claw != null)
        {
            Pose2d currentPose = poseEstimator.getEstimatedPose();
            Pose2d targetPose = poseEstimator.closestBranchLocation(poseEstimator.getPrimaryTagID(), isRight);

            return
            leds.setColorBlinkCommand(Color.kBlue)
            .andThen(
                GeneralCommands.driveToPositionCommand(targetPose, currentPose)
                .alongWith(
                    GeneralCommands.moveScorerToSetPositionCommand(targetPosition))
            .andThen(
                finishScoringCoralCommand()));
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Command scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(boolean isRight)
    // {
    //     // TODO: fix this.  idk why owen wrote this the choose closest branch like this.  feels wrong at best
    //     if(drivetrain != null && gyro != null && elevator != null && pivot != null && claw != null)
    //     {
    //         Pose2d currentPose = poseEstimator.getEstimatedPose();
    //         Pose2d targetPose = poseEstimator.closestBranchLocation(camera.getTagID(), isRight)
    //         // Pose2d targetPose = new Pose2d(poseEstimator.chooseClosestBranch(poseEstimator.getAprilTagPose(0), isRight));

    //         return
    //         leds.setColorBlinkCommand(Color.kBlue)
    //         .andThen(
    //             GeneralCommands.driveToPositionCommand()
    //         );
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    // public static Command exampleCommand()
    // {
    //     if(subsystems != null)
    //     {
    //         return someCompoundCommand;
    //     }
    //     else
    //         return Commands.none();
            // BOW TO YOUR GOD
    // }
}
