package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner14;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.Constants.TargetPosition;
import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;

/**
 * This is the Pivot. It allows the robot to move it's arm.
 */
public class Pivot extends SubsystemLance
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
       
    // inputs
    private double currentPosition;
    // private double currentAngle;
    // private double currentVelocity;

    // output
    private double motorSpeed = 0.0;
    
    // private final SparkMaxLance motor = new SparkMaxLance(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Pivot Motor");
    private final TalonFXLance motor = new TalonFXLance(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Pivot Motor");
    // private final SparkMaxLance followMotor = new SparkMaxLance(Constants.Pivot.LEFT_MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Left Pivot Motor");

    // private SparkLimitSwitch forwardLimitSwitch;
    // private SparkLimitSwitch reverseLimitSwitch;

    // private TargetPosition targetPosition = TargetPosition.kOverride;
    private final double threshold = 1.0;

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    
    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);
        configPivotMotors();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    // private void configCANcoder()
    // {
    //     canCoderConfig = new CANcoderConfiguration();
    //     // values go here once decided
    //     setup(() -> cancoder.getConfigurator().apply(canCoderCOnfig), "Setup CANcoder"); //sends all values to the device
    // }

    private void configPivotMotors()
    {
    //     // Factory Defaults
        motor.setupFactoryDefaults();
    //     followMotor.setupFactoryDefaults();
        motor.setupBrakeMode();
    //     followMotor.setupBrakeMode();
        motor.setupInverted(true);
    //     followMotor.setupInverted(true);
        motor.setPosition(0.0);
    //     followMotor.setPosition(0.0);
        // leadMotor.setupFollower(Constants.Pivot.RIGHT_MOTOR_PORT, true);

    //     // Hard Limits
        motor.setupForwardHardLimitSwitch(false, false);
        motor.setupReverseHardLimitSwitch(false, false);
        
        motor.setupPIDController(0,0.2,0,0);

        //Configure PID Controller
        // pidController.setP(kP);
        // pidController.setI(kI);
        // pidController.setD(kD);
        // pidController.setIZone(kIz);
        // pidController.setFF(kFF);
        // pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    // /**
    // * This turns on the motor.
    // * @param motorSpeed
    // */
    private void set(double motorSpeed)
    {
        // targetPosition = Constants.TargetPosition.kOverride;
        motor.set( MathUtil.clamp(motorSpeed, -0.2, 0.2));
    }

    public void stop()
    {
        set(0.0);
    }

    // public void hold()
    // {
    //     targetPosition = Constants.TargetPosition.kOverride;
    //     motor.set(0.0);
    // }

    // public void resetEncoder()
    // {
    //     resetState = ResetState.kStart;
    // }

    /** @return encoder ticks (double) */
    public double getPosition() // encoder ticks
    {
        return motor.getPosition();
    }

    // ask how to do this
    /** returns the current angle of arm
     *  @return angle (double) 
    */
    // public double getAngle()
    // {
    //     return currentAngle;
    // }

    /** move the shoulder to Level 1 */
    // public void L1()
    // {
    //     targetPosition = Constants.TargetPosition.kL1;
    // }

    // /** move the shoulder to Level 2*/
    // public void L2()
    // {
    //     targetPosition = Constants.TargetPosition.kL2;
    // }

    // /** move the shoulder to Level 3 */
    // public void L3()
    // {
    //     targetPosition = Constants.TargetPosition.kL3;
    // }

    // /** move the shoulder to Level 4 */
    // public void L4()
    // {
    //     targetPosition = Constants.TargetPosition.kL4;
    // }

    /** move the shoulder to Starting Position */
    // public void StartingPosition()
    // {
    //     targetPosition = Constants.TargetPosition.kStartingPosition;
    // }

    // /** move the shoulder to Grab Coral Position */
    // public void GrabCoralPosition()
    // {
    //     targetPosition = Constants.TargetPosition.kGrabCoralPosition;
    // }

    // public void moveToSetPosition(Constants.TargetPosition targetPosition)
    // {
    //     motor.setControlPosition(targetPosition.pivot);
    // }

    public Command onCommand(double speed)
    {
        return run(() -> set(speed)).withName("Turn On Pivot");
    }

    // public Command holdCommand()
    // {
    //     return run(() -> stop()).withName("Hold Pivot");
    // }

    public Command moveToSetPositionCommand(Constants.TargetPosition targetPosition)
    {
        return run(() -> motor.setControlPosition(targetPosition.pivot));
    }

    public Command stopCommand()
    {
        return runOnce(() -> stop()).withName("Stop Pivot");
    }


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run

    }

    // @Override
    // public String toString()
    // {
    //     // return "Current Pivot Angle: \n" + getAngle();
    // }
}
