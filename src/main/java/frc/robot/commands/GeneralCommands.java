package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandsManager.TargetPosition;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs.Color;
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
    private static LEDs leds;
    private static Proximity intakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity grabberProximity;
   


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
        leds = robotContainer.getLEDs();
        intakeProximity = robotContainer.getCoralIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        grabberProximity = robotContainer.getGrabberProximity();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * 
     * @param targetPosition
     * @return
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
