package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.config.core.util.OpModeCommand;
import org.firstinspires.ftc.teamcode.config.pedro.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Intake;
import org.firstinspires.ftc.teamcode.config.subsystems.Outtake;

@TeleOp
public class OverallTest extends OpModeCommand {

    private Intake i;
    private Outtake o;
    private Follower f;
    private Gamepad currentGamepad1, currentGamepad2, previousGamepad1, previousGamepad2;

    @Override
    public void initialize() {
        i = new Intake(hardwareMap);
        o = new Outtake(hardwareMap);
        f = Constants.createFollower(hardwareMap);
        f.setStartingPose(new Pose(0, 0, 0));

        currentGamepad1 = new Gamepad();
        currentGamepad2 = new Gamepad();
        previousGamepad1 = new Gamepad();
        previousGamepad2 = new Gamepad();

        f.update();
    }

    @Override
    public void start() {
        f.startTeleopDrive();
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        if (currentGamepad1.a && !previousGamepad1.a)
            i.switchPivotIntake();

        if (currentGamepad1.b && !previousGamepad1.b)
            i.switchPivotTransfer();

        if (gamepad1.y)
            i.outtake();

        if (gamepad2.a)
            o.transfer();

        if (gamepad2.b)
            o.score();

        if (gamepad2.left_bumper)
            o.open();

        if (gamepad2.right_bumper)
            o.close();

        o.manual(gamepad2.right_trigger, gamepad2.left_trigger, telemetry);
        i.manual(gamepad1.right_trigger, gamepad1.left_trigger, telemetry);

        telemetry.update();

        o.periodic();
        i.periodic();

        f.setTeleOpDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        f.update();
    }
}
