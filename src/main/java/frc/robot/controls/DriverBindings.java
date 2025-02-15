package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import javax.lang.model.util.ElementScanner14;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.commands.ScoringCommands;
import frc.robot.controls.Xbox.RumbleEvent;
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
    //Variables should be private and static
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
        controller = robotContainer.getDriverController();

        if(controller != null)
        {
            System.out.println("  Constructor Started:  " + fullClassName);

            drivetrain = robotContainer.getCommandSwerveDrivetrain();

            configSuppliers();

            configAButton();
            configBButton();
            configXButton();
            configYButton();
            configLeftBumper();
            configRightBumper();
            configBackButton();
            configStartButton();
            configLeftTrigger();
            configRightTrigger();
            configLeftStick();
            configRightStick();
            configDpadUp();
            configDpadDown();
            configRumble(3);
            configDefaultCommands();

            System.out.println("  Constructor Finished: " + fullClassName);
        }
    }

    private static void configSuppliers()
    {
        leftYAxis = () -> -controller.getRawAxis(1);
        leftXAxis = () -> -controller.getRawAxis(0);
        rightXAxis = () -> -controller.getRawAxis(4);

        // isTeleop = () -> DriverStation.isTeleopEnabled();
        // matchTime = () -> DriverStation.getMatchTime();

    }

    private static void configAButton()
    {
        Trigger aButton = controller.a();
        aButton
            .onTrue(ScoringCommands.scoreProcessorWithIntakeCommand());
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
        Trigger yButton = controller.y(); 
        yButton
        .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));
    }



    private static void configLeftBumper()
    {
        Trigger leftBumper = controller.leftBumper();
    }


    private static void configRightBumper()
    {
        Trigger rightBumper = controller.rightBumper();
    }


    private static void configBackButton()
    {
        Trigger backButton = controller.back();
    }


    private static void configStartButton()
    {
        Trigger startButton = controller.start();
    }


    private static void configLeftTrigger()
    {
        Trigger leftTrigger = controller.leftTrigger();
    }


    private static void configRightTrigger()
    {
        Trigger rightTrigger = controller.rightTrigger();
    }


    private static void configLeftStick()
    {
        Trigger leftStick = controller.leftStick();
        // leftStick
            // .onTrue(robotContainer.getCommandSwerveDrivetrain())
    }


    private static void configRightStick()
    {
        Trigger rightTrigger = controller.rightStick();
    }


    private static void configDpadUp()
    {
        Trigger dpadUp = controller.povUp();
    }


    private static void configDpadDown()
    {
        Trigger dpadDown = controller.povDown();
    }

    public static void configRumble(int time)
    {
        BooleanSupplier isRumbleTime = () -> Math.abs(DriverStation.getMatchTime() - time) <= 0.5 && DriverStation.isTeleopEnabled();
        Trigger rumble = new Trigger(isRumbleTime);
        
        rumble
        .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));
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
