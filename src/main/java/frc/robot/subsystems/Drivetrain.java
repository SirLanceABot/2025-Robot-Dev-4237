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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.controls.AdaptiveSlewRateLimiter;
import frc.robot.sensors.GyroLance;

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



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here

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

    private final GyroLance gyro;
    private final SwerveDriveKinematics kinematics;

    //Acceleration and decceleration limits (10.0 is last year's value)
    private final AdaptiveSlewRateLimiter adaptiveXRateLimiter = new AdaptiveSlewRateLimiter(10.0, 10.0);
    private final AdaptiveSlewRateLimiter adaptiveYRateLimiter = new AdaptiveSlewRateLimiter(10.0, 10.0);

    private SwerveModulePosition frontLeftPosition;
    private SwerveModulePosition frontRightPosition;
    private SwerveModulePosition backLeftPosition;
    private SwerveModulePosition backRightPosition;

    private ChassisSpeeds chassisSpeeds;
    private SwerveDriveOdometry odometry;
    private SwerveModuleState[] inputSwerveModuleStates;
    
    


    public Drivetrain(GyroLance gyro, boolean useFullRobot, boolean usePoseEstimator, BooleanSupplier isRedAllianceSupplier)
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



    private void lockWheels()
    {                
        inputSwerveModuleStates[0] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
        inputSwerveModuleStates[1] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
        inputSwerveModuleStates[2] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
        inputSwerveModuleStates[3] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));

        setSwerveModuleStates(inputSwerveModuleStates);
    }
    

    private void drive(double xSpeed, double ySpeed, double turnSpeed, boolean fieldRelative, boolean useSlewRateLimiter)
    {
        if(Math.abs(xSpeed) < 0.04)
            xSpeed = 0.0;
        if(Math.abs(ySpeed) < 0.04)
            ySpeed = 0.0;
        if(Math.abs(turnSpeed) < 0.04)
            turnSpeed = 0.0;    

        if(useSlewRateLimiter)
        {
            xSpeed = adaptiveXRateLimiter.calculate(xSpeed);
            ySpeed = adaptiveYRateLimiter.calculate(ySpeed);
        }
    

        if(fieldRelative)
            chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, turnSpeed, gyro.getRotation2d());
        else
            chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);

        //Using Chassis speeds
        inputSwerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);

        //Makes sure wheel speeds aren't too high
        SwerveDriveKinematics.desaturateWheelSpeeds(inputSwerveModuleStates, 10.0);

        setSwerveModuleStates(inputSwerveModuleStates);
    }


    private void setSwerveModuleStates(SwerveModuleState[] states)
    {
        frontLeft.setDesiredState(states[0]);
        frontRight.setDesiredState(states[1]);
        backLeft.setDesiredState(states[2]);
        backRight.setDesiredState(states[3]);
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

    public Command driveCommand(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn, DoubleSupplier scale)
    {
        return
        Commands.runOnce(
                () -> drive(
                    xSpeed.getAsDouble() * scale.getAsDouble(),
                    ySpeed.getAsDouble() * scale.getAsDouble(),
                    turn.getAsDouble() * scale.getAsDouble(), 
                    true,
                    true), this
                )
                .withName("driveCommand()");
    }

    public Command lockWheelsCommand()
    {
        return Commands.runOnce(() -> lockWheels(), this).withName("lockWheelsCommand()");
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
