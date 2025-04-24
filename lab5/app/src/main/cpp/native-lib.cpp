#include <jni.h>
#include <string>
#include <algorithm> // Do std::sort
#include <functional> // Do std::greater


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_lab5_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_lab5_MainActivity_sortArray(
        JNIEnv* env,
        jobject /* this */,
        jintArray array) {


    jsize length = env->GetArrayLength(array);
    jint* arrayElements = env->GetIntArrayElements(array, nullptr);

    std::sort(arrayElements, arrayElements + length);

    env->ReleaseIntArrayElements(array, arrayElements, 0);
}