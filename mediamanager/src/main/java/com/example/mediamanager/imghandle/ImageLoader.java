package com.example.mediamanager.imghandle;

import android.content.Context;
import android.widget.ImageView;

import com.example.mediamanager.R;
import com.example.mediamanager.common.MediaData;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by namjinha on 2016-01-22.
 */

public class ImageLoader
{
    private final int D_RES_ID = R.drawable.ic_launcher;
    private ExecutorService mExecutorService = null;
    private HashMap<ImageView, String> mImageViewMap = null;

    public ImageLoader(Context aContext)
    {
        mImageViewMap = new HashMap<ImageView, String>();
        mExecutorService = Executors.newFixedThreadPool(10);
    }

    public void displayImage(MediaData mediaData)
    {
        if(null == mImageViewMap)
        {
            return;
        }

        if(null == mediaData)
        {
            return;
        }

        mImageViewMap.put(mediaData.imgview, mediaData.mediapath);
        mExecutorService.submit(new ImageRunnable(mediaData, mImageViewMap));
        mediaData.imgview.setImageResource(D_RES_ID);
    }
}

