package com.example.hybridtest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private WebView mWebView = null;

    public class WebCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,
                                                String url)
        {
            return false;
        }
    }

    public class AndroidJavaScriptInterface
    {
        private Context mContext = null;
        private Handler handler = new Handler();

        public AndroidJavaScriptInterface(Context aContext)
        {
            mContext = aContext;
        }

        @JavascriptInterface
        public void showToastMessage(final String aMessage)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(mContext,
                            aMessage,
                            Toast.LENGTH_SHORT).show();

                    runningApp(aMessage);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebCustomClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new AndroidJavaScriptInterface(this), "MYApp");
        mWebView.loadUrl("http://192.168.0.21:8080/Hybrid/hybridapptest.jsp");

        Button btn = (Button)findViewById(R.id.sendmsg);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.sendmsg:
                TextView sendText = (TextView)findViewById(R.id.sendtxt);
                String sendmessage = sendText.getText().toString();
                mWebView.loadUrl("javascript: showDisplayMessage('"+ sendmessage +"')");
                break;
            default:
                break;
        }
    }

    private void runningApp(String aPackageName)
    {
        boolean isState = false;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> installedApps = pm.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo resolveInfo : installedApps)
        {
            if(true == resolveInfo.activityInfo.packageName.equals(aPackageName))
            {
                Intent intent = getPackageManager().getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
                startActivity(intent);
                isState = true;
                break;
            }
        }

        if(false == isState)
        {
            Toast.makeText(this, "설치된 앱이 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
}
