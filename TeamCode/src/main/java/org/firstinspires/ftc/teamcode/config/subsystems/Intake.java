package org.firstinspires.ftc.teamcode.config.subsystems;

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.config.core.hardware.CachedMotor;

@Configurable
public class Intake extends SubsystemBase {
    private CachedMotor i, e;
    private Servo p;
    private Motor.Encoder en;
    private PIDFController pidf;
    private boolean usePIDF = false; // Flag to determine if PIDF control is used

    // Position constants for the intake and transfer for the pivot servo
    public static double pTransfer = 1;
    public static double pIntake = 0;
    public static double pHover = 0.5;

    // Power constants for the intake motor
    public static double mIntake = 1;
    public static double mTransfer = -0.5;

    public static double kP = 0.01; // Proportional gain for the PIDF controller
    public static double kI = 0;    // Integral gain for the PIDF controller
    public static double kD = 0.0001;    // Derivative gain for the PIDF controller
    public static double kF = 0.001;    // Feedforward gain for the PIDF controller

    /**
     * Constructor for the Intake subsystem.
     * Initializes the hardware components and registers the subsystem with the CommandScheduler.
     *
     * @param h The HardwareMap instance to access hardware components.
     */
    public Intake(HardwareMap h) {
        CommandScheduler.getInstance().registerSubsystem(this);

        // Initialize Hardware Components here
        i = h.get(CachedMotor.class, "i");
        p = h.get(Servo.class, "ip");
        e = h.get(CachedMotor.class, "e");
        en = h.get(Motor.Encoder.class, "e");

        pidf = new PIDFController(kP, kI, kD, kF);

        en.reset();
    }

    @Override
    public void periodic() {
        super.periodic();

        // Update the PIDF controller with the current encoder value
        if (usePIDF()) {
            double currentEncoderValue = en.getDistance();
            double output = pidf.calculate(currentEncoderValue);
            e.setPower(output);
        }
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
        return en.getDistance();
    }

    public void extend() {
        usePIDF = true;

    }

    public void retract() {
        usePIDF = true;
    }

    /**
     * Manually extends the intake using the provided power.
     *
     * @param power The power level to set for the extension motor.
     */
    public void manualExtend(double power) {
        if (power > 0.05 || power < -0.05) {
            usePIDF = false;
            e.setPower(power);
        }
    }

    /**
     * Checks if the PIDF controller is being used.
     *
     * @return true if the PIDF controller is used, false otherwise.
     */
    public boolean usePIDF() {
        return usePIDF;
    }
}
