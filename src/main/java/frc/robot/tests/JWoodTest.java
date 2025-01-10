package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.SparkMaxLance;

public class JWoodTest implements Test
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
    // private final ExampleSubsystem exampleSubsystem;
    private final SparkMaxLance motor = new SparkMaxLance(5, Constants.ROBORIO, "Test Motor");


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public JWoodTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // this.exampleSubsystem = robotContainer.exampleSubsystem;

        motor.setupFactoryDefaults();
        motor.setupInverted(true);

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
    {
        
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        motor.set(0.05);
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        motor.set(0.0);
    } 
}
