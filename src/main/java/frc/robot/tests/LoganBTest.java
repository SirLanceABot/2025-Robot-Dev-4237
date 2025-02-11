package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Claw;
// import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.PoseEstimator;

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
    private final Claw claw;
    private final Elevator elevator;
    private final PoseEstimator poseEstimator;
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
        claw = robotContainer.getClaw();
        elevator = robotContainer.getElevator();
        poseEstimator = robotContainer.getPoseEstimator();
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
     * BOW TO YOUR GOD
     */
    public void periodic()
    {
        if(joystick.getRawButton(1)) // A button
        {
            // climb.climbUpCommand().schedule();
            // pivot.moveToSetPositionCommand(TargetPosition.kL1).schedule(); // value of 100.0 from motor encoder
            // intake.pickupCommand().schedule();
            // intakeWrist.moveToSetPositionCommand(Position.kStartingPosition).schedule(); // 10.0
            // claw.grabGamePieceCommand().schedule();
            // elevator.moveToSetPositionCommand(TargetPosition.kGrabCoralPosition).schedule(); // 0.0
            poseEstimator.setPlacingSideToLeftCommand().schedule();
            System.out.println("Placing side set to Left");
        }
        else if(joystick.getRawButton(2))
        {
            poseEstimator.setPlacingSideToRightCommand().schedule();
            System.out.println("Placing side set to Right");
        }
        else if(joystick.getRawButton(3))
        {
            System.out.println("X: " + poseEstimator.getEstimatedPose().getX());
            System.out.println("Y: " + poseEstimator.getEstimatedPose().getY());
            System.out.println();

            double branchX = poseEstimator.closestBranchLocation((int) NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0), poseEstimator.getIsRightBranch()).getX();
            double branchY = poseEstimator.closestBranchLocation((int) NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0), poseEstimator.getIsRightBranch()).getY();

            System.out.println("Scoring Node X: " + branchX);
            System.out.println("Scoring Node Y: " + branchY);
            System.out.println("Scoring Node Rotation: " + poseEstimator.closestBranchLocation((int) NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0), poseEstimator.getIsRightBranch()).getRotation());

            System.out.println();
            System.out.println("Total Distance between camera and tag: " + Math.sqrt(Math.pow(branchX, 2) + Math.pow(branchY, 2)));
        }
        // else if(joystick.getRawButton(2)) // B button
        // {
        //     // climb.climbDown();
        //     // pivot.moveToSetPositionCommand(TargetPosition.kL2).schedule(); // value of 1.0 from motor encoder
        //     // intake.ejectCommand().schedule();
        //     // intakeWrist.moveToSetPositionCommand(Position.kShootingPosition).schedule(); // 20.0
            // claw.ejectAlgaeCommand().schedule();
        //     // elevator.moveToSetPositionCommand(TargetPosition.kStartingPosition).schedule(); // 20.0
        // }
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

