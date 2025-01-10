package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.nio.channels.GatheringByteChannel;

import com.revrobotics.spark.SparkLimitSwitch;

import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

public class Climb extends SubsystemLance
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
        private double position = 0.0;
        // OUTPUTS
        private double speed = 0.0;
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final TalonFXLance motor = new TalonFXLance(Constants.Climb.MOTOR_PORT, Constants.Climb.MOTOR_CAN_BUS, "Climb Motor");


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Climb. 
     */
    public Climb()
    {
        super("Climb");
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
    }

    public double getPosition()
    {
        return periodicData.position;
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void set(double speed)
    {
        periodicData.speed = speed;
    }

    // Moves the climb so the robot will climb up the cage
    public void climbUp()
    {
        periodicData.speed = 0.1;
    }

    // Moves the climb so the robot will climb down the cage
    public void climbDown()
    {
        periodicData.speed = -0.1;
    }

    public void moveToUpPosition()
    {
        if(periodicData.position < Constants.Climb.CLIMB_UP_POSITION)
        {
            climbUp();
        }
        else if(periodicData.position > Constants.Climb.CLIMB_UP_POSITION)
        {
            climbDown();
        }
        else
        {
            stop();
        }
    }

    public void moveToDownPosition()
    {
        if(periodicData.position < Constants.Climb.CLIMB_DOWN_POSITION)
        {
            climbUp();
        }
        else if(periodicData.position < Constants.Climb.CLIMB_DOWN_POSITION)
        {
            climbDown();
        }
        else
        {
            stop();
        }
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
        periodicData.position = motor.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        motor.set(periodicData.speed);
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
