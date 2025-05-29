package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

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

        i.manualExtend(gamepad1.right_trigger - gamepad1.left_trigger);
    }
}
