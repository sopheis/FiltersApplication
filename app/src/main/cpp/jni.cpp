
#include "openGL.cpp"
#include <jni.h>

extern "C"
JNIEXPORT void JNICALL Java_com_example_sophie_practice_1project_12017_OpenGLLibJNIWrapper_on_1surface_1created
        (JNIEnv * env, jclass cls) {
    on_surface_created();
}

extern "C"
JNIEXPORT void JNICALL Java_com_example_sophie_practice_1project_12017_OpenGLLibJNIWrapper_on_1surface_1changed
        (JNIEnv * env, jclass cls, jint width, jint height) {
    on_surface_changed();
}

extern "C"
JNIEXPORT void JNICALL Java_com_example_sophie_practice_1project_12017_OpenGLLibJNIWrapper_on_1draw_1frame
        (JNIEnv * env, jclass cls) {
    on_draw_frame();
}