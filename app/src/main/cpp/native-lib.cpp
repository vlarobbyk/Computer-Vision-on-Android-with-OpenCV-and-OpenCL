#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/core/ocl.hpp>
#include <opencv2/imgproc.hpp>
#include <android/log.h>


extern "C" JNIEXPORT jstring JNICALL
Java_ups_giiata_aplicacionnativaopencvopencl_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL Java_ups_giiata_aplicacionnativaopencvopencl_MainActivity_processFrameOpenCL(JNIEnv *env, jobject /* this */, jlong addrRgba, jlong addrOut) {
    cv::Mat& mColor = *(cv::Mat*)addrRgba;
    cv::Mat& mOut = *(cv::Mat*)addrOut;

    cv::ocl::setUseOpenCL(true);
    __android_log_print(ANDROID_LOG_INFO, "OpenCV_Tutorial", "OpenCL disponible y activado: %d", cv::ocl::useOpenCL());

    cv::UMat uRgba = mColor.getUMat(cv::ACCESS_READ);
    cv::UMat uGray, uBlur, uEdges, uEdgesColor, uBorders;

    cv::cvtColor(uRgba, uGray, cv::COLOR_RGBA2GRAY);
    cv::GaussianBlur(uGray, uBlur, cv::Size(5, 5), 0);
    cv::Canny(uBlur, uEdges, 50, 150);

    cv::cvtColor(uEdges, uEdgesColor, cv::COLOR_GRAY2RGBA);

    cv::addWeighted(uRgba, 0.5, uEdgesColor, 0.5, 0.0, uBorders);

    //uEdges.copyTo(mOut);
    uBorders.copyTo(mOut);
}
