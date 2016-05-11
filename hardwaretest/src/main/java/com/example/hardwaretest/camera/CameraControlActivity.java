package com.example.hardwaretest.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hardwaretest.R;

public class CameraControlActivity extends AppCompatActivity
        implements SurfaceHolder.Callback,
        View.OnClickListener,
        Camera.PictureCallback
{
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Camera mCamera = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSurfaceView = (SurfaceView)
                findViewById(R.id.cameraview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        Button btn = (Button)this.findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View aView)
    {
        switch(aView.getId())
        {
            case R.id.button:
                if (mCamera != null)
                {
                    mCamera.takePicture(null, null, this);
                }
                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format,
                               int width,
                               int height)
    {
        mSurfaceHolder.setFixedSize(width, 1000);
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mCamera = Camera.open();
        try
        {
            mCamera.setPreviewDisplay(holder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onPictureTaken(byte[] aData, Camera aCamera)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(aData,
                0,
                aData.length);

        ImageView imageview = (ImageView) findViewById(R.id.imgdisplay);
        imageview.setImageBitmap(bitmap);

        aCamera.startPreview();
    }
}


