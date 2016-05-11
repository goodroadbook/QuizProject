package com.example.hardwaretest.fingerprint;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hardwaretest.R;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;


public class FingerprintActivity extends AppCompatActivity
{
    private Context mContext = null;
    private SpassFingerprint mSpassFingerprint = null;
    private Spass mSpass = null;

    private SpassFingerprint.RegisterListener mRegisterListener
            = new SpassFingerprint.RegisterListener()
    {
        @Override
        public void onFinished()
        {
            Toast.makeText(mContext, "지문 등록이 완료되었습니다.",
                    Toast.LENGTH_SHORT).show();

            TextView regtxt = (TextView)FingerprintActivity.this.
                    findViewById(R.id.fingerregstate);
            regtxt.setText("등록된 지문이 있습니다.");
        }
    };

    private SpassFingerprint.IdentifyListener listener = new SpassFingerprint.IdentifyListener()
    {
        @Override
        public void onFinished(int eventStatus)
        {
            TextView authtxt = (TextView)FingerprintActivity.this.
                    findViewById(R.id.authstate);
            if (eventStatus ==
                    SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS)
            {
                Toast.makeText(FingerprintActivity.this,
                        "등록된 지문과 일치 합니다.",
                        Toast.LENGTH_SHORT).show();
                authtxt.setText("등록된 지문과 일치합니다.");
            }
            else if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS)
            {
                Toast.makeText(FingerprintActivity.this,
                        "비밀번호가 일치 합니다.",
                        Toast.LENGTH_SHORT).show();
                authtxt.setText("등록된 비밀번호와 일치합니다.");
            }
            else
            {
                Toast.makeText(FingerprintActivity.this,
                        "등록된 지문 또는 비밀번호가 일치하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
                authtxt.setText(" 지문 또는 비밀번호가 일치하지 않습니다.");
            }
        }

        @Override
        public void onReady()
        {
            ;
        }

        @Override
        public void onStarted()
        {
            ;
        }

        @Override
        public void onCompleted() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        TextView supporttxt = (TextView)this.findViewById(R.id.fingerstate);
        TextView regtxt = (TextView)this.findViewById(R.id.fingerregstate);

        mSpass = new Spass();
        try
        {
            mSpass.initialize(this);
        }
        catch (SsdkUnsupportedException e)
        {
            supporttxt.setText(" 단말에서 지문 인식을 지원하지 않습니다.");
        }

        boolean featureEnabled =
                mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);
        if(true == featureEnabled)
        {
            mSpassFingerprint = new SpassFingerprint(this);
            supporttxt.setText("단말에서 지문 인식을 지원합니다.");
        }
        else
        {
            supporttxt.setText(" 단말에서 지문 인식을 지원하지 않습니다.");
        }

        boolean hasRegisteredFinger = mSpassFingerprint.hasRegisteredFinger();
        if(false == hasRegisteredFinger)
        {
            mSpassFingerprint.registerFinger(this, mRegisterListener);
            regtxt.setText("등록된 지문이 없습니다.");
        }
        else
        {
            regtxt.setText("등록된 지문이 있습니다.");
            // 지문인식 팝업 없이 지문 인식 가능
            //mSpassFingerprint.startIdentify(listener);

            // 지문인식과 비밀번호 모두 사용할 수 있도록 팝업 제공
            mSpassFingerprint.startIdentifyWithDialog(this, listener, true);

            // 지문인식 인식만 할 수 있는 팝업 제공
            //mSpassFingerprint.startIdentifyWithDialog(this, listener, false);
        }
    }
}


