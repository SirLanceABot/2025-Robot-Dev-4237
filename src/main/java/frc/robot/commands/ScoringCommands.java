package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandsManager.TargetPosition;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs.Color;
import frc.robot.subsystems.Pivot.PivotPosition;
import frc.robot.subsystems.Pivot;
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
    private static Intake intake;
    private static IntakeWrist intakeWrist;
    private static Pivot pivot;
    private static Elevator elevator;
    private static Grabber grabber;
    private static LEDs leds;
    private static Proximity algaeIntakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity grabberProximity;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private ScoringCommands()
    {}

    public static void createCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        intake = robotContainer.getIntake();
        intakeWrist = robotContainer.getIntakeWrist();
        pivot = robotContainer.getPivot();
        elevator = robotContainer.getElevator();
        grabber = robotContainer.getGrabber();
        leds = robotContainer.getLEDs();
        algaeIntakeProximity = robotContainer.getAlgaeIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        grabberProximity = robotContainer.getGrabberProximity();

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
        if(elevator != null && pivot != null && grabber != null && leds != null && grabberProximity != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(targetPosition.elevator).getAsBoolean() && pivot.isAtPosition(targetPosition.pivot).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(targetPosition.elevator),
                pivot.moveToSetPositionCommand(targetPosition.pivot))
            .andThen(
                Commands.waitUntil(() -> (!(grabberProximity.isDetectedSupplier().getAsBoolean())))
                .deadlineFor(
                    grabber.placeCoralCommand()))
            .andThen(
                Commands.waitUntil(() -> (elevator.isAtPosition(targetPosition.elevator).getAsBoolean() && pivot.isAtPosition(targetPosition.pivot).getAsBoolean()))
                .deadlineFor(
                    grabber.stopCommand(),
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
        if(elevator != null && pivot != null && grabber != null && leds != null && grabberProximity != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kL4).getAsBoolean() && pivot.isAtPosition(PivotPosition.kScoreBargePosition).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(ElevatorPosition.kL4),
                pivot.moveToSetPositionCommand(PivotPosition.kScoreBargePosition))
            .andThen(
                Commands.waitUntil(() -> (!(grabberProximity.isDetectedSupplier()).getAsBoolean()))
                .deadlineFor(
                    grabber.ejectAlgaeCommand()))
            .andThen(
                Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kRestingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kRestingPosition).getAsBoolean()))
                .deadlineFor(
                    grabber.stopCommand(),
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
        if(elevator != null && pivot != null && grabber != null && leds != null && grabberProximity != null)
        {
            return
            Commands.waitUntil(() -> (!(grabberProximity.isDetectedSupplier()).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                grabber.ejectAlgaeCommand())
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

    /**
     * This command will spit out the algae into the processor with the grabber and then move the elevator and pivot back to resting position
     * @return the command to move elevator and pivot and release algae
     * @author Logan Bellinger
     */
    public static Command scoreProcessorWithGrabberCommand()
    {
        if(elevator != null && pivot != null && grabber != null && leds != null && grabberProximity != null)
        {
            return
            Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kScoreProcessorPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kScoreProcessorPosition).getAsBoolean()))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kBlue),
                elevator.moveToSetPositionCommand(ElevatorPosition.kScoreProcessorPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kScoreProcessorPosition))
            .andThen(
                Commands.waitUntil(() -> (!(grabberProximity.isDetectedSupplier()).getAsBoolean()))
                .deadlineFor(
                    grabber.ejectAlgaeCommand()))
                .andThen(
                    Commands.waitUntil(() -> (elevator.isAtPosition(ElevatorPosition.kRestingPosition).getAsBoolean() && pivot.isAtPosition(PivotPosition.kRestingPosition).getAsBoolean()))
                    .deadlineFor(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition),
                        pivot.moveToSetPositionCommand(PivotPosition.kRestingPosition)))
                .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Finish Scoring in Processor with Grabber Command");
        }
        else
        {
            return Commands.none();
        }
    }

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
