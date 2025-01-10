package frc.robot;

import java.lang.invoke.MethodHandles;
import frc.robot.tests.Test;

// *** IMPORTANT - PLEASE READ ***
// 1. Put your test code in your own frc.robot.tests.[yourname]Test.java file
// 2. Uncomment one of the IMPORT statements below
// 3. Uncomment one of the INITIALIZATION statements below
// 4. Use the RobotContainer class to construct needed objects
// 5. Test your code
// 6. Undo the changes to this file when finished with testing


// *** IMPORT statements ***
// Uncomment one of these statements

// import frc.robot.tests.ExampleTest;
import frc.robot.tests.JWoodTest;


public class TestMode
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS VARIABLES ***
    private Test myTest = null;
    

    public TestMode(RobotContainer robotContainer)
    {
        // *** INITIALIZATION statements ***
        // Uncomment one of these statements

        // myTest = new ExampleTest(robotContainer);
        myTest = new JWoodTest(robotContainer);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        myTest.init();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        myTest.periodic();
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        myTest.exit();

        // Set the Test object to null so that garbage collection will remove the object.
        myTest = null;
    }    
}
