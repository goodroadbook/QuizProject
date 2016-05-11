package com.example.mediamanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mediamanager.common.MediaData;
import com.example.mediamanager.imghandle.ImageLoader;

import java.util.ArrayList;

/**
 * Created by namjinha on 2016-01-22.
 */

public class ImageAdapter extends BaseAdapter
{
    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private ImageLoader mImageLoader	= null;

    private ArrayList<MediaData> mMediaList	= null;

    public ImageAdapter(Context aContext)
    {
        mContext = aContext;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public void initData(ArrayList<MediaData> aMediaList)
    {
        mMediaList = aMediaList;
    }

    public int getCount()
    {
        if(null == mMediaList)
        {
            return 0;
        }

        return mMediaList.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent)
    {
        MediaData mediaData = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.thumbnail, null);
            mediaData = new MediaData();
            mediaData.imgview = (ImageView) convertView.findViewById(R.id.thumbnail);

            convertView.setTag(mediaData);
        }
        else
        {
            mediaData = (MediaData) convertView.getTag();
        }

        if(null == mMediaList || mMediaList.size() == 0)
        {
            return convertView;
        }

        mediaData.type = mMediaList.get(position).type;
        mediaData.mediaid = mMediaList.get(position).mediaid;
        mediaData.mediapath = mMediaList.get(position).mediapath;
        mediaData.orientation = mMediaList.get(position).orientation;

        mImageLoader.displayImage(mediaData);
        return convertView;
    }
}

