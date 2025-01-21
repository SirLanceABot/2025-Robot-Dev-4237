package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;

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

    private final SparkMaxLance kickMotor = new SparkMaxLance(Constants.Grabber.KICK_MOTOR_PORT, Constants.Grabber.KICK_MOTOR_CAN_BUS, "Grabber Kick Motor");
    private final TalonFXLance grabMotor = new TalonFXLance(Constants.Grabber.GRAB_MOTOR_PORT, Constants.Grabber.GRAB_MOTOR_CAN_BUS, "Grabber Grab Motor");
    // private final SparkMaxLance backMotor = new SparkMaxLance(Constants.Grabber.BACK_MOTOR_PORT, Constants.Grabber.BACK_MOTOR_CAN_BUS, "Back Grabber Motor");

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Grabber and congures the motors. 
     */
    public Grabber()
    {
        super("Grabber");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configMotors()
    {
        kickMotor.setupFactoryDefaults();
        grabMotor.setupFactoryDefaults();
        kickMotor.setupCoastMode();
        grabMotor.setupCoastMode();
    }

    /**
     *Sets the speed of the grab motor
    */
    public void setGrabSpeed(double speed)
    {
        grabMotor.set(speed);
    }

    /**
     *Sets the speed of the kick motor
    */
    public void setKickSpeed(double speed)
    {
        kickMotor.set(speed);
    }

    /**
     * Sets the grab motor to grab the game piece
     */
    public void grabGamePiece()
    {
        grabMotor.set(0.1);
    }

    public void ejectAlgae()
    {
        setGrabSpeed(-0.1);
    }

    /** 
     * Sets speed of the kick motor to spit out coral
    */
    public void placeCoral()
    {
        setKickSpeed(0.1);
    }

    public void stop()
    {
        setGrabSpeed(0.0);
        setKickSpeed(0.0);
    }

    /**
     * grabs the game piece
     * @return the command lol
     */
    public Command grabGamePieceCommand()
    {
        // design pending
        return Commands.run(() -> grabGamePiece(), this).withName("Grab GamePiece");
    }

    /**
     * ejects the algae held by the grabber
     * @return the command lol
     */
    public Command ejectAlgaeCommand()
    {
        return Commands.run(() -> ejectAlgae(), this).withName("Eject Algae");
    }

    /**
     * runs place coral method
    */
    public Command placeCoralCommand()
    {
        return Commands.run(() -> placeCoral(), this).withName("Place Coral");
    }
    
    /**
     * runs stop command
     * 
     */
    public Command stopCommand()
    {
        return Commands.run(() -> stop(), this).withName("Stop");
    }
    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public String toString()
    {
        return "Grabber Kick Motor Speed: " + kickMotor.get() + "Grabber Grab Motor Speed: " + grabMotor.get();
    }
}
