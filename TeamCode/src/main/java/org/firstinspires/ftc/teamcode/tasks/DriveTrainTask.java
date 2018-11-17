package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.IMU;

public class DriveTrainTask extends TaskThread {
    public DcMotor rB, lB;
    public IMU imu;



    LinearOpMode opMode;

    public DriveTrainTask( LinearOpMode opMode) {
        this.opMode = opMode;
        lB = opMode.hardwareMap.dcMotor.get("lB");
        rB = opMode.hardwareMap.dcMotor.get("rB");
        BNO055IMU adaImu = opMode.hardwareMap.get(BNO055IMU.class, "imu");

        imu = new IMU(adaImu);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);

    }

    public void run(){
        while (opMode.opModeIsActive()) {
          //  opMode.telemetry.addData("", "running");
            lB.setPower(opMode.gamepad1.left_stick_y);
            rB.setPower(opMode.gamepad1.right_stick_y);
        }
    }

    public void initialize(){

    }
}
