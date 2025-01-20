package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.RobotContainer;

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

    // public static Command pickupCoralCommand()
    // {
    //     if(robotContainer.getIntakeWrist() != null && robotContainer.getElevator() != null && robotContainer.getGrabber() != null && robotContainer.getLEDs() != null)
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
