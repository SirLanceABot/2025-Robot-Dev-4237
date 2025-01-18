package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner14;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

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
    
    private final SparkMaxLance leadMotor = new SparkMaxLance(Constants.Pivot.RIGHT_MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Right Pivot Motor");
    // private final SparkMaxLance followMotor = new SparkMaxLance(Constants.Pivot.LEFT_MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Left Pivot Motor");

    private SparkLimitSwitch forwardLimitSwitch;
    private SparkLimitSwitch reverseLimitSwitch;

    private TargetPosition targetPosition = TargetPosition.kOverride;
    private final double threshold = 0.1;


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
        leadMotor.setupFactoryDefaults();
    //     followMotor.setupFactoryDefaults();
        leadMotor.setupBrakeMode();
    //     followMotor.setupBrakeMode();
        leadMotor.setupInverted(true);
    //     followMotor.setupInverted(true);
        leadMotor.setPosition(0.0);
    //     followMotor.setPosition(0.0);
        leadMotor.setupFollower(Constants.Pivot.RIGHT_MOTOR_PORT, true);

    //     // Hard Limits
        leadMotor.setupForwardHardLimitSwitch(true, true);
        leadMotor.setupReverseHardLimitSwitch(true, true);
        
        leadMotor.setupPIDController(0,1,0,0);

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
    public void on(double motorSpeed)
    {
        targetPosition = Constants.TargetPosition.kOverride;
        leadMotor.set(motorSpeed);
    }

    public void hold()
    {
        targetPosition = Constants.TargetPosition.kOverride;
        leadMotor.set(0.0);
    }

    // public void resetEncoder()
    // {
    //     resetState = ResetState.kStart;
    // }

    /** @return encoder ticks (double) */
    public double getPosition() // encoder ticks
    {
        return leadMotor.getPosition();
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
    public void L1()
    {
        targetPosition = Constants.TargetPosition.kL1;
    }

    /** move the shoulder to Level 2*/
    public void L2()
    {
        targetPosition = Constants.TargetPosition.kL2;
    }

    /** move the shoulder to Level 3 */
    public void L3()
    {
        targetPosition = Constants.TargetPosition.kL3;
    }

    /** move the shoulder to Level 4 */
    public void L4()
    {
        targetPosition = Constants.TargetPosition.kL4;
    }

    /** move the shoulder to Starting Position */
    public void StartingPosition()
    {
        targetPosition = Constants.TargetPosition.kStartingPosition;
    }

    /** move the shoulder to Grab Coral Position */
    public void GrabCoralPosition()
    {
        targetPosition = Constants.TargetPosition.kGrabCoralPosition;
    }

    public void moveToSetPosition(Constants.TargetPosition targetPosition)
    {
        if(getPosition() > targetPosition.pivot + threshold)
        {
            on(-0.5);
        }
        else if(getPosition() > targetPosition.pivot - threshold)
        {
            on(0.5);
        }
        else
        {
            hold();
        }
    }

    public Command onCommand(double speed)
    {
        return Commands.run(() -> on(speed), this).withName("Turn On Pivot");
    }

    public Command holdCommand()
    {
        return Commands.run(() -> hold(), this).withName("Hold Pivot");
    }

    public Command moveToSetPositionCommand(Constants.TargetPosition targetPosition)
    {
        return Commands.run(() -> moveToSetPosition(targetPosition), this).withName("Move To Set Position Pivot");
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
