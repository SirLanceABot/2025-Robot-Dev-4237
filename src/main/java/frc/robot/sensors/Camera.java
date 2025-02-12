package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.PoseEstimator;

/**
 * Use this class as a template to create other sensors.
 */
public class Camera extends SensorLance
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
    private String cameraName;
    private LimelightHelpers.PoseEstimate poseEstimate;
    private double[] poseArray = new double[3];

    private DoubleEntry yawEntry;
    private DoubleArrayEntry poseEntry;
    private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable(Constants.ADVANTAGE_SCOPE_TABLE_NAME);


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Camera. 
     */
    public Camera(String cameraName)
    {   
        super("Camera");
        this.cameraName = cameraName;
        System.out.println("  Constructor Started:  " + fullClassName + ">>" + cameraName);

        poseEntry = ASTable.getDoubleArrayTopic(cameraName + " pose").getEntry(new double[3]);
        yawEntry = ASTable.getDoubleTopic("GyroYaw").getEntry(0.0);

        poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);

        System.out.println("  Constructor Finished: " + fullClassName + ">>" + cameraName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public Pose2d getPose()
    {
        return poseEstimate.pose;
    }

    public double getTimestamp()
    {
        return poseEstimate.timestampSeconds;
    }

    public int getTagCount()
    {
        return poseEstimate.tagCount;
    }

    public double avgTagDistance()
    {
        return poseEstimate.avgTagDist;
    }
    

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);

        // poseArray[0] = poseEstimate.pose.getX();
        // poseArray[1] = poseEstimate.pose.getY();
        // poseArray[2] = poseEstimate.pose.getRotation().getDegrees();

        // poseEntry.set(poseArray);

        LimelightHelpers.SetRobotOrientation(cameraName, yawEntry.get(), 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public String toString()
    {
        return "";
    }
}