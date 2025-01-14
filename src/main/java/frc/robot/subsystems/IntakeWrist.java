package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

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
    private enum TargetPosition
    {
        kStartingPosition(IntakeWrist.STARTING_POSITION),
        kIntakeCoralPosition(IntakeWrist.INTAKE_CORAL_POSITION),
        kIntakeAlgaePosition(IntakeWrist.INTAKE_ALGAE_POSITION),
        kShootingPosition(IntakeWrist.SHOOTING_POSITION);

        double value;

        private TargetPosition(double value)
        {
            this.value = value;
        }

    }
    

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance motor = new TalonFXLance(Constants.IntakeWrist.MOTOR_PORT, Constants.IntakeWrist.MOTOR_CAN_BUS, "Intake Wrist Motor");
    private double speed;
    private static final double STARTING_POSITION = 0.0;
    private static final double INTAKE_CORAL_POSITION = 0.0;
    private static final double INTAKE_ALGAE_POSITION = 0.0;
    private static final double SHOOTING_POSITION = 0.0;

    private final double tolerance = 5.0;
    
    



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
    }

    /*
     * returns the motors position.
     */
    public double getPosition()
    {
        return motor.getPosition();
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */

    /*
     * sets the speed of motor
     */
    private void set(double speed)
    {
        motor.set(speed);
    }

    /*
     * stops the motor.
     */
    private void stop()
    {
        motor.set(0.0);
    }

    /*
     * moves wrist to position.
     */
    private void moveToPosition(TargetPosition targetPosition)
    {
        if(getPosition() > (targetPosition.value + tolerance))
        {
            set(-0.2);
        }
        else if(getPosition() < (targetPosition.value - tolerance))
        {
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
    public Command moveToSetPositionCommand(TargetPosition targetPosition)
    {
        return Commands.run(() -> moveToPosition(targetPosition), this).withName("Move To Set Position Intake Wrist");
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
