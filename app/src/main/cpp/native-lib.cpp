#include <jni.h>
#include <string>
#include <vector>
//FIRST TASK
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_sophie_filtersapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject, jstring TestString/* this */) {
    const char *str= env->GetStringUTFChars(TestString, 0);
    std::string new_string = str;
    new_string.insert(12, " (here is changed text) ");
    env->ReleaseStringUTFChars(TestString, str);
    return env->NewStringUTF(new_string.c_str());
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_sophie_filtersapplication_MainActivity_intFromJNI(
        JNIEnv* env,
        jobject, jint TestNumber){
    int new_number = (int)(TestNumber*2);
    return new_number;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_sophie_filtersapplication_MainActivity_countSum(
        JNIEnv* env,
        jobject, jintArray TestArray, jint Length) {
    int sum = 0;
    jint *new_array = env->GetIntArrayElements(TestArray, 0);
    for (int i=0; i<Length; i++) {
        sum += new_array[i];
    }
    env->ReleaseIntArrayElements(TestArray, new_array, 0);
    return (jint)sum;
}