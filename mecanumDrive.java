package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class mecanumDrive extends OpMode {

    DcMotor FR;
    DcMotor FL;
    DcMotor BR;
    DcMotor BL;
    DcMotor wormGear;
    DcMotor longArm;
    Servo Claw;

    @Override
    public void init(){

        FR = hardwareMap.get(DcMotorEx.class, "FR");
        FL = hardwareMap.get(DcMotorEx.class, "FL");
        BR = hardwareMap.get(DcMotorEx.class, "BR");
        BL = hardwareMap.get(DcMotorEx.class, "BL");
        wormGear = hardwareMap.get(DcMotorEx.class, "wormGear");
        longArm = hardwareMap.get(DcMotorEx.class, "longArm");

        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        wormGear.setDirection(DcMotorSimple.Direction.FORWARD);
        longArm.setDirection(DcMotorSimple.Direction.FORWARD);

        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wormGear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        longArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wormGear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        longArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Claw = hardwareMap.get(Servo.class, "Claw");
        Claw.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){


        mecanum(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        wormGear.setPower(gamepad1.right_trigger);
        wormGear.setPower(-gamepad1.left_trigger);
        longArm.setPower(gamepad1.right_stick_y);

        if (gamepad1.a){
            Claw.setPosition(0);
        }
        else if (gamepad1.b) {
            Claw.setPosition(1);
        }

    }

    public void mecanum(double leftY, double leftX, double rightX) {
        FL.setPower(leftY - leftX - rightX);
        BL.setPower(leftY + leftX - rightX);
        FR.setPower(leftY + leftX + rightX);
        BR.setPower(leftY - leftX + rightX);
    }
}
