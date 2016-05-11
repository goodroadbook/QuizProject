package com.example.preferencetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button envbtn = (Button)this.findViewById(R.id.envbtn);
        envbtn.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String displayName = sharedPreferences.getString("name", "N/A");

        TextView nametxt = (TextView) this.findViewById(R.id.dispnametxt);
        nametxt.setText(displayName.toString());
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.envbtn:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
