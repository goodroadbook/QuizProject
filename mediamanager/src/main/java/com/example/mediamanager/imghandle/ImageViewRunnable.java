package com.example.mediamanager.imghandle;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.mediamanager.R;
import com.example.mediamanager.common.MediaData;

import java.util.HashMap;

/**
 * Created by namjinha on 2016-01-22.
 */
public class ImageViewRunnable implements Runnable
{
    private final int D_RES_ID = R.drawable.ic_launcher;
    private Bitmap mBitmap = null;
    private MediaData mMediaData = null;
    private HashMap<ImageView, String> mImageViewMap = null;

    public ImageViewRunnable(Bitmap aBitmap,
                             MediaData aMediaData,
                             HashMap<ImageView, String> aImageViewMap)
    {
        mBitmap = aBitmap;
        mMediaData = aMediaData;
        mImageViewMap = aImageViewMap;
    }

    public void run()
    {
        if(false == isImageViewValid())
        {
            return;
        }

        if(mBitmap != null)
        {
            mMediaData.imgview.setImageBitmap(mBitmap);
        }
        else
        {
            mMediaData.imgview.setImageResource(D_RES_ID);
        }
    }

    private boolean isImageViewValid()
    {
        String mediaPath = mImageViewMap.get(mMediaData.imgview);
        if(mediaPath == null ||
                false == mediaPath.equals(mMediaData.mediapath))
        {
            return false;
        }

        return true;
    }
}

