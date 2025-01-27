package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import javax.lang.model.util.ElementScanner14;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public final class DriverBindings {

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
    private static CommandSwerveDrivetrain drivetrain;
    private static CommandXboxController controller;

    private static DoubleSupplier leftYAxis;
    private static DoubleSupplier leftXAxis;
    private static DoubleSupplier rightXAxis;

    private static BooleanSupplier isTeleop;
    private static DoubleSupplier matchTime;

    // *** CLASS CONSTRUCTOR ***
    private DriverBindings()
    {}

    public static void createBindings(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        drivetrain = robotContainer.getDrivetrain();
        controller = robotContainer.getDriverController();

        configSuppliers();

        // configAButton();
        configBButton();
        configXButton();
        configYButton();
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
        configRumble(30);
        configDefaultCommands();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private static void configSuppliers()
    {
        leftYAxis = () -> -controller.getRawAxis(1);
        leftXAxis = () -> -controller.getRawAxis(0);
        rightXAxis = () -> -controller.getRawAxis(4);

        isTeleop = () -> DriverStation.isTeleopEnabled();
        matchTime = () -> DriverStation.getMatchTime();
    }

    private static void configAButton()
    {
        Trigger aButton = controller.a();
    }


    private static void configBButton()
    {
        Trigger bButton = controller.b();
        bButton.whileTrue(drivetrain.pointCommand(leftYAxis, leftXAxis));
        
        //applyRequest(() -> 
        //CommandSwerveDrivetrain.point.withModuleDirection(new Rotation2d(-leftYAxis.getAsDouble(), -leftXAxis.getAsDouble()))));
    }


    private static void configXButton()
    {
        Trigger xButton = controller.x();
        xButton.whileTrue(drivetrain.lockWheelsCommand());
    }


    private static void configYButton()
    {
        // Trigger yButton = controller.y(); 
        // yButton
        // .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        // .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));
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

    public static void configRumble(int time)
    {
        Trigger rumble = new Trigger(() -> (matchTime.getAsDouble() == time && isTeleop.getAsBoolean()));
        
        rumble
            .onTrue(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
            .onFalse(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));
    }


    private static void configDefaultCommands()
    {
        if(drivetrain != null)
        {
            drivetrain.setDefaultCommand(drivetrain.driveCommand(leftYAxis, leftXAxis, rightXAxis));
               
            //.applyRequest(() ->
            //         CommandSwerveDrivetrain.drive.withVelocityX(leftYAxis.getAsDouble() * (TunerConstants.MaxDriveSpeed / 4.0))// Drive forward with negative Y (forward)
            //             .withVelocityY(leftXAxis.getAsDouble() * (TunerConstants.MaxDriveSpeed / 4.0)) // Drive left with negative X (left)
            //             .withRotationalRate(rightXAxis.getAsDouble() * TunerConstants.MaxAngularRate)) // Drive counterclockwise with negative X (left)
            // );
        }
        
    }


    


    
}
