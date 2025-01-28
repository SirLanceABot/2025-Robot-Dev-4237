package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs.Color;
import frc.robot.subsystems.Pivot;

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
    private static Intake intake = new Intake();
    private static IntakeWrist intakeWrist = new IntakeWrist();
    private static Pivot pivot = new Pivot();
    private static Elevator elevator = new Elevator();
    private static Grabber grabber = new Grabber();
    private static LEDs leds = new LEDs();
    private static Proximity intakeProximity = new Proximity(Constants.Proximity.INTAKE_PORT);
    private static Proximity elevatorProximity = new Proximity(Constants.Proximity.ELEVATOR_PORT);
    private static Proximity grabberProximity = new Proximity(Constants.Proximity.GRABBER_PORT);
   


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
        intakeProximity = robotContainer.getIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        grabberProximity = robotContainer.getGrabberProximity();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    // public static Command placeCoralCommand(Constants.TargetPosition targetPosition)
    // {
    //     if(robotContainer.getElevator() != null && robotContainer.getPivot() != null && robotContainer.getGrabber() != null && robotContainer.getDrivetrain() != null && robotContainer.getLEDs() != null)
    //     {
    //         // NEEDS CHANGED
    //         return 
    //         robotContainer.getLEDs().setBlueBlinkCommand()
    //         .andThen(

    //         )

    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    // public static Command scoreProcessorWithArmCommand()
    // {
    //     if(robotContainer.getElevator() != null && robotContainer.getPivot() != null && robotContainer.getGrabber() != null && robotContainer.getLEDs() != null)
    //     {
    //         // NEEDS CHANGED
    //         return 
    //         robotContainer.getLEDs().setBlueBlinkCommand()
    //         .andThen(
                
    //         )
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    public static Command scoreProcessorWithIntakeCommand()
    {
        if(intake != null && intakeWrist != null && leds != null)
        {
            // NEEDS CHANGED
            return 
            leds.setColorBlinkCommand(Color.kBlue)
            .andThen(
                intake.ejectCommand())
                .withTimeout(1.5)
            .andThen(
                intakeWrist.moveToSetPositionCommand(Position.kRestingPosition))
            .withName("Score Processor With Intake Command");
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Command scoreAlgaeInBargeCommand()
    // {
    //     if(robotContainer.getElevator() != null && robotContainer.getPivot() != null && robotContainer.getGrabber() != null && robotContainer.getLEDs() != null)
    //     {
    //         // NEEDS CHANGED
    //         return 
    //         robotContainer.getLEDs().setBlueBlinkCommand()
    //         .andThen(
                
    //         )
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
    // }
}
