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
        kCoral3Algae0FarSide(" -- Score_Coral_3 -- Score_Algae_0 -- Far_Side"),
        kCoral3Algae0(" -- Score_Coral_3 -- Score_Algae_0"),
        kCoral4Algae0(" -- Score_Coral_4 -- Score_Algae_0"),
        kNo("");

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

    public static enum Right_Wall
    {
        kCoral3Algae0FarSide(" -- Score_Coral_3 -- Score_Algae_0 -- Far_Side"),
        kCoral3Algae0(" -- Score_Coral_3 -- Score_Algae_0"),
        kCoral4Algae0(" -- Score_Coral_4 -- Score_Algae_0"),
        kNo("");

        private final String name;

        private Right_Wall(String name)
        {
            this.name = "Right_Wall_" + name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static enum Middle
    {
        kCoral1Algae2FarSide(" -- Score_Coral_1 -- Score_Algae_2"),
        kNo("");

        private final String name;

        private Middle(String name)
        {
            this.name = "Middle" + name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    

    // public static enum SitPretty
    // {
    //     kYes("Do_Nothing"), 
    //     kNo("Run_Autonomous"); 


    //     private final String name;

    //     private SitPretty(String name)
    //     {
    //         this.name = " -- " + name;
    //     }

    //     @Override
    //     public String toString()
    //     {
    //         return name;
    //     }
    // }

    
    // public static enum Score_Coral_
    // {
    //     // k0(" -- ScoreExtraNotes_0"), 
    //     // k1(" -- ScoreExtraNotes_1"), 
    //     // k2(" -- ScoreExtraNotes_2"), 
    //     // k3(" -- ScoreExtraNotes_3"),
    //     // k4(" -- ScoreExtraNotes_4");
    //     k0("0"), 
    //     k1("1"), 
    //     k2("2"), 
    //     k3("3"),
    //     k4("4");

    //     private final String name;

    //     private Score_Coral_(String name)
    //     {
    //         this.name = " -- ScoreExtraNotes_" + name;
    //     }

    //     @Override
    //     public String toString()
    //     {
    //         return name;
    //     }
    // }




    //-------------------------------------------------------------------//

    // IMPORTANT: Any variables added here must be initialized in the copy constructor below
    public Left_Wall leftWall = Left_Wall.kNo;
    public Right_Wall rightWall = Right_Wall.kNo;
    public Middle middle = Middle.kNo;
    //public SitPretty sitPretty = SitPretty.kNo;
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
        rightWall = atd.rightWall;
        middle = atd.middle;
        //sitPretty = atd.sitPretty;
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
        
       
        // boolean isSitPretty =
        // (sitPretty == AutonomousTabData.SitPretty.kYes);
        

        
        

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


    
        str += "Left Wall             : " + leftWall   + "\n";
        str += "Right Wall             : " + rightWall   + "\n"; 
        str += "Middle             : " + middle   + "\n";   
        //str += "Sit Pretty                     : " + sitPretty + "\n";

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