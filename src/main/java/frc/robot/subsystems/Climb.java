package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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

    

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance motor = new TalonFXLance(Constants.Climb.MOTOR_PORT, Constants.Climb.MOTOR_CAN_BUS, "Climb Motor");
    private double position = 0.0;


    private final double FORWARD_SOFT_LIMIT = 1000.0;
    private final double REVERSE_SOFT_LIMIT = 0.0;
    private final double POSITION_TOLERANCE = 1.0;

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

        motor.setupInverted(false);
        motor.setPosition(0.0);

        motor.setupForwardSoftLimit(FORWARD_SOFT_LIMIT, true);
        motor.setupReverseSoftLimit(REVERSE_SOFT_LIMIT, true);
        motor.setupForwardHardLimitSwitch(true, true);
        motor.setupReverseHardLimitSwitch(true, true);
    }

    public double getPosition()
    {
        return position;
    }

    private void set(double speed)
    {
        motor.set(speed);
    }

    // Moves the climb so the robot will climb up the cage
    public void climbUp()
    {
        motor.set(0.1);
    }

    // Moves the climb so the robot will climb down the cage
    public void climbDown()
    {
        motor.set(-0.1);
    }

    public void moveToUpPosition()
    {
        if(position < Constants.Climb.CLIMB_UP_POSITION - POSITION_TOLERANCE)
        {
            climbUp();
        }
        else if(position > Constants.Climb.CLIMB_UP_POSITION + POSITION_TOLERANCE)
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
        if(position < Constants.Climb.CLIMB_DOWN_POSITION)
        {
            climbUp();
        }
        else if(position < Constants.Climb.CLIMB_DOWN_POSITION)
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
        motor.set(0.0);
    }

    public Command setCommand(double speed)
    {
        return Commands.run(() -> set(speed), this).withName("Set Climb Speed");
    }

    public Command climbUpCommand()
    {
        return Commands.run(() -> climbUp(), this).withName("Climb Up");
    }

    public Command climbDownCommand()
    {
        return Commands.run(() -> climbDown(), this).withName("Climb Down");
    }

    public Command moveToUpPositionCommand()
    {
        return Commands.run(() -> moveToUpPositionCommand(), this).withName("Move To Up Position");
    }

    public Command moveToDownPositionCommand()
    {
        return Commands.run(() -> moveToDownPositionCommand(), this).withName("Move To Down Position");
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stopCommand(), this).withName("Stops Climb");
    }
    
    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
        position = motor.getPosition();
    }

    @Override
    public String toString()
    {
        return "Climb position = " + position;
    }
}
