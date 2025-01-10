package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import frc.robot.Constants;
import frc.robot.motors.MotorControllerLance;
import frc.robot.motors.TalonFXLance;

class SwerveModule extends RobotDriveBase
{

    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final String moduleName;
    // private final TalonFX driveMotor;
    private final MotorControllerLance driveMotor;
    // private final RelativeEncoder driveMotorEncoder;
    private final boolean driveMotorInverted;
    private final CANcoder turnEncoder;
    private final double turnEncoderOffset;
    // private final TalonFX turnMotor;
    private final MotorControllerLance turnMotor;
    // private final RelativeEncoder turnMotorEncoder;


     // *** CLASS CONSTRUCTORS ***
     SwerveModule(SwerveModuleConfig smd)
    {
        moduleName = smd.moduleName;
        System.out.println("  Constructor Started:  " + fullClassName + " >> " + moduleName);

        //TODO enter name of CANbus
        driveMotor = new TalonFXLance(smd.driveMotorChannel, "---------", smd.moduleName + "Drive Motor");

        driveMotorInverted = smd.driveMotorInverted;

        //TODO enter CANbus
        turnEncoder = new CANcoder(smd.turnEncoderChannel, "------");  
        turnEncoderOffset = smd.turnEncoderOffset;

        //TODO enter name of CANbus
        turnMotor = new TalonFXLance(smd.turnMotorChannel, "----------", smd.moduleName + "Turn Motor");
    }

    
    public void stopModule()
    {
        // driveMotor.set(ControlMode.PercentOutput, 0.0);
        driveMotor.set(0.0);

        // turnMotor.set(ControlMode.PercentOutput, 0.0);
        turnMotor.set(0.0);
    }


    @Override
    public String getDescription()
    {
        return "Swerve " + moduleName;
    }

    @Override
    public void stopMotor()
    {
        stopModule();
    }

}
