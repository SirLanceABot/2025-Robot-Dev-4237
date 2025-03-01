package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;

/**
 * This class creates the claw subsystem and setsup related practice commands
 * The claw will handle coral and algae, it intakes and outakes both game pieces
 * 
 * @author Robbie Frank
 * @author Robbie Jeffery
 * @author Logan Bellinger
 */
public class Claw extends SubsystemLance
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

    private final SparkMaxLance kickMotor = new SparkMaxLance(Constants.Claw.KICK_MOTOR_PORT, Constants.Claw.KICK_MOTOR_CAN_BUS, "Claw Kick Motor");
    private final TalonFXLance grabMotor = new TalonFXLance(Constants.Claw.GRAB_MOTOR_PORT, Constants.Claw.GRAB_MOTOR_CAN_BUS, "Claw Grab Motor");
    // private final SparkMaxLance backMotor = new SparkMaxLance(Constants.Grabber.BACK_MOTOR_PORT, Constants.Grabber.BACK_MOTOR_CAN_BUS, "Back Claw Motor");

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Claw and configures the motors. 
     */
    public Claw()
    {
        super("Claw");
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
        kickMotor.setupBrakeMode();
        grabMotor.setupBrakeMode();
        kickMotor.setInverted(false);
        kickMotor.setSafetyEnabled(false);
        grabMotor.setSafetyEnabled(false);
    }

    /**
     *Sets the speed of the grab motor
    */
    private void setGrabSpeed(double speed)
    {
        grabMotor.set(speed);
    }

    /**
     *Sets the speed of the kick motor
    */
    private void setKickSpeed(double speed)
    {
        kickMotor.set(speed);
    }

    /**
     * Sets the grab motor to grab the game piece
     */
    public void grabGamePiece()
    {
        setGrabSpeed(0.5);
    }

    public void ejectAlgae()
    {
        setGrabSpeed(-0.1);
    }

    public void holdAlgae()
    {
        setGrabSpeed(0.02);
    }
    /** 
     * Sets speed of the kick motor to spit out coral
    */
    public void placeCoral()
    {
        setKickSpeed(1.0); //0.1 was too slow
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
        return run( () -> grabGamePiece()).withName("Grab GamePiece");
    }

    /**
     * ejects the algae held by the claw
     * @return the command lol
     */
    public Command ejectAlgaeCommand()
    {
        return runOnce(() -> ejectAlgae()).withName("Eject Algae");
    }

    /**
     * TODO Might have to decide which side of the robot the coral will be placed on, and depending on that invert the kickMotor speed
     * @return runs place coral method
    */
    public Command placeCoralCommand()
    {
        return runOnce(() -> placeCoral()).withName("Place Coral");
    }

    public Command holdAlgaeCommand()
    {
        return runOnce(() -> holdAlgae()).withName("Hold Algae");
    }
    
    /**
     * runs stop command
     * 
     */
    public Command stopCommand()
    {
        return runOnce(() -> stop()).withName("Stop");
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
        return "Claw Kick Motor Speed: " + kickMotor.get() + "Claw Grab Motor Speed: " + grabMotor.get();
    }
}
