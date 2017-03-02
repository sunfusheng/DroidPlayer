package com.sunfusheng.droidplayer.sample.model;

/**
 * Created by sunfusheng on 2017/3/1.
 */
public class VideoModel {

    public String title;
    public String image_url;
    public String video_url;
    public String size;
    public String duration;

    public VideoModel() {
    }

    public VideoModel(String title, String video_url, String image_url, String size, String duration) {
        this.title = title;
        this.video_url = video_url;
        this.image_url = image_url;
        this.size = size;
        this.duration = duration;
    }
}
