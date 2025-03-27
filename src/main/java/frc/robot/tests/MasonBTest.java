package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner14;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfigurator;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.MotorArrangementValue;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.TalonFXLance;

@SuppressWarnings("unused")
public class MasonBTest implements Test
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



    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    private final TalonFXS motor = new TalonFXS(52, Constants.ROBORIO);
    private final TalonFXSConfiguration talonFXSConfiguration = new TalonFXSConfiguration();
    private final TalonFXSConfigurator talonFXSConfigurator = motor.getConfigurator();
    private final MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
    private final Joystick joystick = new Joystick(0);

    // private final ExampleSubsystem exampleSubsystem;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public MasonBTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // talonFXSConfigurator.refresh(talonFXSConfiguration);
        talonFXSConfiguration.Commutation.MotorArrangement = MotorArrangementValue.Minion_JST;
        motor.getConfigurator().apply(talonFXSConfiguration);
        // this.exampleSubsystem = robotContainer.exampleSubsystem;

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

        

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {}

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if(joystick.getRawButton(1))
        {
            motor.set(0.1);
        }
        else if(joystick.getRawButton(2))
        {
            motor.set(-0.1);
        }
        else
        {
            motor.set(0.0);
        }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {} 
}
