package frc.robot.tests;

import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.Pivot;
import frc.robot.RobotContainer;
import frc.robot.Constants;
// import frc.robot.Constants.TargetPosition;
// import frc.robot.subsystems.IntakeWrist.TargetPosition;

public class RobbieFTest implements Test
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



    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    private final Claw claw;
    private final Shuttle shuttle;
    private final IntakeWrist intakeWrist;
    private final Elevator elevator;

    private final Joystick joystick = new Joystick(0);
    // private final ExampleSubsystem exampleSubsystem;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public RobbieFTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        claw = robotContainer.getClaw();
        shuttle = robotContainer.getShuttle();
        intakeWrist = robotContainer.getIntakeWrist();
        elevator = robotContainer.getElevator();
        // this.exampleSubsystem = robotContainer.exampleSubsystem;

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

        

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {}

    // /**
    //  * This method runs periodically (every 20ms).
    //  */
    public void periodic()
    {
        // System.out.println("Position" + elevator.getLeftPosition());

        if (joystick.getRawButton(1))
        {
        
            claw.grabGamePieceCommand().schedule();

        }
        else if (joystick.getRawButton(2))
        {
            claw.stopCommand().schedule();
        }
        else if (joystick.getRawButton(3))
        {
           claw.ejectAlgaeCommand().schedule();
        }
        else 
        {
            claw.stopCommand().schedule();
        }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {} 
}
