package com.example.mediamanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener 
{

    public static String TYPE = "TYPE";
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_ALL = 2;

    private final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button imgBtn = (Button) findViewById(R.id.imagebtn);
        imgBtn.setOnClickListener(this);

        final Button videoBtn = (Button)findViewById(R.id.videobtn);
        videoBtn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                Toast.makeText(this, "안드로이드 6.0부터 마시멜로부터 일부 권한에 대해 사용자에게 동의 필요!", Toast.LENGTH_LONG).show();
                imgBtn.setEnabled(false);
                videoBtn.setEnabled(false);
                return;
            }
        }

        Button allbtn = (Button)findViewById(R.id.allbtn);
        allbtn.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Button imgBtn = (Button) findViewById(R.id.imagebtn);
                    imgBtn.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.imagebtn:
                openImageViewer();
                break;
            case R.id.videobtn:
                openVideoPlayer();
                break;
            case R.id.allbtn:
                openAllMedia();
            default:
                break;
        }
    }

    private void openImageViewer()
    {
		// 버튼을 클릭하면 이미지를 볼 수 있는 뷰어 액티비티를
		// 실행할 수 있도록 추가한다.
        Intent i = new Intent(this, ImageGridActivity.class);
        i.putExtra(TYPE, TYPE_IMAGE);
        startActivity(i);
    }

    private void openVideoPlayer()
    {
		// 기기의 비디오 파일들을 thumbnail를 이용하여
		// 화면에 보여 주는 액티비티를 호출한다.
        Intent i = new Intent(this, ImageGridActivity.class);
        i.putExtra(TYPE, TYPE_VIDEO);
        startActivity(i);
    }

    private void openAllMedia()
    {
        Intent i = new Intent(this, ImageGridActivity.class);
        i.putExtra(TYPE, TYPE_ALL);
        startActivity(i);
    }
}

