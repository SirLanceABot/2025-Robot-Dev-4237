package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
// import frc.robot.Constants.TargetPosition;
import frc.robot.motors.TalonFXLance;

/**
 * Two big motors (probably falcons), one on either side
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
    public enum ElevatorPosition
    {
        kL4(50.0),
        kUpperReefAlgae(40.0),
        kSafeSwingPosition(30.0),
        kLowerReefAlgae(20.0),
        kReadyToGrabCoralPosition(12.0),
        kGrabCoralPosition(10.0),
        kScoreProcessorPosition(8.0),
        kL3(3.0),
        kL2(2.0),
        kL1(1.0),
        kHoldingPosition(0.0);

        public final double elevatorPosition;

        private ElevatorPosition(double elevatorPosition)
        {
            this.elevatorPosition = elevatorPosition;
        }
    
    }



    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance leftMotor = new TalonFXLance(Constants.Elevator.LEFT_MOTOR_PORT, Constants.Elevator.LEFT_MOTOR_CAN_BUS, "Left Motor");
    private final TalonFXLance rightMotor = new TalonFXLance(Constants.Elevator.RIGHT_MOTOR_PORT, Constants.Elevator.RIGHT_MOTOR_CAN_BUS, "Right Motor");
    // private SparkLimitSwitch forwardLimitSwitch;
    // private SparkLimitSwitch reverseLimitSwitch;
    // private RelativeEncoder encoder;
    // private Constants.TargetPosition targetPosition = Constants.TargetPosition.kOverride;
    private final double threshold = 1.0;
    private final double MAX_OUTPUT = 0.6;
    private final double MIN_OUTPUT = 0.1;

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
        leftMotor.setPosition(1.0);
        rightMotor.setPosition(1.0);

        rightMotor.setupFollower(Constants.Elevator.LEFT_MOTOR_PORT, true);

        leftMotor.setupForwardSoftLimit(150.0, true);
        leftMotor.setupReverseSoftLimit(0.0, true);
        leftMotor.setupForwardHardLimitSwitch(false, false);
        leftMotor.setupReverseHardLimitSwitch(false, false);

        leftMotor.setupPIDController(0, 1.0, 0.0, 0.0); // TODO tune this

        // Configure PID Controller
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

    private void moveToSetPosition(ElevatorPosition targetPosition)
    {
        // System.out.println("1");

        // if(getLeftPosition() > (targetPosition.elevator + threshold))
        // {
        //     // System.out.println("2");
        //     manualMove(-0.5);
        // }
        // else if(getLeftPosition() < (targetPosition.elevator - threshold))
        // {
        //     // System.out.println("3");
        //     manualMove(0.5);
        // }
        // else
        // {
        //     // System.out.println("4");
        //     stop();
        // }

        leftMotor.setControlPosition(targetPosition.elevatorPosition);
    }

    private void set(double speed)
    {
        leftMotor.set(MathUtil.clamp(speed, MIN_OUTPUT, MAX_OUTPUT));
    }

    public void stop()
    {
        leftMotor.set(0.0);
    }

    public BooleanSupplier isAtPosition(ElevatorPosition position)
    {
        return () -> Math.abs(leftMotor.getPosition() - position.elevatorPosition) < threshold;
    }

    public Command setCommand(double speed)
    {
        return run(() -> set(speed)).withName("Set Command");
    }

    public Command stopCommand()
    {
        return runOnce(() -> stop()).withName("Stop Elevator");
    }

    public Command moveToSetPositionCommand(ElevatorPosition position)
    {
        return run(() -> moveToSetPosition(position)).withName("Move To Set Position Elevator");
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
        return "Elevator Position: " + getLeftPosition();
    }
}
