package org.firstinspires.ftc.teamcode.config.subsystems;

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.core.hardware.CachedMotor;

@Configurable
public class Outtake extends SubsystemBase {
    private CachedMotor l, r;
//    private Servo p, t, s;
    public int pos;
    public PIDController pid;
    public int pidLevel = 0;
    public static int target;

    // Position constants for the Pivot servo
    public static double pTransfer = 0;
    public static double pScore = 0.9;

    // Position constants for the Tilt servo
    public static double tTransfer = 0.5;
    public static double tScore = 0.9;



    public static double high = 5200;
    public static double low = 2600;
    public static double zero = 0;

    public static double kP = 0.01; // Proportional gain for the PIDF controller
    public static double kI = 0;    // Integral gain for the PIDF controller
    public static double kD = 0.0001;    // Derivative gain for the PIDF controller

    /**
     * Constructor for the Outtake subsystem.
     * Initializes the hardware components and registers the subsystem with the CommandScheduler.
     *
     * @param h The HardwareMap instance to access hardware components.
     */
    public Outtake(HardwareMap h) {
        CommandScheduler.getInstance().registerSubsystem(this);

        // Initialize Hardware Components here
        l = new CachedMotor(h.get(DcMotorEx.class, "l"));
        r = new CachedMotor(h.get(DcMotorEx.class, "r"));
//        p = h.get(Servo.class, "op");
//        t = h.get(Servo.class, "t");
//        s = h.get(Servo.class, "s");
        
        r.setDirection(DcMotorSimple.Direction.REVERSE);

        pid = new PIDController(kP, kI, kD);

        l.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        l.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void periodic() {
        if(pidLevel == 1) {
            pid.setPID(kP, kI, kD);

            l.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            r.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            double power = pid.calculate(getPos(), target);

            if (getPos() < 25 && target < 25) {
                setPower(0);
            } else {
                setPower(power);
            }
        } else if (pidLevel == 2){
            target = getPos();
            l.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            r.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            target = getPos();
            l.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            r.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            setPower(0);
        }
        super.periodic();
    }

    /**
     * Sets the power of the Outtake motor.
     *
     * @param power The power level to set for the Outtake motor.
     */
    public void setPower(double power) {
        l.setPower(power);
        r.setPower(power);
    }

    /**
     * Manually extends the Outtake using the provided power.
     *
     * @param left The power to apply via the left trigger
     * @param right The power to apply via the right trigger
     */
    public void manual(double left, double right, Telemetry t) {
        double power = left - right;
        power *= 0.95;

        if ((power > 0.05 && getPos() >= high) || (power < -0.05 && getPos() <= zero)) {
            setPower(0);
            return;
        }

        if(Math.abs(power) > 0.05) {
            pidLevel = 2;
            setPower(power);
        } else if (pidLevel == 2) {
            setPower(0);
        }

        t.addData("Power: ", power);
    }

    /**
     * Sets the target position for the Outtake extension and activates the PID controller.
     * @param t The target position to set for the Outtake extension.
     */
    public void setTarget(int t) {
        pidLevel = 1;
        target = t;
    }

    /**
     * Gets the current position of the Outtake motor's encoder.
     */
    public int getPos() {
        pos = l.getPosition();
        return l.getPosition();
    }
}
