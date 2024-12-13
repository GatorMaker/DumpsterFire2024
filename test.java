package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;
import com.shprobotics.pestocore.drivebases.MecanumController;
import com.shprobotics.pestocore.drivebases.TeleOpController;
import com.shprobotics.pestocore.drivebases.ThreeWheelOdometryTracker;
import com.shprobotics.pestocore.geometries.Pose2D;
import com.shprobotics.pestocore.geometries.Vector2D;

@TeleOp
public class test extends LinearOpMode {

    private DcMotor wormGearMotor;
    private DcMotor strongArmMotor;
    private DcMotor viperslide;

    private Servo claw;

    @Override
    public void runOpMode() {
        MecanumController mecanumController = PestoFTCConfig.getMecanumController(hardwareMap);
        ThreeWheelOdometryTracker threeWheelOdometryTracker = PestoFTCConfig.getTracker(hardwareMap);
        TeleOpController teleOpController = PestoFTCConfig.getTeleOpController(mecanumController, threeWheelOdometryTracker, hardwareMap);

        wormGearMotor = hardwareMap.get(DcMotor.class, "wormGear");
        wormGearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        wormGearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wormGearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        strongArmMotor = hardwareMap.get(DcMotor.class, "strongArm");
        strongArmMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        strongArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        strongArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        viperslide = hardwareMap.get(DcMotor.class, "arm");
        viperslide.setDirection(DcMotorSimple.Direction.FORWARD);
        viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        viperslide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw = hardwareMap.get(Servo.class, "Claw");
        claw.setDirection(Servo.Direction.FORWARD);


        waitForStart();

        while (opModeIsActive()) {
            threeWheelOdometryTracker.updateOdometry();
            Vector2D currentPosition = threeWheelOdometryTracker.getCurrentPosition();
            double heading = threeWheelOdometryTracker.getCurrentHeading();

            teleOpController.updateSpeed(gamepad1, gamepad2);

            teleOpController.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            //WormGear Code
            if (gamepad1.right_trigger>0.2){
                wormGearMotor.setPower(gamepad1.right_trigger);
            }
            else if (gamepad1.left_trigger>0.2) {
                wormGearMotor.setPower(-gamepad1.left_trigger);
            }
            else {
                wormGearMotor.setPower(0.0);
            }

            //Move Worm Gear To Position
            if (gamepad1.right_trigger){
                wormGearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                wormGearMotor.setTargetPosition(-1850);
                wormGearMotor.setPower(1.0);

                while (viperslide.isBusy()){
                    Wait for motor to reach target position
                }
                wormGearMotor.setPower(0.0);
                wormGearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            // Strong Arm Code
            if (gamepad2.right_stick_y>0.2){
               strongArmMotor.setPower(gamepad2.right_stick_y);
            } else if (gamepad2.right_stick_y<0.2) {
                strongArmMotor.setPower(gamepad2.right_stick_y);
            }
            else {
                strongArmMotor.setPower(0.0);
//                stongArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION)
//                strongArmMotor.setTargetPosition(InsertHere...)
            }

            //Viper Slide Code

            if (gamepad1.right_stick_y>0.2 && !(viperslide.getCurrentPosition()<-1500 && wormGearMotor.getCurrentPosition()>15000)){
                viperslide.setPower(gamepad1.right_stick_y);
            } else if (gamepad1.right_stick_y<0.2) {
                viperslide.setPower(gamepad1.right_stick_y);
            }
            else {
                viperslide.setPower(0.0);
            }


            //Viper Slide Button Positions
            if (gamepad1.dpad_up){
                viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                viperslide.setTargetPosition(-2375);
                viperslide.setPower(1.0);
                viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //    while (viperslide.isBusy()){
                    //Wait for motor to reach target position
            //    }
            //    viperslide.setPower(0.0);
            //
            }
            if (gamepad1.dpad_down){
                viperslide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                viperslide.setTargetPosition(145);
                viperslide.setPower(1.0);
                viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //    while (viperslide.isBusy()){
                    //Wait for motor to reach target position
            //    }
            //    viperslide.setPower(0.0);
            //    viperslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            //Claw
            if (gamepad1.x){
                claw.setPosition(0.75);
            }
            if (gamepad1.y){
                claw.setPosition(1.0);
            }


            telemetry.addData("x", currentPosition.getX());
            telemetry.addData("y", currentPosition.getY());
            telemetry.addData("rotation", heading);
            telemetry.addData("wormGear", wormGearMotor.getCurrentPosition());
            telemetry.addData("strongArm", strongArmMotor.getCurrentPosition());
            telemetry.addData("viperSlide",viperslide.getCurrentPosition());
            telemetry.update();


            if (gamepad1.b) {
                threeWheelOdometryTracker.reset();
                //teleOpController.resetIMU();
            }

        }
    }
}

//12/7 Comp Code
