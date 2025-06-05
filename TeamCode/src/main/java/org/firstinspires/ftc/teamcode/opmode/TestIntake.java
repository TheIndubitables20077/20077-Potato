package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.config.core.util.OpModeCommand;
import org.firstinspires.ftc.teamcode.config.subsystems.Intake;

@TeleOp
public class TestIntake extends OpModeCommand {

    private Intake i;

    @Override
    public void initialize() {
        i = new Intake(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a)
            i.intake();

        if (gamepad1.b)
            i.transfer();

        if (gamepad1.y)
            i.outtake();

        if (gamepad1.x)
            i.stop();

        if (gamepad1.dpad_down)
            i.setPosition(Intake.pTransfer);

        i.manual(gamepad1.right_trigger, gamepad1.left_trigger, telemetry);

        telemetry.addLine("Intake Controls" +
                "\n");
        telemetry.addLine("Press: " +
                "A to intake " +
                "B to transfer" +
                "Y to outtake" +
                "X to stop" + "\n" +
                "Use triggers to extend/retract the intake" + "\n");
        telemetry.addData("Encoder", i.getPos());
        telemetry.addData("Gamepad1 Left Trigger", gamepad1.left_trigger);
        telemetry.addData("Gamepad1 Right Trigger", gamepad1.right_trigger);
        telemetry.update();

        i.periodic();
    }
}
