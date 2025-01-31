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

    public static final double MAX_BATTERY_VOLTAGE          = 12.0;
    public static final double END_OF_MATCH_BATTERY_VOLTAGE = 11.5; // This is the estimated voltage at the end of each match, used in subsystems with setVoltage()
    
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

        // public static final double L4                                   = 100.0;
        // public static final double UPPER_REEF_ALGAE                     = 90.0;
        // public static final double L3                                   = 80.0;
        // public static final double LOWER_REEF_ALGAE                     = 70.0;
        // public static final double L2                                   = 60.0;
        // public static final double L1                                   = 40.0;
        // public static final double GRAB_CORAL_POSITION                  = 20.0;
        // public static final double RESTING_POSITION                     = 0.0;
    }

    public static class Intake
    {
        public static final int MOTOR_PORT                              = 0;
        // public static final int BOTTOM_MOTOR_PORT                       = 0;
        public static final String MOTOR_CAN_BUS                        = ROBORIO;
        // public static final String BOTTOM_MOTOR_CAN_BUS                 = CANIVORE;
    }
    
    public static class Climb
    {
        public static final int MOTOR_PORT                              = 0;
        public static final String MOTOR_CAN_BUS                        = CANIVORE;

        public static final double CLIMB_UP_CAGE_POSITION                    = 50.0;   // Check value once we have robot
        public static final double CLIMB_DOWN_CAGE_POSITION                  = 10.0;      // Check value once we have robot
    }

    public static class Pivot
    {
        // public static final int LEFT_MOTOR_PORT                         = 0;
        public static final int MOTOR_PORT                              = 0;
        public static final String MOTOR_CAN_BUS                        = ROBORIO;

    //     public static final double L4                                   = 40.0;
    //     public static final double L3                                   = 30.0;
    //     public static final double GRAB_REEF_ALGAE                      = 25.0;
    //     public static final double L2                                   = 20.0;
    //     public static final double L1                                   = 10.0;
    //     public static final double HOLD_ALGAE_POSITION                  = 5.0;
    //     public static final double RESTING_POSITION                     = 0.0;
    }

    public static class Grabber
    {
        public static final int KICK_MOTOR_PORT                         = 0;
        public static final int GRAB_MOTOR_PORT                         = 0;
        public static final String KICK_MOTOR_CAN_BUS                   = ROBORIO;
        public static final String GRAB_MOTOR_CAN_BUS                   = ROBORIO;
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
        public static final int MOTOR_PORT                              = 0;
        public static final String MOTOR_CAN_BUS                        = ROBORIO;
    }

    public static class Shuttle
    {
        public static final int MOTOR_PORT                              = 0;
        public static final String MOTOR_CAN_BUS                        = ROBORIO;
    }

    public static class Proximity
    {
        public static final int CORAL_INTAKE_PORT                             = 0;
        public static final int ALGAE_INTAKE_PORT                       = 0;
        public static final int ELEVATOR_PORT                           = 0;
        public static final int GRABBER_PORT                            = 0;
    }

    public static class Controllers
    {
        public static final int DRIVER_CONTROLLER_PORT                  = 0;
        public static final int OPERATOR_CONTROLLER_PORT                = 1;
    }

    // public enum TargetPosition
    // {
    //     kStartingPosition(Constants.Elevator.RESTING_POSITION, Constants.Pivot.RESTING_POSITION),
    //     kGrabCoralPosition(Constants.Elevator.GRAB_CORAL_POSITION, Constants.Pivot.RESTING_POSITION),
    //     kHoldAlgaePosition(Constants.Elevator.L1, Constants.Pivot.HOLD_ALGAE_POSITION),
    //     kL1(Constants.Elevator.L1, Constants.Pivot.L1),
    //     kL2(Constants.Elevator.L2, Constants.Pivot.L2),
    //     kLowerReefAlgae(Constants.Elevator.LOWER_REEF_ALGAE, Constants.Pivot.GRAB_REEF_ALGAE),
    //     kL3(Constants.Elevator.L3, Constants.Pivot.L3),
    //     kUpperReefAlgae(Constants.Elevator.UPPER_REEF_ALGAE, Constants.Pivot.GRAB_REEF_ALGAE),
    //     kL4(Constants.Elevator.L4, Constants.Pivot.L4),
    //     kOverride(-4237, -4237);

    //     public final double pivot;
    //     public final double elevator;

    //     private TargetPosition(double elevator, double pivot)
    //     {
    //         this.pivot = pivot;
    //         this.elevator = elevator;
    //     }

    // }
}
