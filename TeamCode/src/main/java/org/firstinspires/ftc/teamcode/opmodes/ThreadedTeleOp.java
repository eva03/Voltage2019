package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.tasks.CollectTask;
import org.firstinspires.ftc.teamcode.tasks.DriveTrainTask;
import org.firstinspires.ftc.teamcode.tasks.SlideTask;

@TeleOp(name = "Threaded Teleop")
public class ThreadedTeleOp extends LinearOpMode {
    DriveTrainTask driveTrain;
    SlideTask slideTask;
    CollectTask collectTask;
    long startTime = System.currentTimeMillis();



    @Override
    public void runOpMode() {
        initialize();
        telemetry.addData("Ready", "For start");
        waitForStart();
        slideTask.start();
        driveTrain.start();
        collectTask.start();


        while (opModeIsActive()) {
            telemetry.update();
        }

        stop();

    }
    public void initialize(){

        driveTrain = new DriveTrainTask(this);
        slideTask = new SlideTask(this);
        collectTask = new CollectTask(this);
    }
}
