package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.config.subsystems.Outtake;

@TeleOp
@Configurable
public class TestOuttakePID extends OpMode {

    public static int t = 0;
    public static int p = 0;
    Outtake o;
    @Override
    public void init() {
        o = new Outtake(hardwareMap);
    }

    @Override
    public void loop() {
        p = o.getPos();
        o.setTarget(t);
        o.periodic();
    }
}
