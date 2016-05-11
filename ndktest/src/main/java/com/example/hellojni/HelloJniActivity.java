package com.example.hellojni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloJniActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hellojni);

        TextView showtext = (TextView)findViewById(R.id.showtext);
        showtext.setText(stringFromJNI());
    }

    public native String stringFromJNI();

    static {
        System.loadLibrary("hello-jni");
    }
}
