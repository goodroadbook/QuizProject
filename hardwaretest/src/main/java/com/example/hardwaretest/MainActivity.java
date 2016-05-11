package com.example.hardwaretest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hardwaretest.bluetooth.BluetoothMainActivity;
import com.example.hardwaretest.camera.CameraAppActivity;
import com.example.hardwaretest.camera.CameraControlActivity;
import com.example.hardwaretest.fingerprint.FingerprintActivity;
import com.example.hardwaretest.wifi.WiFiControlActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) this.findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) this.findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) this.findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button btn4 = (Button) this.findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        Button btn5 = (Button) this.findViewById(R.id.button5);
        btn5.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                return;
            }
        }
    }

    @Override
    public void onClick(View aView)
    {
        Intent i = null;
        switch (aView.getId())
        {
            case R.id.button1:
                i = new Intent(this, CameraAppActivity.class);
                break;
            case R.id.button2:
                i = new Intent(this, CameraControlActivity.class);
                break;
            case R.id.button3:
                i = new Intent(this, WiFiControlActivity.class);
                break;
            case R.id.button4:
                i = new Intent(this, BluetoothMainActivity.class);
                break;
            case R.id.button5:
                i = new Intent(this, FingerprintActivity.class);
                break;
            default:
                break;
        }
        startActivity(i);
    }
}


