package com.example.sophie.filtersapplication;

public class OpenGLLibJNIWrapper {
    static {
        System.loadLibrary("jni");
    }

    public static native void on_surface_created();             //NDK

    public static native void on_surface_changed(int width, int height);

    public static native void on_draw_frame();
}