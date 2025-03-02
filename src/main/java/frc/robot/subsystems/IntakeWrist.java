package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

/**
 * Use this class as a template to create other subsystems.
 */
public class IntakeWrist extends SubsystemLance
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
    public enum Position
    {
        kIntakeCoralPosition(10), //TODO these positions do not correlate with the position the motor tells us
        kManipAlgaePosition(5.7622),
        kRestingPosition(0.0);

        double value;

        private Position(double value)
        {
            this.value = value;
        }

    }
    

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance motor = new TalonFXLance(Constants.IntakeWrist.MOTOR_PORT, Constants.IntakeWrist.MOTOR_CAN_BUS, "Intake Wrist Motor");
    // private final DigitalInput forwardLimitSwitch = new DigitalInput(1);
    // private final DigitalInput reverseLimitSwitch = new DigitalInput(0);
    private double speed;

    private static final double kP = 0.45;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final double kS = 0.0;
    private static final double kV = 0.0;
    private static final double kA = 0.0;
    private static final double kG = 0.0;
    private static final GravityTypeValue gravType = GravityTypeValue.Arm_Cosine;

    private final double tolerance = 0.25;

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * creates a new Intake Wrist. 
     */
    public IntakeWrist()
    {
        super("Intake Wrist");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();

        // SendableRegistry.addLW(this, "Example Subsystem", "MY Subsystem");
        // addChild("Motor 1", motor1);
        // addChild("Motor 2", motor2);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    /*
     * configures motors.
     */
    private void configMotors()
    {
        motor.setupFactoryDefaults();
        motor.setupBrakeMode();
        motor.setPosition(0.0);
        motor.setupForwardSoftLimit(12.0, true); //values for testing
        motor.setupReverseSoftLimit(0.0, true); //values for testing
        motor.setSafetyEnabled(false);

        motor.setupForwardHardLimitSwitch(true, true, 1);
        motor.setupReverseHardLimitSwitch(true, true, 0);

        motor.setupPIDController(0, 0.45, kI, kD); // USE FOR GOING DOWN
        motor.setupPIDController(1, 0.75, kI, kD); // USE FOR GOING UP
    }

    /*
     * returns the motors position.
     */
    public double getPosition()
    {
        return motor.getPosition();
    }

    private void resetPosition()
    {
        motor.setPosition(0.0);
    }

    /*
     * sets the speed of motor
     */
    public void set(double speed)
    {
        motor.set(speed);
        // if(speed > 0)
        // {
        //     if(forwardLimitSwitch.get())
        //     {
        //         motor.set(0.0);
        //     }
        //     else
        //     {
        //         motor.set(MathUtil.clamp(speed, -0.5, 0.5));
        //     }
        // }
        // else
        // {
        //     if(reverseLimitSwitch.get())
        //     {
        //         motor.set(0.0);
        //     }
        //     else
        //     {
        //         motor.set(MathUtil.clamp(speed, -0.5, 0.5));
        //     }
        // }
    }

    /*
     * stops the motor.
     */
    public void stop()
    {
        set(0.0);
    }

    /*
     * moves wrist to position.
     */
    private void moveToPosition(Position targetPosition)
    {
        // if(getPosition() > (targetPosition.value + tolerance))
        // {
        //     // motor.setControlPosition(targetPosition.value);
        //     set(-0.2);
        // }
        // else if(getPosition() < (targetPosition.value - tolerance))
        // {
        //     // motor.setControlPosition(targetPosition.value);
        //     set(0.2);
        // }
        // else
        // {
        //     stop();
        // }

        if(targetPosition.value > getPosition()) // For when the target position is HIGHER than the current (moving down)
        {
            motor.setControlPosition(targetPosition.value, 0); // SLOT 0 is the lower P value
        }
        else if(targetPosition.value < getPosition()) // For when the target position is LOWER than the current (moving up)
        {
            motor.setControlPosition(targetPosition.value, 1); // SLOT 1 is the higher P value
        }
        // motor.setControlPosition(targetPosition.value);

        // if(targetPosition.value > motor.getPosition())  // For when the target position is HIGHER than the current (moving down)
        // {
        //     if(!forwardLimitSwitch.get())
        //     {
        //         motor.setControlPosition(targetPosition.value, 0);  // SLOT 0 is the lower P value
        //     }
        // }
        // else    // For when the target position is LOWER than the current (moving up)
        // {
        //     if(!reverseLimitSwitch.get())
        //     {
        //         motor.setControlPosition(targetPosition.value, 1); // SLOT 1 is the higher P value
        //     }
        // }
        
    }

    public BooleanSupplier isAtPosition(Position position)
    {
        return () -> Math.abs(motor.getPosition() - position.value) < tolerance;
    }

    /*
     * runs the moveToPosition() method.
     */
    public Command moveToSetPositionCommand(Position targetPosition)
    {
        return run(() -> moveToPosition(targetPosition)).withName("Move To Set Position Intake Wrist");
    }

    public Command moveUpCommand() //For testing
    {
        return run(() -> set(0.05));
    }

    public Command moveDownCommand() //For testing
    {
        return run(() -> set(-0.05));
    }

    public Command stopCommand()
    {
        return runOnce(() -> stop()).withName("Stop Intake Wrist");
    }

    public Command resetPositionCommand()
    {
        return runOnce(() -> resetPosition()).withName("Reset Intake Wrist Position");
    }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
        // if(forwardLimitSwitch.get() && motor.getVelocity() > 0.0)
        // {
        //     motor.stopMotor();
        // }
        // else if(reverseLimitSwitch.get() && motor.getVelocity() < 0.0)
        // {
        //     motor.stopMotor();
        // }
        // System.out.println("Intake Wrist Pos: " + motor.getPosition());
    }

    @Override
    public String toString()
    {
        return "Intake Wrist Position: " + getPosition();
    }
}
