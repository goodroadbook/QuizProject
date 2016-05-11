package com.example.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button servicestopbtn = (Button)findViewById(R.id.servicestopbtn);
        servicestopbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.servicestopbtn:
                Intent i = new Intent(this, DaemonService.class);
                stopService(i);
                break;
            default:
                break;
        }
    }
}
