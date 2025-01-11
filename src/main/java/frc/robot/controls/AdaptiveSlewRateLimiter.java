package frc.robot.controls;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.WPIUtilJNI;

public class AdaptiveSlewRateLimiter 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final double accelRateLimit;
    private final double decelRateLimit;
    private double prevVal;
    private double prevTime;

    /**
     * Creates a new SlewRateLimiter with the given rate limit and initial value.
     *
     * @param accelRateLimit The rate-of-change limit, in units per second.
     * @param decelRateLimit The rate-of-change limit, in units per second.
     */
    public AdaptiveSlewRateLimiter(double accelRateLimit, double decelRateLimit) 
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.accelRateLimit = Math.abs(accelRateLimit);
        this.decelRateLimit = Math.abs(decelRateLimit);
        
        prevVal = 0;
        prevTime = WPIUtilJNI.now() * 1.0e-6;

        System.out.println("  Constructor Finished: " + fullClassName);
    }

     /**
     * Filters the input to limit its slew rate.
     *
     * @param input The input value whose slew rate is to be limited.
     * @return The filtered value, which will not change faster than the slew rate.
     */
    public double calculate(double input) 
    {
        double currentTime = WPIUtilJNI.now() * 1.0e-6;
        double elapsedTime = currentTime - prevTime;
        double currRateLimit = (Math.abs(input) > Math.abs(prevVal) ? accelRateLimit : decelRateLimit);

        // SmartDashboard.putNumber("Acc?",Math.abs(input) > Math.abs(prevVal) ? 1 : 0);
        // SmartDashboard.putNumber("Prev Val",prevVal);

        prevVal +=
                MathUtil.clamp(input - prevVal, -currRateLimit * elapsedTime, currRateLimit * elapsedTime);
        prevTime = currentTime;

        return prevVal;
    }

    /**
     * Resets the slew rate limiter to the specified value; ignores the rate limit when doing so.
     *
     * @param value The value to reset to.
     */
    public void reset(double value) 
    {
        prevVal = value;
        prevTime = WPIUtilJNI.now() * 1e-6;
    }
}