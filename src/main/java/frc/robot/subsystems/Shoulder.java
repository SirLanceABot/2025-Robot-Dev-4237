package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

/**
 * Use this class as a template to create other subsystems.
 */
public class Shoulder extends SubsystemLance
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
        private double currentPosition;
        private double currentAngle;
        private double currentVelocity;

        // OUTPUTS
        private double motorSpeed;
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    
    private SparkLimitSwitch forwardLimitSwitch;
    private SparkLimitSwitch reverseLimitSwitch;
    
    private final TalonFXLance motor1 = new TalonFXLance(4, Constants.ROBORIO, "Motor 1");
    private final TalonFXLance motor2 = new TalonFXLance(12, Constants.ROBORIO, "Motor 2");


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Shoulder()
    {
        super("Example Subsystem");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();
        // relativeEncoder.setPosition(Constants.Shoulder.STARTING_POSITION);

        // SendableRegistry.addLW(this, "Example Subsystem", "MY Subsystem");
        // addChild("Motor 1", motor1);
        // addChild("Motor 2", motor2);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configMotors()
    {
        motor1.setupFactoryDefaults();
        motor2.setupFactoryDefaults();

        //add limit switch here
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void set(double speed)
    {
        periodicData.motorSpeed = speed;
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
    }

    // public void resetEncoder()
    // {
    //     resetState = ResetState.kStart;
    // }

    // /** @return encoder ticks (double) */
    // public double getPosition() // encoder ticks
    // {
    //     return periodicIO.currentPosition;
    // }

    // /** @return angle (double) */
    // public double getAngle()
    // {
    //     return periodicIO.currentAngle;
    // }

    // /** move the shoulder to Level 1 */
    // public void L1()
    // {
    //     targetPosition = TargetPosition.kL1;
    // }

    // /** move the shoulder to Level 2*/
    // public void L2()
    // {
    //     targetPosition = TargetPosition.kL2;
    // }

    // /** move the shoulder to Level 3 */
    // private void L3()
    // {
    //     targetPosition = TargetPosition.kL3;
    // }

    // /** move the shoulder to Level 4 */
    // private void L4()
    // {
    //     targetPosition = TargetPosition.kL4;
    // }

    // /** move the shoulder to Starting Position */
    // private void StartingPosition()
    // {
    //     targetPosition = TargetPosition.kStartingPosition;
    // }

    // /** move the shoulder to Grab Coral Position */
    // private void GrabCoralPosition()
    // {
    //     targetPosition = TargetPosition.kGrabCoralPosition;
    // }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here
    @Override
    public void readPeriodicInputs()
    {
        
    }

    @Override
    public void writePeriodicOutputs()
    {
        motor1.set(periodicData.motorSpeed);
        motor2.set(periodicData.motorSpeed);
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
