// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.Pivot.PivotPosition;

/** 
 * An example command that uses an example subsystem. 
 */
public final class CommandsManager
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    // private final ExampleSubsystem exampleSubsystem;

    enum TargetPosition
    {
        kScoreBargePosition(PivotPosition.kScoreBargePosition, ElevatorPosition.kL4),
        kL4(PivotPosition.kL4, ElevatorPosition.kL4),
        kUpperReefAlgae(PivotPosition.kUpperReefAlgae, ElevatorPosition.kUpperReefAlgae),
        kL3(PivotPosition.kL3, ElevatorPosition.kL3),
        kLowerReefAlgae(PivotPosition.kLowerReefAlgae, ElevatorPosition.kLowerReefAlgae),
        kL2(PivotPosition.kL2, ElevatorPosition.kL2),
        kL1(PivotPosition.kL1, ElevatorPosition.kL1),
        kScoreProcessorWithClawPosition(PivotPosition.kScoreProcessorPosition, ElevatorPosition.kL1),
        kHoldAlgaePosition(PivotPosition.kHoldAlgaePosition, ElevatorPosition.kRestingPosition),
        kGrabCoralPosition(PivotPosition.kGrabCoralPosition, ElevatorPosition.kGrabCoralPosition),
        kRestingPosition(PivotPosition.kRestingPosition, ElevatorPosition.kRestingPosition);

        public final PivotPosition pivot;
        public final ElevatorPosition elevator;

        private TargetPosition(PivotPosition pivot, ElevatorPosition elevator)
        {
            this.pivot = pivot;
            this.elevator = elevator;
        }
    }


    /**
     * Creates a new ExampleCommand.
     */
    private CommandsManager() 
    {}

    public static void createCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        GeneralCommands.createCommands(robotContainer);
        IntakingCommands.createCommands(robotContainer);
        ScoringCommands.createCommands(robotContainer);

        createNamedCommands();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private static void createNamedCommands()
    {
        // Intaking Commands
        NamedCommands.registerCommand("Intake Coral From Floor", IntakingCommands.intakeCoralCommand());
        NamedCommands.registerCommand("Intake Coral From Station", IntakingCommands.intakeCoralFromStationCommand());
        NamedCommands.registerCommand("Intake Algae From Floor", IntakingCommands.intakeAlgaeCommand());
        NamedCommands.registerCommand("Intake Upper Level Algae", IntakingCommands.intakeAlgaeFromReefCommand(TargetPosition.kUpperReefAlgae));
        NamedCommands.registerCommand("Intake Lowel Level Algae", IntakingCommands.intakeAlgaeFromReefCommand(TargetPosition.kLowerReefAlgae));
        
        // Score Coral Commands
        NamedCommands.registerCommand("Score Coral on L1", ScoringCommands.scoreCoralCommand(TargetPosition.kL1));
        NamedCommands.registerCommand("Score Coral on L2", ScoringCommands.scoreCoralCommand(TargetPosition.kL2));
        NamedCommands.registerCommand("Score Coral on L3", ScoringCommands.scoreCoralCommand(TargetPosition.kL3));
        NamedCommands.registerCommand("Score Coral on L4", ScoringCommands.scoreCoralCommand(TargetPosition.kL4));

        NamedCommands.registerCommand("Finish Scoring Coral", ScoringCommands.finishScoringCoralCommand());
        NamedCommands.registerCommand("Score Coral Only", GeneralCommands.scoreCoralOnlyCommand());

        // Score Algae Commands
        NamedCommands.registerCommand("Score Algae in Processor", ScoringCommands.scoreProcessorWithIntakeCommand());
        NamedCommands.registerCommand("Score Algae in Barge", ScoringCommands.scoreAlgaeInBargeCommand());

        SmartDashboard.putData("Command Scheduler", CommandScheduler.getInstance());
    }
}
