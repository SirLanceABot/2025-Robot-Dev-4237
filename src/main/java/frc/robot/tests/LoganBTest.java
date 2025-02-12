package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.sensors.Camera;
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
    private final Camera camera;
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
        camera = robotContainer.getScoringSideCamera();
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
            // Retrieve the current estimated pose and the nearest scoring pose
            Pose2d currentPose = poseEstimator.getEstimatedPose();
            int tagId = (int) NetworkTableInstance.getDefault()
                    .getTable("limelight")
                    .getEntry("tid")
                    .getDouble(0);

            System.out.println("X: " + currentPose.getX());
            System.out.println("Y: " + currentPose.getY());

            if(tagId != 0)
            {
                boolean isRightBranch = poseEstimator.getIsRightBranch();
                Pose2d scoringPose = poseEstimator.closestBranchLocation(tagId, isRightBranch);

                // Calculate the total distance between the current pose and the scoring pose
                Transform2d poseDifference = scoringPose.minus(currentPose);
                double distance = Math.hypot(poseDifference.getX(), poseDifference.getY());

                // Print all relevant info on one line
                System.out.printf("Pose: (X: %.2f, Y: %.2f) | Scoring Node: (X: %.2f, Y: %.2f, Rot: %s) | Distance: %.2f%n",
                        currentPose.getX(), currentPose.getY(),
                        scoringPose.getX(), scoringPose.getY(), scoringPose.getRotation(),
                        distance);
            }
            else 
            {
                System.out.println("No tags found");
            }
            
        }
        System.out.println(camera.getTagCount());
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

