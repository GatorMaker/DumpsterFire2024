package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.TeleOpController;
import com.shprobotics.pestocore.drivebases.ThreeWheelOdometryTracker;

@Config
public class PestoFTCConfig {
    public static double ODOMETRY_TICKS_PER_INCH = 336.877962878;
    public static double FORWARD_OFFSET = -6.75; //-9
    public static double ODOMETRY_WIDTH = 7.25; //8.125
    public static double DECELERATION = -73.8807308633;
    //public static double MAX_VELOCITY = 39;
    public static double MAX_VELOCITY = 19;

    public static final DcMotorSimple.Direction leftEncoderDirection = FORWARD;
    public static final DcMotorSimple.Direction centerEncoderDirection = FORWARD;
    public static final DcMotorSimple.Direction rightEncoderDirection = REVERSE;

    public static String leftName = "frontLeft"; //left
    public static String centerName = "backLeft"; //center
    public static String rightName = "frontRight"; //right

    public static MecanumController getMecanumController(HardwareMap hardwareMap) {
        MecanumController mecanumController = new MecanumController(hardwareMap, new String[] {
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        });

        mecanumController.configureMotorDirections(new DcMotorSimple.Direction[]{
                DcMotorSimple.Direction.REVERSE, //reverse
                DcMotorSimple.Direction.FORWARD, //forward
                DcMotorSimple.Direction.REVERSE, //reverse
                DcMotorSimple.Direction.FORWARD, //forward
        });

//        mecanumController.setPowerVectors(new Vector2D[]{
//                Vector2D.scale(new Vector2D(57, 39), 1/69.0651865993),
//                Vector2D.scale(new Vector2D(-57, 39), 1/69.0651865993),
//                Vector2D.scale(new Vector2D(-57, 39), 1/69.0651865993),
//                Vector2D.scale(new Vector2D(57, 39), 1/69.0651865993)
//        });

        mecanumController.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mecanumController.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        return mecanumController;
    }

    public static TeleOpController getTeleOpController(MecanumController mecanumController, ThreeWheelOdometryTracker tracker, HardwareMap hardwareMap) {
        TeleOpController teleOpController = new TeleOpController(mecanumController, hardwareMap);

        //teleOpController.configureIMU(
          //      RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
          //      RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
       // );

        teleOpController.useTrackerIMU(tracker);

        teleOpController.setSpeedController((gamepad) -> {
            if (gamepad.right_trigger > 0.1) {
                return 1.0;
            }
            return 0.6;
        });

        teleOpController.counteractCentripetalForce(tracker, MAX_VELOCITY);
//        teleOpController.deactivateCentripetalForce();

        return teleOpController;
    }

    public static ThreeWheelOdometryTracker getTracker(HardwareMap hardwareMap) {
        return new ThreeWheelOdometryTracker.TrackerBuilder(
                hardwareMap,
                ODOMETRY_TICKS_PER_INCH,
                FORWARD_OFFSET,
                ODOMETRY_WIDTH,
                leftName,
                centerName,
                rightName,
                leftEncoderDirection,
                centerEncoderDirection,
                rightEncoderDirection
        ).build();
    }
}