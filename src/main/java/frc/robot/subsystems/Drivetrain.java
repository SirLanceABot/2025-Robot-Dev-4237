package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;
import frc.robot.sensors.Gyro4237;

public class Drivetrain extends SubsystemLance
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

    private enum DriveMode
    {
        kDrive, kLockwheels, kStop, kArcadeDrive;
    }

    public enum ArcadeDriveDirection
    {
        kStraight(0.0), kStrafe(90.0);

        public double value;
        
        private ArcadeDriveDirection(double value)
        {
            this.value =  value;
        }
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private DriveMode driveMode = DriveMode.kDrive;

    //TODO Find out wheelbase and trackwidth values
    //Location of modules relative to robot center
    private final Translation2d frontLeftLocation = new Translation2d(0.0 / 2, 0.0 / 2);
    private final Translation2d frontRightLocation = new Translation2d(0.0 / 2, -0.0 / 2);
    private final Translation2d backLeftLocation = new Translation2d(-0.0 / 2, 0.0 / 2);
    private final Translation2d backRightLocation = new Translation2d(-0.0 / 2, -0.0 / 2);


    //TODO find encoder offsets, and which drive motors need to be inverted
    private final SwerveModuleConfig frontLeftSwerveModule = new SwerveModuleConfig(
        "Front Left", frontLeftLocation, Constants.Drivetrain.FRONT_LEFT_DRIVE_PORT, false, Constants.Drivetrain.FRONT_LEFT_ENCODER_PORT, 0.0, Constants.Drivetrain.FRONT_LEFT_TURN_PORT);
    private final SwerveModuleConfig frontRightSwerveModule = new SwerveModuleConfig(
        "Front Right", frontRightLocation, Constants.Drivetrain.FRONT_RIGHT_DRIVE_PORT, false, Constants.Drivetrain.FRONT_RIGHT_ENCODER_PORT, 0.0, Constants.Drivetrain.FRONT_RIGHT_TURN_PORT);
    private final SwerveModuleConfig backLeftSwerveModule = new SwerveModuleConfig(
        "Back Left", backLeftLocation, Constants.Drivetrain.BACK_LEFT_DRIVE_PORT, false, Constants.Drivetrain.BACK_LEFT_ENCODER_PORT, 0.0, Constants.Drivetrain.BACK_LEFT_TURN_PORT);
    private final SwerveModuleConfig backRightSwerveModule = new SwerveModuleConfig(
        "Back Right", backRightLocation, Constants.Drivetrain.BACK_RIGHT_DRIVE_PORT, false, Constants.Drivetrain.BACK_RIGHT_ENCODER_PORT, 0.0, Constants.Drivetrain.BACK_RIGHT_TURN_PORT); 
    
    private final SwerveModule frontLeft;
    private final SwerveModule frontRight;
    private final SwerveModule backLeft;
    private final SwerveModule backRight;

    private final Gyro4237 gyro;
    private final SwerveDriveKinematics kinematics;

    private double xSpeed;
    private double ySpeed;
    private double turnSpeed;
    private SwerveModulePosition frontLeftPosition;
    private SwerveModulePosition frontRightPosition;
    private SwerveModulePosition backLeftPosition;
    private SwerveModulePosition backRightPosition;

    private ChassisSpeeds chassisSpeeds;
    private SwerveDriveOdometry odometry;
    private SwerveModuleState[] inputSwerveModuleStates;
    private boolean fieldRelative = true;

    
    


    public Drivetrain(Gyro4237 gyro, boolean useFullRobot, boolean usePoseEstimator, BooleanSupplier isRedAllianceSupplier)
    {   
        super("Drivetrain");
        System.out.println("  Constructor Started:  " + fullClassName);


        frontLeft = new SwerveModule(frontLeftSwerveModule);
        frontRight = new SwerveModule(frontRightSwerveModule);
        backLeft = new SwerveModule(backLeftSwerveModule);
        backRight = new SwerveModule(backRightSwerveModule);

        kinematics = new SwerveDriveKinematics(
            frontLeftLocation,
            frontRightLocation,
            backLeftLocation,
            backRightLocation);

        this.gyro = gyro;

        odometry = new SwerveDriveOdometry(
            kinematics, 
            gyro.getRotation2d(),
            new SwerveModulePosition[] 
            {
                frontLeft.getPosition(),
                frontRight.getPosition(),
                backLeft.getPosition(),
                backRight.getPosition()
            });


        System.out.println("  Constructor Finished: " + fullClassName);
    }


    public SwerveDriveKinematics getKinematics()
    {
        return kinematics;
    }


    public void driveRobotRelative(ChassisSpeeds chassisSpeeds)
    {
        driveMode = DriveMode.kDrive;
        inputSwerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);
        //TODO: Find out max speed, 10 was last year's value
        SwerveDriveKinematics.desaturateWheelSpeeds(inputSwerveModuleStates, 10.0);
    }

    public void drive(double xSpeed, double ySpeed, double turnSpeed, boolean fieldRelative, boolean useSlewRateLimiter)
    {
        driveMode = DriveMode.kDrive;

        if(Math.abs(xSpeed) < 0.04)
            xSpeed = 0.0;
        if(Math.abs(ySpeed) < 0.04)
            ySpeed = 0.0;
        if(Math.abs(turnSpeed) < 0.04)
            turnSpeed = 0.0;    
        
        
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;

        this.turnSpeed = turnSpeed;
        this.fieldRelative = fieldRelative;
    
    }


    public ChassisSpeeds getRobotRelativeSpeeds()
    {
        SwerveModuleState[] moduleStates = new SwerveModuleState[] 
            {
                frontLeft.getState(),
                frontRight.getState(),
                backLeft.getState(),
                backRight.getState()
            }; 
        
        return kinematics.toChassisSpeeds(moduleStates);
    }

     @Override
    public void readPeriodicInputs() 
    {}

    @Override
    public void writePeriodicOutputs() 
    {}

    @Override
    public void periodic()
    {
        switch (driveMode)
        {
            case kDrive:

                if(fieldRelative)
                    chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, turnSpeed, gyro.getRotation2d());
                else
                    chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);

                //Using Chassis speeds
                inputSwerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);

                //Makes sure wheel speeds aren't too high
                SwerveDriveKinematics.desaturateWheelSpeeds(inputSwerveModuleStates, 10.0);
                break;

            case kLockwheels:

                inputSwerveModuleStates = new SwerveModuleState[4];
                
                inputSwerveModuleStates[0] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
                inputSwerveModuleStates[1] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
                inputSwerveModuleStates[2] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
                inputSwerveModuleStates[3] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
                break;

            case kArcadeDrive:
                //TODO: Find out max speed, 10 was last year's value
                //Makes sure wheel speeds aren't too high
                SwerveDriveKinematics.desaturateWheelSpeeds(inputSwerveModuleStates, 10.0);
                break;

            case kStop:
                //No calculations to do
                break;

            
        }
        
        frontLeft.setDesiredState(inputSwerveModuleStates[0]);
        frontRight.setDesiredState(inputSwerveModuleStates[1]);
        backLeft.setDesiredState(inputSwerveModuleStates[2]);
        backRight.setDesiredState(inputSwerveModuleStates[3]);
    }

    @Override
    public String toString()
    {
        return "";
    }

}
