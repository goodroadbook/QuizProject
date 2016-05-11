package com.example.networkcheck;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPGetActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new HttpRequestAsyncTask().execute(getCurrentVersion());
    }

    private int getCurrentVersion()
    {
        int curversion			= 0;
        PackageInfo pkgInfo	= null;

        try
        {
            pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            curversion = pkgInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return 0;
        }

        return curversion;
    }

    private int request(int aCurrentVersion)
    {
        final int TIME_OUT		= 20;
        final String GET_METHOD	= "GET";
        //독자마다 IP 주소는 다를 수 있다.
        final String SERVER_URL	= "http://192.168.0.4:8080/newversion.jsp";
        final String QUERY_STR	= "?CURRENT_VERSION=";

        int result = 0;
        String urlStr = null;
        String line	= null;
        URL url	= null;
        HttpURLConnection httpConn = null;

        try
        {
            // 서버 URL과 전송 할 데이터를 구성한다.
            urlStr = SERVER_URL + QUERY_STR + aCurrentVersion;

            // URL 객체를 생성한다.
            url = new URL(urlStr);

            // URL을 통해 HttpURLConnnection 생성
            httpConn = (HttpURLConnection)url.openConnection();

            // 연결 TimeOut 설정
            httpConn.setConnectTimeout(TIME_OUT * 1000);

            // Read TimeOut 설정
            httpConn.setReadTimeout(TIME_OUT * 1000);

            // 요청방식 선택 (GET, POST)
            httpConn.setRequestMethod(GET_METHOD);

            // 서버의 최신버전 정보를 요청한다.
            httpConn.connect();

            // 서버 요청에 대한 응답코드를 받는다.
            int responseCode = httpConn.getResponseCode();

            // 200 ~ 299는 성공이다. 나머지는 에러를 리턴한다.
            if(responseCode< 200 || responseCode>= 300)
            {
                return -1;
            }

            // 서버 응답 정보를 InputStream에서 String으로 읽는다.
            InputStream in = httpConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            in.close();

            // 응답 결과 유무를 확인한다.
            if(null == response || 0 == response.length())
            {
                return -1;
            }

            // 응답 결과에서 최신 버전 정보를 추출한다.
            String parserStr[] = response.toString().trim().split("=");
            result = Integer.valueOf(parserStr[1]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }

        return result;
    }

    public class HttpRequestAsyncTask extends AsyncTask<Integer, Integer, Integer>
    {
        private int currVersion		= 0;
        private ProgressDialog mWaitDlg	= null;

        /**
         * 작업을 시작하기 전에 필요한 UI를 화면에 보여주도록 한다.
         */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //서버에 요청 동안 Wating dialog를 보여주도록 한다.
            mWaitDlg = new ProgressDialog(HTTPGetActivity.this);
            mWaitDlg.setMessage("최신 버전 확인중...");
            mWaitDlg.show();
        }

        /**
         * 서버에 요청한다.
         */
        @Override
        protected Integer doInBackground(Integer... arg)
        {
            currVersion = arg[0].intValue();

            // 서버에서 최신 버전 정보를 가져온다.
            int newVersion = request(currVersion);

            return newVersion;
        }

        /**
         * 서버 요청 시에 UI를갱신할필요가있는경우호출
         * doInBackground에서 publicshProgress()를 호출 후 invoked
         */
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }

        /**
         * 서버 요청 완료 후 화면에 필요한 UI를 보여주도록 한다.
         */
        @Override
        protected void onPostExecute(Integer aResult)
        {
            super.onPostExecute(aResult);

            int ret = 0;

            //서버 요청 완료 후 Waiting dialog를 제거한다.
            if(null != mWaitDlg)
            {
                mWaitDlg.dismiss();
                mWaitDlg = null;
            }

            int newVersion = aResult.intValue();

            if(newVersion> 0)
            {
                TextView currversiontext = (TextView) findViewById(R.id.currversiontext);
                currversiontext.setText("단말에 설치된 제품 버전은 " + currVersion + " 있습니다.");
                TextView newversiontext = (TextView) findViewById(R.id.newversiontext);
                newversiontext.setText("제품의 최신 버전은 " + newVersion + " 있습니다.");

                if(newVersion>currVersion)
                {
                    ret = 1;
                }
                else
                {
                    ret = 0;
                }
            }
            else
            {
                ret = -1;
            }

            // 결과를화면에알려준다.
            TextView showText = (TextView)findViewById(R.id.showtext);
            switch(ret)
            {
                case -1:
                    showText.setText("오류로 인하여 제품 업그레이드 버전을 확인할 수 없습니다.");
                    break;
                case 0:
                    showText.setText("최신버전을사용중입니다.");
                    break;
                case 1:
                    showText.setText("최신버전이 있습니다. 제품 업그레이드 후에 사용하시기 바랍니다.");
                    break;
                default:
                    break;
            }
        }
    }


}
