package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

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

    // private final double tolerance



    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
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

    private void configMotors()
    {
        motor.setupFactoryDefaults();
        motor.setupBrakeMode();
        motor.setPosition(0.0);
    }

    public double getPosition()
    {
        return motor.getPosition();
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    private void set(double speed)
    {
        motor.set(speed);
    }

    private void stop()
    {
        motor.set(0.0);
    }

    private void moveToPosition(TargetPosition targetPosition)
    {
        // if
    }


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here
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
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }

    @Override
    public String toString()
    {
        return "";
    }
}
