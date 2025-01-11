package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.motors.MotorControllerLance;
import frc.robot.motors.TalonFXLance;

class SwerveModule extends RobotDriveBase
{

    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final String moduleName;
    private final MotorControllerLance driveMotor;
    private final boolean driveMotorInverted;
    private final CANcoder turnEncoder;
    private final double turnEncoderOffset;
    private final MotorControllerLance turnMotor;
    //TODO Tune PIDS
    private final PIDController drivePIDController = new PIDController(0.0, 0.0, 0.0);
    private final ProfiledPIDController turningPIDController =
            new ProfiledPIDController(
                0.0, 0.0, 0.0,
                //Enter max turn speed and acceleration
                new TrapezoidProfile.Constraints(0.0, 0.0));
    //How much speed it takes to move, how much additional speed per volt, how much additional acceleration per volt
    private final SimpleMotorFeedforward driveFeedforward = new SimpleMotorFeedforward(0.0, 0.0, 0.0);
    
    private final SimpleMotorFeedforward turnFeedforward = new SimpleMotorFeedforward(0.0, 0.0, 0.0);

     // *** CLASS CONSTRUCTORS ***
    SwerveModule(SwerveModuleConfig smd)
    {
        moduleName = smd.moduleName;
        System.out.println("  Constructor Started:  " + fullClassName + " >> " + moduleName);

        driveMotor = new TalonFXLance(smd.driveMotorChannel, "rio", smd.moduleName + "Drive Motor");
        driveMotorInverted = smd.driveMotorInverted;
        turnEncoder = new CANcoder(smd.turnEncoderChannel, "rio");  
        turnEncoderOffset = smd.turnEncoderOffset;
        turnMotor = new TalonFXLance(smd.turnMotorChannel, "rio", smd.moduleName + "Turn Motor");

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + moduleName);

    }

    public SwerveModulePosition getPosition() 
    {
        return new SwerveModulePosition(getDrivingEncoderPosition(), Rotation2d.fromDegrees(getTurningEncoderPosition()));
    }


    public SwerveModuleState getState()
    {
        return new SwerveModuleState(getDrivingEncoderRate(), Rotation2d.fromDegrees(getTurningEncoderPosition()));
    }


    public double getDrivingEncoderRate()
    {
        //Rotations per second
        double velocity = driveMotor.getVelocity();
        
        return velocity;

    }

    public double getDrivingEncoderPosition()
    {
        //Rotations
        double position = driveMotor.getPosition();
        
        return position;
    }


    public double getTurningEncoderPosition()
    {
        //returns position in degrees
        return turnEncoder.getAbsolutePosition().getValueAsDouble() * 360.0;
    }

    /**
     * Normalizes voltage from -1 to 1 using current battery voltage
     * 
     * @param outputVolts
     * @return normalizedVoltage
     */
    //TODO find out max battery voltage (replace 13.0)
    public static double normalizeVoltage(double outputVolts)
    {
        return MathUtil.clamp(outputVolts / 13.0, -1.0, 1.0); //RobotController.getBatteryVoltage();
    }

    /**
     * Sets the desired state for the module.
     *
     * @param desiredState Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState desiredState)
    {
        double driveP = SmartDashboard.getNumber("Drive P", 0.0);
        double driveD = SmartDashboard.getNumber("Drive D", 0.0);
        drivePIDController.setP(driveP);
        drivePIDController.setD(driveD);

        final double driveOutput =
            drivePIDController.calculate(getDrivingEncoderRate(), desiredState.speedMetersPerSecond);

        final double driveFeedforwardValue = driveFeedforward.calculate(desiredState.speedMetersPerSecond);
       
        final double turnOutput = turningPIDController.calculate(getTurningEncoderPosition(), desiredState.angle.getDegrees());


        final double turnFeedforwardValue =
            turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);

        //FIXME Convert to Talon FX
        double normalizedDriveVoltage = normalizeVoltage(driveOutput + driveFeedforwardValue);
        double normalizedTurnVoltage = normalizeVoltage(turnOutput + turnFeedforwardValue);

        // TODO Is this the same as output
        // driveMotor.set(ControlMode.PercentOutput, normalizedDriveVoltage);
        driveMotor.set(normalizedDriveVoltage);

        // Used for running PIDF on TalonFX
        // var ticksPer100MS = state.speedMetersPerSecond * (1 / Constants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC);
        // System.out.println("M/S: " + state.speedMetersPerSecond + ", Ticks/100MS: " + ticksPer100MS);
        // driveMotor.set(ControlMode.Velocity, ticksPer100MS);
        // getDrivingEncoderRate();

        turnMotor.set(normalizedTurnVoltage);
    }


    public void stopModule()
    {
        // driveMotor.set(ControlMode.PercentOutput, 0.0);
        driveMotor.set(0.0);

        // turnMotor.set(ControlMode.PercentOutput, 0.0);
        turnMotor.set(0.0);
    }


    @Override
    public String getDescription()
    {
        return "Swerve " + moduleName;
    }

    @Override
    public void stopMotor()
    {
        stopModule();
    }

}
