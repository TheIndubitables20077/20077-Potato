package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.config.core.util.OpModeCommand;
import org.firstinspires.ftc.teamcode.config.pedro.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Intake;

@TeleOp
public class OverallTest extends OpModeCommand {

    private Intake i;
    private Follower f;

    @Override
    public void initialize() {
        i = new Intake(hardwareMap);
        f = Constants.createFollower(hardwareMap);
        f.setStartingPose(new Pose(0, 0,0));
        f.update();
    }

    @Override
    public void start() {
        f.startTeleopDrive();
    }

    @Override
    public void loop() {
        f.setTeleOpDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true);
        f.update();

        if (gamepad1.a)
            i.intake();

        if (gamepad1.b)
            i.transfer();

        if (gamepad1.y)
            i.outtake();

        if (gamepad1.x)
            i.stop();

        i.manual(gamepad1.right_trigger, gamepad1.left_trigger);

        telemetry.addLine("Intake Controls" +
                "\n");
        telemetry.addLine("Press: " +
                "A to intake " +
                "B to transfer" +
                "Y to outtake" +
                "X to stop" + "\n" +
                "Use triggers to extend/retract the intake" + "\n");
        telemetry.addData("Encoder", i.getEncoder());
        telemetry.update();
    }
}
