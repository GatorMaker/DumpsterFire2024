package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.shprobotics.pestocore.algorithms.PID;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.ThreeWheelOdometryTracker;
import com.shprobotics.pestocore.geometries.BezierCurve;
import com.shprobotics.pestocore.geometries.ParametricHeading;
import com.shprobotics.pestocore.geometries.PathContainer;
import com.shprobotics.pestocore.geometries.PathFollower;
import com.shprobotics.pestocore.geometries.Vector2D;

@Autonomous
public class auto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumController mecanumController = PestoFTCConfig.getMecanumController(hardwareMap);
        ThreeWheelOdometryTracker threeWheelOdometryTracker = PestoFTCConfig.getTracker(hardwareMap);

        PathContainer pathContainer = new PathContainer.PathContainerBuilder()
                .addCurve(new BezierCurve(new Vector2D[]{
                        new Vector2D(0, 0),
                        new Vector2D(0, 100)
                }),
                        new ParametricHeading(0, -Math.PI / 2)
                )

                .addCurve(new BezierCurve(new Vector2D[]{
                                new Vector2D(0, 100),
                                new Vector2D(100, 100)
                        }),
                        new ParametricHeading(-Math.PI / 2, -Math.PI / 2)
                )
                .build();



        PathFollower pathFollower = new PathFollower.PathFollowerBuilder(mecanumController, threeWheelOdometryTracker, pathContainer)
                .setDeceleration(PestoFTCConfig.DECELERATION)
                .setHeadingPID(new PID(0.1, 0, 0))
                .setEndpointPID(new PID(0,  0, 0))
                .build();

        waitForStart();

        while (opModeIsActive()) {
            threeWheelOdometryTracker.updateOdometry();
            pathFollower.update();

            Vector2D currentPosition = threeWheelOdometryTracker.getCurrentPosition();
            double heading = threeWheelOdometryTracker.getCurrentHeading();

            telemetry.addData("x", currentPosition.getX());
            telemetry.addData("y", currentPosition.getY());
            telemetry.addData("rotation", heading);
            telemetry.update();
        }
    }
}
