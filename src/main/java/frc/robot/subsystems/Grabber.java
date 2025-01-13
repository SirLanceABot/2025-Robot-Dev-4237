package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;

/**
 * This class creates the grabber subsystem and setsup related practice commands
 */
public class Grabber extends SubsystemLance
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

    private final SparkMaxLance frontMotor = new SparkMaxLance(Constants.Grabber.FRONT_MOTOR_PORT, Constants.Grabber.FRONT_MOTOR_CAN_BUS, "Front Grabber Motor");
    private final SparkMaxLance backMotor = new SparkMaxLance(Constants.Grabber.BACK_MOTOR_PORT, Constants.Grabber.BACK_MOTOR_CAN_BUS, "Back Grabber Motor");

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Grabber. 
     */
    public Grabber()
    {
        super("Grabber");
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
        frontMotor.setupFactoryDefaults();
        backMotor.setupFactoryDefaults();
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void setSpeed(double speed)
    {
        frontMotor.set(speed);
        backMotor.set(-speed);
    }

    public void stop()
    {
        setSpeed(0.0);
    }

    /* 
     * Turns on motors to place coral on reef
     */
    // * different speed and function for L4?

    // public void placeCoral(double speed)
    // {
    //     topMotor.set(speed);
    //     lowerMotor.set(speed);
    // }

    // /* 
    //  * Turns on motors to set speed to release coral from the claw
    //  * redundant with place coral
    //  */
    // public void ejectCoral(double speed)
    // {
    //     topMotor.set(speed);
    //     lowerMotor.set(speed);
    // }

    public Command acceptCoralCommand()
    {
        // design pending
        return Commands.run(() -> setSpeed(-0.075), this).withName("Accept Coral");
    }

    public Command placeCoralCommand()
    {
        return Commands.run(() -> setSpeed(0.075), this).withName("Place Coral");
    }

    public Command ejectCoralCommand()
    {
        return Commands.run(() -> setSpeed(0.075), this).withName("Eject Coral");
    }   
    
    public Command stopCommand()
    {
        return Commands.run(() -> stop(), this).withName("Stop");
    }
    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
        // use for sensors and data log
    }

    @Override
    public String toString()
    {
        return "Grabber Top Motor Speed: " + frontMotor.get() + "Grabber Lower Motor Speed: " + backMotor.get();
    }
}
