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
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.RobotContainer;
import frc.robot.commands.GeneralCommands;
import frc.robot.commands.IntakingCommands;
import frc.robot.commands.ScoringCommands;
import frc.robot.controls.Xbox.RumbleEvent;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Drivetrain;

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
    private static Drivetrain drivetrain;
    private static CommandXboxController controller;

    private static DoubleSupplier leftYAxis;
    private static DoubleSupplier leftXAxis;
    private static DoubleSupplier rightXAxis;
    private static DoubleSupplier scaleFactorSupplier;
    private static double scaleFactor = 0.5;

    private static BooleanSupplier isTeleop;
    private static DoubleSupplier matchTime;

    private static final double CRAWL_SPEED = 0.4;
    private static final double WALK_SPEED = 0.65;
    private static final double RUN_SPEED = 1.0;


    // *** CLASS CONSTRUCTOR ***
    private DriverBindings()
    {}

    public static void createBindings(RobotContainer robotContainer)
    {
        controller = robotContainer.getDriverController();

        if(controller != null)
        {
            System.out.println("  Constructor Started:  " + fullClassName);

            drivetrain = robotContainer.getDrivetrain();

            configSuppliers();

            // configAButton();
            // configBButton();
            // configXButton();
            // configYButton();
            configLeftBumper();
            // configRightBumper();
            // configBackButton();
            configStartButton();
            // configLeftTrigger();
            // configRightTrigger();
            // configLeftStick();
            // configRightStick();
            // configDpadUp();
            // configDpadDown(); 
            // configRumble(3);
            configDefaultCommands();

            System.out.println("  Constructor Finished: " + fullClassName);
        }
    }

    private static void configSuppliers()
    {
        leftYAxis = () -> -controller.getRawAxis(1);
        leftXAxis = () -> -controller.getRawAxis(0);
        rightXAxis = () -> -controller.getRawAxis(4);
        scaleFactorSupplier = () -> scaleFactor;

        // isTeleop = () -> DriverStation.isTeleopEnabled();
        // matchTime = () -> DriverStation.getMatchTime();

    }

    private static void configAButton()
    {
        Trigger aButton = controller.a();
        aButton
            .onTrue(IntakingCommands.testCommand1());
    }


    private static void configBButton()
    {
        Trigger bButton = controller.b();
        // bButton.whileTrue(drivetrain.pointCommand(leftYAxis, leftXAxis));
        bButton
            .onTrue(IntakingCommands.testCommand2());
        
        //applyRequest(() -> 
        // drivetrain.point.withModuleDirection(new Rotation2d(-leftYAxis.getAsDouble(), -leftXAxis.getAsDouble()))));
    }


    private static void configXButton()
    {
        Trigger xButton = controller.x();
        // xButton.onTrue(IntakingCommands.testCommand3());
        xButton.onTrue(GeneralCommands.moveScorerToHoldAlgaeCommand());
    }


    private static void configYButton()
    {
        Trigger yButton = controller.y(); 
        // yButton
        // .onTrue(IntakingCommands.testCommand4());
        // .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        // .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));

        yButton.onTrue(IntakingCommands.intakeCoralFromStationCommand());
        
    }


    //When Left Bumper is pressed Speed is WALK_SPEED
    private static void configLeftBumper()
    {
        Trigger leftBumper = controller.leftBumper();

        leftBumper.onTrue(Commands.runOnce(() -> scaleFactor = (scaleFactor > (CRAWL_SPEED + WALK_SPEED) / 2.0) ? CRAWL_SPEED : RUN_SPEED));

    }


    private static void configRightBumper()
    {
        Trigger rightBumper = controller.rightBumper();
        rightBumper
            .onTrue(IntakingCommands.intakeCoralCommand());
    }


    private static void configBackButton()
    {
        Trigger backButton = controller.back();
    }


    private static void configStartButton()
    {
        Trigger startButton = controller.start();
        startButton
            .onTrue(GeneralCommands.resetGyroCommand());
    }


    //Toggles between RUN_SPEED and WALK_SPEED
    private static void configLeftTrigger()
    {
        Trigger leftTrigger = controller.leftTrigger();

        leftTrigger.onTrue(Commands.runOnce(() -> scaleFactor = (scaleFactor > (WALK_SPEED + RUN_SPEED) / 2.0) ? WALK_SPEED : RUN_SPEED));

    }


    private static void configRightTrigger()
    {
        Trigger rightTrigger = controller.rightTrigger();
        rightTrigger
            .onTrue(IntakingCommands.intakeAlgaeCommand());
    }


    private static void configLeftStick()
    {
        Trigger leftStick = controller.leftStick();
        // leftStick
            // .onTrue(robotContainer.getDrivetrain())
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
            drivetrain.setDefaultCommand(drivetrain.driveCommand(leftYAxis, leftXAxis, rightXAxis, scaleFactorSupplier));
               
            //.applyRequest(() ->
            //         drivetrain.drive.withVelocityX(leftYAxis.getAsDouble() * (TunerConstants.MaxDriveSpeed / 4.0))// Drive forward with negative Y (forward)
            //             .withVelocityY(leftXAxis.getAsDouble() * (TunerConstants.MaxDriveSpeed / 4.0)) // Drive left with negative X (left)
            //             .withRotationalRate(rightXAxis.getAsDouble() * TunerConstants.MaxAngularRate)) // Drive counterclockwise with negative X (left)
            // );
        }
        
    }


    


    
}
