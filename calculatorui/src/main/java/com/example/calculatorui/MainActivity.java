package com.example.calculatorui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private String mStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button)findViewById(R.id.btn1);
        Button btn2 = (Button)findViewById(R.id.btn2);
        Button btn3 = (Button)findViewById(R.id.btn3);
        Button btn4 = (Button)findViewById(R.id.btn4);
        Button btn5 = (Button)findViewById(R.id.btn5);
        Button btn6 = (Button)findViewById(R.id.btn6);
        Button btn7 = (Button)findViewById(R.id.btn7);
        Button btn8 = (Button)findViewById(R.id.btn8);
        Button btn9 = (Button)findViewById(R.id.btn9);
        Button btn10 = (Button)findViewById(R.id.btn10);
        Button btn11 = (Button)findViewById(R.id.btn11);
        Button btn12 = (Button)findViewById(R.id.btn12);
        Button btn13 = (Button)findViewById(R.id.btn13);
        Button btn14 = (Button)findViewById(R.id.btn14);
        Button btn15 = (Button)findViewById(R.id.btn15);
        Button btn16 = (Button)findViewById(R.id.btn16);
        Button btn17 = (Button)findViewById(R.id.btn17);
        Button btn18 = (Button)findViewById(R.id.btn18);
        Button btn19 = (Button)findViewById(R.id.btn19);
        Button btn20 = (Button)findViewById(R.id.btn20);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);
        btn13.setOnClickListener(this);
        btn14.setOnClickListener(this);
        btn15.setOnClickListener(this);
        btn16.setOnClickListener(this);
        btn17.setOnClickListener(this);
        btn18.setOnClickListener(this);
        btn19.setOnClickListener(this);
        btn20.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        TextView showtext = (TextView)findViewById(R.id.showtext);

        switch (v.getId())
        {
            case R.id.btn1:
                mStr = "";
                showtext.setText(mStr);
                break;
            case R.id.btn2:
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                mStr = mStr + "7";
                showtext.setText(mStr);
                break;
            case R.id.btn6:
                mStr = mStr + "8";
                showtext.setText(mStr);
                break;
            case R.id.btn7:
                mStr = mStr + "9";
                showtext.setText(mStr);
                break;
            case R.id.btn8:
                break;
            case R.id.btn9:
                mStr = mStr + "4";
                showtext.setText(mStr);
                break;
            case R.id.btn10:
                mStr = mStr + "5";
                showtext.setText(mStr);
                break;
            case R.id.btn11:
                mStr = mStr + "6";
                showtext.setText(mStr);
                break;
            case R.id.btn12:
                break;
            case R.id.btn13:
                mStr = mStr + "1";
                showtext.setText(mStr);
                break;
            case R.id.btn14:
                mStr = mStr + "2";
                showtext.setText(mStr);
                break;
            case R.id.btn15:
                mStr = mStr + "3";
                showtext.setText(mStr);
                break;
            case R.id.btn16:
                break;
            case R.id.btn17:
                break;
            case R.id.btn18:
                break;
            case R.id.btn19:
                break;
            case R.id.btn20:
                break;
            default:
                break;
        }
    }
}
