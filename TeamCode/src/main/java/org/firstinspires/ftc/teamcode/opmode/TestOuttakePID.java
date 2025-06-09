package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.ftcontrol.panels.Panels;
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.config.subsystems.Outtake;

@TeleOp
@Configurable
public class TestOuttakePID extends OpMode {
    TelemetryManager pT;

    public static int t = 0;
    Outtake o;
    @Override
    public void init() {
        o = new Outtake(hardwareMap);
        pT = Panels.getTelemetry();
    }

    @Override
    public void loop() {
        o.setTarget(t);
        o.periodic();

        pT.graph("Position", o.getPos());
        pT.graph("Target", t);

        telemetry.update();
        pT.update(telemetry);
    }
}
