package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
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

    public enum Color
    {
        kRed(255, 0, 0),
        kGreen(0, 255, 0),
        kBlue(0, 0, 255),
        kYellow(255, 255, 0),
        kPurple(255, 0, 255),
        kHotPink(255, 105, 180),
        kOff(0, 0, 0);

        int r;
        int g;
        int b;

        private Color(int r, int g, int b)
        {
            this.r = r;
            this.g = g;
            this.b = b;
        }

    }


    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final AddressableLED ledStrip = new AddressableLED(1);
    private final AddressableLEDBuffer blankBuffer;
    private final AddressableLEDBuffer setBuffer;
    // private final AddressableLEDBuffer greenBuffer = new AddressableLEDBuffer(60);
    // private final AddressableLEDBuffer blueBuffer = new AddressableLEDBuffer(60);
    // private final AddressableLEDBuffer yellowBuffer = new AddressableLEDBuffer(60);
    // private final AddressableLEDBuffer purpleBuffer = new AddressableLEDBuffer(60);
    private final Timer timer = new Timer();
    // private final LEDPattern rainbow = LEDPattern.rainbow(255, 100);
    // private static final Distance LEDSpacing = Meters.of(1/ 120.0);

    

    


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
        
        ledStrip.setLength(5);
        blankBuffer = new AddressableLEDBuffer(5);
        setBuffer = new AddressableLEDBuffer(5);

        configBuffers();
        configLEDs();
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
        }
    }

    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void off()
    {
        ledStrip.setData(blankBuffer);
        // setColorSolidCommand(Color.kOff);
    }

    private void setColorSolid(int r, int g, int b)
    {
        for(int i = 0; i < blankBuffer.getLength(); i++)
        {
            setBuffer.setRGB(i, r, g, b);
        }

        ledStrip.setData(setBuffer);
    }

    private void setColorBlink(int r, int g, int b)
    {
        for(int i = 0; i < blankBuffer.getLength(); i++)
        {
            setBuffer.setRGB(i, r, g, b);
        }

        if(timer.get() % 0.4 < 0.2)
        {
            ledStrip.setData(setBuffer);
        }
        else
        {
            ledStrip.setData(blankBuffer);
        }
    }

    private void setColorRainbow(int r, int g, int b)
    {
        // for(int c = 0; c < blankBuffer.getLength(); c++)
        // {
        //     for(int i = 0; i < blankBuffer.getLength(); i++)
        //     {
        //         setBuffer.setRGB(i, r, g, b);
        //     }
        // }
     
        for(int i = 0; i < blankBuffer.getLength(); i++)
        {
            setBuffer.setRGB(i, r, g, b);

            r += 25;
            g += 25;
            b += 25;

        }
        

    }

    //COMMANDS

    public Command stopCommand()
    {
        return runOnce(() -> off()).withName("Turn Off LED");
    }

    public Command setColorSolidCommand(Color color)
    {
        return runOnce(() -> setColorSolid(color.r, color.g, color.b)).withName("Set LED Solid");
    }

    public Command setColorBlinkCommand(Color color)
    {
        return run(() -> setColorBlink(color.r, color.g, color.b)).withName("Set LED Blink");
    }

    public Command setColorRainbowCommand(Color color)
    {
        return run(() -> setColorRainbow(color.r, color.g, color.b)).withName("Set LED Rainbow");
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
