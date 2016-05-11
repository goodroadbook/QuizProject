package com.example.mediamanager.common;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mediamanager.MainActivity;
import com.example.mediamanager.common.MediaData;

import java.util.ArrayList;

/**
 * Created by namjinha on 2016-01-22.
 */
public class GetMediaData
{
    public static void getImageArrayData(Context aContext,
                                         ArrayList<MediaData> aMediaData)
    {
        Cursor imageCursor = null;

        String[] imagecolumns = {
                MediaStore.Images.Media._ID,            // 미디어 ID
                MediaStore.Images.Media.DATA,          // 이미지 경로
                MediaStore.Images.Media.ORIENTATION  // 이미지 방향
        };

        //컨텐트 리졸브의 query() 함수와 URI를 이용하여 이미지 정보를 가져온다.
        imageCursor = aContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imagecolumns,
                null,
                null,
                null);

        // 가져온 이미지 정보는 Cursor를 통해 확인할 수 있다.
        if(imageCursor != null && imageCursor.moveToFirst())
        {
            int imageid = 0;
            String imagePath = null;
            int orientation = 0;

            int imageIdColumnIndex = imageCursor.
                    getColumnIndex(MediaStore.Images.Media._ID);
            int imagePathColumnIndex = imageCursor.
                    getColumnIndex(MediaStore.Images.Media.DATA);
            int imageOrientationColumnIndex = imageCursor.
                    getColumnIndex(MediaStore.Images.Media.ORIENTATION);
            do
            {
                imageid = imageCursor.getInt(
                        imageIdColumnIndex);
                imagePath = imageCursor.getString(
                        imagePathColumnIndex);
                orientation = imageCursor.getInt(
                        imageOrientationColumnIndex);

                // 이미지 정보 중에 필요한 미디어 ID, 이미지 경로, 이미지 방향을 
                // MediaData 클래스와 ArrayList로 관리한다.
                MediaData info = new MediaData();
                info.type = MainActivity.TYPE_IMAGE;
                info.mediaid = imageid;
                info.mediapath = imagePath;
                info.orientation = orientation;
                aMediaData.add(info);
            }
            while(imageCursor.moveToNext());
        }
    }

    public static void getVideoArrayData(Context aContext,
                                         ArrayList<MediaData> aMediaData)
    {
        Cursor videoCursor = null;

        String[] videocolumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA
        };


        videoCursor = aContext.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videocolumns,
                null,
                null,
                null);

        if(videoCursor != null && videoCursor.moveToFirst())
        {
            int videoId = 0;
            String videoPath = null;

            int videoIdColumnIndex = videoCursor.
                    getColumnIndex(MediaStore.Video.Media._ID);
            int videoPathColumnIndex = videoCursor.
                    getColumnIndex(MediaStore.Video.Media.DATA);

            do
            {
                videoId = videoCursor.getInt(videoIdColumnIndex);
                videoPath = videoCursor.getString(
                        videoPathColumnIndex);

                MediaData info = new MediaData();
                info.type = MainActivity.TYPE_VIDEO;
                info.mediaid = videoId;
                info.mediapath = videoPath;
                aMediaData.add(info);
            }
            while(videoCursor.moveToNext());
        }
    }
}
