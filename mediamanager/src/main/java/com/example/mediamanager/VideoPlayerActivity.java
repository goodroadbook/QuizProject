package com.example.mediamanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.mediamanager.videohandle.VideoController;

/**
 * Created by namjinha on 2016-01-23.
 */
public class VideoPlayerActivity extends AppCompatActivity implements
        View.OnClickListener,
        SurfaceHolder.Callback
{
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceholder	= null;
    private VideoController mVideoController = null;
    private String mVideoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        Intent i = getIntent();
        mVideoPath = i.getStringExtra("VIDEO_PATH");

        mSurfaceView = (SurfaceView) findViewById(
                R.id.surfaceview);
        mSurfaceView.setOnClickListener(this);
        mSurfaceholder = mSurfaceView.getHolder();
        mSurfaceholder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height)
    {
        ;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mVideoController = new VideoController();
        mVideoController.openVideo(mVideoPath, mSurfaceholder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        ;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.surfaceview:
                mVideoController.handleVideo();
                break;
            default:
                break;
        }
    }
}

