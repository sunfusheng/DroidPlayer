package com.sunfusheng.droidplayer.sample.http;

import com.sunfusheng.droidplayer.sample.model.VideoEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sunfusheng on 2017/1/17.
 */
public interface ApiService {

    String BASE_URL = "http://c.3g.163.com/";
    String ID = "V9LG4B3A0";
    String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    // 获取视频列表
    @Headers(AVOID_HTTP403_FORBIDDEN)
    @GET("nc/video/list/V9LG4B3A0/n/{startPage}-10.html")
    Observable<Map<String, List<VideoEntity>>> getVideoList(@Path("startPage") int startPage);

}
