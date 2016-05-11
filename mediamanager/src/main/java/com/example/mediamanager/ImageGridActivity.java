package com.example.mediamanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mediamanager.common.GetMediaData;
import com.example.mediamanager.common.MediaData;

import java.util.ArrayList;

/**
 * Created by namjinha on 2016-01-22.
 */
public class ImageGridActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private int mType = MainActivity.TYPE_IMAGE;
    private ArrayList<MediaData> mMediaList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imggrid);

        Intent i = this.getIntent();
        mType = i.getIntExtra(MainActivity.TYPE, MainActivity.TYPE_IMAGE);

        mMediaList = new ArrayList<MediaData>();

        switch(mType)
        {
            case MainActivity.TYPE_IMAGE:
                GetMediaData.getImageArrayData(this, mMediaList);
                break;
            case MainActivity.TYPE_VIDEO:
                GetMediaData.getVideoArrayData(this, mMediaList);
                break;
            case MainActivity.TYPE_ALL:
                GetMediaData.getImageArrayData(this, mMediaList);
                GetMediaData.getVideoArrayData(this, mMediaList);
                break;
            default:
                break;
        }

        GridView gv = (GridView)findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        imageAdapter.initData(mMediaList);
        gv.setAdapter(imageAdapter);
        gv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View aView,
                            int aPos, long aId)
    {
        switch(mMediaList.get(aPos).type)
        {
            case MainActivity.TYPE_IMAGE:
                showOriImage(mMediaList.get(aPos).mediapath,
                        mMediaList.get(aPos).orientation);
                break;
            case MainActivity.TYPE_VIDEO:
                showVideoPlayer(mMediaList.get(aPos).mediapath);
                break;
            default:
                break;
        }
    }

    private void showOriImage(String aImagePath,
                              int aOrientation)
    {
        Intent i = new Intent(this, ImageViewerActivity.class);
        i.putExtra("IMAGE_PATH", aImagePath);
        i.putExtra("ORIENTATION", aOrientation);
        startActivity(i);
    }

    private void showVideoPlayer(String aVideoPath)
    {
        Intent i = new Intent(this, VideoPlayerActivity.class);
        i.putExtra("VIDEO_PATH", aVideoPath);
        startActivity(i);
    }
}



