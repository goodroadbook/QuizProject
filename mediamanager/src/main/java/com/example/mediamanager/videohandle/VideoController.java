package com.example.mediamanager.videohandle;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

/**
 * Created by namjinha on 2016-01-25.
 */

public class VideoController implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener
{
    private MediaPlayer mMediaPlayer = null;
    public VideoController()
    {
        ;
    }

    public void openVideo(String aVideoFilePath,
                          SurfaceHolder aSurfaceHolder)
    {
        try
        {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(aVideoFilePath);
            mMediaPlayer.setDisplay(aSurfaceHolder);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
        }
        catch (Exception e)
        {
            ;
        }
    }

    public void handleVideo()
    {
        if(true == mMediaPlayer.isPlaying())
        {
            pauseVideo();
        }
        else
        {
            startVideo();
        }
    }

    private void startVideo()
    {
        if(null == mMediaPlayer)
        {
            return;
        }

        mMediaPlayer.start();
    }

    private void pauseVideo()
    {
        if(null == mMediaPlayer)
        {
            return;
        }

        mMediaPlayer.pause();
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        if(null != mp)
        {
            mp.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        if(null != mp)
        {
            mp.stop();
            mp.release();
        }
    }
}

