package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.tfod.AnnotatedYuvRgbFrame;
import org.firstinspires.ftc.robotcore.internal.tfod.TfodParameters;
import org.firstinspires.ftc.robotcore.internal.tfod.VuforiaFrameGenerator;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;

public class ObjectDetection extends OpMode {
    private TFObjectDetector tf;
    private VuforiaLocalizer vuforia;
    private static final String VUFORIA_KEY = "ARB3zLf/////AAABmdjITZ883ELVh8Ye9ayWtNUJUdukm1if5BxlyaoGAaYDg/OoIj4WKtcomn7M0hLGLEf69SN0HfvaH7dpNL4BX6Dz/dcr27VKcC2DC/ajG1VztTYebupAqTvq2XXaBq9QsKqFFedrYLQODIvUdatNfDyU/g6wm06RKD5dboCpdxI45ijv6aJVljS8vylfQZws3tCORDLxcGb0gNqgn8+12cy2yoDqPTp1NG6EBWKPRegz/lOe6I36DyPu95MhVTqgTex3OxB0zB3GPIeHnf8m2A+jPTfLAu9+Lg9ZpvZ28entTEFS3yV98eiBzyD6umjhLzNJc128W9zwdeEyMcQfLGdGKYiIA6rDd/z9/ph1Gjer";

    public void init(){
        initVuforia();
        initTfod();
    }

    public void loop(){


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
}
