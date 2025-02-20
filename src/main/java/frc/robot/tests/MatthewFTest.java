package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.IntakeWrist.Position;

@SuppressWarnings("unused")
public class MatthewFTest implements Test
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

    // private final ExampleSubsystem exampleSubsystem;
    private final Joystick joystick = new Joystick(0);
    private final Intake intake;
    private final IntakeWrist intakeWrist;
    


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public MatthewFTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        this.intake = robotContainer.getIntake();
        this.intakeWrist = robotContainer.getIntakeWrist();
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

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        
        if (joystick.getRawButton(1))
        {
            intakeWrist.moveToSetPositionCommand(Position.kIntakeCoralPosition).schedule();
            // intake.pickupCoralCommand().schedule();
        }
        else if (joystick.getRawButton(2))
        {
            intakeWrist.moveToSetPositionCommand(Position.kManipAlgaePosition).schedule();
            // intake.ejectCoralCommand().schedule();
        }
        else if(joystick.getRawButton(4))
        {
            intakeWrist.moveToSetPositionCommand(Position.kRestingPosition).schedule();
        }
        else
        {
            intakeWrist.stopCommand().schedule();
        } 

        if(joystick.getRawButton(5))
        {
            intake.pickupCoralCommand().schedule();
        }
        else if(joystick.getRawButton(6))
        {
            intake.ejectCoralCommand().schedule();
        }
        else
        {
            intake.stopCommand().schedule();
        }

        System.out.println(intakeWrist.toString());
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        intakeWrist.stopCommand().schedule();
        intake.stopCommand().schedule();
    } 
}

