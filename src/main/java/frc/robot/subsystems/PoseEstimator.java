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
    private final CommandSwerveDrivetrain drivetrain;
    private final Camera[] cameraArray;

    private final SwerveDrivePoseEstimator poseEstimator;

    private final NetworkTable ASTable;
    private final double fieldXDimension = 17.5482504;
    private final double fieldYDimension = 8.0519016;
    private final double[] defaultPosition = {0.0, 0.0, 0.0};

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


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new PoseEstimator. 
     */
    public PoseEstimator(CommandSwerveDrivetrain drivetrain, GyroLance gyro, Camera[] cameraArray)
    {
        super("PoseEstimator");
        System.out.println("  Constructor Started:  " + fullClassName);

        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.cameraArray = cameraArray;

        ASTable = NetworkTableInstance.getDefault().getTable(Constants.ADVANTAGE_SCOPE_TABLE_NAME);
        // This is where the robot starts in AdvantageScope
        poseEstimatorEntry = ASTable.getDoubleArrayTopic("PoseEstimator").getEntry(defaultPosition);

        double[] doubleArray = {0.0, 0.0, 0.0};

        visionStdDevs = new Matrix<N3, N1>(Nat.N3(), Nat.N1(), doubleArray);
        stateStdDevs = new Matrix<N3, N1>(Nat.N3(), Nat.N1(), doubleArray);

        configStdDevs();

        if(drivetrain != null && gyro != null)
        {
            poseEstimator = new SwerveDrivePoseEstimator(
                drivetrain.getKinematics(), 
                gyro.getRotation2d(), 
                drivetrain.getState().ModulePositions,
                drivetrain.getState().Pose,
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

    /**
     * gets the estimated pose updated by both the odometry and the vision
     * @return estimated pose
     */
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

    /**
     * checks if the pose given is within the field boundaries in meters
     * @param pose
     * @return true or false
     */
    public boolean isPoseInsideField(Pose2d pose)
    {
        if((pose.getX() > -1.0 && pose.getX() < fieldXDimension + 1.0) && (pose.getY() > -1.0 && pose.getY() < fieldYDimension + 1.0))
        {
            return true;
        }
        else
        {
            return false;
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
            swerveModulePositions = drivetrain.getState().ModulePositions;

            // Updates odometry
            estimatedPose = poseEstimator.update(gyroRotation, swerveModulePositions);
        }

        //does this for each camera in the camera array
        for(Camera camera : cameraArray)
        {
            if(camera.getTagCount() > 0)
            {
                if(camera != null)
                {
                    Pose2d visionPose = camera.getPose();

                    // only updates the pose with the cameras if the pose shown by the vision is within the field limits
                    if(isPoseInsideField(visionPose)) // maybe don't check if inside field in order to make pose more accurate or find different solution later
                    {
                        totalTagCount += camera.getTagCount();
                        poseEstimator.addVisionMeasurement(
                            visionPose,
                            camera.getTimestamp(),
                            visionStdDevs);//visionStdDevs.times(camera.getAverageTagDistance() * 0.5));
                    }
                }
            }
        }

        // maybe combine this if statement with the one above
        if(totalTagCount >= 2)
        {
            //TODO: Updating the drivetrain's odometry might not be necessary
            // drivetrain.resetOdometryOnly(poseEstimator.getEstimatedPosition());
        }

        //OUTPUTS
        if(drivetrain != null && gyro != null && poseEstimator != null)
        {
            estimatedPose = poseEstimator.getEstimatedPosition();

            //puts pose onto AdvantageScope
            double[] pose = {estimatedPose.getX(), estimatedPose.getY(), estimatedPose.getRotation().getDegrees()};
            poseEstimatorEntry.set(pose);
        }
    }

    @Override
    public String toString()
    {
        return "Estimated Pose: " + getEstimatedPose();
    }
}