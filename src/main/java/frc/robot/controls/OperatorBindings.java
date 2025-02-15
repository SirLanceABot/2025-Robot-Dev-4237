package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.CommandsManager;
import frc.robot.commands.ScoringCommands;
import frc.robot.commands.CommandsManager.TargetPosition;

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

    private static RobotContainer robotContainer;

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
            configLeftTrigger();
            configRightTrigger();
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
        Trigger aButton = controller.a();

        //Create a path on the fly and score on L1 level
        aButton.onTrue(Commands.runOnce(() -> ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL1)));
    }


    private static void configBButton()
    {
        Trigger bButton = controller.b();

        //Create a path on the fly and score on L3 level
        bButton.onTrue(Commands.runOnce(() -> ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL3)));

    }


    private static void configXButton()
    {
        Trigger xButton = controller.x();

        //Create a path on the fly and score on L2 level
        xButton.onTrue(Commands.runOnce(() -> ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL2)));

    }


    private static void configYButton()
    {
        Trigger yButton = controller.y();

        //Create a path on the fly and score on L4 level
        yButton.onTrue(Commands.runOnce(() -> ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL4)));

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
        leftTrigger
            .onTrue(robotContainer.getPoseEstimator().setPlacingSideToLeftCommand()
            .andThen(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kLeftRumble, 1.0)))
            .withTimeout(0.25));
    }


    private static void configRightTrigger()
    {
        Trigger rightTrigger = controller.rightTrigger();
        rightTrigger
            .onTrue(robotContainer.getPoseEstimator().setPlacingSideToRightCommand()
            .andThen(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kRightRumble, 1.0)))
            .withTimeout(0.25));
    }


    private static void configLeftStick()
    {
        Trigger leftStick = controller.leftStick();
    }


    private static void configRightStick()
    {
        Trigger rightStick = controller.rightStick();
    }


    private static void configDpadUp()
    {
        Trigger dpadDown = controller.povUp();
    }


    private static void configDpadDown()
    {
        Trigger dpadDown = controller.povDown();
    }


    private static void configRumble(int time)
    {
        BooleanSupplier isRumbleTime = () -> Math.abs(DriverStation.getMatchTime() - time) <= 0.5 && DriverStation.isTeleopEnabled();
        Trigger rumble = new Trigger(isRumbleTime);
        
        rumble
        .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));    }


    private static void configDefaultCommands()
    {
        
    }


    

}
