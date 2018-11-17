package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Functions;
import org.firstinspires.ftc.teamcode.robotutil.IMU;
import org.firstinspires.ftc.teamcode.tasks.TensorFlowObject;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;
@Autonomous
public class Auto extends LinearOpMode {
    private TFObjectDetector tf;
    private VuforiaLocalizer vuforia;
    private static final String VUFORIA_KEY = "AQgn1d//////AAAAGS+F+GWwAEbtqn64lm+fvolRqft5tIJLGdUCsB51qVZHMP3UU8cTCBMKvjCBUTxHfkooO1dljaRLNzaDMMTbWw978Agd7qMrUQF/I4dsE+oVUhLVTHxHPl4r8T4LJ1+B5KHvXQyTr7S3bTU1xy/id/uACCppztVO6mH6Aj0FwY/v3lDYnL9sQNVi2DNXNrnQmmshyJC74C4Se8a6A/II7vcaQ00Ot3PlSB9LjH6K28EQ3oiLnc6tKTGjbU+uTBdoix2KUDL7xVa8c6biG2lcuu7j6dRrw/uvUrh7RpWcmvQDdoshtLlXLsvacLwr5NzMX+4quVkydj/3KRrixOKnepk0ZSPiSlt+J+ThynHcgevu";
    public DcMotor rB, lB;
    public IMU imu;
    DriveTrain driveTrain;
    TensorFlowObject tensorFlowObject;
    @Override
    public void runOpMode(){
        initialize();

        waitForStart();
        tensorFlowObject.start();

       /* while(opModeIsActive()){
            telemetry.addData("goldleft", tensorFlowObject.getGoldLeft());
            telemetry.addData("goldconf", tensorFlowObject.getGoldConfidence());
            telemetry.update();

        }*/

       Functions.waitFor(2000);

       rotateToGold(170,.1, 5000);
        //driveTrain.encoderDrive(.6, 24, DriveTrain.Direction.FORWARD, 4);
        //driveTrain.rotate(90, .4, 3000);

       // Functions.waitFor(10000);
       // driveTrain.rotateIMURamp(-90, .8, 4,telemetry );

        //sleep(100000);
        //Lower from hanging
      //  driveTrain.slidesUpEncoder(1, 24, 5);
        //driveTrain.moveFwd(.4, 5, 3);


        //Turn around
      //  driveTrain.rotateIMURamp(90, .8, 4,telemetry );
       // driveTrain.moveFwd(.4, 10, 4);



        //scan for gold block

  //      turnCwToGold(.3, 10);



        //knock of gold block
       // driveTrain.moveFwd(.5, 24, 5);
       // driveTrain.moveBkwd(.5, 24, 5);



        // go towards wall
        //driveTrain.rotateIMURamp(70, .7, 4, telemetry);
        //driveTrain.moveFwd(.4, 40, 4);

        //turn 90 degrees
        //driveTrain.rotateIMURamp(90, .4, 4, telemetry);



        //run backwards and dump the team marker







    }


    public void initialize(){


        driveTrain  = new DriveTrain(this);
        tensorFlowObject = new TensorFlowObject(this);
        lB = hardwareMap.dcMotor.get("lB");
        rB = hardwareMap.dcMotor.get("rB");
        BNO055IMU adaImu = hardwareMap.get(BNO055IMU.class, "imu");

        imu = new IMU(adaImu);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tf = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tf.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    public void turnCwToGold(double power, double timeout){
        long startTime = System.currentTimeMillis();
        while (tensorFlowObject.getGoldConfidence() < .94 ){
            rB.setPower(power);
            lB.setPower(-power);
        }

    }
    public void turnCCwToGold(double power, double timeout){
        long startTime = System.currentTimeMillis();
        while (tensorFlowObject.getGoldConfidence() < .94 ){
            rB.setPower(-power);
            lB.setPower(power);
        }

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
        telemetry.addData("start  ", start);
        telemetry.addData("target",target );
        telemetry.update();
        double currentheading = imu.getAngle();
        double anglediff = target - currentheading;
        while(Math.abs(anglediff)>3 && tensorFlowObject.getGoldConfidence() < .54 && opModeIsActive()){
            if(anglediff < 0){
                lB.setPower(power);
                rB.setPower(-power);

                telemetry.addData("anglediff", anglediff);
                telemetry.update();
            }
            else if(anglediff > 0){
                lB.setPower(-power);
                rB.setPower(power);

                telemetry.addData("anglediff", anglediff);
                telemetry.update();
            }
            currentheading = imu.getAngle();
            anglediff = target - currentheading;
        }


        lB.setPower(0);
        rB.setPower(0);

    }


}
