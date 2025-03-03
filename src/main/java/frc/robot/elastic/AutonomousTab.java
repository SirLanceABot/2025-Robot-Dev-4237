package frc.robot.elastic;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
// import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
//import frc.robot.elastic.AutoCommandList;
import frc.robot.subsystems.Drivetrain;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutonomousTab
{
  // This string gets the full name of the class, including the package name
  private static final String fullClassName =
    MethodHandles.lookup().lookupClass().getCanonicalName();

  // *** STATIC INITIALIZATION BLOCK ***
  // This block of code is run first when the class is loaded
  static { System.out.println("Loading: " + fullClassName); }

  private enum ButtonState
  {
    kPressed,
    kStillPressed,
    kReleased,
    kStillReleased
  }

  // *** CLASS & INSTANCE VARIABLES ***
  // Create a Shuffleboard Tab
  // Create the Autonomous Command List that will be scheduled to run during
  // autonomousInit()
  private Command autonomousCommand = null;
  private RobotContainer robotContainer;
  private Field2d autofield = new Field2d();
  private Field2d field = new Field2d();
  private Drivetrain drivetrain;
  private PoseEstimator poseEstimator;
  private Trajectory trajectory;
  private Alert autonomousAlert;



  // *** CLASS CONSTRUCTOR ***
  public AutonomousTab()
  {
    System.out.println("  Constructor Started:  " + fullClassName);


    SmartDashboard.putData("AutoField", autofield);
    SmartDashboard.putData("Field", field);
    
    autonomousAlert = new Alert("Logan Likes Rainbows", AlertType.kWarning);

    // Create the trajectory to follow in autonomous. It is best to initialize
    // trajectories here to avoid wasting time in autonomous.
    trajectory = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
      new Pose2d(3, 0, Rotation2d.fromDegrees(0)),
      new TrajectoryConfig(Units.feetToMeters(3.0), Units.feetToMeters(3.0)));

    // Create and push Field2d to SmartDashboard.
    SmartDashboard.putData(autofield);

    // Push the trajectory to Field2d.
    autofield.getObject("traj").setTrajectory(trajectory);
    SmartDashboard.putData(field);


    System.out.println("  Constructor Finished: " + fullClassName);
  }

  // *** CLASS & INSTANCE METHODS ***


}