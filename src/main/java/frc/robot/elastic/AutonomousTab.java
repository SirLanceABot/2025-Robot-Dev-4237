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
import frc.robot.elastic.AutoCommandList;
import frc.robot.elastic.AutonomousTabData.Right_Wall;
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
  private final AutonomousTabData autonomousTabData = new AutonomousTabData();
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

  // Create the Box objects
  SendableChooser<AutonomousTabData.Left_Wall> leftWallBox =
    new SendableChooser<>();
  SendableChooser<AutonomousTabData.Right_Wall> rightWallBox =
    new SendableChooser<>();
  SendableChooser<AutonomousTabData.Middle> middleBox = new SendableChooser<>();
  // private SendableChooser<AutonomousTabData.Score_Coral_> scoreCoralBox = new
  // SendableChooser<>(); private static
  // SendableChooser<AutonomousTabData.SitPretty> sitPrettyBox = new
  // SendableChooser<>();

  private GenericEntry successfulDownload;
  private GenericEntry doesAutonomousExist;
  private GenericEntry errorMessageBox;
  private GenericEntry autonomousNameBox;

  // Create the Button object
  private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();

  private ButtonState previousButtonState = ButtonState.kStillReleased;
  // private boolean isDataValid = true;
  // private boolean isAutoValid = true;
  // private String errorMessage = "No Errors";
  public String autonomousName = "";

  // *** CLASS CONSTRUCTOR ***
  public AutonomousTab()
  {
    System.out.println("  Constructor Started:  " + fullClassName);

    // this.robotContainer = robotContainer;
    createLeftWallBox();
    createRightWallBox();
    createMiddleBox();

    SmartDashboard.putData("AutoField", autofield);
    SmartDashboard.putData("Field", field);
    // createSitPrettyBox();
    //  createScoreMoreNotesBox();
    //  createDriveOutOfStartZoneBox();
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

    // createSendDataButton();
    // successfulDownload = createSuccessfulDownloadBox();
    // successfulDownload.setBoolean(false);

    // doesAutonomousExist = createDoesAutonomousExistBox();
    // doesAutonomousExist.setBoolean(false);

    System.out.println("  Constructor Finished: " + fullClassName);
  }

  // *** CLASS & INSTANCE METHODS ***

  /**
   * <b>Starting Location</b> Box
   * <p>Create an entry in the Network Table and add the Box to the Shuffleboard
   * Tab
   */
  private void createLeftWallBox()
  {

    // add options to  Box
    leftWallBox.setDefaultOption("Not This Side",
                                 AutonomousTabData.Left_Wall.kNo);
    leftWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0 -- Far_Side",
                          AutonomousTabData.Left_Wall.kCoral3Algae0FarSide);
    leftWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0",
                          AutonomousTabData.Left_Wall.kCoral3Algae0);
    leftWallBox.addOption(" -- Score_Coral_1 -- Score_Algae_2",
                          AutonomousTabData.Left_Wall.kCoral4Algae0);
    SmartDashboard.putData("Left Wall", leftWallBox);
  }

  private void createRightWallBox()
  {

    // add options to  Box
    rightWallBox.setDefaultOption("Not This Side",
                                  AutonomousTabData.Right_Wall.kNo);
    rightWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0 -- Far_Side",
                           AutonomousTabData.Right_Wall.kCoral3Algae0FarSide);
    rightWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0",
                           AutonomousTabData.Right_Wall.kCoral3Algae0);
    rightWallBox.addOption(" -- Score_Coral_1 -- Score_Algae_2",
                           AutonomousTabData.Right_Wall.kCoral4Algae0);
    SmartDashboard.putData("Right Wall", rightWallBox);
  }

  private void createMiddleBox()
  {


    // add options to  Box
    middleBox.setDefaultOption("Not This Side", AutonomousTabData.Middle.kNo);
    middleBox.addOption(" -- Score_Coral_1 -- Score_Algae_2",
                        AutonomousTabData.Middle.kCoral1Algae2FarSide);
    SmartDashboard.putData("Middle", middleBox);
  }

  /**
   * <b>Send Data</b> Button
   * <p>Create an entry in the Network Table and add the Button to the
   * Shuffleboard Tab
   */
  private void createSendDataButton()
  {
    sendDataButton.setDefaultOption("No", false);
    sendDataButton.addOption("Yes", true);
  }

  private void updateAutonomousTabData()
  {
    autonomousTabData.leftWall = leftWallBox.getSelected();
    autonomousTabData.rightWall = rightWallBox.getSelected();
    autonomousTabData.middle = middleBox.getSelected();
    autonomousTabData.createPathPlannerStringAndCommand();
  }

  private void createPathPlannerString()
  {
    AutoCommandList.pathPlannerString = "";
    AutoCommandList.pathPlannerString += autonomousTabData.leftWall;
    AutoCommandList.pathPlannerString += autonomousTabData.rightWall;
    AutoCommandList.pathPlannerString += autonomousTabData.middle;
  }

  // FIXME check this again
  public boolean isNewData()
  {
    boolean isNewData = false;

    boolean isSendDataButtonPressed = sendDataButton.getSelected();

    switch (previousButtonState) {
      case kStillReleased:
        if (isSendDataButtonPressed) {
          previousButtonState = ButtonState.kPressed;
          // autonomousNameBox.setString(AutoCommandList.pathPlannerString);
        }
        break;

      case kPressed:
        updateAutonomousTabData();
        // updateIsDataValidAndErrorMessage();
        if (autonomousTabData.isDataValid()) {
          successfulDownload.setBoolean(true);
          // updateAutonomousTabData();
          // updateIsAutoValid();
          if (autonomousTabData.isAutoValid()) {
            doesAutonomousExist.setBoolean(true);
          } else {
            doesAutonomousExist.setBoolean(false);
          }

          isNewData = true;

          // errorMessageBox.setString(errorMessage);
          // autonomousNameBox.setString(AutoCommandList.pathPlannerString);

        } else {
          doesAutonomousExist.setBoolean(false);
          successfulDownload.setBoolean(false);

          // errorMessageBox.setString(errorMessage);
          // autonomousNameBox.setString(AutoCommandList.pathPlannerString);
        }
        errorMessageBox.setString(autonomousTabData.getErrorMessage());
        DriverStation.reportWarning(autonomousTabData.getErrorMessage(), false);
        autonomousNameBox.setString(autonomousTabData.getPathPlannerString());
        previousButtonState = ButtonState.kStillPressed;
        break;

      case kStillPressed:
        if (!isSendDataButtonPressed) {
          previousButtonState = ButtonState.kReleased;
        }
        break;

      case kReleased:
        successfulDownload.setBoolean(false);
        previousButtonState = ButtonState.kStillReleased;
        break;
    }

    return isNewData;
  }

  public void updateIsAutoValid()
  {
    autonomousCommand = new AutoCommandList(robotContainer, autonomousTabData);
    autonomousTabData.createPathPlannerStringAndCommand();
    autonomousCommand = autonomousTabData.getPathPlannerCommand();
  }

  public Command getAutonomousCommand()
  {
    return autonomousTabData.getPathPlannerCommand();
  }

  public AutonomousTabData.Left_Wall getLeftWall()
  {
    return autonomousTabData.leftWall;
  }

  public AutonomousTabData.Right_Wall getRightWall()
  {
    return autonomousTabData.rightWall;
  }

  public AutonomousTabData.Middle getMiddle()
  {
    return autonomousTabData.middle;
  }

  public void updateIsDataValid()
  {
    boolean isValid = true;
    // autonomousNameBox.setString(AutoCommandList.pathPlannerString);
  }
}