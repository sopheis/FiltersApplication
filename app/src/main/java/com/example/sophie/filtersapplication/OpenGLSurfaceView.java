package com.example.sophie.filtersapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;

class OpenGLSurfaceView extends GLSurfaceView {

    private final OpenGLRenderer mRenderer;

    public OpenGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new OpenGLRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
