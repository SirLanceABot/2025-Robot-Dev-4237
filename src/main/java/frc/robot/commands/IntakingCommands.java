package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.RobotContainer;
// import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.LEDs.Color;
import frc.robot.subsystems.Pivot.PivotPosition;

public final class IntakingCommands
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    // Put all inner enums and inner classes here



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private static RobotContainer robotContainer = null;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private IntakingCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public static void setRobotContainer(RobotContainer robotContainer)
    {
        if(IntakingCommands.robotContainer == null)
            IntakingCommands.robotContainer = robotContainer;
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    /**
     * Command to pickup a coral from the ground
     * @return the command to pickup coral
     * @author Logan Bellinger
    */
    public static Command pickupCoralCommand()
    {
        if(robotContainer.getIntake() != null && robotContainer.getIntakeWrist() != null && robotContainer.getElevator() != null && robotContainer.getGrabber() != null && robotContainer.getLEDs() != null && robotContainer.getIntakeProximity() != null && robotContainer.getElevatorProximity() != null && robotContainer.getGrabberProximity() != null)
        {
            // Does it work?  I don't know.  I'm sure its fine
            return 
            Commands.waitUntil(robotContainer.getIntakeWrist().isAtPosition(Position.kIntakeCoralPosition))
            .deadlineFor(
                robotContainer.getLEDs().setColorBlinkCommand(Color.kYellow),
                robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kIntakeCoralPosition))
            .andThen(
                Commands.waitUntil(robotContainer.getIntakeProximity().isDetectedSupplier())
                .deadlineFor(
                    robotContainer.getIntake().pickupCommand(),
                    robotContainer.getElevator().moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition)))
            .andThen(
                Commands.waitUntil(robotContainer.getIntakeWrist().isAtPosition(Position.kPassToGrabberPosition))
                .deadlineFor(
                    robotContainer.getIntake().stopCommand(),
                    robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kPassToGrabberPosition)))
            .andThen(
                    Commands.waitUntil(robotContainer.getElevatorProximity().isDetectedSupplier())
                    .deadlineFor(
                        robotContainer.getIntake().ejectCommand(),
                        robotContainer.getGrabber().grabGamePieceCommand()))
            .andThen(
                Commands.waitUntil(robotContainer.getGrabberProximity().isDetectedSupplier())
                .deadlineFor(
                    robotContainer.getElevator().moveToSetPositionCommand(ElevatorPosition.kRestingPosition)))
            .andThen(
                Commands.waitUntil(robotContainer.getIntakeWrist().isAtPosition(Position.kRestingPosition))
                .deadlineFor(
                    robotContainer.getGrabber().stopCommand(),
                    robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kRestingPosition),
                    robotContainer.getLEDs().setColorSolidCommand(Color.kRed)));
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * The command to intake algae from the ground with our ground intake
     * @return Command to intake algae
     * @author Logan Bellinger
     */
    public static Command intakeAlgaeCommand()
    {
        if(robotContainer.getIntake() != null && robotContainer.getIntakeWrist() != null && robotContainer.getIntakeProximity() != null && robotContainer.getLEDs() != null)
        {
            return
            Commands.waitUntil(robotContainer.getIntakeProximity().isDetectedSupplier())
            .deadlineFor(
                robotContainer.getLEDs().setColorBlinkCommand(Color.kYellow),
                robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kManipAlgaePosition),
                robotContainer.getIntake().pickupCommand())
            .andThen(
                Commands.waitUntil(robotContainer.getIntakeWrist().isAtPosition(Position.kAlgaeIntakedPosition))
                .deadlineFor(
                    robotContainer.getIntakeWrist().moveToSetPositionCommand(Position.kAlgaeIntakedPosition),
                    robotContainer.getIntake().stopCommand(),
                    robotContainer.getLEDs().setColorSolidCommand(Color.kRed)));
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to intake an Algae from the reef
     * @param targetPosition the position to move the elevator and pivot to, either the UPPER_REEF_ALGAE (between L3 and L4) or the LOWER_REEF_ALGAE (between L2 and L3)
     * @return The command to intake the algae
     * @author Logan Bellinger
     */
    public static Command intakeAlgaeFromReefCommand(ElevatorPosition elevatorPosition, PivotPosition pivotPosition)
    {
        if(robotContainer.getGrabber() != null && robotContainer.getElevator() != null && robotContainer.getPivot() != null && robotContainer.getGrabberProximity() != null && robotContainer.getLEDs() != null)
        {
            return
            Commands.waitUntil(robotContainer.getGrabberProximity().isDetectedSupplier())
            .deadlineFor(
                robotContainer.getLEDs().setColorBlinkCommand(Color.kYellow),
                robotContainer.getElevator().moveToSetPositionCommand(elevatorPosition),
                robotContainer.getPivot().moveToSetPositionCommand(pivotPosition),
                robotContainer.getGrabber().grabGamePieceCommand())
            .andThen(
                Commands.waitUntil(() -> (robotContainer.getPivot().isAtPosition(pivotPosition).getAsBoolean() && robotContainer.getElevator().isAtPosition(elevatorPosition).getAsBoolean()))
                .deadlineFor(
                    robotContainer.getElevator().moveToSetPositionCommand(ElevatorPosition.kHoldAlgaePosition),
                    robotContainer.getPivot().moveToSetPositionCommand(PivotPosition.kHoldAlgaePosition),
                    robotContainer.getGrabber().stopCommand(),
                    robotContainer.getLEDs().setColorSolidCommand(Color.kRed)));
        }
        else
        {
            return Commands.none();
        }
    }


    // public static Command exampleCommand()
    // {
    //     if(subsystems != null)
    //     {
    //         return someCompoundCommand;
    //     }
    //     else
    //         return Commands.none();
    // }
}
