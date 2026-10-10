package ups.giiata.aplicacionnativaopencvopencl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.CvType;

import java.util.Collections;
import java.util.List;

public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mCameraView;
    private Mat mOutputMat;

    // Cargar OpenCV y la librería generada por tu proyecto
    static {
        if (OpenCVLoader.initLocal()) {
            System.loadLibrary("aplicacionnativaopencvopencl");
        }
    }

    // Declaración de la función en C++
    public native void processFrameOpenCL(long matAddrGray, long matAddrOut);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carga el layout configurado en el paso 2
        setContentView(R.layout.activity_main);

        // Solicitar permisos de cámara en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        mCameraView = findViewById(R.id.camera_view);
        mCameraView.setCvCameraViewListener(this);
        mCameraView.enableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mOutputMat = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        if (mOutputMat != null) mOutputMat.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();

        // Invocación a C++ pasando las direcciones de memoria
        processFrameOpenCL(rgba.getNativeObjAddr(), mOutputMat.getNativeObjAddr());

        return mOutputMat;
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mCameraView);
    }
}
