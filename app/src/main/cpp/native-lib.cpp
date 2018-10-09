#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_shubhobrata_objloaderwithdetection_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_shubhobrata_objloaderwithdetection_MainActivity_matchOpenCV(JNIEnv *env,
                                                                             jobject instance,
                                                                             jlong rgbaMat) {




    return env->NewStringUTF("Hi");
}