package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.robotutil.IMU;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;

public class TensorFlowObject extends TaskThread {
    private TFObjectDetector tf;
    private VuforiaLocalizer vuforia;
    private static final String VUFORIA_KEY = "ARB3zLf/////AAABmdjITZ883ELVh8Ye9ayWtNUJUdukm1if5BxlyaoGAaYDg/OoIj4WKtcomn7M0hLGLEf69SN0HfvaH7dpNL4BX6Dz/dcr27VKcC2DC/ajG1VztTYebupAqTvq2XXaBq9QsKqFFedrYLQODIvUdatNfDyU/g6wm06RKD5dboCpdxI45ijv6aJVljS8vylfQZws3tCORDLxcGb0gNqgn8+12cy2yoDqPTp1NG6EBWKPRegz/lOe6I36DyPu95MhVTqgTex3OxB0zB3GPIeHnf8m2A+jPTfLAu9+Lg9ZpvZ28entTEFS3yV98eiBzyD6umjhLzNJc128W9zwdeEyMcQfLGdGKYiIA6rDd/z9/ph1Gjer";
    private float goldConfidence =0;
    private float goldLeft;

    LinearOpMode opMode;
    List<Recognition> recognitionList;


    public TensorFlowObject( LinearOpMode opMode) {
      this.opMode = opMode;


    }

    public void run(){;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            opMode.telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */
        opMode.telemetry.addData(">", "Press Play to start tracking");
        opMode.telemetry.update();
      //  opMode.waitForStart();

        if (opMode.opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tf != null) {
                tf.activate();
            }

            while (opMode.opModeIsActive()) {
                if (tf != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tf.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        opMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() >0) {
                            recognitionList = updatedRecognitions;
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                    goldConfidence = recognition.getConfidence();
                                    goldLeft = recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    opMode.telemetry.addData("Gold Mineral Position", "Left");
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    opMode.telemetry.addData("Gold Mineral Position", "Right");
                                } else {
                                    opMode.telemetry.addData("Gold Mineral Position", "Center");
                                }


                            }
                        }

                        opMode.telemetry.update();
                    }
                }
            }
        }

        if (tf != null) {
            tf.shutdown();
        }
    }

    public void initialize(){

    }

    public float getGoldLeft(){
        return goldLeft;
    }
    public float getGoldConfidence(){
        return goldConfidence;
    }
    public List<Recognition> getRecognitionList(){return recognitionList;}

    private void initTfod() {
        int tfodMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
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
}
