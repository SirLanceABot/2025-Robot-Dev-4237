package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

/**
 * Use this class as a template to create other subsystems.
 */
public class Elevator extends SubsystemLance
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
        private double leftMotorEncoderPosition;
        private double rightMotorEncoderPosition;

        // OUTPUTS
        private double speed;
        
    }

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final TalonFXLance leftMotor = new TalonFXLance(Constants.Elevator.LEFT_MOTOR_PORT, Constants.Elevator.LEFT_MOTOR_CAN_BUS, "Left Motor");
    private final TalonFXLance rightMotor = new TalonFXLance(Constants.Elevator.RIGHT_MOTOR_PORT, Constants.Elevator.RIGHT_MOTOR_CAN_BUS, "Right Motor");

    

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Elevator()
    {
        super("Elevator");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();

        // SendableRegistry.addLW(this, "Elevator", "MY Elevator");
        // addChild("Motor 1", motor1);
        // addChild("Motor 2", motor2);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configMotors()
    {
        leftMotor.setupFactoryDefaults();
        rightMotor.setupFactoryDefaults();
        leftMotor.setupBrakeMode();
        rightMotor.setupBrakeMode();
        leftMotor.setPosition(0.0);
        rightMotor.setPosition(0.0);

        rightMotor.setupFollower(Constants.Elevator.LEFT_MOTOR_PORT, true);

        leftMotor.setupForwardSoftLimit(0.0, true);
        leftMotor.setupReverseSoftLimit(0.0, true);
        leftMotor.setupForwardHardLimitSwitch(true, true);
        leftMotor.setupReverseHardLimitSwitch(true, true);
    }

    public double getLeftPosition()
    {
        return periodicData.leftMotorEncoderPosition;
    }

    public double getRightPosition()
    {
        return periodicData.rightMotorEncoderPosition;
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void set(double speed)
    {
        periodicData.speed = speed;
    }

    public void stop()
    {
        periodicData.speed = 0.0;
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
        leftMotor.set(periodicData.speed);
        rightMotor.set(periodicData.speed);
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
