package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotutil.IMU;

public class CollectTask extends TaskThread {
    double counter = 0;
    LinearOpMode opMode;
    Servo quarter;
    CRServo cr;
    double pos = .4;
    double starttime;

    public CollectTask( LinearOpMode opMode) {
        this.opMode = opMode;
        quarter = opMode.hardwareMap.servo.get("quarter");
        cr = opMode.hardwareMap.crservo.get("cr");
        cr.setDirection(DcMotorSimple.Direction.FORWARD);
        quarter.setPosition(pos);


    }

    public void run(){
        starttime = System.currentTimeMillis();
        while (opMode.opModeIsActive()) {
            cr.setPower(1);

            counter = System.currentTimeMillis() - starttime;
            opMode.telemetry.addData("", "running");

           if(opMode.gamepad1.dpad_right && counter > 40 ) {
               pos = pos + .005;
               starttime = System.currentTimeMillis();
                }
           else if(opMode.gamepad1.dpad_left && counter > 40 ){
                pos = pos - .005;
                starttime = System.currentTimeMillis();
               }
           limitServo();
           quarter.setPosition(pos);
           opMode.telemetry.addData("Pos = ", pos);
        }
    }
    void limitServo(){
        if(pos>1){
            pos = 1;
        }
        else if(pos < 0){
            pos = 0;
        }
    }

    public void initialize(){

    }
}
