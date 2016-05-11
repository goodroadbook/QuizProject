package com.example.networkcheck;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(false == isConnected())
        {
            showResultText("연결된 네트워크 정보가 없습니다.");
            return;
        }

        switch(getNetworkType())
        {
            case ConnectivityManager.TYPE_WIFI:
                Button httpgetbtn = (Button)findViewById(R.id.httpgetbtn);
                Button httppostbtn = (Button)findViewById(R.id.httppostbtn);
                httpgetbtn.setEnabled(true);
                httppostbtn.setEnabled(true);
                httpgetbtn.setOnClickListener(this);
                httppostbtn.setOnClickListener(this);
                break;
            case ConnectivityManager.TYPE_MOBILE:
                Toast.makeText(this, "Wi-Fi 연결상태가 아닙니다. Wi-Fi 연결 후 다시 실행해 주세요.", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        String networktypename = getNetworkTypeName();
        TextView networktypetext = (TextView)findViewById(R.id.networkstate);
        networktypetext.setText(networktypename);
        showResultText(networktypename + "에 연결되어 있습니다.");
    }

    private String getNetworkTypeName()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork.getTypeName();
    }

    private int getNetworkType()
    {
        /*
        ConnectivityManager.TYPE_MOBILE         // 3G or 4G 기타 등등, 과긂 발생
        ConnectivityManager.TYPE_WIFI           // Wi-Fi 무료로 사용 가능
        */
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork.getType();
    }

    private boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    private void showResultText(String aResultTex)
    {
        TextView resulttext = (TextView)findViewById(R.id.networkresult);
        resulttext.setText(aResultTex);
    }

    @Override
    public void onClick(View v)
    {
        Intent i = null;
        switch (v.getId())
        {
           case R.id.httpgetbtn:
               i = new Intent(this, HTTPGetActivity.class);
               break;
           case R.id.httppostbtn:
               i = new Intent(this, HTTPPostActivity.class);
               break;
           default:
               break;
        }
        startActivity(i);
    }
}
