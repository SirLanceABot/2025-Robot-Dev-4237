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
import frc.robot.commands.GeneralCommands;
import frc.robot.commands.ScoringCommands;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.PoseEstimator;

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
    private static Elevator elevator;
    private static Pivot pivot;
    private static Drivetrain drivetrain;
    private static PoseEstimator poseEstimator;
    private static Claw claw;
    private static Climb climb;

    private static BooleanSupplier isTeleop;
    private static DoubleSupplier matchTime;

    // private static RobotContainer robotContainer;

    // *** CLASS CONSTRUCTOR ***
    private OperatorBindings()
    {}

    public static void createBindings(RobotContainer robotContainer)
    {
        elevator = robotContainer.getElevator();
        pivot = robotContainer.getPivot();
        poseEstimator = robotContainer.getPoseEstimator();
        controller = robotContainer.getOperatorController();
        drivetrain = robotContainer.getDrivetrain();
        claw = robotContainer.getClaw();
        climb = robotContainer.getClimb();

        if(controller != null)
        {
            System.out.println("  Constructor Started:  " + fullClassName);


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
            configPOV();
            configS2();
            // configLeftStick();
            // configRightStick();
            // configDpadUp();
            // configDpadDown();
            // configDpadLeft();
            // configDpadRight();
            configRumble(30);
            configDefaultCommands();

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

        //Create a path on the fly and score on L1 level, (stops the path if it takes over 10 seconds - probably not needed)
        // aButton.whileTrue(
        //     ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand((() -> drivetrain.getState().Pose), (() -> poseEstimator.closestBranchLocation(() -> poseEstimator.getPrimaryTagID(), poseEstimator.getIsRightBranch()))));
        // aButton.onTrue(GeneralCommands.moveScorerToL1Command());
        // aButton.onTrue(GeneralCommands.moveScorerToL1Command());
    }


    private static void configBButton()
    {
        Trigger bButton = controller.b();

        //Create a path on the fly and score on L2 level, (stops the path if it takes over 10 seconds - probably not needed)
        // bButton.onTrue(Commands.runOnce(() -> 
        //     ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL2)).withTimeout(10.0));
        // bButton.whileTrue(
        //     ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand((() -> drivetrain.getState().Pose), (() -> poseEstimator.closestBranchLocation(() -> poseEstimator.getPrimaryTagID(), false))));
        // bButton.onTrue(GeneralCommands.moveScorerToL2Commmand());
        bButton.whileTrue(ScoringCommands.autoAlignL2Command((() -> drivetrain.getState().Pose), (() -> poseEstimator.closestBranchLocation(() -> poseEstimator.getPrimaryTagID(), poseEstimator.getIsRightBranch()))));
        // bButton.onTrue(GeneralCommands.moveScorerToL2Commmand());
    }


    private static void configXButton()
    {
        Trigger xButton = controller.x();

        //Create a path on the fly and score on L3 level, (stops the path if it takes over 10 seconds - probably not needed)
        // xButton.onTrue(Commands.runOnce(() -> 
        //     ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL3)).withTimeout(10.0));
        xButton.whileTrue(ScoringCommands.autoAlignL3Command((() -> drivetrain.getState().Pose), (() -> poseEstimator.closestBranchLocation(() -> poseEstimator.getPrimaryTagID(), poseEstimator.getIsRightBranch()))));
        // xButton.onTrue(GeneralCommands.moveScorerToL3Command());
    }


    private static void configYButton()
    {
        Trigger yButton = controller.y();
        Trigger aButton = controller.a();

        //Create a path on the fly and score on L4 level, (stops the path if it takes over 10 seconds - probably not needed)
        yButton.and(aButton.negate()).whileTrue(ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand((() -> drivetrain.getState().Pose), (() -> poseEstimator.closestBranchLocation(() -> poseEstimator.getPrimaryTagID(), poseEstimator.getIsRightBranch()))));
        // yButton.onTrue(Commands.runOnce(() -> 
            // ScoringCommands.scoreCoralAutonomouslyReallyCoolAndAwesomeCommand(robotContainer.getPoseEstimator().getIsRightBranch(), TargetPosition.kL4)).withTimeout(10.0));
        // yButton.onTrue(GeneralCommands.moveScorerToL4Command());
    }


    private static void configLeftBumper()
    {
        Trigger leftBumper = controller.leftBumper();
        Trigger aButton = controller.a();

        // leftBumper.onTrue(IntakingCommands.intakeAlgaeFromReefCommand(TargetPosition.kLowerReefAlgae));
        leftBumper.and(aButton.negate()).onTrue(GeneralCommands.burpCoralCommand());

    }


    private static void configRightBumper()
    {
        Trigger rightBumper = controller.rightBumper();
        Trigger aButton = controller.a();

        rightBumper.and(aButton.negate()).onTrue(GeneralCommands.scoreCoralOnlyCommand());
    }


    private static void configBackButton()
    {
        Trigger backButton = controller.back();
        Trigger aButton = controller.a();

        // backButton.onTrue(IntakingCommands.intakeCoralFromStationCommand());
        // backButton.onTrue(GeneralCommands.scoreCoralOnlyCommand());
        backButton.and(aButton.negate()).onTrue(GeneralCommands.getReadyToClimbCommand());
    }


    private static void configStartButton()
    {
        Trigger startButton = controller.start();
            // startButton.onTrue(Commands.either(ScoringCommands.flipScorerCommand(), GeneralCommands.moveScorerToIntakingPositionCommand(), elevator.isAtPosition(ElevatorPosition.kGrabCoralPosition)));
        startButton.onTrue(GeneralCommands.moveScorerToIntakingPositionCommand());
    }

    private static void configLeftTrigger()
    {
        Trigger leftTrigger = controller.leftTrigger();
        Trigger aButton = controller.a();
        leftTrigger.and(aButton.negate()).onTrue(poseEstimator.setPlacingSideToLeftCommand()
            // .andThen(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kLeftRumble, 1.0))).withTimeout(0.25)
            // .andThen(Commands.waitSeconds(0.5))
            // .andThen(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kLeftRumble, 0.0)))
            .withTimeout(0.25));
    }


    private static void configRightTrigger()
    {
        Trigger rightTrigger = controller.rightTrigger();
        Trigger aButton = controller.a();

        rightTrigger.and(aButton.negate()).onTrue(poseEstimator.setPlacingSideToRightCommand()
            // .andThen(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kRightRumble, 1.0))).withTimeout(0.25)
            // .andThen(Commands.waitSeconds(0.5))
            // .andThen(Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kRightRumble, 0.0)))
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
        Trigger dpadUp = controller.povUp();

        // dpadUp.onTrue(GeneralCommands.scoreLowCoralOnlyCommand());
    }


    private static void configDpadDown()
    {
        Trigger dpadDown = controller.povDown();

        // dpadDown.whileTrue(climb.climbDownCommand());
    }

    private static void configDpadLeft()
    {
        Trigger dpadLeft = controller.povLeft();

        // dpadLeft.onTrue(GeneralCommands.moveScorerToProcessorCommand());
    }

    private static void configDpadRight()
    {
        Trigger dpadRight = controller.povRight();

        // dpadRight.onTrue(GeneralCommands.moveScorerToBargeCommand());
    }

    private static void configS1()
    {
        Trigger aButton = controller.a();
        Trigger rightTrigger = controller.rightTrigger();

        rightTrigger.and(aButton).onTrue(poseEstimator.setPlacingFaceToS1Command());
    }

    private static void configS2()
    {
        Trigger aButton = controller.a();
        Trigger rightBumper = controller.rightBumper();

        // aButton.and(rightBumper).onTrue(poseEstimator.setPlacingFaceToS2Command());
        aButton.and(rightBumper).onTrue(GeneralCommands.moveScorerToL2Commmand());
    }

    private static void configS3()
    {
        Trigger aButton = controller.a();
        Trigger yButton = controller.y();

        aButton.and(yButton).onTrue(poseEstimator.setPlacingFaceToS3Command());
    }

    private static void configS4()
    {
        Trigger aButton = controller.a();
        Trigger backButton = controller.back();

        aButton.and(backButton).onTrue(poseEstimator.setPlacingFaceToS4Command());
    }

    private static void configS5()
    {
        Trigger aButton = controller.a();
        Trigger leftBumper = controller.leftBumper();

        aButton.and(leftBumper).onTrue(poseEstimator.setPlacingFaceToS5Command());
    }

    private static void configS6()
    {
        Trigger aButton = controller.a();
        Trigger leftTrigger = controller.leftTrigger();

        aButton.and(leftTrigger).onTrue(poseEstimator.setPlacingFaceToS6Command());
    }

    private static void configPOV()
    {
        if(climb != null)
        {
            Trigger upPOV = controller.povUp();
            upPOV.onTrue(
                climb.climbUpCommand());

            Trigger downPOV = controller.povDown();
            downPOV.onTrue(
                climb.climbDownCommand());

            upPOV.negate().and(downPOV.negate())
                .onTrue(climb.stopCommand());
        }
    }


    private static void configRumble(int time)
    {
        BooleanSupplier isRumbleTime = () -> Math.abs(DriverStation.getMatchTime() - time) <= 0.5 && DriverStation.isTeleopEnabled();
        Trigger rumble = new Trigger(isRumbleTime);
        
        rumble
        .onTrue( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 1.0)))
        .onFalse( Commands.runOnce(() -> controller.getHID().setRumble(RumbleType.kBothRumble, 0.0)));    
    }


    private static void configDefaultCommands()
    {
        
    }
}