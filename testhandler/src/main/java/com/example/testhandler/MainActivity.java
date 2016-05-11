package com.example.testhandler;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private ProgressDialog mProgressDlg = null;
    private MainHandler mHandler = null;

    class MainHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                    mProgressDlg = new ProgressDialog(MainActivity.this);
                    mProgressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDlg.setMessage("sum = 0");
                    mProgressDlg.setMax(100);
                    mProgressDlg.show();
                    break;
                case 1:
                    mProgressDlg.setMessage("count = " + msg.arg1 + " , " + "sum = " + msg.arg2);
                    mProgressDlg.setProgress(msg.arg1);
                    break;
                case 2:
                    mProgressDlg.dismiss();
                    showText(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

    class CountThread extends Thread
    {
        @Override
        public void run()
        {
            int i = 0;
            int sum = 0;

            while (i < 10)
            {
                i++;
                sum += i;

                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.arg1 = i;
                msg.arg2 = sum;
                mHandler.sendMessage(msg);

                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            Message msg = mHandler.obtainMessage();
            msg.what = 2;
            msg.arg1 = sum;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new MainHandler();

        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        mHandler.sendMessage(msg);

        CountThread thread = new CountThread();
        thread.start();
    }

    private void showText(int sum)
    {
        TextView sumtext = (TextView)findViewById(R.id.sumvaltext);
        sumtext.setText(String.valueOf(sum));
    }
}
