// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("image");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("image")
//      }
//    }

#include <jni.h>
#include <android/log.h>

//extern "C" JNIEXPORT void JNICALL
//Java_com_rockstar_image_MainActivity_helloWorld(JNIEnv *env, jobject /* this */) {
//    __android_log_print(ANDROID_LOG_INFO, "HelloWorld", "Hello World from C++!");
//}

// Modify your C++ code to include a function that returns a string
extern "C" JNIEXPORT jstring JNICALL
Java_com_rockstar_image_MainActivity_helloWorld(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Predictions...");
}
