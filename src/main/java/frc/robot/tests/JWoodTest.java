package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import java.util.List;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.SparkMaxLance;

@SuppressWarnings("unused")
public class JWoodTest implements Test
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



    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;

    // private final ExampleSubsystem exampleSubsystem;
    // private final SparkMaxLance motor = new SparkMaxLance(5, Constants.ROBORIO, "Test Motor");

    private final AprilTagFieldLayout aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);
    // private final List<AprilTag> aprilTagList = aprilTagFieldLayout.getTags();

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public JWoodTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // this.exampleSubsystem = robotContainer.exampleSubsystem;

        // motor.setupFactoryDefaults();
        // motor.setupInverted(true);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

        

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        aprilTagTest();
        // AprilTagLance.printAllTags();
        
    }

    private void aprilTagTest()
    {
        Pose2d aprilTagPose, desiredLeftPipeRobotPose, desiredRightPipeRobotPose, desiredStationRobotPose;
        Translation2d cwPipeTranslation, ccwPipeTranslation;
        Rotation2d aprilTagRotation, robotRotation;
        Transform2d leftPipeTransform, rightPipeTransform;

        cwPipeTranslation = new Translation2d(0.47, -0.33); //0.49, -0.33);
        ccwPipeTranslation = new Translation2d(0.47, -0.03); //0.49, -0.03);
        robotRotation = new Rotation2d(Units.degreesToRadians(-90.0));

        System.out.println();
        for(int i = 1; i <= 22; i++)
        {
            aprilTagPose = aprilTagFieldLayout.getTagPose(i).get().toPose2d();

            if(isReefTag(i))
            {
                if(isBackSideOfReefTag(i))
                {
                    // Swap posts because it is from the perspective of the driver station
                    leftPipeTransform = new Transform2d(ccwPipeTranslation, robotRotation);
                    rightPipeTransform = new Transform2d(cwPipeTranslation, robotRotation);
                }
                else
                {
                    leftPipeTransform = new Transform2d(cwPipeTranslation, robotRotation);
                    rightPipeTransform = new Transform2d(ccwPipeTranslation, robotRotation);
                }
                desiredRightPipeRobotPose = aprilTagPose.transformBy(rightPipeTransform);
                desiredLeftPipeRobotPose = aprilTagPose.transformBy(leftPipeTransform);

                System.out.println("Tag " + i);
                System.out.println("April Tag Pose:     " + poseToString(aprilTagPose));
                System.out.println("Desired Left Pose:  " + poseToString(desiredLeftPipeRobotPose));
                System.out.println("Desired Right Pose: " + poseToString(desiredRightPipeRobotPose) + "\n");
            }

            if(isRightCoralStationTag(i))
            {
                Translation2d offset = new Translation2d(0.296325, -0.273999);
                Rotation2d rotation = new Rotation2d(Units.degreesToRadians(90.0));

                desiredStationRobotPose = aprilTagPose.transformBy(new Transform2d(offset, rotation));
                
                System.out.println("Tag " + i);
                System.out.println("April Tag Pose:     " + poseToString(aprilTagPose));
                System.out.println("Desired Coral Station Pose:  " + poseToString(desiredStationRobotPose));
                
                for(int j = 1; j <= 9; j++)
                {
                    System.out.println("Slot " + j + ": " + poseToString(
                        aprilTagPose.transformBy(
                            new Transform2d(
                                new Translation2d(0.0, Units.inchesToMeters(8 * (5 - j))),
                                Rotation2d.kZero)
                            )
                        )
                    );
                }
                System.out.println("");
            }

            if(isLeftCoralStationTag(i))
            {
                Translation2d offset = new Translation2d(0.296325, 0.273999);
                Rotation2d rotation = new Rotation2d(Units.degreesToRadians(90.0));

                desiredStationRobotPose = aprilTagPose.transformBy(new Transform2d(offset, rotation));
                
                System.out.println("Tag " + i);
                System.out.println("April Tag Pose:     " + poseToString(aprilTagPose));
                System.out.println("Desired Coral Station Pose:  " + poseToString(desiredStationRobotPose));

                for(int j = 1; j <= 9; j++)
                {
                    System.out.println("Slot " + j + ": " + poseToString(
                        aprilTagPose.transformBy(
                            new Transform2d(
                                new Translation2d(0.0, Units.inchesToMeters(8 * (5 - j))),
                                Rotation2d.kZero)
                            )
                        )
                    );
                }
                System.out.println("");
            }
        }
    }

    private String poseToString(Pose2d pose)
    {
        return "(" + pose.getTranslation().getX() + ", " + pose.getTranslation().getY()+ ") " + pose.getRotation().getDegrees();
    }

    private boolean isBackSideOfReefTag(int tagID)
    {
        return (tagID >= 9 && tagID <= 11 || tagID >= 20 && tagID <= 22);
    }

    private boolean isReefTag(int tagID)
    {
        return (tagID >= 6 && tagID <= 11 || tagID >= 17 && tagID <= 22);
    }

    private boolean isRightCoralStationTag(int tagID)
    {
        return (tagID == 2 || tagID == 12);
    }

    private boolean isLeftCoralStationTag(int tagID)
    {
        return (tagID == 1 || tagID == 13);
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // motor.set(0.05);
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        // motor.set(0.0);
    } 
}

