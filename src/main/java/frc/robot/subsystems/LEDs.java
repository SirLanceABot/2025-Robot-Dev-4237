package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;

/**
 * This is an example of what a subsystem should look like.
 */
public class LEDs extends SubsystemLance
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
    private final AddressableLED ledStrip = new AddressableLED(1);
    private final AddressableLEDBuffer blankBuffer = new AddressableLEDBuffer(60);
    private final AddressableLEDBuffer redBuffer = new AddressableLEDBuffer(60);
    private final AddressableLEDBuffer greenBuffer = new AddressableLEDBuffer(60);
    private final AddressableLEDBuffer blueBuffer = new AddressableLEDBuffer(60);
    private final AddressableLEDBuffer yellowBuffer = new AddressableLEDBuffer(60);
    private final AddressableLEDBuffer purpleBuffer = new AddressableLEDBuffer(60);
    private final Timer timer = new Timer();

    

    


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new LEDs. 
     */
    public LEDs()
    {
        super("LEDs");
        System.out.println("  Constructor Started:  " + fullClassName);
        
        timer.start();
        configLEDs();
        configBuffers();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configLEDs()
    {
        ledStrip.setData(blankBuffer);
        ledStrip.start();
    }

    private void configBuffers()
    {
        for(int i = 0; i < blankBuffer.getLength(); i++)
        {
            blankBuffer.setRGB(i, 0, 0, 0);
            redBuffer.setRGB(i, 255, 0, 0);
            greenBuffer.setRGB(i, 0, 255, 0);
            blueBuffer.setRGB(i, 0, 0, 255);
            yellowBuffer.setRGB(i, 255, 255, 0);
            purpleBuffer.setRGB(i, 255, 0, 255);
        }
    }

    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void off()
    {
        ledStrip.setData(blankBuffer);
    }

    private void setRed()
    {
        ledStrip.setData(redBuffer);
    }

    private void setGreenBlink()
    {
        if(timer.get() < 0.2)
        {
            ledStrip.setData(greenBuffer);
        }
        else if(timer.get() < 0.4)
        {
            ledStrip.setData(blankBuffer);
        }
        else
        {
            timer.restart();
        }
        
    }

    private void setBlueBlink()
    {
        if(timer.get() < 0.2)
        {
            ledStrip.setData(blueBuffer);
        }
        else if(timer.get() < 0.4)
        {
            ledStrip.setData(blankBuffer);
        }
        else
        {
            timer.restart();
        }
        
    }

    private void setYellowBlink()
    {
        if(timer.get() < 0.2)
        {
            ledStrip.setData(yellowBuffer);
        }
        else if(timer.get() < 0.4)
        {
            ledStrip.setData(blankBuffer);
        }
        else
        {
            timer.restart();
        }
        
    }

    private void setPurpleBlink()
    {
        if(timer.get() < 0.2)
        {
            ledStrip.setData(purpleBuffer);
        }
        else if(timer.get() < 0.4)
        {
            ledStrip.setData(blankBuffer);
        }
        else
        {
            timer.restart();
        }
        
    }

    //COMMANDS

    public Command stopCommand()
    {
        return Commands.run(() -> off(), this).withName("Turn Off LED");
    }

    public Command setRedCommand()
    {
        return Commands.run(() -> setRed(), this).withName("Set LED Red");
    }

    public Command setGreenBlinkCommand()
    {
        return Commands.run(() -> setGreenBlink(), this).withName("Set LED Green Blink");
    }

    public Command setBlueBlinkCommand()
    {
        return Commands.run(() -> setBlueBlink(), this).withName("Set LED Blue Blink");
    }

    public Command setYellowBlinkCommand()
    {
        return Commands.run(() -> setYellowBlink(), this).withName("Set LED Yellow Blink");
    }

    public Command setPurpleBlinkCommand()
    {
        return Commands.run(() -> setPurpleBlink(), this).withName("Set LED Purple Blink");
    }

    


    // Use a method reference instead of this method
    // public Command stopCommand()
    // {
    //     return run( () -> stop() );
    // }


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
        // Use this for sensors that need to be read periodically.
        // Use this for data that needs to be logged.
    }

    @Override
    public String toString()
    {
        return "";
    }
}
