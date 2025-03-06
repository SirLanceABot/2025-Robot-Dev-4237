package frc.robot.commands;

import java.lang.annotation.ElementType;
import java.lang.invoke.MethodHandles;
import java.util.List;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.EnumKeySerializer;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AddressableLED.ColorOrder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandsManager.TargetPosition;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.IntakeWrist.Position;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.LEDs.ColorPattern;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Pivot.PivotPosition;

public final class GeneralCommands
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
    private static Intake intake;
    private static IntakeWrist intakeWrist;
    private static Pivot pivot;
    private static Elevator elevator;
    private static Claw claw;
    private static Climb climb;
    private static LEDs leds;
    private static Pigeon2 gyro;
    private static Proximity intakeProximity;
    private static Proximity elevatorProximity;
    private static Proximity clawProximity;
    private static PoseEstimator poseEstimate;
    private static Drivetrain drivetrain;
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private GeneralCommands()
    {}

    public static void createCommands(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        intake = robotContainer.getIntake();
        intakeWrist = robotContainer.getIntakeWrist();
        pivot = robotContainer.getPivot();
        elevator = robotContainer.getElevator();
        claw = robotContainer.getClaw();
        climb = robotContainer.getClimb();
        leds = robotContainer.getLEDs();
        gyro = (robotContainer.getDrivetrain() != null ? robotContainer.getDrivetrain().getPigeon2() : null);
        intakeProximity = robotContainer.getIntakeProximity();
        elevatorProximity = robotContainer.getElevatorProximity();
        clawProximity = robotContainer.getClawProximity();
        drivetrain = robotContainer.getDrivetrain();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


     /**
     * Command to set the led color and pattern,
     * use this so that leds don't break the robot when disabled
     * @param pattern pattern of the led color(s)
     * @param colors the color(s) of the led
     * @return the command to set the led color and pattern
     * @author Matthew Fontecchio
     */
    public static Command setLedCommand(ColorPattern pattern, Color... colors)
    {
        if(leds != null)
        {
            switch(pattern)
            {
                case kSolid:
                    return colors != null ? leds.setColorSolidCommand(colors[0]) : Commands.none();
                case kBlink:
                    return colors != null ? leds.setColorBlinkCommand(colors) : Commands.none();
                case kBreathe:
                    return colors != null ? leds.setColorBreatheCommand(colors) : Commands.none();
                case kGradient:
                    return colors != null ? leds.setColorGradientCommand(colors) : Commands.none();
                case kRainbow:
                    return leds.setColorRainbowCommand();
                case kOff:
                    return leds.offCommand();
                default:
                    return Commands.none();
            }
        }
        else
        {
            return Commands.none();
        }
             
    }

    public static Command intakeTestCommand()
    {
        if(intake != null && intakeWrist != null && intakeProximity != null)
        {
            return 
            intakeWrist.moveToSetPositionCommand(Position.kIntakeCoralPosition)
                .until(intakeWrist.isAtPosition(Position.kIntakeCoralPosition))
                .withTimeout(1.5)
            .andThen(
                intake.pickupCoralCommand()
                    .until(intakeProximity.isDetectedSupplier()))
            .andThen(intake.stopCommand())
            .andThen(
                intakeWrist.moveToSetPositionCommand(Position.kRestingPosition)
                    .until(intakeWrist.isAtPosition(Position.kRestingPosition)))
            .andThen(
                intake.ejectCoralCommand()
                .withTimeout(1.0)
            );
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command testgrabbingFromIntakeCommand()
    {
        if( elevator != null & pivot != null && claw != null && clawProximity != null)
        {
            return
            elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                .withTimeout(2.0)
            .andThen(
                pivot.moveToSetPositionCommand(PivotPosition.kDownPosition)
                    .until(pivot.isAtPosition(PivotPosition.kDownPosition))
                    .withTimeout(2.0)
            )
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kReadyToGrabCoralPosition))
                    .withTimeout(2.0)
            )
            .andThen(
                Commands.parallel(
                    claw.grabGamePieceCommand()
                        .until(clawProximity.isDetectedSupplier())
                            .andThen(claw.stopCommand()),

                    elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition)
                        .until(elevator.isAtPosition(ElevatorPosition.kGrabCoralPosition))
                        .withTimeout(2.0)
                )
            )
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kReadyToGrabCoralPosition))
                    .withTimeout(2.0)
               
            );
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command testGrabAndFlipCommand()
    {
        if( elevator != null & pivot != null && claw != null)
        {
            return
            elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                .withTimeout(2.0)
            .andThen(
                pivot.moveToSetPositionCommand(PivotPosition.kDownPosition)
                    .until(pivot.isAtPosition(PivotPosition.kDownPosition))
                    .withTimeout(2.0)
            )
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kReadyToGrabCoralPosition))
                    .withTimeout(2.0)
            )
            .andThen(
                Commands.parallel(
                    claw.grabGamePieceCommand()
                        .until(clawProximity.isDetectedSupplier())
                            .andThen(
                            claw.stopCommand()
                            ),

                    elevator.moveToSetPositionCommand(ElevatorPosition.kGrabCoralPosition)
                        .until(elevator.isAtPosition(ElevatorPosition.kGrabCoralPosition))
                        .withTimeout(2.0)
                )
            )
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    .withTimeout(2.0)  
            )
            .andThen(
                pivot.moveToSetPositionCommand(PivotPosition.kSafeDropPosition)
                    .until(pivot.isAtPosition(PivotPosition.kSafeDropPosition))
                    .withTimeout(2.0)
            )
            .andThen(
                Commands.parallel(
                    pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition)
                        .until(pivot.isAtPosition(PivotPosition.kFlippedPosition))
                        .withTimeout(2.0),

                    elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)
                        .until(elevator.isAtPosition(ElevatorPosition.kHoldingPosition))
                        .withTimeout(2.0)
                )
            );
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command testScoringAndReadyToIntakeCommand()
    {
        if(elevator != null && pivot != null && claw != null && clawProximity != null)
        {
            if(elevator.getPosition() > 30)
            {
                return
                elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    .withTimeout(2.0)
                .andThen(
                    pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition)
                        .until(pivot.isAtPosition(PivotPosition.kFlippedPosition))
                        .withTimeout(2.0)
                )
                .andThen(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kL4)
                        .until(elevator.isAtPosition(ElevatorPosition.kL4))
                        .withTimeout(2.0)
                )
                .andThen(
                    pivot.moveToSetPositionCommand(PivotPosition.kL4)
                        .until(pivot.isAtPosition(PivotPosition.kL4))
                        .withTimeout(2.0)
                )
                .andThen(
                    claw.placeCoralCommand()
                        .until(() -> !(clawProximity.isDetectedSupplier().getAsBoolean()))
                )
                .andThen(
                    pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition)
                        .until(pivot.isAtPosition(PivotPosition.kFlippedPosition))
                        .withTimeout(2.0)
                )
                .andThen(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)
                        .until(elevator.isAtPosition(ElevatorPosition.kHoldingPosition))
                        .withTimeout(2.0)
                );

            
            }
            else
            {
                return 
                elevator.moveToSetPositionCommand(ElevatorPosition.kL4)
                    .until(elevator.isAtPosition(ElevatorPosition.kL4))
                    .withTimeout(2.0)
                .andThen(
                    pivot.moveToSetPositionCommand(PivotPosition.kL4)
                        .until(pivot.isAtPosition(PivotPosition.kL4))
                        .withTimeout(2.0)
                )
                .andThen(
                    claw.placeCoralCommand()
                        .until(() -> !(clawProximity.isDetectedSupplier().getAsBoolean()))
                )
                .andThen(
                    pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition)
                        .until(pivot.isAtPosition(PivotPosition.kFlippedPosition))
                        .withTimeout(2.0)
                )
                .andThen(
                    elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)
                        .until(elevator.isAtPosition(ElevatorPosition.kHoldingPosition))
                        .withTimeout(2.0)
                );
            }
        }
        else
        {
            return Commands.none();
        }
    }



    public static Command testScoringCommand()
    {
        if(elevator != null && pivot != null && claw != null)
        {
            return
            Commands.waitUntil(elevator.isAtPosition(ElevatorPosition.kL2))
            .deadlineFor(elevator.moveToSetPositionCommand(ElevatorPosition.kL2))

            .andThen(
                Commands.waitUntil(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                .deadlineFor(pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)))
        
            .andThen(claw.placeCoralCommand());
        }
        else
        {
            return Commands.none();
        }
    }

   


    /**
     * Moves the scorer to the position passed to the command  **USE moveScorerTo(insert position here) instead, uses logic to make sure we don't assassinate our claw on our source intake**
     * @param targetPosition position to move scorer to
     * @return the command to do the thing
     * @author Logan Bellinger
     */
    // public static Command moveScorerToSetPositionCommand(TargetPosition targetPosition)
    // {
    //     if(elevator != null && pivot != null)
    //     {
    //         return
    //         Commands.waitUntil(() -> (elevator.isAtPosition(targetPosition.elevator).getAsBoolean() && pivot.isAtPosition(targetPosition.pivot).getAsBoolean()))
    //         .deadlineFor(
    //             setLedCommand(ColorPattern.kBlink, Color.kBlue),
    //             elevator.moveToSetPositionCommand(targetPosition.elevator),
    //             pivot.moveToSetPositionCommand(targetPosition.pivot))
    //         .withName("Move Scorer to Set Position Command");
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    /**
     * Moves scorer to L1 - used for the autonomous lineup
     * @return the thing
     * @author Biggie
     */
    public static Command moveScorerToL1Command()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kBlue)

            .andThen(
                Commands.parallel(

                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                            .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                        .andThen(elevator.moveToSetPositionCommand(ElevatorPosition.kL1)
                            .until(elevator.isAtPosition(ElevatorPosition.kL1))),

                        Commands.waitSeconds(0.5)
                        .andThen(
                            pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)
                                .until(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                                .withTimeout(3.0)),

                        claw.grabGamePieceCommand())).withTimeout(3.0)
                .andThen(claw.stopCommand())
                    // elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                    //     .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                    // .andThen(
                    //     pivot.moveToSetPositionCommand(PivotPosition.kFlippedPosition)
                    //         .until(pivot.isAtPosition(PivotPosition.kFlippedPosition)))
                    // .andThen(
                    //     elevator.moveToSetPositionCommand(ElevatorPosition.kL1)
                    //         .until(elevator.isAtPosition(ElevatorPosition.kL1)))
                    // .andThen(
                    //     pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)
                    //         .until(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))),

                
            .withName("Move Scorer to L1 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Moves the scorer to the passed level - used in the autonomous lineup command
     * @author Logan Bellinger
     * @author Owen Doms
     */
    public static Command chooseLevelCommand(TargetPosition targetPosition)
    {
        switch(targetPosition)
        {
        case kL1:
            return moveScorerToL1Command();
        case kL2:
            return moveScorerToL2Commmand();
        case kL3:
            return moveScorerToL3Command();
        case kL4:
            return moveScorerToL4Command();
        default:
            return Commands.none();
        }
    }

    /**
     * Moves scorer to L2 - Used for auto score
     */
    public static Command moveScorerToL2Commmand()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kBlue)

            .andThen(
                Commands.parallel(

                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                            .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                        .andThen(elevator.moveToSetPositionCommand(ElevatorPosition.kL2)
                            .until(elevator.isAtPosition(ElevatorPosition.kL2))),

                        Commands.waitSeconds(0.5)
                        .andThen(
                            pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)
                                .until(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                                .withTimeout(3.0)),

                        claw.grabGamePieceCommand()
                    )).withTimeout(3.0)
            .andThen(claw.stopCommand())
            .withName("Move Scorer to L2 Command");        
        }
        else
        {
            return Commands.none();
        }
    }

    /** 
     * Moves scorer to L3 - used for auto score
     */
    public static Command moveScorerToL3Command()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kBlue)

            .andThen(
                Commands.parallel(

                        elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                            .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))
                        .andThen(elevator.moveToSetPositionCommand(ElevatorPosition.kL3)
                            .until(elevator.isAtPosition(ElevatorPosition.kL3))),

                        Commands.waitSeconds(0.5)
                        .andThen(
                            pivot.moveToSetPositionCommand(PivotPosition.kLowLevelCoralPosition)
                                .until(pivot.isAtPosition(PivotPosition.kLowLevelCoralPosition))
                                .withTimeout(3.0)),

                        claw.grabGamePieceCommand()
                    )).withTimeout(3.0)
            .andThen(claw.stopCommand())
            .withName("Move Scorer to L3 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Moves scorer to L4 - used for auto score  <- The absolute moneymaker right here
     * @return
     */
    public static Command moveScorerToL4Command()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kBlue)

            .andThen(
                Commands.parallel(

                        elevator.moveToSetPositionCommand(ElevatorPosition.kL4)
                            .until(elevator.isAtPosition(ElevatorPosition.kL4)),

                        Commands.waitSeconds(0.5)
                        .andThen(
                            pivot.moveToSetPositionCommand(PivotPosition.kL4)
                                .until(pivot.isAtPosition(PivotPosition.kL4))
                                .withTimeout(3.0)),

                        claw.grabGamePieceCommand()
                    )).withTimeout(3.0)
            .andThen(claw.stopCommand())
            .withName("Move Scorer to L4 Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Moves the scorer to the position where we are ready to intake coral - not to the position where the actually intake the coral
     * @author Logan Bellinger
     * @return
     */
    public static Command moveScorerToIntakingPositionCommand()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kYellow)

            .andThen(
                Commands.either(
                
                    // IF SCORER IS ABOVE THE SAFE SWING POSITION
                    pivot.moveToSetPositionCommand(PivotPosition.kDownPosition)
                        .until(pivot.isAtPosition(PivotPosition.kDownPosition))
                        .withTimeout(3.0)

                    .andThen(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition)
                            .until(elevator.isAtPosition(ElevatorPosition.kReadyToGrabCoralPosition))
                            .withTimeout(3.0)),

                    // IF SCORER IS NOT ABOVE THE SAFE SWING POSITION
                    elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                        .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition))

                    .andThen(
                        Commands.parallel(
                            pivot.moveToSetPositionCommand(PivotPosition.kDownPosition)
                                .until(pivot.isAtPosition(PivotPosition.kDownPosition))
                                .withTimeout(3.0),
                            
                            claw.grabGamePieceCommand().until(pivot.isAtPosition(PivotPosition.kDownPosition))))

                    .andThen(claw.stopCommand())

                    .andThen(
                        elevator.moveToSetPositionCommand(ElevatorPosition.kReadyToGrabCoralPosition)
                            .until(elevator.isAtPosition(ElevatorPosition.kReadyToGrabCoralPosition))),

                () -> (elevator.getPosition() > ElevatorPosition.kSafeSwingPosition.elevatorPosition))) // Checks if elevator is higher than the designated "Safe Swing" position)
            
                .withName("Move Scorer to Intaking Position Command");  
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Moves the scorer to the Barge heights
     * @author Logan Belliner
     * @return
     */
    public static Command moveScorerToBargeCommand()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kBlue)
            
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kL4)
                    .until(elevator.isAtPosition(ElevatorPosition.kL4)))
            .andThen(
                pivot.moveToSetPositionCommand(PivotPosition.kScoreBargePosition)
                    .until(pivot.isAtPosition(PivotPosition.kScoreBargePosition)));
        }
        else
        {
            return Commands.none();
        }  
    }

    /**
     * Moves the scorer to the position where we score in the processor - first moving the elevator and then the pivot
     * @author Logan Bellinger
     * @return
     */
    public static Command moveScorerToProcessorCommand()
    {
        if(elevator != null && pivot != null)
        {
            return
            setLedCommand(ColorPattern.kBlink, Color.kBlue)

            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kSafeSwingPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kSafeSwingPosition)))
            .andThen(
                pivot.moveToSetPositionCommand(PivotPosition.kScoreProcessorPosition)
                    .until(pivot.isAtPosition(PivotPosition.kScoreProcessorPosition)))
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kScoreProcessorPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kScoreProcessorPosition)))

            .withName("Move Scorer To Processor");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * moves the scorer to the position where we hold the algae once it is intaked in the claw - different from where we hold coral due to the algae being obese
     * @author Logan BEllinger
     * @return
     */
    public static Command moveScorerToHoldAlgaeCommand()
    {
        if(elevator != null && pivot != null)
        {
            return
            pivot.moveToSetPositionCommand(PivotPosition.kHoldAlgaePosition)
                .until(pivot.isAtPosition(PivotPosition.kHoldAlgaePosition))
            .andThen(
                elevator.moveToSetPositionCommand(ElevatorPosition.kHoldingPosition)
                    .until(elevator.isAtPosition(ElevatorPosition.kHoldingPosition)))
            .withName("Move Scorer to hold algae position command");
        }
        else
        {
            return Commands.none();
        }
    }
    
    /**
     * Command that only turns on kicker motor to score coral, done here so it can be registered for PathPlanner
     * @return BTYG
     * @author Logan Bellinger
     */
    public static Command scoreCoralOnlyCommand()
    {
        if(claw != null)
        {
            return 
            Commands.parallel(
                claw.placeCoralCommand().withTimeout(0.5).andThen(claw.stopCommand()),
                pivot.moveToSetPositionCommand(PivotPosition.kL4Place).until(pivot.isAtPosition(PivotPosition.kL4Place)).withTimeout(1.0));
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * ejects the algae, used to do things n stuff
     * @return
     */
    public static Command scoreAlgaeOnlyCommand()
    {
        if(claw != null)
        {
            return
            claw.ejectAlgaeCommand().withTimeout(0.5)
            .andThen(claw.stopCommand()).withName("Score Algae Only Command");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Command to climb up the cage
     * @return the command to climb up cage
     * @author Logan Bellinger
     */
    public static Command climbUpCageCommand()
    {
        if(elevator != null && pivot != null && intakeWrist != null && climb != null)
        {
            return
            ScoringCommands.flipScorerCommand()
            .andThen(setLedCommand(ColorPattern.kRainbow))
            .andThen(
                climb.climbToUpPositionCommand())
            .withName("Climb Up Cage");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * The command to climb down the cage to the down set position
     * @return The command to go down
     * @author Biggie Cheese
     */
    public static Command climbDownCageCommand()
    {
        if(elevator != null && pivot != null && intakeWrist != null && climb != null)
        {
            return
            ScoringCommands.flipScorerCommand()
            .andThen(setLedCommand(ColorPattern.kRainbow))
            .andThen(
                climb.climbToUpPositionCommand())
            .withName("Climb Up Cage");
        }
        else
        {
            return Commands.none();
        }
    }

    /**
     * Resets the gyro to 0 degrees
     * @return the command to reset gyro
     * @author Logan Bellinger
     */
    public static Command resetGyroCommand()
    {
        if (gyro != null)
        {
            return Commands.runOnce(drivetrain::setYaw, drivetrain);
        }
        else
        {
            return Commands.print("No gyro to reset");
        }
    }

    /**
     * Drives autonomously from the given pose to the target pose
     * @param targetPose
     * @param currentPose
     * @return
     * @author Biggie Cheese
     */
    public static Command driveToPositionCommand(Pose2d targetPose, Pose2d currentPose)
    {
        PathConstraints constraints = new PathConstraints(0.5, 0.5, Units.degreesToRadians(360), Units.degreesToRadians(360));
        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                                    new Pose2d(currentPose.getTranslation(), currentPose.getRotation()),
                                    new Pose2d(targetPose.getTranslation(), targetPose.getRotation()));          


        PathPlannerPath path = new PathPlannerPath(
                                    waypoints,
                                    constraints,
                                    null,
                                    new GoalEndState(0.0, targetPose.getRotation()));
        path.preventFlipping = true;


        return AutoBuilder.followPath(path);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

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
