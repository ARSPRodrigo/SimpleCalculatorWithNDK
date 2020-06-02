#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_simple_calcndk070_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "0.0";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_simple_calcndk070_MainActivity_addFromJNI(JNIEnv *env, jobject thiz, jdouble num1,
                                                   jdouble num2) {
    return num1 + num2;
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_simple_calcndk070_MainActivity_subtractFromJNI(JNIEnv *env, jobject thiz, jdouble num1,
                                                        jdouble num2) {
    return num1 - num2;
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_simple_calcndk070_MainActivity_multiplyFromJNI(JNIEnv *env, jobject thiz, jdouble num1,
                                                        jdouble num2) {
    return num1 * num2;
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_simple_calcndk070_MainActivity_divideFromJNI(JNIEnv *env, jobject thiz, jdouble num1,
                                                      jdouble num2) {
    return num1 / num2;
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_simple_calcndk070_MainActivity_powerFromJNI(JNIEnv *env, jobject thiz, jdouble num1,
                                                     jdouble num2) {
    return pow(num1, num2);
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_simple_calcndk070_MainActivity_sqrtFromJNI(JNIEnv *env, jobject thiz, jdouble num) {
    return sqrt(num);
}
