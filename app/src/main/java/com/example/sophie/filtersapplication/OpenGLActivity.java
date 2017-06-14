package com.example.sophie.filtersapplication;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

import static android.R.attr.bitmap;

public class OpenGLActivity extends AppCompatActivity {
    GLSurfaceView mView;
    OpenGLRenderer renderer;
    Bitmap currentImage;

    private MenuItem mItemCapture0;
    private MenuItem mItemCapture1;
    private MenuItem mItemCapture2;
    private MenuItem mItemCapture3;
    private MenuItem mItemCapture4;
    private MenuItem mItemCapture5;
    private MenuItem mItemCapture6;
    private MenuItem mItemCapture7;
    private MenuItem mItemCapture8;
    private MenuItem mItemCapture9;
    private MenuItem mItemCapture10;
    private MenuItem mItemCapture11;

    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    // supporting methods
    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        mView.queueEvent(new Runnable() {
            @Override
            public void run() {
                EGL10 egl = (EGL10) EGLContext.getEGL();
                GL10 gl = (GL10)egl.eglGetCurrentContext().getGL();
                currentImage = createBitmapFromGLSurface(0, 0, mView.getWidth(), mView.getHeight(), gl);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bitmapReadyCallbacks.onBitmapReady(currentImage);
                    }
                });

            }
        });

    }

    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {

        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("ERROR", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        byte[] byteArray = getIntent().getByteArrayExtra("picture");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = new GLSurfaceView(this);
        renderer = new OpenGLRenderer(this);
        renderer.image = bmp;
        mView.setEGLContextClientVersion(2);
        mView.setRenderer(renderer);
        setContentView(mView);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
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
        mItemCapture2 = menu.add("Inversion");
        mItemCapture3 = menu.add("Colorize");
        mItemCapture4 = menu.add("Multiply");
        mItemCapture5 = menu.add("Screen");
        mItemCapture6 = menu.add("Overlay");
        mItemCapture7 = menu.add("Blur");
        mItemCapture8 = menu.add("Gradient");
        mItemCapture9 = menu.add("Scetch");
        mItemCapture10 = menu.add("Draw");
        mItemCapture11 = menu.add("Save picture");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mItemCapture0) {
            OpenGLRenderer.shader_selection = 0;
            return true;
        }
        if (item == mItemCapture1) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.BLACKANDWHITE;
            return true;
        }
        if (item == mItemCapture2) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.INVERSION;
            return true;
        }
        if (item == mItemCapture3) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.COLORIZE;
            return true;
        }
        if (item == mItemCapture4) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.MULTIPLY;
            return true;
        }
        if (item == mItemCapture5) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.SCREEN;
            return true;
        }
        if (item == mItemCapture6) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.OVERLAY;
            return true;
        }
        if (item == mItemCapture7) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.BLUR;
            return true;
        }
        if (item == mItemCapture8) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.GRADIENT;
            return true;
        }
        if (item == mItemCapture9) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.SCETCH;
            return true;
        }
        if (item == mItemCapture10) {
            OpenGLRenderer.shader_selection = OpenGLRenderer.DRAWING;
            return true;
        }
        if (item == mItemCapture11) {
            captureBitmap(new BitmapReadyCallbacks() {

                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    saveImage(bitmap);
                }
            });
            return true;
        }
        return false;
    }
    private void saveImage(Bitmap finalBitmap) {
        Time time = new Time();
        time.setToNow();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = Integer.toString(time.year) + Integer.toString(time.month) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) +".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Your image was saved in " + root + " with name " + fname, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}