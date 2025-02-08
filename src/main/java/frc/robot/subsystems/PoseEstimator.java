package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;
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

    public enum branchSide
    {
        kLeft,
        kRight;
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

    private boolean isRightBranch;

    // private final double[][] blueLeftBranchLocationArray = 
    // {{5.447446, 4.1859}, //S1 side
    //  {5.06044, 3.1693908}, //S2 side
    //  {3.888206, 3.1693908}, //S3 side
    //  {3.5312, 4.1859}, //S4 side
    //  {3.9381227, 4.9124092}, //S5 side
    //  {5.0405233, 4.9124092}}; //S6 side

    // private final double[][] blueRightBranchLocationArray = 
    // {{5.447446, 3.8659}, //S1 side
    //  {5.0405233, 3.1393908}, //S2 side
    //  {3.9381227, 3.1393908}, //S3 side
    //  {3.5312, 3.8659}, //S4 side
    //  {3.888206, 4.8824092}, //S5 side
    //  {5.09044, 4.8824092}}; //S6 side

    //  private final double[][] redLeftBranchLocationArray = 
    // {{12.100906, 3.8659}, //S1 side
    //  {12.457658, 4.8827472}, //S2 side
    //  {13.660146, 4.8824092}, //S3 side
    //  {14.016898, 3.8659}, //S4 side
    //  {13.6102433, 3.1393908}, //S5 side
    //  {12.5075747, 3.1393908}}; //S6 side

    // private final double[][] redRightBranchLocationArray = 
    // {{12.100906, 4.1859}, //S1 side
    //  {12.5075747, 4.9124092}, //S2 side
    //  {13.6102293, 4.9124092}, //S3 side
    //  {14.016898, 4.1859}, //S4 side
    //  {13.660146, 3.1693908}, //S5 side
    //  {12.457658, 3.1693908}}; //S6 side

     private final HashMap<Integer, Pose2d> leftBranchMap = new HashMap<Integer, Pose2d>();
     private final HashMap<Integer, Pose2d> rightBranchMap = new HashMap<Integer, Pose2d>();
     
    //  left.put(6, new Pose2d( new Translation2d(13.6102433, 3.1393908), new Rotation2d(Math.toRadians(30))));
 
    private final List<Pose2d> aprilTagLocations = new ArrayList<Pose2d>(){{
        new Pose2d(new Translation2d(5.321046, 4.0259), new Rotation2d(Math.toRadians(0.0))); // Blue S1
        new Pose2d(new Translation2d(4.90474, 3.306318), new Rotation2d(Math.toRadians(300.0))); // Blue S2
        new Pose2d(new Translation2d(4.073906, 3.306318), new Rotation2d(Math.toRadians(240.0))); // Blue S3
        new Pose2d(new Translation2d(3.6576, 4.0259), new Rotation2d(Math.toRadians(180.0))); // Blue S4
        new Pose2d(new Translation2d(4.073906, 4.745482), new Rotation2d(Math.toRadians(120.0))); // Blue S5
        new Pose2d(new Translation2d(4.90474, 4.745482), new Rotation2d(Math.toRadians(60.0))); // Blue S6
        new Pose2d(new Translation2d(12.227306, 4.0259), new Rotation2d(Math.toRadians(180.0))); // Red S1
        new Pose2d(new Translation2d(12.643358, 4.745482), new Rotation2d(Math.toRadians(120.0))); // Red S2
        new Pose2d(new Translation2d(13.474446, 4.745482), new Rotation2d(Math.toRadians(60.0))); // Red S3
        new Pose2d(new Translation2d(13.890498, 4.0259), new Rotation2d(Math.toRadians(0.0))); // Red S4
        new Pose2d(new Translation2d(13.474446, 3.306318), new Rotation2d(Math.toRadians(300.0))); // Red S5
        new Pose2d(new Translation2d(12.643358, 3.306318), new Rotation2d(Math.toRadians(240.0))); // Red S6
    
    }};

    // blah
    // blah.add();
    // aprilTagLocations.add(new Pose2d(5.321046, 4.0259, Math.degreesToRadians(0)));


    private double[][] scoringLocationArray;
    private branchSide branchSide;
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

        fillMaps();

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

    private void fillMaps()
    {
        //Red Left
        leftBranchMap.put(6, new Pose2d( new Translation2d(13.6102433, 3.1393908), new Rotation2d(Math.toRadians(30)))); //S5
        leftBranchMap.put(7, new Pose2d( new Translation2d(14.016898, 3.8659), new Rotation2d(Math.toRadians(90)))); //S4
        leftBranchMap.put(8, new Pose2d( new Translation2d(13.660146, 4.8824092), new Rotation2d(Math.toRadians(150)))); //S3
        leftBranchMap.put(9, new Pose2d( new Translation2d(12.457658, 4.8827472), new Rotation2d(Math.toRadians(210)))); //S2
        leftBranchMap.put(10, new Pose2d( new Translation2d(12.100906, 3.8659), new Rotation2d(Math.toRadians(270)))); //S1
        leftBranchMap.put(11, new Pose2d( new Translation2d(12.5075747, 3.1393908), new Rotation2d(Math.toRadians(-30)))); //S6
        //Blue Left
        leftBranchMap.put(17, new Pose2d( new Translation2d(3.888206, 3.1693908), new Rotation2d(Math.toRadians(-30)))); //S3
        leftBranchMap.put(18, new Pose2d( new Translation2d(3.5312, 4.1859), new Rotation2d(Math.toRadians(270)))); //S4
        leftBranchMap.put(19, new Pose2d( new Translation2d(3.9381227, 4.9124092), new Rotation2d(Math.toRadians(210)))); //S5
        leftBranchMap.put(20, new Pose2d( new Translation2d(5.0405233, 4.9124092), new Rotation2d(Math.toRadians(150)))); //S6
        leftBranchMap.put(21, new Pose2d( new Translation2d(5.447446, 4.1859), new Rotation2d(Math.toRadians(90)))); //S2
        leftBranchMap.put(22, new Pose2d( new Translation2d(5.06044, 3.1693908), new Rotation2d(Math.toRadians(30)))); //S1

        //Red Right
        rightBranchMap.put(6, new Pose2d( new Translation2d(13.660146, 3.1693908), new Rotation2d(Math.toRadians(30)))); //S5
        rightBranchMap.put(7, new Pose2d( new Translation2d(14.016898, 4.1859), new Rotation2d(Math.toRadians(90)))); //S4
        rightBranchMap.put(8, new Pose2d( new Translation2d(13.6102293, 4.9124092), new Rotation2d(Math.toRadians(150)))); //S3
        rightBranchMap.put(9, new Pose2d( new Translation2d(12.5075747, 4.9124092), new Rotation2d(Math.toRadians(210)))); //S2
        rightBranchMap.put(10, new Pose2d( new Translation2d(12.100906, 4.1859), new Rotation2d(Math.toRadians(270)))); //S1
        rightBranchMap.put(11, new Pose2d( new Translation2d(12.457658, 3.1693908), new Rotation2d(Math.toRadians(-30)))); //S6
        //Blue Right
        rightBranchMap.put(17, new Pose2d( new Translation2d(3.9381227, 3.1393908), new Rotation2d(Math.toRadians(-30)))); //S3
        rightBranchMap.put(18, new Pose2d( new Translation2d(3.5312, 3.8659), new Rotation2d(Math.toRadians(270)))); //S4
        rightBranchMap.put(19, new Pose2d( new Translation2d(3.888206, 4.8824092), new Rotation2d(Math.toRadians(210)))); //S5
        rightBranchMap.put(20, new Pose2d( new Translation2d(5.09044, 4.8824092), new Rotation2d(Math.toRadians(150)))); //S6
        rightBranchMap.put(21, new Pose2d( new Translation2d(5.447446, 3.8659), new Rotation2d(Math.toRadians(90)))); //S2
        rightBranchMap.put(22, new Pose2d( new Translation2d(5.0405233, 3.1393908), new Rotation2d(Math.toRadians(30)))); //S1
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

    public Pose2d getAprilTagPose(int index)
    {
        return aprilTagLocations.get(index);
    }

    public boolean isReefTag(double tagID)
    {
        if((tagID >= 6 && tagID <= 11) || (tagID >= 17 && tagID <= 22))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public BooleanSupplier isReefTagSupplier(int tagID)
    {
        if((tagID >= 6 && tagID <= 11) || (tagID >= 17 && tagID <= 22))
        {
            return () -> true;
        }
        else
        {
            return () -> false;
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

    public void chooseLocationArray()
    {
        // if(RobotContainer.isRedAllianceSupplier().getAsBoolean())
        // {

        // }

        for(int i = 0; i < 5; i++)
        {
            for(int n = 0; n < 2; n++)
            {
                // scoringLocationArray[i][n] = blueLeftBranchLocationArray[i][n];
            }
        }
    }

    // public double[] chooseClosestBranch(Pose2d aprilTag, boolean isRight)
    // {
    //     scoringLocationArray = (isRight ?  (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue ? blueRightBranchLocationArray : redRightBranchLocationArray) : (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue ? blueLeftBranchLocationArray : redLeftBranchLocationArray));
    //     double distance;
    //     double[] closestBranch = {};
    //     double distanceToClosestBranch = Double.MAX_VALUE;
    //     if(isPoseInsideField(aprilTag))
    //     {
    //         for(int b = 0; b < 6; b++)
    //         {
    //             distance = Math.sqrt(Math.pow(scoringLocationArray[b][0] - aprilTag.getX(), 2.0) + Math.pow(scoringLocationArray[b][1] - aprilTag.getY(), 2.0));
    //             if(distance < distanceToClosestBranch)
    //             {
    //                 distanceToClosestBranch = distance;
    //                 closestBranch = scoringLocationArray[b];
    //             }
    //         }
    //     }
    //     return closestBranch;
    // }

    public Pose2d closestBranchLocation(int aprilTagID, boolean isRight)
    {
        if(isRight)
        {
            return rightBranchMap.get(aprilTagID);
        }
        else
        {
            return leftBranchMap.get(aprilTagID);
        }
    }

    private boolean getIsRightBranch()
    {
        return isRightBranch;
    }

    private void setPlacingSideToLeft()
    {
        isRightBranch = false;
    }

    private void setPlacingSideToRight()
    {
        isRightBranch = true;
    }
       
    public Command setSideToLeftCommand()
    {
        return runOnce(() -> setPlacingSideToLeft()).withName("Set Placing Side To Left");
    }

    public Command setPlacingSideToRightCommand()
    {
        return runOnce(() -> setPlacingSideToRight()).withName("Set Placing Side To Right");
    }
        
    

    // public Pose2d closestAprilTag()
    // {
    //     Pose2d closest = Pose2d.nearest(aprilTagLocations);
    // }


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
            if(camera != null)
            {
                if(camera.getTagCount() > 0 && camera != null)
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
                    if(isReefTag(NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0))) // TODO: add if distance is less than x meters
                    {
                        estimatedPose = visionPose;
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