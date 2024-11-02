package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.TeleOpController;
import com.shprobotics.pestocore.drivebases.ThreeWheelOdometryTracker;
import com.shprobotics.pestocore.geometries.Pose2D;
import com.shprobotics.pestocore.geometries.Vector2D;

@TeleOp
public class test extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumController mecanumController = PestoFTCConfig.getMecanumController(hardwareMap);
        ThreeWheelOdometryTracker threeWheelOdometryTracker = PestoFTCConfig.getTracker(hardwareMap);
        TeleOpController teleOpController = PestoFTCConfig.getTeleOpController(mecanumController, threeWheelOdometryTracker, hardwareMap);


        waitForStart();

        while (opModeIsActive()) {
            threeWheelOdometryTracker.updateOdometry();
            Vector2D currentPosition = threeWheelOdometryTracker.getCurrentPosition();
            double heading = threeWheelOdometryTracker.getCurrentHeading();

            teleOpController.updateSpeed(gamepad1);

            teleOpController.driveFieldCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


            telemetry.addData("x", currentPosition.getX());
            telemetry.addData("y", currentPosition.getY());
            telemetry.addData("rotation", heading);
            telemetry.update();


            if (gamepad1.b) {
                threeWheelOdometryTracker.reset();
            }
        }
    }
}
