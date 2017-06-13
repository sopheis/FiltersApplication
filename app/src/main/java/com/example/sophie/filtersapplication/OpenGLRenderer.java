package com.example.sophie.filtersapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    /*public void onSurfaceCreated(GL10 unused, EGLConfig config) {    //OLD, NDK
        // Set the background frame color
        OpenGLLibJNIWrapper.on_surface_created();
        //GLES20.glClearColor(0f, 1f, 0f, 1f);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        OpenGLLibJNIWrapper.on_draw_frame();
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        OpenGLLibJNIWrapper.on_surface_changed(width, height);
        //GLES20.glViewport(0, 0, width, height);
    }*/
    private final Context mActivityContext;
    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];  //camera
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];   //combined matrix for shader program

    private final FloatBuffer mCubePositions;
    private final FloatBuffer mCubeColors;
    private final FloatBuffer mCubeTextureCoordinates;

    private int mMVPMatrixHandle;
    private int mTextureUniformHandle0;
    private int mTextureUniformHandle1;
    private int mPositionHandle;
    // private int mColorHandle;
    private int mTextureCoordinateHandle;

    private final int mBytesPerFloat = 4;
    private final int mPositionDataSize = 3;
    // private final int mColorDataSize = 4;
    private final int mTextureCoordinateDataSize = 2;

    private int mProgramHandle;
    private int mTextureDataHandle0;
    private int mTextureDataHandle1;

    static public int shader_selection = 0;
    static public final int BLACKANDWHITE= 1;
    static public final int INVERSION = 2;
    static public final int COLORIZE = 3;
    static public final int MULTIPLY= 4;
    static public final int SCREEN = 5;
    static public final int OVERLAY = 6;
    static public final int BLUR = 7;

    public OpenGLRenderer(final Context activityContext) {
        mActivityContext = activityContext;
        final float[] cubePositionData = {
                -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f };
        final float[] cubeColorData = {
                1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f };
        final float[] cubeTextureCoordinateData = {
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f };

        mCubePositions = ByteBuffer
                .allocateDirect(cubePositionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(cubePositionData).position(0);

        mCubeColors = ByteBuffer
                .allocateDirect(cubeColorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeColors.put(cubeColorData).position(0);

        mCubeTextureCoordinates = ByteBuffer
                .allocateDirect(
                        cubeTextureCoordinateData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);
    }

    protected String getVertexShader() {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext,
                R.raw._vertex_shader);
    }
    protected String getFragmentShader() {
        int id;
        switch (shader_selection){
            case BLACKANDWHITE: id = R.raw.blacknwhite_fragment_shader; break;
            case INVERSION: id = R.raw.inversion_fragment_shader;break;
            case COLORIZE: id = R.raw.colorize_fragment_shader;break;
            case MULTIPLY: id = R.raw.multiply_fragment_shader;break;
            case SCREEN: id = R.raw.screen_fragment_shader;break;
            case OVERLAY: id = R.raw.overlay_fragment_shader;break;
            case BLUR: id = R.raw.blur_fragment_shader; break;
            default: id = R.raw._fragment_shader;break;
        }
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, id);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  //background to black
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final float eyeX = 0.0f;    //position of the eye
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        final float lookX = 0.0f;   //distance
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY,  //camera position
                lookZ, upX, upY, upZ);

        mTextureDataHandle0 = Textures.loadTexture(mActivityContext,  //load the texture
                R.drawable.sample_picture);

    }
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);    //viewport the same size as surface

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();

        final int vertexShaderHandle = Shaders.compileShader(
                GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = Shaders.compileShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mProgramHandle = Shaders.createAndLinkProgram(vertexShaderHandle,
                fragmentShaderHandle, new String[] { "a_Position",
                        "a_TexCoordinate" });

        GLES20.glUseProgram(mProgramHandle);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle,
                "u_MVPMatrix");
        mTextureUniformHandle0 = GLES20.glGetUniformLocation(mProgramHandle,
                "u_Texture0");
        mTextureUniformHandle1 = GLES20.glGetUniformLocation(mProgramHandle,
                "u_Texture1");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle,
                "a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle,
                "a_TexCoordinate");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle0);

        GLES20.glUniform1i(mTextureUniformHandle0, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle1);
        GLES20.glUniform1i(mTextureUniformHandle1, 1);


        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3.2f);
        Matrix.rotateM(mModelMatrix, 0, 0.0f, 1.0f, 1.0f, 0.0f);
        drawCube();
    }

    private void drawCube() {
        mCubePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
                GLES20.GL_FLOAT, false, 0, mCubePositions);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        mCubeTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,
                mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0,
                mCubeTextureCoordinates);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);  //combined matrix
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }
}
