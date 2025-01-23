package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.MathUtil;
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
        kStartingPosition(10.0),
        kIntakePosition(20.0),
        kShootingPosition(40.0);

        double value;

        private Position(double value)
        {
            this.value = value;
        }

    }
    

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance motor = new TalonFXLance(Constants.IntakeWrist.MOTOR_PORT, Constants.IntakeWrist.MOTOR_CAN_BUS, "Intake Wrist Motor");
    private double speed;

    private static final double kP = 0.0;
    private static final double kI = 0.0;
    private static final double kD = 0.0;

    private final double tolerance = 1.0;

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

        motor.setupPIDController(0, kP, kI, kD);
    }

    /*
     * returns the motors position.
     */
    public double getPosition()
    {
        return motor.getPosition();
    }

    /*
     * sets the speed of motor
     */
    private void set(double speed)
    {
        motor.set(MathUtil.clamp(speed, -0.5, 0.5));
    }

    /*
     * stops the motor.
     */
    private void stop()
    {
        set(0.0);
    }

    /*
     * moves wrist to position.
     */
    private void moveToPosition(Position targetPosition)
    {
        if(getPosition() > (targetPosition.value + tolerance))
        {
            // motor.setControlPosition(targetPosition.value);
            set(-0.2);
        }
        else if(getPosition() < (targetPosition.value - tolerance))
        {
            // motor.setControlPosition(targetPosition.value);
            set(0.2);
        }
        else
        {
            stop();
        }
    }

    /*
     * runs the moveToPosition() method.
     */
    public Command moveToSetPositionCommand(Position targetPosition)
    {
        return run(() -> moveToPosition(targetPosition)).withName("Move To Set Position Intake Wrist");
    }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public String toString()
    {
        return "Intake Wrist Position: " + getPosition();
    }
}
