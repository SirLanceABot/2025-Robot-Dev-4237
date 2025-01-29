package frc.robot.commands;

import java.lang.invoke.MethodHandles;

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
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.LEDs.Color;
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



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private static Intake intake;
    private static IntakeWrist intakeWrist;
    private static Pivot pivot;
    private static Elevator elevator;
    private static Grabber grabber;
    private static LEDs leds;
    private static Proximity intakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity grabberProximity;


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
        grabber = robotContainer.getGrabber();
        leds = robotContainer.getLEDs();
        intakeProximity = robotContainer.getIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        grabberProximity = robotContainer.getGrabberProximity();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    /**
     * Command to pickup a coral from the ground
     * @return the command to pickup coral
     * @author Logan Bellinger
    */
    public static Command intakeCoralCommand()
    {
        if(intake != null && intakeWrist != null && elevator != null && grabber != null && leds != null && intakeProximity != null && elevatorProximity != null && grabberProximity != null)
        {
            // Does it work?  I don't know.  I'm sure its fine
            return 
            Commands.waitUntil(intakeWrist.isAtPosition(Position.kIntakeCoralPosition))
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kYellow),
                intakeWrist.moveToSetPositionCommand(Position.kIntakeCoralPosition))
            .andThen(
                Commands.waitUntil(intakeProximity.isDetectedSupplier())
                .deadlineFor(
                    intake.pickupCommand(),
                    elevator.moveToSetPositionCommand(TargetPosition.kGrabCoralPosition.elevator)))
            .andThen(
                Commands.waitUntil(intakeWrist.isAtPosition(Position.kPassToGrabberPosition))
                .deadlineFor(
                    intake.stopCommand(),
                    intakeWrist.moveToSetPositionCommand(Position.kPassToGrabberPosition)))
            .andThen(
                    Commands.waitUntil(elevatorProximity.isDetectedSupplier())
                    .deadlineFor(
                        intake.ejectCommand(),
                        grabber.grabGamePieceCommand()))
            .andThen(
                Commands.waitUntil(grabberProximity.isDetectedSupplier())
                .deadlineFor(
                    elevator.moveToSetPositionCommand(TargetPosition.kRestingPosition.elevator)))
            .andThen(
                Commands.waitUntil(intakeWrist.isAtPosition(Position.kRestingPosition))
                .deadlineFor(
                    grabber.stopCommand(),
                    intakeWrist.moveToSetPositionCommand(Position.kRestingPosition),
                    leds.setColorSolidCommand(Color.kRed)));
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
        if(intake != null && intakeWrist != null && leds != null && intakeProximity != null)
        {
            return
            Commands.waitUntil(intakeProximity.isDetectedSupplier())
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kYellow),
                intakeWrist.moveToSetPositionCommand(Position.kManipAlgaePosition),
                intake.pickupCommand())
            .andThen(
                Commands.waitUntil(intakeWrist.isAtPosition(Position.kAlgaeIntakedPosition))
                .deadlineFor(
                    intakeWrist.moveToSetPositionCommand(Position.kAlgaeIntakedPosition),
                    intake.stopCommand(),
                    leds.setColorSolidCommand(Color.kRed)));
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
        if(grabber != null && elevator != null && pivot != null && grabber != null && leds != null && grabberProximity != null)
        {
            return
            Commands.waitUntil(grabberProximity.isDetectedSupplier())
            .deadlineFor(
                leds.setColorBlinkCommand(Color.kYellow),
                elevator.moveToSetPositionCommand(targetPosition.elevator),
                pivot.moveToSetPositionCommand(targetPosition.pivot),
                grabber.grabGamePieceCommand())
            .andThen(
                Commands.waitUntil(() -> (pivot.isAtPosition(targetPosition.pivot).getAsBoolean() && elevator.isAtPosition(targetPosition.elevator).getAsBoolean()))
                .deadlineFor(
                    elevator.moveToSetPositionCommand(TargetPosition.kHoldAlgaePosition.elevator),
                    pivot.moveToSetPositionCommand(TargetPosition.kHoldAlgaePosition.pivot),
                    grabber.stopCommand(),
                    leds.setColorSolidCommand(Color.kRed)));
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
