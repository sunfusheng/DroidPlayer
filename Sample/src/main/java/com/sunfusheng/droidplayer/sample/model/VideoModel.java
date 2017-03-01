package com.sunfusheng.droidplayer.sample.model;

/**
 * Created by sunfusheng on 2017/3/1.
 */
public class VideoModel {

    public String title;
    public String image_url;
    public String video_url;

    public VideoModel() {
    }

    public VideoModel(String title, String video_url) {
        this.title = title;
        this.video_url = video_url;
    }

    public VideoModel(String title, String image_url, String video_url) {
        this.title = title;
        this.image_url = image_url;
        this.video_url = video_url;
    }
}
