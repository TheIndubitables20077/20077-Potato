package org.firstinspires.ftc.teamcode.config.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.BezierPoint;
import com.seattlesolvers.solverslib.command.CommandBase;

/**
 * A command that calls {@link Follower#holdPoint(Pose)}
 */
public class HoldPoint extends CommandBase {
    private final Follower follower;
    private Pose pose;
    private final boolean isFieldCentric;

    /**
     * Moves robot to a new {@link Pose} that is either field or robot centric
     * @param follower The follower object
     * @param pose The pose that the robot should go to (see isFieldCentric parameter)
     *             The following robot centric movements are true assuming that the robot is facing forwards to the long side on the submersible:
     *             {@link Pose#getX()} -Y is forwards, +Y is backwards
     *             {@link Pose#getY()} +X is left, -X is right
     *             {@link Pose#getHeading()} Heading is in radians, +heading turns left and -heading turns right
     * @param isFieldCentric Whether the move should be field centric or robot centric (based off the follower's position at the time of scheduling the command)
     */
    public HoldPoint(Follower follower, Pose pose, boolean isFieldCentric) {
        this.follower = follower;
        this.pose = pose;
        this.isFieldCentric = isFieldCentric;
    }

    public HoldPoint(Follower follower, BezierPoint bezierPoint, double heading, boolean isFieldCentric) {
        this.follower = follower;
        this.pose = new Pose(bezierPoint.getFirstControlPoint().getX(), bezierPoint.getFirstControlPoint().getY(), heading);
        this.isFieldCentric = isFieldCentric;
    }

    @Override
    public void initialize() {
        if (!isFieldCentric) {
            pose = pose.plus(follower.getPose());
        }

        follower.holdPoint(pose);
    }

    @Override
    public boolean isFinished() {
        return !follower.isBusy();
    }
}