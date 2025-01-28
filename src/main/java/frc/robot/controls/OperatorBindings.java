package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Commands;
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
    //Variables should be private and static
    private static CommandXboxController controller;
    private static DoubleSupplier leftYAxis;
    private static DoubleSupplier leftXAxis;
    private static DoubleSupplier rightXAxis;
    private static DoubleSupplier rightYAxis;

    private static BooleanSupplier isTeleop;
    private static DoubleSupplier matchTime;

    // *** CLASS CONSTRUCTOR ***
    private OperatorBindings()
    {}

    public static void createBindings(RobotContainer robotContainer)
    {
        controller = robotContainer.getOperatorController();

        if(controller != null)
        {
            System.out.println("  Constructor Started:  " + fullClassName);


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
            // configRumble(30);
            // configDefaultCommands();

            System.out.println("  Constructor Finished: " + fullClassName);
        }
    }


    private static void configSuppliers()
    {
        leftYAxis = () -> -controller.getRawAxis(1);
        leftXAxis = () -> -controller.getRawAxis(0);
        rightXAxis = () -> -controller.getRawAxis(4);
        rightYAxis = () -> -controller.getRawAxis(5);

        isTeleop = () -> DriverStation.isTeleopEnabled();
        matchTime = () -> DriverStation.getMatchTime();
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


    private static void configRumble(int time)
    {
        BooleanSupplier isRumbleTime = () -> isTeleop.getAsBoolean() && Math.abs(matchTime.getAsDouble() - time) < 0.5;
        Trigger rumble = new Trigger(isRumbleTime);
        
        rumble
        .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));
        
    }


    private static void configDefaultCommands()
    {
        
    }


    

}
