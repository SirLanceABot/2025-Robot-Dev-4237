package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import javax.lang.model.util.ElementScanner14;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
// import frc.robot.Constants.TargetPosition;
// import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;
// import frc.robot.commands.

/**
 * This is the Pivot. It allows the robot to move it's arm.
 * @author Greta
 * @author Niyati
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

    public enum PivotPosition
    {
        kFlippedPosition(0.0),
        kScoreBargePosition(12.25),
        kHoldAlgaePosition(16.5),
        kLowLevelCoralPosition(25.0),
        kL4(34.0),
        kSafeDropPosition(50.0), //TODO needs tuned
        kReefAlgaePosition(54.5),
        kScoreProcessorPosition(75.5),
        kDownPosition(117.9);

        public final double pivotPosition;

        private PivotPosition(double pivotPosition)
        {
            this.pivotPosition = pivotPosition;
        }

    }



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
       
    // inputs
    private double currentPosition;
    // private double currentAngle;
    // private double currentVelocity;

    // output
    private double motorSpeed = 0.0;
    
    // private final SparkMaxLance motor = new SparkMaxLance(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Pivot Motor");
    private final SparkMaxLance motor = new SparkMaxLance(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Pivot Motor");
    // private final SparkMaxLance followMotor = new SparkMaxLance(Constants.Pivot.LEFT_MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "Left Pivot Motor");

    // private SparkLimitSwitch forwardLimitSwitch;
    // private SparkLimitSwitch reverseLimitSwitch;

    // private TargetPosition targetPosition = TargetPosition.kOverride;
    private final double kP = 0.04;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double threshold = 4.0;

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
        // motor.setupForwardHardLimitSwitch(false, false);
        // motor.setupReverseHardLimitSwitch(false, false);
        
        motor.setupPIDController(0, 0.02, kI, kD); // Use when bringing pivot up
        motor.setupPIDController(1, 0.0075, kI, kD); // Use when bringing pivot down

        //Configure PID Controller
        // pidController.setP(kP);
        // pidController.setI(kI);
        // pidController.setD(kD);
        // pidController.setIZone(kIz);
        // motor.setFF(kFF);
        // pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    // /**
    // * This turns on the motor.
    // * @param motorSpeed
    // */
    public void set(double motorSpeed)
    {
        // targetPosition = Constants.TargetPosition.kOverride;
        motor.set( MathUtil.clamp(motorSpeed, -0.4, 0.4));
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

    private void resetPosition()
    {
        motor.setPosition(0.0);
    }

    public BooleanSupplier isAtPosition(PivotPosition position)
    {
        return () -> Math.abs(motor.getPosition() - position.pivotPosition) < threshold;
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
        return runOnce(() -> set(speed)).withName("Turn On Pivot");
    }

    private void moveToSetPosition(PivotPosition targetPosition)
    {
        if(targetPosition.pivotPosition > getPosition()) // For when the target position is HIGHER than the current (moving down)
        {
            motor.setControlPosition(targetPosition.pivotPosition, 1); // SLOT 1 is the lower P value
        }
        else if(targetPosition.pivotPosition < getPosition()) // For when the target position is LOWER than the current (moving up)
        {
            motor.setControlPosition(targetPosition.pivotPosition, 0); // SLOT 0 is the higher P value
        }
    }

    // public Command holdCommand()
    // {
    //     return run(() -> stop()).withName("Hold Pivot");
    // }

    public Command moveToSetPositionCommand(PivotPosition targetPosition)
    {
        return run(() -> moveToSetPosition(targetPosition)).withName("Move Pivot To Set Position");
    }

    public Command resetPositionCommand()
    {
        return runOnce(() -> resetPosition()).withName("Reset Pivot Position");
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
        if(motor.isReverseLimitSwitchPressed() && getPosition() != 0.0)
        {
            resetPosition();
        }
    }

    // @Override
    // public String toString()
    // {
    //     // return "Current Pivot Angle: \n" + getAngle();
    // }
}
