package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.math.geometry.Translation2d;


public class SwerveModuleConfig {

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
    public final String moduleName;
    public final Translation2d moduleLocation;
    public final int driveMotorChannel;
    public final boolean driveMotorInverted;
    public final int turnEncoderChannel;
    public final double turnEncoderOffset;
    public final int turnMotorChannel;
    
    public SwerveModuleConfig(String moduleName,
                            Translation2d moduleLocation,
                            int driveMotorChannel, 
                            boolean driveMotorInverted, 
                            int turnEncoderChannel, 
                            double turnEncoderOffset, 
                            int turnMotorChannel)
    {
        this.moduleName = moduleName;
        this.moduleLocation = moduleLocation;
        this.driveMotorChannel = driveMotorChannel;
        this.driveMotorInverted = driveMotorInverted;
        this.turnEncoderChannel = turnEncoderChannel;
        this.turnEncoderOffset = turnEncoderOffset;
        this.turnMotorChannel = turnMotorChannel;
    }

    public String toString()
    {
        return " ";
    }
}
