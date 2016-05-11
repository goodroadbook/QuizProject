
package com.example.mediamanager.common;

import android.widget.ImageView;

/**
 * Created by namjinha on 2016-01-22.
 */
public class MediaData
{
    public int type = 0;                  // 비디오, 이미지 구분
    public int mediaid = 0;              //미디어 ID
    public String mediapath = null;     //미디어 경로
    public int orientation = 0;         //미디어 방향
    public ImageView imgview = null;     //이미지를 표시하기 위한 뷰
}

