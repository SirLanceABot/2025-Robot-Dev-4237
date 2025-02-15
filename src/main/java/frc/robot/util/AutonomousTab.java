
package frc.robot.util;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;

// import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.AutoCommandList;
// import frc.robot.RobotContainer;
import frc.robot.util.AutonomousTabData.Right_Wall;
import edu.wpi.first.util.sendable.SendableRegistry;


public class AutonomousTab 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private enum ButtonState
    {
        kPressed, kStillPressed, kReleased, kStillReleased
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Create a Shuffleboard Tab
    //private ShuffleboardTab autonomousTab = Shuffleboard.getTab("Autonomous");
    private final AutonomousTabData autonomousTabData = new AutonomousTabData();
    // Create the Autonomous Command List that will be scheduled to run during autonomousInit()
    // private Command autonomousCommand = null;
    // private RobotContainer robotContainer;
    
    
  
    // Create the Box objects
    SendableChooser<AutonomousTabData.Left_Wall> leftWallBox = new SendableChooser<>();
    SendableChooser<AutonomousTabData.Right_Wall> rightWallBox = new SendableChooser<>();
    SendableChooser<AutonomousTabData.Middle> middleBox = new SendableChooser<>();
    //private SendableChooser<AutonomousTabData.Score_Coral_> scoreCoralBox = new SendableChooser<>();
    //private static SendableChooser<AutonomousTabData.SitPretty> sitPrettyBox = new SendableChooser<>();
    

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
        //createSitPrettyBox();
        // createScoreMoreNotesBox();
        // createDriveOutOfStartZoneBox();


    
        
        // createSendDataButton();
        // successfulDownload = createSuccessfulDownloadBox();
        // successfulDownload.setBoolean(false);

        // doesAutonomousExist = createDoesAutonomousExistBox();
        // doesAutonomousExist.setBoolean(false);

        // errorMessageBox = createErrorMessageBox();
        // autonomousNameBox = createAutonomousNameBox();
    
        

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // *** CLASS & INSTANCE METHODS ***

    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createLeftWallBox()
    {
        //create and name the Box
        // SendableRegistry.add(leftWallBox, "Left Wall");
        // SendableRegistry.setName(leftWallBox, "Left Wall");
        
        //add options to  Box
        leftWallBox.setDefaultOption("Not This Side", AutonomousTabData.Left_Wall.kNo);
        leftWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0 -- Far_Side", AutonomousTabData.Left_Wall.kCoral3Algae0FarSide);
        leftWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0", AutonomousTabData.Left_Wall.kCoral3Algae0);
        leftWallBox.addOption(" -- Score_Coral_1 -- Score_Algae_2", AutonomousTabData.Left_Wall.kCoral4Algae0);
        SmartDashboard.putData("Left Wall", leftWallBox);
    }

    private void createRightWallBox()
    {
        //create and name the Box
        // SendableRegistry.add(rightWallBox, "Right Wall");
        // SendableRegistry.setName(rightWallBox, "Right Wall");
        
        //add options to  Box
        rightWallBox.setDefaultOption("Not This Side", AutonomousTabData.Right_Wall.kNo);
        rightWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0 -- Far_Side", AutonomousTabData.Right_Wall.kCoral3Algae0FarSide);
        rightWallBox.addOption(" -- Score_Coral_3 -- Score_Algae_0", AutonomousTabData.Right_Wall.kCoral3Algae0);
        rightWallBox.addOption(" -- Score_Coral_1 -- Score_Algae_2", AutonomousTabData.Right_Wall.kCoral4Algae0);
        SmartDashboard.putData("Right Wall", rightWallBox);
        //put the widget on the shuffleboard
        // autonomousTab.add(rightWallBox)
        //     .withWidget(BuiltInWidgets.kSplitButtonChooser)
        //     .withPosition(1, 1)
        //     .withSize(5, 3);
    }

    private void createMiddleBox()
    {
        //create and name the Box
        SendableRegistry.add(middleBox, "Middle");
        SendableRegistry.setName(middleBox, "Middle");
        
        //add options to  Box
        middleBox.setDefaultOption("Not This Side", AutonomousTabData.Middle.kNo);
        middleBox.addOption(" -- Score_Coral_1 -- Score_Algae_2", AutonomousTabData.Middle.kCoral1Algae2FarSide);
        SmartDashboard.putData("Middle", middleBox);
    }

    {
    
    

    }
    // private void createScoreCoralBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(scoreCoralBox, "Score Coral?");
    //     SendableRegistry.setName(scoreCoralBox, "Score Coral?");
        
    //     //add options to  Box
    //     scoreCoralBox.addOption("0", AutonomousTabData.Score_Coral_.k0);
    //     scoreCoralBox.setDefaultOption("1", AutonomousTabData.Score_Coral_.k1);
    //     scoreCoralBox.addOption("2", AutonomousTabData.Score_Coral_.k2);
    //     scoreCoralBox.addOption("3", AutonomousTabData.Score_Coral_.k3);
    //     scoreCoralBox.addOption("4", AutonomousTabData.Score_Coral_.k4);
        

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(scoreCoralBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(6, 1)
    //         .withSize(5, 3);
    // }

    // private void createSitPrettyBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(sitPrettyBox, "Sit Pretty");
    //     SendableRegistry.setName(sitPrettyBox, "Sit Pretty");
        
    //     //add options to  Box
    //     sitPrettyBox.setDefaultOption("No", AutonomousTabData.SitPretty.kNo);
    //     sitPrettyBox.addOption("Yes", AutonomousTabData.SitPretty.kYes);

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(sitPrettyBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(11, 1)
    //         .withSize(3, 3);
    // }

    // private void createStageBox()
    // {
    //     //create and name the box
    //     SendableRegistry.add(stageBox,"Stage Positioning");
    //     SendableRegistry.setName(stageBox,"Stage  Positioning");

    //     //add options to Box
    //     stageBox.setDefaultOption("None", AutonomousTabData.StagePositioning.kNone);
    //     // stageBox.addOption("Around Stage", AutonomousTabData.StagePositioning.kAroundStage);
    //     stageBox.addOption("Through Stage", AutonomousTabData.StagePositioning.kThroughStage);

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(stageBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(1,4)
    //         .withSize(8, 3);
    // }

    /**
     * <b>Send Data</b> Button
     * <p>Create an entry in the Network Table and add the Button to the Shuffleboard Tab
     */
    private void createSendDataButton()
    {
        SendableRegistry.add(sendDataButton, "Send Data");
        SendableRegistry.setName(sendDataButton, "Send Data");

        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);

    }

    /**
    * <b>Successful Download</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private GenericEntry createSuccessfulDownloadBox()
    // {
    //     Map<String, Object> booleanBoxProperties = new HashMap<>();
    
    //     booleanBoxProperties.put("Color when true", "Lime");
    //     booleanBoxProperties.put("Color when false", "Red");
        
    //     // return autonomousTab.add("Is Data Valid?", false)
    //     //      .withWidget(BuiltInWidgets.kBooleanBox)
    //     //      .withPosition(19, 1)
    //     //      .withSize(4, 4)
    //     //      .withProperties(booleanBoxProperties)
    //     //      .getEntry();
    // }

    // private GenericEntry createDoesAutonomousExistBox()
    // {
    //     Map<String, Object> booleanBoxProperties = new HashMap<>();
    
    //     booleanBoxProperties.put("Color when true", "Lime");
    //     booleanBoxProperties.put("Color when false", "Red");
        
    //     return autonomousTab.add("Does Autonomous Exist", false)
    //          .withWidget(BuiltInWidgets.kBooleanBox)
    //          .withPosition(19, 10)
    //          .withSize(4, 4)
    //          .withProperties(booleanBoxProperties)
    //          .getEntry();
    // }

    /**
    * <b>Error Message</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private GenericEntry createErrorMessageBox()
    // {
    //      return autonomousTab.add("Error Messages", "No Errors")
    //          .withWidget(BuiltInWidgets.kTextView)
    //          .withPosition(1, 7)
    //          .withSize(18, 3)
    //          .getEntry();
    // }

    // private GenericEntry createAutonomousNameBox()
    // {
    //      return autonomousTab.add("Autonomous Name", "")
    //          .withWidget(BuiltInWidgets.kTextView)
    //          .withPosition(1, 10)
    //          .withSize(18, 3)
    //          .getEntry(); 

    // }

    
    private void updateAutonomousTabData()
    {
        autonomousTabData.leftWall = leftWallBox.getSelected();
        autonomousTabData.rightWall = rightWallBox.getSelected();
        autonomousTabData.middle = middleBox.getSelected();

        // autonomousTabData.sitPretty = sitPrettyBox.getSelected();
        // autonomousTabData.scoreCoral = scoreCoralBox.getSelected();
        // autonomousTabData.driveOutOfStartZone = driveOutOfStartZoneBox.getSelected();

   
        autonomousTabData.createPathPlannerStringAndCommand();
    }

    private void createPathPlannerString()
    {
        AutoCommandList.pathPlannerString = "";
        AutoCommandList.pathPlannerString += autonomousTabData.leftWall;
        AutoCommandList.pathPlannerString += autonomousTabData.rightWall;
        AutoCommandList.pathPlannerString += autonomousTabData.middle;
        // AutoCommandList.pathPlannerString += autonomousTabData.sitPretty;
        // if (!doNothing())
        // {
        //     AutoCommandList.pathPlannerString += autonomousTabData.scoreExtraNotes;
        // }
    }

    // FIXME check this again
    public boolean isNewData()
    {
        boolean isNewData = false;
        
        boolean isSendDataButtonPressed = sendDataButton.getSelected();

        switch(previousButtonState)
        {
            case kStillReleased:
                if(isSendDataButtonPressed)
                {
                    previousButtonState = ButtonState.kPressed;
                    //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
                }
                break;

            case kPressed:
                // errorMessage = "";
                updateAutonomousTabData();
                // updateIsDataValidAndErrorMessage();
                if(autonomousTabData.isDataValid())
                {
                    successfulDownload.setBoolean(true);
                    // updateAutonomousTabData();
                    // updateIsAutoValid();
                    if(autonomousTabData.isAutoValid())
                    {
                        doesAutonomousExist.setBoolean(true);
                    }
                    else
                    {
                        doesAutonomousExist.setBoolean(false);
                    }
                    
                    isNewData = true;
                    
                    
                    // errorMessageBox.setString(errorMessage);
                    //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
                    
                }
                else
                {
                    doesAutonomousExist.setBoolean(false);
                    successfulDownload.setBoolean(false);
                    
                    // errorMessageBox.setString(errorMessage);
                    //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
                }
                errorMessageBox.setString(autonomousTabData.getErrorMessage());
                DriverStation.reportWarning(autonomousTabData.getErrorMessage(), false);
                autonomousNameBox.setString(autonomousTabData.getPathPlannerString());
                previousButtonState = ButtonState.kStillPressed;
                break;

            case kStillPressed:
                if(!isSendDataButtonPressed)
                {
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

    // public AutonomousTabData getAutonomousTabData()
    // {
    //     return autonomousTabData;
    // }

    // public AutoCommandList getAutoCommandList()
    // {
    //     return autoCommandList;
    // }

    // public void updateIsAutoValid()
    // {
    //     String msgAuto = "";
    //     boolean autoExists = true;

    //     // autonomousCommand = new AutoCommandList(robotContainer, autonomousTabData);
    //     autonomousTabData.createPathPlannerStringAndCommand();
    //     autonomousCommand = autonomousTabData.getPathPlannerCommand();

    //     // if(!AutoBuilder.getAllAutoNames().contains(AutoCommandList.pathPlannerString))
    //     if(!AutoBuilder.getAllAutoNames().contains(autonomousTabData.getPathPlannerString()))
    //     {
    //         // add(AutoBuilder.buildAuto(AutoCommandList.pathPlannerString));
    //         autoExists = false;
    //         //doesAutonomousExist.setBoolean(false);

    //         msgAuto = " [Selected Autonomous Path does NOT exist ]\n";
            
    //     }

    //     if(!autoExists)
    //         errorMessage += msgAuto;

    //     isAutoValid = autoExists;

    // }

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

//     public void updateIsDataValidAndErrorMessage()
//     {
//         //errorMessage = "";
//         String msgValid = "";
//         boolean isValid = true;
//         //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
        

//         boolean isSitPretty =
//         (sitPrettyBox.getSelected() == AutonomousTabData.SitPretty.kYes);
        


        
        

//         // Do NOT remove any of the remaining code
//         // Check if the selections are valid
//         if(!isValid)
//             errorMessage += msgValid;

        
        
//         // Displays either "No Errors" or the error messages
        
//         //autonomousNameBox.setString(" ");

//         //autonomousNameBox.setString(AutoCommandList.pathPlannerString);

//         // 
//         isDataValid = isValid;
        

//     }   

    // public static boolean doNothing()
    // {
    //     boolean doNothing =
    //     (sitPrettyBox.getSelected() == AutonomousTabData.SitPretty.kYes);
        
    //     return doNothing;
    // }

    
    

}
