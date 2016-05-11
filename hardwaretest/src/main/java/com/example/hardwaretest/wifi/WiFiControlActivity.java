package com.example.hardwaretest.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.hardwaretest.R;

public class WiFiControlActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener
{
    private WifiManager mWiFiManager = null;
    private String mAPName = null;

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String strAction = intent.getAction();
            if (strAction.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
            {
                switch(mWiFiManager.getWifiState())
                {
                    case WifiManager.WIFI_STATE_DISABLED:
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                    case WifiManager.WIFI_STATE_ENABLING:
                        String ssid = mWiFiManager.getConnectionInfo().getSSID();
                        if(null != mAPName && ssid.contains(mAPName))
                        {
                            mWiFiManager.disconnect();
                        }
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        boolean wifiState = mWiFiManager.isWifiEnabled();

        ToggleButton tbtn = (ToggleButton) findViewById(R.id.tbutton);
        tbtn.setOnCheckedChangeListener(this);
        tbtn.setChecked(!wifiState);

        Button apbtn = (Button) this.findViewById(R.id.apbtn);
        apbtn.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver,filter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onClick(View aView)
    {
        switch (aView.getId())
        {
            case R.id.apbtn:
                EditText apname = (EditText) findViewById(R.id.apname);
                mAPName = apname.getText().toString();

                String ssid = mWiFiManager.getConnectionInfo().getSSID();
                if(null != mAPName && ssid.contains(mAPName))
                {
                    mWiFiManager.disconnect();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked)
    {
        if (true == isChecked)
        {
            // Wi-Fi 설정이 ON 된 경우 처리한다.
            setWiFiControl(false);
        }
        else
        {
            // Wi-Fi 설징이 OFF 된 경우 처리한다.
            setWiFiControl(true);
        }
    }

    private void setWiFiControl(boolean aEnableState)
    {
        mWiFiManager.setWifiEnabled(aEnableState);
    }
}


