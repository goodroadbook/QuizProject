package com.example.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.RemoteViews;

/**
 * Created by namjinha on 2016-02-12.
 */
public class WidgetSupport2 extends AppWidgetProvider
{
    public static final String ACTION_WIFI_ONOFF = "com.example.androidwidget.WIFI_ONOFF";
    public static final String ACTION_UPDATE_TXT = "com.example.androidwidget.UPDATE_TXT";

    @Override
    public void onReceive(Context aContext, Intent aIntent)
    {
        super.onReceive(aContext, aIntent);
        String action = aIntent.getAction();
        if(action.equals(ACTION_WIFI_ONOFF))
        {
            WifiManager wiFiManager = (WifiManager)
                    aContext.getSystemService(Context.WIFI_SERVICE);

            if(true == wiFiManager.isWifiEnabled())
            {
                wiFiManager.setWifiEnabled(false);
            }
            else
            {
                wiFiManager.setWifiEnabled(true);
            }
        }
        else if (action.equals(ACTION_UPDATE_TXT))
        {
            AppWidgetManager manager = AppWidgetManager.getInstance(aContext);
            this.onUpdate(aContext,
                    manager,
                    manager.getAppWidgetIds(new ComponentName(aContext, getClass())));
        }
        else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
        {
            WifiManager wiFiManager = (WifiManager)
                    aContext.getSystemService(Context.WIFI_SERVICE);
            switch(wiFiManager.getWifiState())
            {
                case WifiManager.WIFI_STATE_DISABLED:
                case WifiManager.WIFI_STATE_DISABLING:
                case WifiManager.WIFI_STATE_ENABLED:
                case WifiManager.WIFI_STATE_ENABLING:
                    AppWidgetManager manager = AppWidgetManager.getInstance(aContext);
                    this.onUpdate(aContext,
                            manager,
                            manager.getAppWidgetIds(new ComponentName(aContext, getClass())));
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        int count = appWidgetIds.length;
        for (int i=0; i<count; i++)
        {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context aContext,
                                 AppWidgetManager appWidgetManager,
                                 int appWidgetId)
    {
        Intent wifionoffintent = new Intent(ACTION_WIFI_ONOFF);
        PendingIntent wifionoffpendingIntent = PendingIntent.getBroadcast(aContext,
                0,
                wifionoffintent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent updatetxtintent = new Intent(ACTION_UPDATE_TXT);
        PendingIntent updatetxtpendingIntent = PendingIntent.getBroadcast(aContext,
                0,
                updatetxtintent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews updateViews = new RemoteViews(aContext.getPackageName(),
                R.layout.widget_wifi2);
        updateViews.setOnClickPendingIntent(R.id.widgetbtn, wifionoffpendingIntent);

        setWidgetText(aContext, updateViews);

        appWidgetManager.updateAppWidget(appWidgetId,
                updateViews);
    }

    private void setWidgetText(Context aContext, RemoteViews aUpdateViews)
    {
        WifiManager wiFiManager = (WifiManager) aContext.getSystemService(Context.WIFI_SERVICE);
        switch(wiFiManager.getWifiState())
        {
            case WifiManager.WIFI_STATE_DISABLED:
            case WifiManager.WIFI_STATE_DISABLING:
            case WifiManager.WIFI_STATE_UNKNOWN:
                aUpdateViews.setTextViewText(R.id.widgetbtn, "Wi-Fi : OFF");
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                aUpdateViews.setTextViewText(R.id.widgetbtn, "Wi-Fi : ON");
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                aUpdateViews.setTextViewText(R.id.widgetbtn, "Wi-Fi : ON");
                break;
        }
    }
}
