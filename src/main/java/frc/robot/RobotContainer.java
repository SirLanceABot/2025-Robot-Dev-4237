package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shuttle;

import frc.robot.sensors.GyroLance;




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

    private boolean useGrabber                 = false;
    private boolean useClimb                = false;
    private boolean useDrivetrain           = false;
    private boolean useElevator             = false;
    private boolean useIntake               = false;
    private boolean useIntakeWrist          = false;
    private boolean usePivot             = false;
    private boolean useShuttle              = false;

    private boolean useGyro                 = false;

    public final boolean fullRobot;

    private final Grabber grabber;
    private final Climb climb;
    // private final Drivetrain drivetrain;
    private final Elevator elevator;
    private final Intake intake;
    private final IntakeWrist intakeWrist;
    private final Pivot pivot;
    private final Shuttle shuttle;

    private final GyroLance gyro;
    

    /** 
     * The container for the robot. Contains subsystems, OI devices, and commands.
     * Uses the default access modifier so that the RobotContainer object can only be constructed in this same package.
     */
    RobotContainer()
    {
        fullRobot          = (useFullRobot);
        
        grabber               = (useFullRobot || useGrabber)             ? new Grabber()                                         : null;
        climb              = (useFullRobot || useClimb)            ? new Climb()                                        : null;
        gyro              = (useFullRobot || useGyro)              ? new GyroLance()                                    : null;
        // drivetrain         = (useFullRobot || useDrivetrain)       ? new Drivetrain(gyro, useFullRobot, true, isRedAllianceSupplier())                                   : null;
        elevator           = (useFullRobot || useElevator)         ? new Elevator()                                     : null;
        intake             = (useFullRobot || useIntake)           ? new Intake()                                       : null;
        intakeWrist        = (useFullRobot || useIntakeWrist)      ? new IntakeWrist()                                  : null;
        pivot           = (useFullRobot || usePivot)         ? new Pivot()                                     : null;
        shuttle            = (useFullRobot || useShuttle)          ? new Shuttle()                                      : null;

        configureBindings();
    }

    public Grabber getGrabber()
    {
        return grabber;
    }

    public Climb getClimb()
    {
        return climb;
    }

    public GyroLance getGyro()
    {
        return gyro;
    }

    // public Drivetrain getDrivetrain()
    // {
    //     return drivetrain;
    // }

    public Elevator getElevator()
    {
        return elevator;
    }

    public Intake getIntake()
    {
        return intake;
    }

    public IntakeWrist getIntakeWrist()
    {
        return intakeWrist;
    }

    public Pivot getPivot()
    {
        return pivot;
    }

    public Shuttle getShuttle()
    {
        return shuttle;
    }

    public BooleanSupplier isRedAllianceSupplier()
    {
        return () ->
        {
            var alliance = DriverStation.getAlliance();
            if (alliance.isPresent())
            {
            return alliance.get() == DriverStation.Alliance.Red;
            }
            DriverStation.reportError("No alliance is avaliable, assuming Blue", false);
            return false;
        };
    }

    private void configureBindings() 
    {}

    public Command getAutonomousCommand() 
    {
        return Commands.print("No autonomous command configured");
    }
}
