package com.example.networkcheck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPPostActivity extends AppCompatActivity implements View.OnClickListener
{
    private final String IMG_FILE_PATH = "imgfilepath";
    private final String IMG_TITLE = "imgtitle";
    private final String IMG_ORIENTATION = "imgorientation";

    private final int REQ_CODE_SELECT_IMAGE = 1001;
    private String mImgPath = null;
    private String mImgTitle = null;
    private String mImgOrient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectbtn = (Button) findViewById(R.id.selectimg);
        selectbtn.setOnClickListener(this);

        Button sendinfo = (Button) findViewById(R.id.sendinfo);
        sendinfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectimg:
                getGallery();
                break;
            case R.id.sendinfo:
                new HttpRequestAsyncTask().execute(this.mImgPath, this.mImgTitle, this.mImgOrient);
                break;
            default:
                break;
        }
    }

    /**
     * 사진 선택을 위해 갤러리를 호출한다.
     */
    private void getGallery()
    {
        Intent intent = null;

        // 안드로이드 KitKat(level 19)부터는 ACTION_PICK 이용
        if(Build.VERSION.SDK_INT >= 19)
        {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 선택된 사진을 받아 서버에 업로드한다.
        if (requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Uri uri = data.getData();
                getImageNameToUri(uri);

                try
                {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ImageView img = (ImageView) findViewById(R.id.imageview);
                    img.setImageBitmap(bm);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * URI 정보를 이용하여 사진 정보 가져옴
     */
    private void getImageNameToUri(Uri data) {
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION
        };

        Cursor cursor = this.getContentResolver().query(data, proj, null, null, null);
        cursor.moveToFirst();

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        int column_orientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        mImgPath = cursor.getString(column_data);
        mImgTitle = cursor.getString(column_title);
        mImgOrient = cursor.getString(column_orientation);
    }

    private int uploadImageInfo(HashMap<String, String> aParams) {
        final int TIME_OUT = 20;
        final String POST_METHOD = "POST";
        //독자마다 IP는 다를 수 있다.
        final String SERVER_URL = "http://192.168.0.4:8080/uploadimginfo.jsp";

        int result = 0;
        String line = null;
        URL url = null;
        HttpURLConnection httpConn = null;

        try {
            // URL 객체를생성한다.
            url = new URL(SERVER_URL);

            // URL을통해HttpURLConnnection객체를
            httpConn = (HttpURLConnection) url.openConnection();

            // 연결TimeOut설정
            httpConn.setConnectTimeout(TIME_OUT * 1000);

            // Read TimeOut설정
            httpConn.setReadTimeout(TIME_OUT * 1000);

            // 요청방식선택 (GET, POST)
            httpConn.setRequestMethod(POST_METHOD);

            // HTTP 요청시에 Url encoded 방식으로 인코딩 후 전송한다.
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // OutputStream으로 이미지 정보를 write 한다.
            OutputStream os = httpConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(aParams));
            writer.flush();
            writer.close();
            os.close();

            // 서버요청에 대한 응답 코드를 받는다.
            int responseCode = httpConn.getResponseCode();

            // 200 ~ 299는 성공이다. 나머지는 에러를 리턴한다.
            if (responseCode >= 200 && responseCode < 300) {
                // 응답정보를InputStream에서 String 으로변경한다.
                InputStream in = httpConn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }

                rd.close();
                in.close();

                if (null != response && 0 != response.length()) {
                    if (response.toString().trim().equals("0")) {
                        result = 0; // 정보전송성공
                    } else {
                        result = -1; // 정보전송실패
                    }
                }
            } else {
                result = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            httpConn.disconnect();
        }
        return result;
    }

    private String getPostDataString(HashMap<String, String> params) {
        boolean isFirst = true;
        StringBuilder result = new StringBuilder();

        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (true == isFirst) {
                    isFirst = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public class HttpRequestAsyncTask extends AsyncTask<String, Integer, Integer> {
        private String mImagePath = null;
        private String mImageTitle = null;
        private String mImageOrientation = null;

        private ProgressDialog mWaitDlg = null;

        /**
         * 작업을 시작하기 전에 필요한 UI를 화면에 보여주도록 한다
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //최신버전을 서버에 확인하는 동안 Wating dialog를 보여주도록 한다.
            mWaitDlg = new ProgressDialog(HTTPPostActivity.this);
            mWaitDlg.setMessage("이미지 정보 전송중...");
            mWaitDlg.show();
        }

        /**
         * 서버 요청 작업을 진행한다.
         */
        @Override
        protected Integer doInBackground(String... arg) {
            mImagePath = arg[0];
            mImageTitle = arg[1];
            mImageOrientation = arg[2];

            HashMap<String, String> params = new HashMap<String, String>();
            params.put(IMG_FILE_PATH, mImagePath);
            params.put(IMG_TITLE, mImageTitle);
            params.put(IMG_ORIENTATION, mImageOrientation);


            int result = uploadImageInfo(params);
            return result;
        }

        /**
         * 서버 작업 진행 중에 UI를 갱신할 필요가 있는 경우 호출 되어진다.
         * doInBackground에서 publicshProgress()를 호출하면 invoked 된다.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        /**
         * 서버 작업 완료 후 화면에 필요한 UI를 보여주도록 한다.
         */
        @Override
        protected void onPostExecute(Integer aResult) {
            super.onPostExecute(aResult);

            if (null != mWaitDlg) {
                mWaitDlg.dismiss();
                mWaitDlg = null;
            }

            if (null == aResult) {
                return;
            }

            if (0 == aResult.intValue()) {
                Toast.makeText(HTTPPostActivity.this, "사진 정보 보내기를 성공 하였습니다.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HTTPPostActivity.this, "사진 정보 보내기를 실패하였습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
