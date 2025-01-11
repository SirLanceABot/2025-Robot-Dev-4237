package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance leftMotor = new TalonFXLance(Constants.Elevator.LEFT_MOTOR_PORT, Constants.Elevator.LEFT_MOTOR_CAN_BUS, "Left Motor");
    private final TalonFXLance rightMotor = new TalonFXLance(Constants.Elevator.RIGHT_MOTOR_PORT, Constants.Elevator.RIGHT_MOTOR_CAN_BUS, "Right Motor");
    private SparkLimitSwitch forwardLimitSwitch;
    private SparkLimitSwitch reverseLimitSwitch;
    private RelativeEncoder encoder;
    private Constants.TargetPosition targetPosition = Constants.TargetPosition.kOverride;
    private final double threshold = 0.01;

    private double leftMotorEncoderPosition = 0.0;
    private double rightMotorEncoderPosition = 0.0;
    private double speed = 0.0;
    private DoubleLogEntry elevatorPositionEntry;

    // PID Values
    private final double kP = 0.0;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kS = 0.0;



    

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
        return leftMotor.getPosition();
    }

    public double getRightPosition()
    {
        return rightMotor.getPosition();
    }

    // public void moveToPosition(Constants.TargetPosition targetPosition)
    // {
    //     this.targetPosition = targetPosition;
    // }

    // public void movetoStartingPosition()
    // {
    //     targetPosition = Constants.TargetPosition.kStartingPosition;
    // }

    // public void movetoGrabCoral()
    // {
    //     targetPosition = Constants.TargetPosition.kGrabCoralPosition;
    // }

    // public void movetoL1()
    // {
    //     targetPosition = Constants.TargetPosition.kL1;
    // }

    // public void movetoL2()
    // {
    //     targetPosition = Constants.TargetPosition.kL2;
    // }

    // public void movetoL3()
    // {
    //     targetPosition = Constants.TargetPosition.kL3;
    // }

    // public void movetoL4()
    // {
    //     targetPosition = Constants.TargetPosition.kL4;
    // }

    public void moveToSetPosition(Constants.TargetPosition targetPosition)
    {
        if(getLeftPosition() > targetPosition.elevator + threshold)
        {
            manualMove(-0.5);
        }
        else if(getLeftPosition() > targetPosition.elevator - threshold)
        {
            manualMove(0.5);
        }
        else
        {
            stop();
        }
    }

    private void manualMove(double speed)
    {
        targetPosition = Constants.TargetPosition.kOverride;
        leftMotor.set(speed);
    }

    public void stop()
    {
        targetPosition = Constants.TargetPosition.kOverride;
        leftMotor.set(0.0);
    }

    public Command manualMoveCommand(double speed)
    {
        return Commands.run(() -> manualMove(speed), this).withName("Manual Move Elevator");
    }

    public Command stopCommand()
    {
        return Commands.run(() -> stop(), this).withName("Stop Elevator");
    }

    public Command moveToSetPositionCommand(Constants.TargetPosition targetPosition)
    {
        return Commands.run(() -> moveToSetPosition(targetPosition), this).withName("Move To Set Position Elevator");
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
        return "Elevator Position: " + getLeftPosition();
    }
}
