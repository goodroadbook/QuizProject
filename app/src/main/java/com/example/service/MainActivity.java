package com.example.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button servicebtn = (Button)findViewById(R.id.servicebtn);
        servicebtn.setOnClickListener(this);

        Button activitybtn = (Button)findViewById(R.id.activitybtn);
        activitybtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.servicebtn:
                Intent i = new Intent(this, DaemonService.class);
                startService(i);
                break;
            case R.id.activitybtn:
                Intent a = new Intent(this, SecondActivity.class);
                startActivity(a);
                break;
            default:
                break;
        }
    }
}
