package frc.robot;

import java.lang.invoke.MethodHandles;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public static final String NETWORK_TABLE_NAME = "TeamLHS";
    public static final String ADVANTAGE_SCOPE_TABLE_NAME = "ASTable";

    // These are the names of the CAN bus set on the roboRIO and CANivore
    public static final String CANIVORE = "CANivore";
    public static final String ROBORIO  = "rio";

    public static class Elevator
    {
        public static final int LEFT_MOTOR_PORT                         = 0;
        public static final int RIGHT_MOTOR_PORT                        = 0;
        public static final String LEFT_MOTOR_CAN_BUS                   = CANIVORE;
        public static final String RIGHT_MOTOR_CAN_BUS                  = CANIVORE;

        public static final double L4                                   = 0.0;
        public static final double L3                                   = 0.0;
        public static final double L2                                   = 0.0;
        public static final double L1                                   = 0.0;
        public static final double STARTING_POSITION                     = 0.0;
        public static final double GRAB_CORAL_POSITION                  = 0.0;
    }

    public static class Intake
    {
        public static final int TOP_MOTOR_PORT                          = 0;
        public static final int BOTTOM_MOTOR_PORT                       = 0;
        public static final String TOP_MOTOR_CAN_BUS                    = CANIVORE;
        public static final String BOTTOM_MOTOR_CAN_BUS                 = CANIVORE;
    }
    
    public static class Climb
    {
        public static final int MOTOR_PORT                              = 0;
        public static final String MOTOR_CAN_BUS                        = CANIVORE;

        public static final double CLIMB_UP_POSITION                    = 1000.0;   // Check value once we have robot
        public static final double CLIMB_DOWN_POSITION                  = 0.0;      // Check value once we have robot
    }

    public static class Shoulder
    {
        public static final int MOTOR_PORT                              = 0;
        public static final String MOTOR_CAN_BUS                        = CANIVORE;

        public static final double L4                                   = 0.0;
        public static final double L3                                   = 0.0;
        public static final double L2                                   = 0.0;
        public static final double L1                                   = 0.0;
        public static final double STARTING_POSITION                    = 0.0;
        public static final double GRAB_CORAL_POSITION                  = 0.0;
    }

    public static class Claw
    {
        public static final int TOP_MOTOR_PORT                          = 0;
        public static final int BOTTOM_MOTOR_PORT                       = 0;
        public static final String TOP_MOTOR_CAN_BUS                    = ROBORIO;
        public static final String BOTTOM_MOTOR_CAN_BUS                 = ROBORIO;
    }

    public static class Drivetrain
    {
        public static int FRONT_LEFT_DRIVE_PORT                         = 0;
        public static int FRONT_LEFT_TURN_PORT                          = 0;
        public static int FRONT_LEFT_ENCODER_PORT                       = 0;

        public static int FRONT_RIGHT_DRIVE_PORT                        = 0;
        public static int FRONT_RIGHT_TURN_PORT                         = 0;
        public static int FRONT_RIGHT_ENCODER_PORT                      = 0;

        public static int BACK_LEFT_DRIVE_PORT                          = 0;
        public static int BACK_LEFT_TURN_PORT                           = 0;
        public static int BACK_LEFT_ENCODER_PORT                        = 0;

        public static int BACK_RIGHT_DRIVE_PORT                         = 0;
        public static int BACK_RIGHT_TURN_PORT                          = 0;
        public static int BACK_RIGHT_ENCODER_PORT                       = 0;
    }

    public static class IntakeWrist
    {
        public static int MOTOR_PORT                                    = 0;
        public static String MOTOR_CAN_BUS                              = CANIVORE;
    }

    public static class Shuttle
    {
        public static int MOTOR_PORT                                    = 0;
        public static String MOTOR_CAN_BUS                              = ROBORIO;
    }

    public enum TargetPosition
    {
        kStartingPosition(Constants.Elevator.STARTING_POSITION, Constants.Shoulder.STARTING_POSITION),
        kGrabCoralPosition(Constants.Elevator.GRAB_CORAL_POSITION, Constants.Shoulder.GRAB_CORAL_POSITION),
        kL1(Constants.Elevator.L1, Constants.Shoulder.L1),
        kL2(Constants.Elevator.L2, Constants.Shoulder.L2),
        kL3(Constants.Elevator.L3, Constants.Shoulder.L3),
        kL4(Constants.Elevator.L4, Constants.Shoulder.L4),
        kOverride(-4237, -4237);

        public final double shoulder;
        public final double elevator;

        private TargetPosition(double shoulder, double elevator)
        {
            this.shoulder = shoulder;
            this.elevator = elevator;
        }

    }
}
