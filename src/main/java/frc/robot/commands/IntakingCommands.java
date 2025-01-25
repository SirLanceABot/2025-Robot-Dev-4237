package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.RobotContainer;
import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.LEDs.Color;

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
    private static RobotContainer robotContainer = null;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private IntakingCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public static void setRobotContainer(RobotContainer robotContainer)
    {
        if(IntakingCommands.robotContainer == null)
            IntakingCommands.robotContainer = robotContainer;
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    public static Command pickupCoralCommand()
    {
        if(robotContainer.getIntake() != null && robotContainer.getIntakeWrist() != null && robotContainer.getElevator() != null && robotContainer.getGrabber() != null && robotContainer.getLEDs() != null)
        {
            // NEEDS CHANGED
            return 
            robotContainer.getLEDs().setColorBlinkCommand(Color.kYellow)
            .alongWith(
                robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kIntakePosition))
            .andThen(
                // Commands.waitUntil(Put Proxy sensor detection for intake here) **withDeadline now, not deadlineWith
                    robotContainer.getIntake().pickupCommand())
            .andThen(
                robotContainer.getIntake().stopCommand())
            .andThen(
                    robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kStartingPosition)
                    .alongWith(
                        robotContainer.getGrabber().grabGamePieceCommand()))
            .andThen(
                robotContainer.getElevator().moveToSetPositionCommand(TargetPosition.kGrabCoralPosition)
                .alongWith(
                    robotContainer.getIntake().ejectCommand()));
            // .andThen(
            //     // Commands.waitUntil(put proxy detection for grabber here)
            //     .dead
            // )
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
