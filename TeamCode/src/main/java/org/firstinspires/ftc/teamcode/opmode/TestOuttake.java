package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.config.core.util.OpModeCommand;
import org.firstinspires.ftc.teamcode.config.subsystems.Intake;
import org.firstinspires.ftc.teamcode.config.subsystems.Outtake;

@TeleOp
public class TestOuttake extends OpModeCommand {

    private Outtake o;

    @Override
    public void initialize() {
        o = new Outtake(hardwareMap);
    }

    @Override
    public void loop() {
//        if (gamepad1.a)
//            o.intake();
//
//        if (gamepad1.b)
//            o.transfer();
//
//        if (gamepad1.y)
//            o.outtake();
//
//        if (gamepad1.x)
//            o.stop();
//
//        if (gamepad1.dpad_down)
//            o.setPosition(Intake.pTransfer);

        o.manual(gamepad1.right_trigger, gamepad1.left_trigger, telemetry);

        telemetry.addLine("Intake Controls" +
                "\n");
        telemetry.addLine("Press: " +
                "A to intake " +
                "B to transfer" +
                "Y to outtake" +
                "X to stop" + "\n" +
                "Use triggers to extend/retract the intake" + "\n");
        telemetry.addData("Encoder", o.getPos());
        telemetry.addData("Gamepad1 Left Trigger", gamepad1.left_trigger);
        telemetry.addData("Gamepad1 Right Trigger", gamepad1.right_trigger);
        telemetry.update();
    }
}
