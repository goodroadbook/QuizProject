package com.example.hardwaretest.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hardwaretest.R;

public class CameraAppActivity extends AppCompatActivity
        implements View.OnClickListener
{
    private final int RESPONSE_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraapp);

        Button btn = (Button) this.findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        switch (requestCode)
        {
            case RESPONSE_CAMERA:
                if ( resultCode == RESULT_CANCELED)
                {
                    return;
                }

                if (data != null)
                {
                    Uri imguri = data.getData();
                    try
                    {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imguri);
                        showCameraImage(bitmap);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View aView)
    {
        switch (aView.getId())
        {
            case R.id.button:
                startCamera();
                break;
            default:
                break;
        }
    }

    private void startCamera()
    {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESPONSE_CAMERA);
    }

    private void showCameraImage(Bitmap aBitmap)
    {
        ImageView img = (ImageView)findViewById(
                R.id.imgdisplay);
        img.setImageBitmap(aBitmap);
    }
}

