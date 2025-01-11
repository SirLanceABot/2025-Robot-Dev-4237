package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

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
    private class PeriodicData
    {
        // INPUTS
        private double xSpeed;
        private double ySpeed;
        private double turnSpeed;
        private boolean fieldRelative;
        private SwerveModulePosition frontLeftPosition;
        private SwerveModulePosition frontRightPosition;
        private SwerveModulePosition backLeftPosition;
        private SwerveModulePosition backRightPosition;


        // OUTPUTS
        private ChassisSpeeds chassisSpeeds;
        private SwerveDriveOdometry odometry;
        private SwerveModuleState[] inputSwerveModuleStates;

        

    }

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
    private final PeriodicData periodicData = new PeriodicData();
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

    private final SwerveDriveKinematics kinematics;
    


    public Drivetrain()
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


        System.out.println("  Constructor Finished: " + fullClassName);
    }


    public SwerveDriveKinematics getKinematics()
    {
        return kinematics;
    }


    public void driveRobotRelative(ChassisSpeeds chassisSpeeds)
    {
        driveMode = DriveMode.kDrive;
        periodicData.fieldRelative = false;
        periodicData.inputSwerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);
        //TODO: Find out max speed, 10 was last year's value
        SwerveDriveKinematics.desaturateWheelSpeeds(periodicData.inputSwerveModuleStates, 10.0);
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
    {
        
    }

    @Override
    public void writePeriodicOutputs() 
    {
        // frontLeft.setDesiredState(periodicData.inputSwerveModuleStates[0]);
        // frontRight.setDesiredState(periodicData.inputSwerveModuleStates[1]);
        // backLeft.setDesiredState(periodicData.inputSwerveModuleStates[2]);
        // backRight.setDesiredState(periodicData.inputSwerveModuleStates[3]);
        
    }

    @Override
    public void periodic()
    {

    }

    @Override
    public String toString()
    {
        return "";
    }

}
