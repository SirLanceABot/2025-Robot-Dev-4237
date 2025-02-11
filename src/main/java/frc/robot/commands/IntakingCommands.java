package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandsManager.TargetPosition;
import frc.robot.sensors.Proximity;
// import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Pivot.PivotPosition;

public final class IntakingCommands
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
    //BTYG



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private static Intake intake;
    private static IntakeWrist intakeWrist;
    private static Pivot pivot;
    private static Elevator elevator;
    private static Claw claw;
    private static LEDs leds;
    private static Proximity coralIntakeProximity;
    private static Proximity algaeIntakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity clawProximity;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private IntakingCommands()
    {}

    public static void createCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        intake = robotContainer.getIntake();
        intakeWrist = robotContainer.getIntakeWrist();
        pivot = robotContainer.getPivot();
        elevator = robotContainer.getElevator();
        claw = robotContainer.getClaw();
        leds = robotContainer.getLEDs();
        coralIntakeProximity = robotContainer.getCoralIntakeProximity();
        algaeIntakeProximity = robotContainer.getAlgaeIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        clawProximity = robotContainer.getClawProximity();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here BTYG

    /**
     * Command to pickup a coral from the ground
     * @return the command to pickup coral
     * @author Logan Bellinger
    */
    public static Command intakeCoralCommand()
    {
        if(intake != null && intakeWrist != null && elevator != null && claw != null && leds != null && coralIntakeProximity != null && elevatorProximity != null && clawProximity != null)
        {
            // Does it work?  I don't know.  I'm sure its fine
            return 
            Commands.waitUntil(intakeWrist.isAtPosition(Position.kIntakeCoralPosition))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kYellow),
                intakeWrist.moveToSetPositionCommand(Position.kIntakeCoralPosition))
            .andThen(
                Commands.waitUntil(coralIntakeProximity.isDetectedSupplier())
                .deadlineFor(
                    intake.pickupCoralCommand(),
                    elevator.moveToSetPositionCommand(TargetPosition.kGrabCoralPosition.elevator),
                    pivot.moveToSetPositionCommand(TargetPosition.kGrabCoralPosition.pivot)))
            .andThen(
                Commands.waitUntil(intakeWrist.isAtPosition(Position.kPassToClawPosition))
                .deadlineFor(
                    intake.stopCommand(),
                    intakeWrist.moveToSetPositionCommand(Position.kPassToClawPosition)))
            .andThen(
                    Commands.waitUntil(elevatorProximity.isDetectedSupplier())
                    .deadlineFor(
                        intake.ejectCoralCommand(),
                        claw.grabGamePieceCommand()))
            .andThen(
                Commands.waitUntil(clawProximity.isDetectedSupplier())
                .deadlineFor(
                    elevator.moveToSetPositionCommand(TargetPosition.kRestingPosition.elevator)))
            .andThen(
                Commands.waitUntil(pivot.isAtPosition(PivotPosition.kFlippedPosition))
                .deadlineFor(
                    claw.stopCommand(),
                    intakeWrist.moveToSetPositionCommand(Position.kRestingPosition),
                    pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition),
                    elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition)))
            .andThen(
                Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kHoldingPosition))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Intake Coral Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to intake a Coral from the Coral Station
     * @return the command to intake coral
     * @author Logan Bellinger
     */
    public static Command intakeCoralFromStationCommand()
    {
        if(elevator != null && pivot != null && claw != null && leds != null && elevatorProximity != null && clawProximity != null)
        {
            return
            Commands.waitUntil(elevatorProximity.isDetectedSupplier())
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kYellow),
                elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition),
                pivot.moveToSetPositionCommand(PivotPosition.kGrabCoralPosition),
                claw.grabGamePieceCommand())
            .andThen(
                Commands.waitUntil(clawProximity.isDetectedSupplier())
                .deadlineFor(
                    claw.stopCommand(),
                    elevator.moveToSetPositionCommand(ElevatorPosition.kRestingPosition)))
            .andThen(
                Commands.waitUntil(pivot.isAtPosition(PivotPosition.kFlippedPosition))
                .deadlineFor(
                    pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition),
                    elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition)))
            .andThen(
                Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kHoldingPosition))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Intake Coral From Station Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * The command to intake algae from the ground with our ground intake
     * @return Command to intake algae
     * @author Logan Bellinger
     */
    public static Command intakeAlgaeCommand()
    {
        if(intake != null && intakeWrist != null && leds != null && coralIntakeProximity != null)
        {
            return
            Commands.waitUntil(algaeIntakeProximity.isDetectedSupplier())
            .deadlineFor(
                leds.setColorBlinkCommand(edu.wpi.first.wpilibj.util.Color.kYellow),
                intakeWrist.moveToSetPositionCommand(Position.kManipAlgaePosition),
                intake.pickupCoralCommand())
            .andThen(
                Commands.waitUntil(intakeWrist.isAtPosition(Position.kAlgaeIntakedPosition))
                .deadlineFor(
                    intakeWrist.moveToSetPositionCommand(Position.kAlgaeIntakedPosition),
                    intake.stopCommand()))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Intake Algae From Ground Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to intake an Algae from the reef
     * @param targetPosition the position to move the elevator and pivot to, either the UPPER_REEF_ALGAE (between L3 and L4) or the LOWER_REEF_ALGAE (between L2 and L3)
     * @return The command to intake the algae
     * @author Logan Bellinger
     */
    public static Command intakeAlgaeFromReefCommand(TargetPosition targetPosition)
    {
        if(claw != null && elevator != null && pivot != null && leds != null && clawProximity != null)
        {
            return
            Commands.waitUntil(clawProximity.isDetectedSupplier())
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kYellow),
                elevator.moveToSetPositionCommand(targetPosition.elevator),
                pivot.moveToSetPositionCommand(targetPosition.pivot),
                claw.grabGamePieceCommand())
            .andThen(
                Commands.waitUntil(() -> (pivot.isAtPosition(targetPosition.pivot).getAsBoolean() && elevator.isAtPosition(targetPosition.elevator).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(TargetPosition.kHoldAlgaePosition.elevator),
                    pivot.moveToSetPositionCommand(TargetPosition.kHoldAlgaePosition.pivot),
                    claw.stopCommand()))
            .andThen(leds.setColorSolidCommand(Color.kRed))
            .withName("Intake Algae From Reef Command");
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
    // }
}
