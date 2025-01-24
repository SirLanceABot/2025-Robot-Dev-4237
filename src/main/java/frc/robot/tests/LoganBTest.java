package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Grabber;
import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.Pivot;

public class LoganBTest implements Test
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
    private final Climb climb;
    private final Pivot pivot;
    private final Intake intake;
    private final IntakeWrist intakeWrist;
    private final Grabber grabber;
    private final Elevator elevator;
    private final Joystick joystick = new Joystick(0);
    // private final ExampleSubsystem exampleSubsystem;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public LoganBTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // this.exampleSubsystem = robotContainer.exampleSubsystem;
        climb = robotContainer.getClimb();
        pivot = robotContainer.getPivot();
        intake = robotContainer.getIntake();
        intakeWrist = robotContainer.getIntakeWrist();
        grabber = robotContainer.getGrabber();
        elevator = robotContainer.getElevator();
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
        if(joystick.getRawButton(1)) // A button
        {
            // climb.climbUp();
            // pivot.moveToSetPositionCommand(TargetPosition.kL1).schedule(); // value of 100.0 from motor encoder
            intake.pickupCommand().schedule();
            // intakeWrist.moveToSetPositionCommand(Position.kStartingPosition).schedule(); // 10.0
            // grabber.grabGamePieceCommand().schedule();
            // elevator.moveToSetPositionCommand(TargetPosition.kGrabCoralPosition).schedule(); // 0.0
        }
        // else if(joystick.getRawButton(2)) // B button
        // {
        //     // climb.climbDown();
        //     // pivot.moveToSetPositionCommand(TargetPosition.kL2).schedule(); // value of 1.0 from motor encoder
        //     // intake.ejectCommand().schedule();
        //     // intakeWrist.moveToSetPositionCommand(Position.kShootingPosition).schedule(); // 20.0
        //     // grabber.ejectAlgaeCommand().schedule();
        //     // elevator.moveToSetPositionCommand(TargetPosition.kStartingPosition).schedule(); // 20.0
        // }
        else
        {
            // elevator.stopCommand().schedule();
            // intakeWrist.stopCommand().schedule();
            intake.stopCommand().schedule();
        }
        // else if(joystick.getRawButton(3)) // X button
        // {
        //     // pivot.moveToSetPositionCommand(TargetPosition.kL3).schedule();
        // }
        // else if(joystick.getRawButton(4)) // Y button
        // {
        //     // pivot.moveToSetPositionCommand(TargetPosition.kL4).schedule();
        // }

        // System.out.println(intakeWrist.getPosition());
        // System.out.println(intakeWrist.getPosition());
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {} 
}

