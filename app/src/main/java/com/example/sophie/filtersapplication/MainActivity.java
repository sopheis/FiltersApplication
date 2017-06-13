package com.example.sophie.filtersapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    GLSurfaceView mView;
    private MenuItem			mItemCapture0;
    private MenuItem			mItemCapture1;
    private MenuItem            mItemCapture2;
    private MenuItem			mItemCapture3;
    private MenuItem			mItemCapture4;
    private MenuItem			mItemCapture5;
    private MenuItem			mItemCapture6;
    private MenuItem            mItemCapture7;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = new GLSurfaceView(this);
        mView.setEGLContextClientVersion(2);
        mView.setRenderer(new OpenGLRenderer(this));
        setContentView(mView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
    }
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mItemCapture0 = menu.add("Original");
        mItemCapture1 = menu.add("Black and white");
        mItemCapture2= menu.add("Inversion");
        mItemCapture3 = menu.add("Colorize");
        mItemCapture4 = menu.add("Multiply");
        mItemCapture5 = menu.add("Screen");
        mItemCapture6 = menu.add("Overlay");
        mItemCapture7 = menu.add("Blur");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mItemCapture0){
            OpenGLRenderer.shader_selection = 0;
            return true;
        }
        if (item == mItemCapture1){
            OpenGLRenderer.shader_selection = OpenGLRenderer.BLACKANDWHITE;
            return true;
        }
        if	(item == mItemCapture2){
            OpenGLRenderer.shader_selection = OpenGLRenderer.INVERSION;
            return true;
        }
        if	(item == mItemCapture3){
            OpenGLRenderer.shader_selection = OpenGLRenderer.COLORIZE;
            return true;
        }
        if  (item == mItemCapture4){
            OpenGLRenderer.shader_selection = OpenGLRenderer.MULTIPLY;
            return true;
        }
        if (item == mItemCapture5){
            OpenGLRenderer.shader_selection = OpenGLRenderer.SCREEN;
            return true;
        }
        if	(item == mItemCapture6){
            OpenGLRenderer.shader_selection = OpenGLRenderer.OVERLAY;
            return true;
        }
        if (item == mItemCapture7) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.BLUR;
            return true;
        }
        return false;
    }

    public native String stringFromJNI();
    public native int intFromJNI(int TestNumber);
    public native int countSum(int[] TestIntArray, int Length);
}
