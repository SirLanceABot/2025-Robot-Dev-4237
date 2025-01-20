package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakeWrist.Position;

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
    private static RobotContainer robotContainer = null;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private ScoringCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public static void setRobotContainer(RobotContainer robotContainer)
    {
        if(ScoringCommands.robotContainer == null)
            ScoringCommands.robotContainer = robotContainer;
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
        if(robotContainer.getIntake() != null && robotContainer.getIntakeWrist() != null && robotContainer.getLEDs() != null)
        {
            // NEEDS CHANGED
            return 
            robotContainer.getLEDs().setBlueBlinkCommand()
            .andThen(
                robotContainer.getIntake().ejectCommand())
                .withTimeout(1.5)
            .andThen(
                robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kIntakeAlgaePosition))
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
