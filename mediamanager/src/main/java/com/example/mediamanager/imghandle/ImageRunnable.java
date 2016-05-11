package com.example.mediamanager.imghandle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.mediamanager.MainActivity;
import com.example.mediamanager.common.MediaData;

import java.util.HashMap;

/**
 * Created by namjinha on 2016-01-22.
 */

public class ImageRunnable implements Runnable
{
    private MediaData mMediaData = null;
    private HashMap<ImageView, String> mImageHashMap = null;

    public ImageRunnable(MediaData aMediaData,
                         HashMap<ImageView, String> aImageViewMap
    )
    {
        mMediaData = aMediaData;
        mImageHashMap = aImageViewMap;
    }

    @Override
    public void run()
    {
        if(false == isImageViewValid(mMediaData))
        {
            return;
        }

        Bitmap bmp = null;

        if(mMediaData.type == MainActivity.TYPE_IMAGE)
        {
            bmp = ImageUtils.getImageThumbnail(mMediaData);
        }
        else
        {
            bmp = ImageUtils.getVideoThumbnail(mMediaData);
        }

        if(false == isImageViewValid(mMediaData))
        {
            return;
        }

        ImageViewRunnable bd = new ImageViewRunnable(
                bmp,
                mMediaData,
                mImageHashMap);
        Activity a = (Activity) mMediaData.imgview.getContext();
        a.runOnUiThread(bd);
    }

    private boolean isImageViewValid(MediaData aMediaData)
    {
        String mediaPath = mImageHashMap.get(
                aMediaData.imgview);
        if(mediaPath == null || false == mediaPath.equals(aMediaData.mediapath))
        {
            return false;
        }

        return true;
    }
}


