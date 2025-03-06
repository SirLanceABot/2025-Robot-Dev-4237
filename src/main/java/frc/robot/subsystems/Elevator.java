package frc.robot.subsystems;
// I love you pookie Logan <3 UwU
// -- Saathvik
import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
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
        kHoldingPosition(0.0),
        kL1(0.0),
        kL2(12.5),
        kL3(33.0),
        kLowerReefAlgae(33.0),
        kGrabCoralPosition(36.0),
        kScoreProcessorPosition(36.0),
        kReadyToGrabCoralPosition(45.0),
        kSafeSwingPosition(60.0),
        kUpperReefAlgae(55.0),
        kL4(70.0);

        public final double elevatorPosition;

        private ElevatorPosition(double elevatorPosition)
        {
            this.elevatorPosition = elevatorPosition;
        }
    
    }



    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final SparkMaxLance motor = new SparkMaxLance(Constants.Elevator.LEFT_MOTOR_PORT, Constants.Elevator.LEFT_MOTOR_CAN_BUS, "Motor");
    // private final SparkMaxLance rightMotor = new SparkMaxLance(Constants.Elevator.RIGHT_MOTOR_PORT, Constants.Elevator.RIGHT_MOTOR_CAN_BUS, "Right Motor");
    private SparkLimitSwitch forwardLimitSwitch;
    private SparkLimitSwitch reverseLimitSwitch;
    // private RelativeEncoder encoder;
    // private Constants.TargetPosition targetPosition = Constants.TargetPosition.kOverride;
    private final double threshold = 2.0;
    private final double MAX_OUTPUT = 0.5;
    private final double MIN_OUTPUT = -0.5;

    private double leftMotorEncoderPosition = 0.0;
    private double rightMotorEncoderPosition = 0.0;
    private double speed = 0.0;
    private DoubleLogEntry elevatorPositionEntry;

    // PID Values
    private final double kP = 0.07;
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
        motor.setupFactoryDefaults();
        // rightMotor.setupFactoryDefaults();
        motor.setupBrakeMode();
        // rightMotor.setupBrakeMode();
        motor.setPosition(0.0);
        // rightMotor.setPosition(1.0);
        motor.setInverted(true);

        // rightMotor.setupFollower(Constants.Elevator.LEFT_MOTOR_PORT, true);

        // motor.setupForwardSoftLimit(150.0, false);
        // motor.setupReverseSoftLimit(0.0, false);
        motor.setSafetyEnabled(false);
        motor.setupForwardHardLimitSwitch(true, true);
        motor.setupReverseHardLimitSwitch(true, true);

        motor.setupPIDController(0, 0.05, kI, kD); // TODO tune this
        motor.setupPIDController(1, 0.06, kI, kD); // was 0.07
        motor.setupClosedLoopRampRate(0.35);

        // Configure PID Controller
    }

    public double getPosition()
    {
        return motor.getPosition();
    }

    // public double getRightPosition()
    // {
    //     return rightMotor.getPosition();
    // }

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

        if(targetPosition.elevatorPosition > getPosition()) // For when the target position is HIGHER than the current (moving down)
        {
            motor.setControlPosition(targetPosition.elevatorPosition, 1); // SLOT 1 is the lower P value
        }
        else if(targetPosition.elevatorPosition < getPosition()) // For when the target position is LOWER than the current (moving up)
        {
            motor.setControlPosition(targetPosition.elevatorPosition, 0); // SLOT 0 is the higher P value
        }
    }

    public void set(double speed)
    {
        motor.set(MathUtil.clamp(speed, MIN_OUTPUT, MAX_OUTPUT));
    }

    public void stop()
    {
        motor.set(0.0);
    }

    private void resetPosition()
    {
        motor.setPosition(0.0);
    }
    

    public BooleanSupplier isAtPosition(ElevatorPosition position)
    {
        return () -> Math.abs(motor.getPosition() - position.elevatorPosition) < threshold;
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

    public Command resetPositionCommand()
    {
        return runOnce(() -> resetPosition()).withName("Reset Elevator Position");
    }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here
    @Override
    public void periodic()
    {
        // System.out.println(motor.isReverseLimitSwitchPressed());
        if(motor.isReverseLimitSwitchPressed() && getPosition() != 0.0)
        {
            resetPosition();
            // System.out.println("Reset to 0: " + getPosition());
        }
        // System.out.println("Position: " + getPosition());
        // This method will be called once per scheduler run
    }

    @Override
    public String toString()
    {
        return "Elevator Position: " + getPosition();
    }
}
