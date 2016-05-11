package com.example.mediamanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.mediamanager.imghandle.ImageUtils;

/**
 * Created by namjinha on 2016-01-22.
 */

public class ImageViewerActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent i = getIntent();
        String imagePath = i.getStringExtra("IMAGE_PATH");
        int orientation = i.getIntExtra("ORIENTATION", 0);

        showImageView(imagePath, orientation);
    }

    private void showImageView(String aImagePath,
                               int aOrientation)
    {
        Bitmap bmp = ImageUtils.getImageBitmap(aImagePath, aOrientation);

        ImageView iView = (ImageView)findViewById(R.id.showimage);
        iView.setImageBitmap(bmp);
    }
}


