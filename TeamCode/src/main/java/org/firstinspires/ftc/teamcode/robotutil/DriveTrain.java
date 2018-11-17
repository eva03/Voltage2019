package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

//import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;

public class DriveTrain {
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    static final double TICKS_PER_INCH_FORWARD = 90;
    static final double TICKS_PER_INCH_STRAFE = 61.3;
    public DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow, sweeperHigh, rightConv, leftConv, rSlide, lSlide;
    public ColorSensor colorSensor;
    public double minMotorPower = 0.085; //minimum power that robot still moves
    public IMU imu;


    // Tunable parameters

    private int conversionFactor = 50;
    public double balanceThreshold = 1.5;
    public double balanceMultiplier = 0.08;
    private int gyroTurnErrorMargin = 3; // turn stop if within the margin of error
    private int gyroTurnRampMax = 60;  // starting point of scaling back speed of motor for turning
    private int gyroTurnRampMin = 3;   // stopping point to turn off motor abs(heading-target)<vlaue
    private double minRotationPower = 0.03; // minimum power to move robot
    private final int driveStraightErrorMargin = 2;
    private final int encoderDriveRampMax = 40;
    private final int encoderDriveRampMin = 1;
    private int ambientBlue = 0;
    private int ambientRed = 0;

    double average;
    LinearOpMode opMode;

    public DriveTrain(LinearOpMode opMode) {
        this.opMode = opMode;
        lB = opMode.hardwareMap.dcMotor.get("lB");
        rB = opMode.hardwareMap.dcMotor.get("rB");
        rSlide = opMode.hardwareMap.dcMotor.get("rSlide");
        lSlide = opMode.hardwareMap.dcMotor.get("lSlide");
        //colorSensor = opMode.hardwareMap.colorSensor.get("colorSensor");
        BNO055IMU adaImu = opMode.hardwareMap.get(BNO055IMU.class, "imu");

        imu = new IMU(adaImu);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);

        rSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        lSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        rSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }



    public enum Direction {
        FORWARD, RIGHT, LEFT, BACKWARD;
    }





    public void encoderDrive(double speed, double inches, Direction direction,double timeout){

        double start = System.currentTimeMillis();

        int rTarget = (int) (rB.getCurrentPosition() +  (inches * TICKS_PER_INCH_FORWARD));
        int lTarget = (int) (lB.getCurrentPosition() + (inches*TICKS_PER_INCH_FORWARD));
        opMode.telemetry.addData("rTarget", rTarget);
        opMode.telemetry.addData("lTarget", lTarget);
        opMode.telemetry.update();
        rB.setTargetPosition(rTarget);
        lB.setTargetPosition(lTarget);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setPower(speed);
        lB.setPower(speed);
        double diffstart = ((rTarget - rB.getCurrentPosition())+(lTarget - lB.getCurrentPosition()))/2;
        while(rB.isBusy()&& lB.isBusy()){
            //diff = System.currentTimeMillis() - start;
            opMode.telemetry.addData("isbusy",rB.isBusy());
            double differenceR = rTarget - rB.getCurrentPosition();
            double differenceL = lTarget - lB.getCurrentPosition();
            double difference = (differenceL + differenceR)/2;
            double ratio = difference/diffstart;
           /* double power = speed*(Math.abs(ratio - .5)/.5);
            if(power < .2){
                power = .2;
            }
            rB.setPower(-power);
            lB.setPower(-power);*/

        }
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setPower(0);
        lB.setPower(0);

    }

    public void rotate(int degrees, double power, int timeout) {
        double minPower = .2;
        double diff = 0;
        double start = System.currentTimeMillis();
        double heading = imu.getAngle();
        double target = heading + degrees;
        if(target<0) {
            target = target = 360;
        }
        else if(target > 360){
            target = target - 360;
        }
        opMode.telemetry.addData("start  ", start);
        opMode.telemetry.addData("target",target );
        opMode.telemetry.update();
        double currentheading = imu.getAngle();
        double anglediff = target - currentheading;
        while(Math.abs(anglediff)>3){
            if(anglediff < 0){
                lB.setPower(power);
                rB.setPower(-power);

                opMode.telemetry.addData("anglediff", anglediff);
                opMode.telemetry.update();
            }
            else if(anglediff > 0){
                lB.setPower(-power);
                rB.setPower(power);

                opMode.telemetry.addData("anglediff", anglediff);
                opMode.telemetry.update();
            }
            currentheading = imu.getAngle();
            anglediff = target - currentheading;
        }


        while(Math.abs(anglediff)>2){
            if(anglediff < 0){
                lB.setPower(minPower);
                rB.setPower(-minPower);

                opMode.telemetry.addData("anglediff", anglediff);
                opMode.telemetry.update();
            }
            else if(anglediff > 0){
                lB.setPower(-minPower);
                rB.setPower(minPower);

                opMode.telemetry.addData("anglediff", anglediff);
                opMode.telemetry.update();
            }
            currentheading = imu.getAngle();
            anglediff = target - currentheading;
        }
        lB.setPower(0);
        rB.setPower(0);

    }

    public void rotateToGold(int degrees, double power, int timeout) {
        double minPower = .2;
        double diff = 0;
        double start = System.currentTimeMillis();
        double heading = imu.getAngle();
        double target = heading + degrees;
        if(target<0) {
            target = target = 360;
        }
        else if(target > 360){
            target = target - 360;
        }
        opMode.telemetry.addData("start  ", start);
        opMode.telemetry.addData("target",target );
        opMode.telemetry.update();
        double currentheading = imu.getAngle();
        double anglediff = target - currentheading;
        while(Math.abs(anglediff)>3){
            if(anglediff < 0){
                lB.setPower(power);
                rB.setPower(-power);

                opMode.telemetry.addData("anglediff", anglediff);
                opMode.telemetry.update();
            }
            else if(anglediff > 0){
                lB.setPower(-power);
                rB.setPower(power);

                opMode.telemetry.addData("anglediff", anglediff);
                opMode.telemetry.update();
            }
            currentheading = imu.getAngle();
            anglediff = target - currentheading;
        }


        lB.setPower(0);
        rB.setPower(0);

    }






}
