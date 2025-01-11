package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Shuttle;




public class RobotContainer 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private boolean useFullRobot            = false;

    private boolean useClaw                 = false;
    private boolean useClimb                = false;
    private boolean useDrivetrain           = false;
    private boolean useElevator             = false;
    private boolean useIntake               = false;
    private boolean useIntakeWrist          = false;
    private boolean useShoulder             = false;
    private boolean useShuttle              = false;

    public final boolean fullRobot;

    public final Claw claw;
    public final Climb climb;
    public final Drivetrain drivetrain;
    public final Elevator elevator;
    public final Intake intake;
    public final IntakeWrist intakeWrist;
    public final Shoulder shoulder;
    public final Shuttle shuttle;
    

    /** 
     * The container for the robot. Contains subsystems, OI devices, and commands.
     * Uses the default access modifier so that the RobotContainer object can only be constructed in this same package.
     */
    RobotContainer()
    {
        fullRobot          = (useFullRobot);
        
        claw               = (useFullRobot || useClaw)             ? new Claw()                                         : null;
        climb              = (useFullRobot || useClimb)            ? new Climb()                                        : null;
        drivetrain         = (useFullRobot || useDrivetrain)       ? new Drivetrain()                                   : null;
        elevator           = (useFullRobot || useElevator)         ? new Elevator()                                     : null;
        intake             = (useFullRobot || useIntake)           ? new Intake()                                       : null;
        intakeWrist        = (useFullRobot || useIntakeWrist)      ? new IntakeWrist()                                  : null;
        shoulder           = (useFullRobot || useShoulder)         ? new Shoulder()                                     : null;
        shuttle            = (useFullRobot || useShuttle)          ? new Shuttle()                                      : null;

        configureBindings();
    }

    private void configureBindings() 
    {}

    public Command getAutonomousCommand() 
    {
        return Commands.print("No autonomous command configured");
    }
}
