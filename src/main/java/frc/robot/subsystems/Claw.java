package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;
import frc.robot.subsystems.Elevator.ElevatorPosition;

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

    private final SparkMaxLance shootMotor = new SparkMaxLance(Constants.Claw.SHOOT_MOTOR_PORT, Constants.Claw.KICK_MOTOR_CAN_BUS, "Claw Shoot Motor");
    private final SparkMaxLance indexMotor = new SparkMaxLance(Constants.Claw.INDEX_MOTOR_PORT, Constants.Claw.GRAB_MOTOR_CAN_BUS, "Claw Index Motor");
    private final TalonFXS stickMotor = new TalonFXS(Constants.Claw.STICK_MOTOR_PORT, Constants.ROBORIO);
    private final MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
    private final TalonFXSConfiguration talonFXSConfiguration = new TalonFXSConfiguration();
    private final PositionVoltage positionVoltage = new PositionVoltage(0.0);

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
        shootMotor.setupFactoryDefaults();
        indexMotor.setupFactoryDefaults();
        shootMotor.setupBrakeMode();
        indexMotor.setupBrakeMode();
        shootMotor.setupInverted(true);
        indexMotor.setupInverted(true);
        shootMotor.setSafetyEnabled(false);
        indexMotor.setSafetyEnabled(false);

        stickMotor.getConfigurator().apply(talonFXSConfiguration);
        talonFXSConfiguration.Commutation.MotorArrangement = MotorArrangementValue.Minion_JST;
        talonFXSConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        stickMotor.getConfigurator().apply(talonFXSConfiguration);

        SlotConfigs slotConfigs = new SlotConfigs();
        slotConfigs.SlotNumber = 0;
        slotConfigs.kP = 3;
        stickMotor.getConfigurator().apply(slotConfigs);
        // FINISHED CONFIGURING STICK MOTOR

        stickMotor.setPosition(0.0);
        
        // indexMotor.setupCurrentLimit(30.0, 50.0, 0.5);
    }

    /**
     *Sets the speed of the grab motor
    */
    public void setIndexSpeed(double speed)
    {
        indexMotor.set(speed);
        // System.out.println("Grab motor amps: " + grabMotor.getCurrentAmps());
    }

    /**
     *Sets the speed of the kick motor
    */
    private void setShootSpeed(double speed)
    {
        shootMotor.set(speed);
    }

    public void setStickSpeed(double speed)
    {
        stickMotor.set(speed);
    }

    public void setStickPosition(double value)
    {
        stickMotor.setPosition(value);
    }

    public void moveStickToSetPosition(double position)
    {
        stickMotor.setControl(positionVoltage.withPosition(position).withSlot(0));
    }

    public BooleanSupplier isAtPosition(double position)
    {
        return () -> Math.abs(getStickPosition() - position) < 1.0;
    }

    // private void pulseGrab()
    // {
    //     if(indexMotor.getCurrentAmps() > 20.0)
    //     {
    //         indexMotor.set(0.0);
    //     }
    //     else
    //     {
    //         indexMotor.set(0.3);
    //     }
    // }

    /**
     * Sets the grab motor to grab the game piece
     */
    public void intakeCoral()
    {
        setIndexSpeed(1);
        setShootSpeed(1);
    }

    public void burpCoral()
    {
        setIndexSpeed(-1);
        setShootSpeed(-1);
    }

    public double getStickPosition()
    {
        return stickMotor.getPosition().getValueAsDouble();
    }

    // public void ejectAlgae()
    // {
    //     setIndexSpeed(-0.1);
    // }

    // public void holdAlgae()
    // {
    //     setIndexSpeed(0.02);
    // }

    /** 
     * Sets speed of the kick motor to spit out coral
    */
    public void shootCoral()
    {
        setShootSpeed(1.0); //0.1 was too slow
    }

    // public void placeLowCoral()
    // {
    //     setShootSpeed(0.5);
    // }

    public void stop()
    {
        indexMotor.set(0.0);
        shootMotor.set(0.0);
    }

    public void stopStick()
    {
        stickMotor.set(0.0);
    }

    /**
     * grabs the game piece
     * @return the command lol
     */
    public Command intakeCoralCommand()
    {
        // design pending
        return run(() -> intakeCoral()).withName("Intake Coral");
    }

    public Command burpCoralCommand()
    {
        return run(() -> burpCoral());
    }

    // public Command pulseCommand()
    // {
    //     return run(() -> pulseGrab());
    // }

    /**
     * ejects the algae held by the claw
     * @return the command lol
     */
    // public Command ejectAlgaeCommand()
    // {
    //     return runOnce(() -> ejectAlgae()).withName("Eject Algae");
    // }

    /**
     * TODO Might have to decide which side of the robot the coral will be placed on, and depending on that invert the kickMotor speed
     * @return runs place coral method
    */
    public Command shootCoralCommand()
    {
        return run(() -> shootCoral()).withName("Place Coral");
    }

    // public Command placeLowCoralCommand()
    // {
    //     return run(() -> placeLowCoral());
    // }

    // public Command holdAlgaeCommand()
    // {
    //     return runOnce(() -> holdAlgae()).withName("Hold Algae");
    // }

    public Command setShootSpeedCommand(double speed)
    {
        return run(() ->  setShootSpeed(speed)).withName("Set Kick Speed");
    }

    public Command moveSticktoSetPositionCommand(double position)
    {
        return run(() -> moveStickToSetPosition(position));
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
        return "Claw Shoot Motor Speed: " + shootMotor.get() + "Claw Index Motor Speed: " + indexMotor.get();
    }
}
