package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by namjinha on 2015-12-28.
 */
public class DaemonService extends Service
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        Toast.makeText(this, "[DaemonService] onCreate() 함수 호출", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent i, int flags, int sId)
    {
        Toast.makeText(this, "[DaemonService] onStartCommand() 함수 호출", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Toast.makeText(this, "DaemonService 서비스를 종료합니다.", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
