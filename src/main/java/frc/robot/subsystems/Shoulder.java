package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

import frc.robot.Constants;
import frc.robot.Constants.TargetPosition;
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
        // Remove all code from here

    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
       
    // inputs
    private double currentPosition;
    // private double currentAngle;
    // private double currentVelocity;

    // output
    private double motorSpeed = 0.0;

    private SparkLimitSwitch forwardLimitSwitch;
    private SparkLimitSwitch reverseLimitSwitch;
    
    private final PeriodicData periodicData = new PeriodicData();
    private final TalonFXLance leadMotor = new TalonFXLance(Constants.Shoulder.RIGHT_MOTOR_PORT, Constants.Shoulder.MOTOR_CAN_BUS, "RightShoulderMotor");
    private final TalonFXLance followMotor = new TalonFXLance(Constants.Shoulder.LEFT_MOTOR_PORT, Constants.Shoulder.MOTOR_CAN_BUS, "LeftShoulderMotor");

    private TargetPosition targetPosition = TargetPosition.kOverride;

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    
    public Shoulder()
    {
        super("Shoulder");
        System.out.println("  Constructor Started:  " + fullClassName);
        configShoulderMotors();

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

    private void configShoulderMotors()
    {
        // Factory Defaults
        leadMotor.setupFactoryDefaults();
        followMotor.setupFactoryDefaults();
        leadMotor.setupBrakeMode();
        followMotor.setupBrakeMode();
        leadMotor.setupInverted(true);
        followMotor.setupInverted(true);
        leadMotor.setPosition(0.0);
        followMotor.setPosition(0.0);
        followMotor.setupFollower(Constants.Shoulder.RIGHT_MOTOR_PORT, true);

        // Hard Limits
        leadMotor.setupForwardHardLimitSwitch(true, true);
        leadMotor.setupReverseHardLimitSwitch(true, true);
        
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void on(double motorSpeed)
    {
        leadMotor.set(0.2);
    }

    public void hold()
    {
        leadMotor.set(0.0);
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

    /** move the shoulder to Level 1 */
    public void L1()
    {
        targetPosition = TargetPosition.kL1;
    }

    /** move the shoulder to Level 2*/
    public void L2()
    {
        targetPosition = TargetPosition.kL2;
    }

    /** move the shoulder to Level 3 */
    public void L3()
    {
        targetPosition = TargetPosition.kL3;
    }

    /** move the shoulder to Level 4 */
    public void L4()
    {
        targetPosition = TargetPosition.kL4;
    }

    /** move the shoulder to Starting Position */
    public void StartingPosition()
    {
        targetPosition = TargetPosition.kStartingPosition;
    }

    /** move the shoulder to Grab Coral Position */
    public void GrabCoralPosition()
    {
        targetPosition = TargetPosition.kGrabCoralPosition;
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
        return "Shoulder position: \n";
    }
}
