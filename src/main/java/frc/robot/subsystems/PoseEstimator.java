package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;
import frc.robot.sensors.GyroLance;
import frc.robot.sensors.Camera;

/**
 * This is an example of what a subsystem should look like.
 */
public class PoseEstimator extends SubsystemLance
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final GyroLance gyro;
    private final Drivetrain drivetrain;
    private final Camera[] cameraArray;

    private final SwerveDrivePoseEstimator poseEstimator;

    private final NetworkTable ASTable;

    // Kalman filter, experiment later
    private Matrix<N3, N1> visionStdDevs;
    private Matrix<N3, N1> stateStdDevs;

    private int totalTagCount = 0;

    // Inputs
    private Rotation2d gyroRotation;
    private SwerveModulePosition[] swerveModulePositions;

    // Outputs
    private Pose2d estimatedPose = new Pose2d();
    private DoubleArrayEntry poseEstimatorEntry;
    

    // *** INNER ENUMS and INNER CLASSES ***
    // Put all inner enums and inner classes here


    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public PoseEstimator(Drivetrain drivetrain, GyroLance gyro, Camera[] cameraArray)
    {
        super("PoseEstimator");
        System.out.println("  Constructor Started:  " + fullClassName);

        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.cameraArray = cameraArray;

        ASTable = NetworkTableInstance.getDefault().getTable(Constants.ADVANTAGE_SCOPE_TABLE_NAME);

        double[] doubleArray = {0.0, 0.0, 0.0};

        visionStdDevs = new Matrix<N3, N1>(Nat.N3(), Nat.N1(), doubleArray);
        stateStdDevs = new Matrix<N3, N1>(Nat.N3(), Nat.N1(), doubleArray);

        configStdDevs();

        if(drivetrain != null && gyro != null)
        {
            poseEstimator = new SwerveDrivePoseEstimator(
                drivetrain.getKinematics(), 
                gyro.getRotation2d(), 
                null, //drivetrain.getSwerveModulePositions(), TODO update once getter is made in drivetrain
                null, //drivetrain.getPose(), TODO update once getter is made in drivetrain
                stateStdDevs,
                visionStdDevs);
        }
        else
        {
            poseEstimator = null;
        }

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * Tells the PoseEstimator how much to trust both the odometry and the vision.  Higher values indicate less trust for either the swerve states or the vision
     */
    public void configStdDevs()
    {
        stateStdDevs.set(0, 0, 0.1); // x in meters
        stateStdDevs.set(1, 0, 0.1); // y in meters
        stateStdDevs.set(2, 0, 0.05); // heading in radians

        visionStdDevs.set(0, 0, 0.9); // x in meters
        visionStdDevs.set(1, 0, 0.9); // y in meters
        visionStdDevs.set(2, 0, 0.95); // heading in radians
    }

    public Pose2d getEstimatedPose()
    {
        if(poseEstimator != null)
        {
            return estimatedPose;
        }
        else
        {
            return new Pose2d();
        }
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
        // Use this for sensors that need to be read periodically.
        // Use this for data that needs to be logged.
        totalTagCount = 0;
        if (drivetrain != null && gyro != null) 
        {
            gyroRotation = gyro.getRotation2d();
            //swerveModulePositions = drivetrain.getSwerveModulePositions();

            // Updates odometry
            estimatedPose = poseEstimator.update(gyroRotation, swerveModulePositions);
        }
    }

    @Override
    public String toString()
    {
        return "";
    }
}