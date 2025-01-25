package frc.robot.sensors;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a new Proximity sensor
 * @author Logan Bellinger
 */
public class Proximity extends SensorLance
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
    private final DigitalInput proximity;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Proximity(int digitalInputPort)
    {   
        super("Proximity");
        System.out.println("  Constructor Started:  " + fullClassName);

        proximity = new DigitalInput(digitalInputPort);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * check proximity to see if it detects something
     * @return true for detected and false for not detected
     */
    public boolean isDetected()
    {
        return !proximity.get();
    }

    public BooleanSupplier isDetectedSupplier()
    {
        return () -> isDetected();
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here
    

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        
    }

    @Override
    public String toString()
    {
        return "Is detected: " + isDetected();
    }
}