package org.firstinspires.ftc.teamcode.config.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.core.hardware.CachedMotor;

@Configurable
public class Intake extends SubsystemBase {
    private CachedMotor i, e;
    private Servo p;
    public int pos;
    public PIDController pid;
    public int pidLevel = 0;
    public static int target;

    // Position constants for the intake and transfer for the pivot servo
    public static double pTransfer = 1;
    public static double pIntake = 0;
    public static double pHover = 0.5;

    // Power constants for the intake motor
    public static double mIntake = 1;
    public static double mTransfer = -0.5;

    public static double full = 5200;
    public static double half = 2600;
    public static double zero = 0;

    public static double kP = 0.01; // Proportional gain for the PIDF controller
    public static double kI = 0;    // Integral gain for the PIDF controller
    public static double kD = 0.0001;    // Derivative gain for the PIDF controller

    /**
     * Constructor for the Intake subsystem.
     * Initializes the hardware components and registers the subsystem with the CommandScheduler.
     *
     * @param h The HardwareMap instance to access hardware components.
     */
    public Intake(HardwareMap h) {
        CommandScheduler.getInstance().registerSubsystem(this);

        // Initialize Hardware Components here
        i = new CachedMotor(h.get(DcMotorEx.class, "i"));
        p = h.get(Servo.class, "ip");
        e = new CachedMotor(h.get(DcMotorEx.class, "e"));

        i.setDirection(DcMotorSimple.Direction.REVERSE);

        pid = new PIDController(kP, kI, kD);

        e.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        e.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void periodic() {
        if(pidLevel == 1) {
            pid.setPID(kP, kI, kD);

            e.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            double power = pid.calculate(getPos(), target);

            if (getPos() < 25 && target < 25) {
                e.setPower(0);
            } else {
                e.setPower(power);
            }
        } else if (pidLevel == 2){
            target = getPos();
            e.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            target = getPos();
            e.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            e.setPower(0);
        }
        super.periodic();
    }

    /**
     * Sets the power of the intake motor.
     *
     * @param power The power level to set for the intake motor.
     */
    public void setPower(double power) {
        i.setPower(power);
    }

    /**
     * Sets the position of the pivot servo.
     *
     * @param position The position to set for the pivot servo.
     */
    public void setPosition(double position) {
        p.setPosition(position);
    }

    /**
     * Sets the intake motor to intake mode and positions the servo accordingly.
     */
    public void intake() {
        setPower(mIntake);
        setPosition(pIntake);
    }

    /**
     * Sets the intake motor to transfer mode and positions the servo accordingly.
     */
    public void transfer() {
        setPower(mTransfer);
        setPosition(pTransfer);
    }

    /**
     * Sets the intake motor to outtake mode and positions the servo accordingly.
     */
    public void outtake() {
        setPower(-mIntake);
        setPosition(pIntake);
    }

    /**
     * Stops the intake motor and sets the servo to hover position.
     */
    public void stop() {
        setPower(0);
        setPosition(pHover);
    }

    /**
     * Returns the encoder value of the intake motor.
     *
     * @return The current encoder value of the intake motor.
     */
    public double getEncoder() {
        return e.getPosition();
    }

    public void extend() {
//        usePIDF = true;
    }

    public void retract() {
//        usePIDF = true;
    }

    /**
     * Manually extends the intake using the provided power.
     *
     * @param left The power to apply via the left trigger
     * @param right The power to apply via the right trigger
     */
    public void manual(double left, double right, Telemetry t) {
        double power = right - left;
        power /= 3;

        if ((power > 0.05 && getPos() >= full) || (power < -0.05 && getPos() <= zero)) {
            e.setPower(0);
            return;
        }

        if(Math.abs(power) > 0.05) {
            pidLevel = 2;
            e.setPower(power);
        } else if (pidLevel == 0) {
            e.setPower(0);
        }

        t.addData("Power: ", power);
    }

    /**
     * Sets the target position for the intake extension and activates the PID controller.
     * @param t The target position to set for the intake extension.
     */
    public void setTarget(int t) {
        pidLevel = 1;
        target = t;
    }

    /**
     * Gets the current position of the intake motor's encoder.
     */
    public int getPos() {
        pos = e.getPosition();
        return e.getPosition();
    }
}