/*


Tag 1
April Tag Pose:     (16.697198, 0.65532) 126.0
Desired Coral Station Pose:  (16.301352687672694, 0.733999389515271) -144.0
Slot 1: (16.039628986972044, 0.17756814693667794) 126.0
Slot 2: (16.204021240229032, 0.2970061102025085) 126.0
Slot 3: (16.36841349348602, 0.416444073468339) 126.0
Slot 4: (16.532805746743012, 0.5358820367341695) 126.0
Slot 5: (16.697198, 0.65532) 126.0
Slot 6: (16.86159025325699, 0.7747579632658306) 126.0
Slot 7: (17.02598250651398, 0.894195926531661) 126.0
Slot 8: (17.190374759770968, 1.0136338897974915) 126.0
Slot 9: (17.354767013027956, 1.1330718530633221) 126.0

Tag 2
April Tag Pose:     (16.697198, 7.3964799999999995) -126.00000000000003
Desired Coral Station Pose:  (16.301352687672694, 7.317800610484729) -36.00000000000003
Slot 1: (17.354767013027956, 6.918728146936677) -126.00000000000003
Slot 2: (17.190374759770968, 7.038166110202508) -126.00000000000003
Slot 3: (17.02598250651398, 7.157604073468338) -126.00000000000003
Slot 4: (16.86159025325699, 7.277042036734169) -126.00000000000003
Slot 5: (16.697198, 7.3964799999999995) -126.00000000000003
Slot 6: (16.532805746743012, 7.51591796326583) -126.00000000000003
Slot 7: (16.36841349348602, 7.635355926531661) -126.00000000000003
Slot 8: (16.204021240229032, 7.754793889797491) -126.00000000000003
Slot 9: (16.039628986972044, 7.874231853063322) -126.00000000000003

Tag 6
April Tag Pose:     (13.474446, 3.3063179999999996) -59.99999999999999
Desired Left Pose:  (13.423657616751136, 2.7342860602213133) -149.99999999999997
Desired Right Pose: (13.683465237886468, 2.8842860602213136) -149.99999999999997

Tag 7
April Tag Pose:     (13.890498, 4.0259) 0.0
Desired Left Pose:  (14.360498, 3.6959) -90.0
Desired Right Pose: (14.360498, 3.9959000000000002) -90.0

Tag 8
April Tag Pose:     (13.474446, 4.745482) 59.99999999999999
Desired Left Pose:  (13.995234383248865, 4.987513939778686) -30.00000000000001
Desired Right Pose: (13.735426762113534, 5.137513939778686) -30.00000000000001

Tag 9
April Tag Pose:     (12.643358, 4.745482) 119.99999999999999
Desired Left Pose:  (12.434338762113532, 5.1675139397786864) 29.999999999999993
Desired Right Pose: (12.694146383248864, 5.317513939778686) 29.999999999999993

Tag 10
April Tag Pose:     (12.227305999999999, 4.0259) 180.0
Desired Left Pose:  (11.757305999999998, 4.0559) 90.0
Desired Right Pose: (11.757305999999998, 4.3559) 90.0

Tag 11
April Tag Pose:     (12.643358, 3.3063179999999996) -120.00000000000004
Desired Left Pose:  (12.382377237886466, 2.914286060221314) 149.99999999999994
Desired Right Pose: (12.122569616751134, 3.064286060221314) 149.99999999999994

Tag 12
April Tag Pose:     (0.851154, 0.65532) 54.0
Desired Coral Station Pose:  (1.2469993123273082, 0.733999389515271) 144.0
Slot 1: (0.19358498697204274, 1.1330718530633221) 54.0
Slot 2: (0.35797724022903205, 1.0136338897974917) 54.0
Slot 3: (0.5223694934860214, 0.894195926531661) 54.0
Slot 4: (0.6867617467430107, 0.7747579632658306) 54.0
Slot 5: (0.851154, 0.65532) 54.0
Slot 6: (1.0155462532569892, 0.5358820367341695) 54.0
Slot 7: (1.1799385065139787, 0.41644407346833895) 54.0
Slot 8: (1.344330759770968, 0.29700611020250844) 54.0
Slot 9: (1.5087230130279572, 0.17756814693667788) 54.0

Tag 13
April Tag Pose:     (0.851154, 7.3964799999999995) -54.00000000000001
Desired Coral Station Pose:  (1.2469993123273082, 7.317800610484729) 36.0
Slot 1: (1.5087230130279572, 7.874231853063321) -54.00000000000001
Slot 2: (1.344330759770968, 7.754793889797491) -54.00000000000001
Slot 3: (1.1799385065139787, 7.635355926531661) -54.00000000000001
Slot 4: (1.0155462532569892, 7.51591796326583) -54.00000000000001
Slot 5: (0.851154, 7.3964799999999995) -54.00000000000001
Slot 6: (0.6867617467430107, 7.277042036734169) -54.00000000000001
Slot 7: (0.5223694934860214, 7.157604073468338) -54.00000000000001
Slot 8: (0.35797724022903205, 7.038166110202508) -54.00000000000001
Slot 9: (0.19358498697204274, 6.918728146936678) -54.00000000000001

Tag 17
April Tag Pose:     (4.073905999999999, 3.3063179999999996) -120.00000000000004
Desired Left Pose:  (3.553117616751134, 3.064286060221314) 149.99999999999994
Desired Right Pose: (3.812925237886466, 2.914286060221314) 149.99999999999994

Tag 18
April Tag Pose:     (3.6576, 4.0259) 180.0
Desired Left Pose:  (3.1876, 4.3559) 90.0
Desired Right Pose: (3.1875999999999998, 4.0559) 90.0

Tag 19
April Tag Pose:     (4.073905999999999, 4.745482) 119.99999999999999
Desired Left Pose:  (4.124694383248864, 5.317513939778686) 29.999999999999993
Desired Right Pose: (3.8648867621135325, 5.1675139397786864) 29.999999999999993

Tag 20
April Tag Pose:     (4.904739999999999, 4.745482) 59.99999999999999
Desired Left Pose:  (5.165720762113533, 5.137513939778686) -30.00000000000001
Desired Right Pose: (5.4255283832488646, 4.987513939778686) -30.00000000000001

Tag 21
April Tag Pose:     (5.321046, 4.0259) 0.0
Desired Left Pose:  (5.791046, 3.9959000000000002) -90.0
Desired Right Pose: (5.791046, 3.6959) -90.0

Tag 22
April Tag Pose:     (4.904739999999999, 3.3063179999999996) -59.99999999999999
Desired Left Pose:  (5.113759237886466, 2.8842860602213136) -149.99999999999997
Desired Right Pose: (4.853951616751135, 2.7342860602213133) -149.99999999999997

*/