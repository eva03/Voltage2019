package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotutil.IMU;

public class SlideTask extends TaskThread {

    public DcMotor rSlide, lSlide;
    public IMU imu;

    LinearOpMode opMode;

    public SlideTask( LinearOpMode opMode) {
        this.opMode = opMode;
        rSlide = opMode.hardwareMap.dcMotor.get("rSlide");

        lSlide = opMode.hardwareMap.dcMotor.get("lSlide");



        rSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void run(){
        while (opMode.opModeIsActive()) {
            if (opMode.gamepad1.dpad_down) {
                rSlide.setPower(.8);
                lSlide.setPower(-.8);
                opMode.telemetry.addData("up", .8);
            } else if (opMode.gamepad1.dpad_up) {
                rSlide.setPower(-.8);
                lSlide.setPower(.8);
                opMode.telemetry.addData("down", .8);
            } else {
                rSlide.setPower(0);
                lSlide.setPower(0);
                opMode.telemetry.addData("stop", 0);
            }


        }
    }

    public void initialize(){

    }
}
