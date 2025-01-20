package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.GeneralCommands;
import frc.robot.commands.IntakingCommands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TESTOperatorControllerBindings 
{
    // / This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private static RobotContainer robotContainer = null;
   

    // *** CLASS CONSTRUCTOR ***
    public TESTOperatorControllerBindings(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);
        
        // if(robotContainer.operatorController != null)
        {
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
            configDpadDown();
            configDpadUp();
            configDpadLeft();
            configDpadRight();
            configRumble();
            configDefaultCommands();
        }

        System.out.println("  Constructor Finished: " + fullClassName);
    }

     public static void setRobotContainer(RobotContainer robotContainer)
    {
        if(TESTOperatorControllerBindings.robotContainer == null)
            TESTOperatorControllerBindings.robotContainer = robotContainer;
    }

    private void configAButton()
    {
        // A Button
        Trigger aButton;
        aButton = robotContainer.getOperatorController().a();
        aButton.onTrue(robotContainer.getElevator().moveToSetPositionCommand(Constants.TargetPosition.kL4));
        // BooleanSupplier aButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kA);
        // Trigger aButtonTrigger = new Trigger(aButton);
    }

    private void configBButton()
    {
        // B Button
        // BooleanSupplier bButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kB);
        // Trigger bButtonTrigger = new Trigger(bButton);
    }

    private void configXButton()
    {
        // X Button
        // BooleanSupplier xButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kX);
        // Trigger xButtonTrigger = new Trigger(xButton);
    }

    private void configYButton()
    {
        // Y Button
        // BooleanSupplier yButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kY);
        // Trigger yButtonTrigger = new Trigger(yButton);
    }

    private void configLeftBumper()
    {
        //Left Bumper
        // BooleanSupplier leftBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftBumper);
        // Trigger leftBumperTrigger = new Trigger(leftBumper);
    }

    private void configRightBumper()
    {
        // Right Bumper
        // BooleanSupplier rightBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightBumper);
        // Trigger rightBumperTrigger = new Trigger(rightBumper);
    }

    private void configBackButton()
    {
        // Back Button
        // BooleanSupplier backButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kBack);
        // Trigger backButtonTrigger = new Trigger(backButton);
    }

    private void configStartButton()
    {
        // Start Button
        // BooleanSupplier startButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kStart);
        // Trigger startButtonTrigger = new Trigger(startButton);
    }

    private void configLeftStick()
    {
        // Left Stick Button
        // BooleanSupplier leftStickButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftStick);
        // Trigger leftStickButtonTrigger = new Trigger(leftStickButton);
    }

    private void configRightStick()
    {
        // Left Stick Button
        // BooleanSupplier rightStickButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightStick);
        // Trigger rightStickButtonTrigger = new Trigger(rightStickButton);
    }

    private void configLeftTrigger()
    {
        //Left trigger 
        // BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        // Trigger leftTriggerTrigger = new Trigger(leftTrigger);
    }

    private void configRightTrigger()
    {
        //Right trigger 
        // BooleanSupplier rightTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
        // Trigger rightTriggerTrigger = new Trigger(rightTrigger);
    }

    private void configDpadDown()
    {
        // Dpad down button
        // BooleanSupplier dPadDown = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kDown);
        // Trigger dPadDownTrigger = new Trigger(dPadDown);
    }

    private void configDpadUp()
    {
        // Dpad down button
        // BooleanSupplier dPadUp = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kUp);
        // Trigger dPadUpTrigger = new Trigger(dPadUp);
    }

    private void configDpadLeft()
    {
        // Dpad down button
        // BooleanSupplier dPadLeft = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kLeft);
        // Trigger dPadLeftTrigger = new Trigger(dPadLeft);
    }

    private void configDpadRight()
    {
        // Dpad down button
        // BooleanSupplier dPadRight = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kRight);
        // Trigger dPadRightTrigger = new Trigger(dPadRight);
    }

    private void configRumble()
    {
        // Rumble
    }

    private void configDefaultCommands()
    {
        // Default Commands

    }
}
