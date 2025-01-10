package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
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

    public Drivetrain()
    {
        super("Drivetrain");
        System.out.println("  Constructor Started:  " + fullClassName);


        System.out.println("  Constructor Finished: " + fullClassName);
    }
    
     @Override
    public void readPeriodicInputs() 
    {
        
    }

    @Override
    public void writePeriodicOutputs() 
    {
        
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
