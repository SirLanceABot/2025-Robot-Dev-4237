package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;

public final class OperatorBindings {

    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private static CommandXboxController controller;
    private static DoubleSupplier leftYAxis;
    private static DoubleSupplier leftXAxis;
    private static DoubleSupplier rightXAxis;
    private static DoubleSupplier rightYAxis;



    // *** CLASS CONSTRUCTOR ***
    private OperatorBindings()
    {}

    public static void createBindings(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        controller = robotContainer.getOperatorController();

        // configSuppliers();

        // configAButton();
        // configBButton();
        // configXButton();
        // configYButton();
        // configLeftBumper();
        // configRightBumper();
        // configBackButton();
        // configStartButton();
        // configLeftTrigger();
        // configRightTrigger();
        // configLeftStick();
        // configRightStick();
        // configDpadUp();
        // configDpadDown();
        // configRumble();
        // configDefaultCommands();

        System.out.println("  Constructor Finished: " + fullClassName);

    }


    private static void configSuppliers()
    {
        leftYAxis = () -> -controller.getRawAxis(1);
        leftXAxis = () -> -controller.getRawAxis(0);
        rightXAxis = () -> -controller.getRawAxis(4);
        rightYAxis = () -> -controller.getRawAxis(5);
    }


    private static void configAButton()
    {

    }


    private static void configBButton()
    {
    }


    private static void configXButton()
    {
    
    }


    private static void configYButton()
    {

    }


    private static void configLeftBumper()
    {

    }


    private static void configRightBumper()
    {
        
    }


    private static void configBackButton()
    {
        
    }


    private static void configStartButton()
    {
        
    }


    private static void configLeftTrigger()
    {
        
    }


    private static void configRightTrigger()
    {
        
    }


    private static void configLeftStick()
    {
        
    }


    private static void configRightStick()
    {
        
    }


    private static void configDpadUp()
    {
        
    }


    private static void configDpadDown()
    {
        
    }


    private static void configRumble()
    {
        
    }


    private static void configDefaultCommands()
    {
        
    }


    

}
