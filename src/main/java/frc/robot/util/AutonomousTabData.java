package frc.robot.util;

import java.lang.invoke.MethodHandles;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutonomousTabData 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    //-------------------------------------------------------------------//

    public static enum Left_Wall
    {
        kCoral1("Left_Wall"),
        kRightWall("Right_Wall"),
        kMiddle("Middle");

        private final String name;

        private Left_Wall(String name)
        {
            this.name = "Left_Wall_" + name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static enum SitPretty
    {
        kYes("Do_Nothing"), 
        kNo("Run_Autonomous"); 


        private final String name;

        private SitPretty(String name)
        {
            this.name = " -- " + name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    // public static enum StagePositioning
    // {
    //     // kNone(""),
    //     // // kAroundStage(" -- Around_Stage"),
    //     // kThroughStage(" -- Through_Stage");

    //     kNone(""),
    //     kThroughStage(" -- Through_Stage");

    //     private final String name;

    //     private StagePositioning(String name)
    //     {
    //         this.name = name;
    //     }

    //     @Override
    //     public String toString()
    //     {
    //         return name;
    //     }
    // }


    public static enum Score_Coral_
    {
        // k0(" -- ScoreExtraNotes_0"), 
        // k1(" -- ScoreExtraNotes_1"), 
        // k2(" -- ScoreExtraNotes_2"), 
        // k3(" -- ScoreExtraNotes_3"),
        // k4(" -- ScoreExtraNotes_4");
        k0("0"), 
        k1("1"), 
        k2("2"), 
        k3("3"),
        k4("4");

        private final String name;

        private Score_Coral_(String name)
        {
            this.name = " -- ScoreExtraNotes_" + name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }


    // public static enum ContainingPreload
    // {
    //     kYes, kNo;
    // }

    // public static enum DriveOutOfStartZone
    // {
    //     kYes, kNo;
    // }



    //-------------------------------------------------------------------//

    // IMPORTANT: Any variables added here must be initialized in the copy constructor below
    public Left_Wall leftWall = Left_Wall.kRightWall;
    public SitPretty sitPretty = SitPretty.kNo;
    // public DriveOutOfStartZone driveOutOfStartZone = DriveOutOfStartZone.kYes;
    // private String commandString = "\n***** AUTONOMOUS COMMAND LIST *****\n";
    private String pathPlannerString = "";
    private Command pathPlannerCommand = Commands.none();
    private String errorMessage = "";

    // Default constructor
    public AutonomousTabData()
    {}

    // Copy Constructor
    public AutonomousTabData(AutonomousTabData atd)
    {
        leftWall = atd.leftWall;
        sitPretty = atd.sitPretty;
        pathPlannerString = atd.pathPlannerString;
        pathPlannerCommand = atd.pathPlannerCommand;
    }

    public void createPathPlannerStringAndCommand()
    {
        pathPlannerCommand = Commands.none();
        errorMessage = "";
        pathPlannerString = "";
        pathPlannerString += leftWall;
       // pathPlannerString += sitPretty;
        //pathPlannerString += scoreCoral;


        // if (sitPretty != AutonomousTabData.SitPretty.kYes)
        // {
        //     pathPlannerString += stagePositioning;
        //     
        // }

        // System.out.println("Test string");
        System.out.println(pathPlannerString);

        if (AutoBuilder.isConfigured())
        {
            if(AutoBuilder.getAllAutoNames().contains(pathPlannerString))
            {
                pathPlannerCommand = AutoBuilder.buildAuto(pathPlannerString);
            }
        }
        else
            System.out.println(" AutoBuilder is not Configured");
    }
    
    public String getPathPlannerString()
    {
        return pathPlannerString;
    }

    public Command getPathPlannerCommand()
    {
        return pathPlannerCommand;
    }

    public boolean isDataValid()
    {
        //errorMessage = "";
        String msgValid = "";
        boolean isValid = true;
        //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
        
        // boolean isContainingPreload = (containingPreloadBox.getSelected() == AutonomousTabData.ContainingPreload.kYes);
        // boolean isScorePreload = (scorePreloadBox.getSelected() == AutonomousTabData.ScorePreload.kYes);
        // boolean isShootDelay = 
        // (shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k0 ||
        // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k1 ||
        // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k2 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k3 );
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k4 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k5 )
        // boolean isPickupSecondNote = (pickupNotesBox.getSelected() == AutonomousTabData.PickupSecondNote.kYes);
        // boolean isScoreMoreNotes = 
        // (//scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k0 ||
        //  scoreCoral == AutonomousTabData.Score_Coral_.k1 ||
        //  scoreCoral == AutonomousTabData.Score_Coral_.k2 ||
        //  scoreCoral == AutonomousTabData.Score_Coral_.k3 ||
        //  scoreCoral == AutonomousTabData.Score_Coral_.k4);
        // boolean isDriveDelay = 
        //  (driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k0 ||
        // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k1 ||
        // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k2 ||
        //  driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k3 );
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k4 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k5 )
        // boolean isStartingLocation =
        // (startingSide == AutonomousTabData.StartingSide.kLeftWall ||
        // startingSide == AutonomousTabData.StartingSide.kRightWall ||
        // startingSide == AutonomousTabData.StartingSide.kMiddle);

        boolean isSitPretty =
        (sitPretty == AutonomousTabData.SitPretty.kYes);
        
        // boolean isOverDelay = 
        // (shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k3 && 
        // driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k3);


        // boolean isStage = 
        // (stageBox.getSelected() == AutonomousTabData.StagePositioning.kThroughStage || 
        // stageBox.getSelected() == AutonomousTabData.StagePositioning.kAroundStage );

        

        // if(!isContainingPreload && isScorePreload) :)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ] - Cannot Score without containing Preload \n";

        // }

        // if(!isPickupSecondNote && isScoreSecondNote)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ] - Cannot Score Second Note without Picking It up \n";

        // }

        
        // if(isShootDelay && isScorePreload) :)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ] - Cannot Score without containing Preload \n";

        // }


        // if(isScoreManyMoreNotes && isOverDelay) 
        // {
        //     isValid = false;
            
        //     msg += "[Not Possible] - Cannot score 2+ extra Notes with double 3 second delays \n";

        // }

        // if(isSitPretty && isScoreMoreNotes)
        // {
        //     isValid = false;

        //     msgValid = " [Backup Option Selected] - Cannot complete any other tasks \n";
        // }

//         if(isNotSub && isStage)
//         {
//             isValid = false;
// //:)
//             msgValid = " [Stage Not Available] - Non-Sub Start selected \n";
//         }

        
        

        // Do NOT remove any of the remaining code
        // Check if the selections are valid
        if(!isValid)
            errorMessage += msgValid;

        
        
        // Displays either "No Errors" or the error messages
        
        //autonomousNameBox.setString(" ");

        //autonomousNameBox.setString(AutoCommandList.pathPlannerString);

        // 
        return isValid;
        

    }   

    public boolean isAutoValid()
    {
        String msgAuto = "";
        boolean autoExists = true;

        // autonomousCommand = new AutoCommandList(robotContainer, autonomousTabData);
        // autonomousTabData.createPathPlannerStringAndCommand();
        // autonomousCommand = autonomousTabData.getPathPlannerCommand();

        // if(!AutoBuilder.getAllAutoNames().contains(AutoCommandList.pathPlannerString))
        if(AutoBuilder.isConfigured())
        {

        
            if(!AutoBuilder.getAllAutoNames().contains(pathPlannerString))
            {
                // add(AutoBuilder.buildAuto(AutoCommandList.pathPlannerString));
                autoExists = false;
                //doesAutonomousExist.setBoolean(false);

                msgAuto = " [Selected Autonomous Path does NOT exist ]\n";
        
            }
        
        }
        else
        {
            autoExists = false;
            msgAuto = "[ AutoBuilder is not configured ]\n";
        }

        if(!autoExists)
            errorMessage += msgAuto;

        return autoExists;

    }

    public String getErrorMessage()
    {
        return errorMessage;
    }


    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";


        str += "Starting Side             : " + leftWall   + "\n";
        str += "Sit Pretty                     : " + sitPretty + "\n";

        // str += "Drive Out Of Start Zone     : " + driveOutOfStartZone  + "\n";
        // str += "Containing Preload          : " + containingPreload + "\n";
        // str += "Score Preload               : " + scorePreload  + "\n";  
        // str += "Shoot Delay                    : " + shootDelay + "\n";   
        // str += "Drive Delay                    : " + driveDelay + "\n";  
        // str += "Pickup Second Note          : " + pickupSecondNote + "\n";
        // str += "Score Extra Notes               : " + scoreExtraNotes + "\n";
        // str += "Stage Positioning               : " + stagePositioning + "\n";
//FIXME IF STATEMENT
        // str += "Sit Pretty                     : " + sitPretty + "\n";


        return str;
    }
}