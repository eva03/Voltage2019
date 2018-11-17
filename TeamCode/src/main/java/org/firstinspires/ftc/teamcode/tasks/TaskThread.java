package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class TaskThread extends Thread {
    public volatile boolean running = true;
    public LinearOpMode opMode;
    public volatile boolean teleOp = false;
    public static volatile double voltage = 13;
    public static final double EXPECTED_VOLTAGE = 13;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public void sleep (int ms) {
        timer.reset();
        while (opMode.opModeIsActive() && timer.time() < ms);
    }

    public static void calculateVoltage(LinearOpMode opMode) {
        double mc7 = opMode.hardwareMap.voltageSensor.get("lB").getVoltage();
        double mc6 = opMode.hardwareMap.voltageSensor.get("rB").getVoltage();
       // double mc3 = opMode.hardwareMap.voltageSensor.get("cap").getVoltage();
        //double mc2 = opMode.hardwareMap.voltageSensor.get("flywheels").getVoltage();
        voltage = (mc7 + mc6 ) / 2;
    }



    public abstract void initialize();
}
