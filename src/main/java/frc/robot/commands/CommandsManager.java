// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.Pivot.PivotPosition;

/** 
 * An example command that uses an example subsystem. 
 */
public class CommandsManager extends Command 
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
        kL4(PivotPosition.kL4, ElevatorPosition.kL4),
        kUpperReefAlgae(PivotPosition.kUpperReefAlgae, ElevatorPosition.kUpperReefAlgae),
        kL3(PivotPosition.kL3, ElevatorPosition.kL3),
        kLowerReefAlgae(PivotPosition.kLowerReefAlgae, ElevatorPosition.kLowerReefAlgae),
        kL2(PivotPosition.kL2, ElevatorPosition.kL2),
        kL1(PivotPosition.kL1, ElevatorPosition.kL1),
        kHoldAlgaePosition(PivotPosition.kHoldAlgaePosition, ElevatorPosition.kHoldAlgaePosition),
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
     *
     * @param subsystem The subsystem used by this command.
     */
    private CommandsManager() 
    {
        // this.exampleSubsystem = exampleSubsystem;
        
        // // Use addRequirements() here to declare subsystem dependencies.
        // if(this.exampleSubsystem != null)
        // {
        //     addRequirements(this.exampleSubsystem);
        // }
    }

    // public static void createCommands(RobotContainer robotContainer)
    // {

    // }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return false;
    }
    
    @Override
    public boolean runsWhenDisabled()
    {
        return false;
    }

    @Override
    public String toString()
    {
        String str = this.getClass().getSimpleName();
        return String.format("Command: %s( )", str);
    }
}
