package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import java.util.List;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.SparkMaxLance;

@SuppressWarnings("unused")
public class JWoodTest implements Test
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
    // private final SparkMaxLance motor = new SparkMaxLance(5, Constants.ROBORIO, "Test Motor");

    private Pose2d aprilTagPose, desiredLeftPostRobotPose, desiredRightPostRobotPose;
    private Translation2d cwPostTranslation, ccwPostTranslation;
    private Rotation2d aprilTagRotation, robotRotation;
    private Transform2d leftPostTransform, rightPostTransform;

    private final AprilTagFieldLayout aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);
    private final List<AprilTag> aprilTagList = aprilTagFieldLayout.getTags();

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public JWoodTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // this.exampleSubsystem = robotContainer.exampleSubsystem;

        // motor.setupFactoryDefaults();
        // motor.setupInverted(true);

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
    {
        aprilTagTest();
    }

    private void aprilTagTest()
    {
        cwPostTranslation = new Translation2d(0.49, -0.33);
        ccwPostTranslation = new Translation2d(0.49, 0.03);
        robotRotation = new Rotation2d(Units.degreesToRadians(-90.0));

        System.out.println();
        for(int i = 1; i <= 22; i++)
        {
            if(isReefTag(i))
            {
                aprilTagPose = aprilTagFieldLayout.getTagPose(i).get().toPose2d();

                if(isBackSideOfReefTag(i))
                {
                    // Swap posts because it is from the perspective of the driver station
                    leftPostTransform = new Transform2d(ccwPostTranslation, robotRotation);
                    rightPostTransform = new Transform2d(cwPostTranslation, robotRotation);
                }
                else
                {
                    leftPostTransform = new Transform2d(cwPostTranslation, robotRotation);
                    rightPostTransform = new Transform2d(ccwPostTranslation, robotRotation);
                }
                desiredRightPostRobotPose = aprilTagPose.transformBy(rightPostTransform);
                desiredLeftPostRobotPose = aprilTagPose.transformBy(leftPostTransform);

                System.out.println("Tag " + i);
                System.out.println("April Tag Pose:     " + poseToString(aprilTagPose));
                System.out.println("Desired Left Pose:  " + poseToString(desiredLeftPostRobotPose));
                System.out.println("Desired Right Pose: " + poseToString(desiredRightPostRobotPose) + "\n");
            }
        }
    }

    private String poseToString(Pose2d pose)
    {
        return pose.getTranslation() + " " + pose.getRotation().getDegrees();
    }

    private boolean isBackSideOfReefTag(int tagID)
    {
        return (tagID >= 9 && tagID <= 11 || tagID >= 20 && tagID <= 22);
    }

    private boolean isReefTag(int tagID)
    {
        return (tagID >= 6 && tagID <= 11 || tagID >= 17 && tagID <= 22);
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // motor.set(0.05);
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        // motor.set(0.0);
    } 
}

/*

Tag 6
April Tag Pose:     Translation2d(X: 13.47, Y: 3.31) -59.99999999999999
Desired Left Pose:  Translation2d(X: 13.29, Y: 2.62) -149.99999999999997
Desired Right Pose: Translation2d(X: 14.16, Y: 3.12) -149.99999999999997

Tag 7
April Tag Pose:     Translation2d(X: 13.89, Y: 4.03) 0.0
Desired Left Pose:  Translation2d(X: 14.39, Y: 3.53) -90.0  3.70
Desired Right Pose: Translation2d(X: 14.39, Y: 4.53) -90.0  4.00

Tag 8
April Tag Pose:     Translation2d(X: 13.47, Y: 4.75) 59.99999999999999
Desired Left Pose:  Translation2d(X: 14.16, Y: 4.93) -30.00000000000001
Desired Right Pose: Translation2d(X: 13.29, Y: 5.43) -30.00000000000001

Tag 9
April Tag Pose:     Translation2d(X: 12.64, Y: 4.75) 119.99999999999999
Desired Left Pose:  Translation2d(X: 11.96, Y: 4.93) 29.999999999999993
Desired Right Pose: Translation2d(X: 12.83, Y: 5.43) 29.999999999999993

Tag 10
April Tag Pose:     Translation2d(X: 12.23, Y: 4.03) 180.0
Desired Left Pose:  Translation2d(X: 11.73, Y: 3.53) 90.0  4.00
Desired Right Pose: Translation2d(X: 11.73, Y: 4.53) 90.0  4.37

Tag 11
April Tag Pose:     Translation2d(X: 12.64, Y: 3.31) -120.00000000000004
Desired Left Pose:  Translation2d(X: 12.83, Y: 2.62) 149.99999999999994
Desired Right Pose: Translation2d(X: 11.96, Y: 3.12) 149.99999999999994

Tag 17
April Tag Pose:     Translation2d(X: 4.07, Y: 3.31) -120.00000000000004
Desired Left Pose:  Translation2d(X: 3.39, Y: 3.12) 149.99999999999994
Desired Right Pose: Translation2d(X: 4.26, Y: 2.62) 149.99999999999994

Tag 18
April Tag Pose:     Translation2d(X: 3.66, Y: 4.03) 180.0
Desired Left Pose:  Translation2d(X: 3.16, Y: 4.53) 90.0
Desired Right Pose: Translation2d(X: 3.16, Y: 3.53) 90.0

Tag 19
April Tag Pose:     Translation2d(X: 4.07, Y: 4.75) 119.99999999999999
Desired Left Pose:  Translation2d(X: 4.26, Y: 5.43) 29.999999999999993
Desired Right Pose: Translation2d(X: 3.39, Y: 4.93) 29.999999999999993

Tag 20
April Tag Pose:     Translation2d(X: 4.90, Y: 4.75) 59.99999999999999
Desired Left Pose:  Translation2d(X: 4.72, Y: 5.43) -30.00000000000001
Desired Right Pose: Translation2d(X: 5.59, Y: 4.93) -30.00000000000001

Tag 21
April Tag Pose:     Translation2d(X: 5.32, Y: 4.03) 0.0
Desired Left Pose:  Translation2d(X: 5.82, Y: 4.53) -90.0
Desired Right Pose: Translation2d(X: 5.82, Y: 3.53) -90.0

Tag 22
April Tag Pose:     Translation2d(X: 4.90, Y: 3.31) -59.99999999999999
Desired Left Pose:  Translation2d(X: 5.59, Y: 3.12) -149.99999999999997
Desired Right Pose: Translation2d(X: 4.72, Y: 2.62) -149.99999999999997

*/