package com.sunfusheng.droidplayer.sample.util;

import com.sunfusheng.droidplayer.sample.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunfusheng on 2017/3/1.
 */
public class ModelUtil {

    public static final String dibai = "http://olnvi6ek1.bkt.clouddn.com/%E8%BF%AA%E6%8B%9C.mp4";
    public static final String magic_leap = "http://olnvi6ek1.bkt.clouddn.com/Maigc%20Leap.mp4";
    public static final String ted = "http://olnvi6ek1.bkt.clouddn.com/TED.mp4";
    public static final String cat = "http://olnvi6ek1.bkt.clouddn.com/%E5%91%86%E8%90%8C%E5%B0%8F%E5%A5%B6%E7%8C%AB.mp4";
    public static final String space = "http://olnvi6ek1.bkt.clouddn.com/%E7%A9%BA%E9%97%B4%E7%AB%99.mp4";
    public static final String dance = "http://olnvi6ek1.bkt.clouddn.com/%E8%B6%85%E5%8A%A8%E6%84%9F%E8%88%9E%E8%B9%88.mp4";
    public static final String plane = "http://olnvi6ek1.bkt.clouddn.com/plane.mp4";
    public static final String animal = "http://olnvi6ek1.bkt.clouddn.com/%E6%A1%8C%E9%9D%A2%E4%B8%8A%E7%9A%84%E8%90%8C%E7%89%A9.mp4";
    public static final String girl_dance = "http://olnvi6ek1.bkt.clouddn.com/%E7%BE%8E%E5%A5%B3%E6%80%A7%E6%84%9F%E8%88%9E%E8%B9%88.mp4";
    public static final String baolaiwu = "http://olnvi6ek1.bkt.clouddn.com/%E4%B8%89%E5%82%BB%E5%A4%A7%E9%97%B9%E5%AE%9D%E8%8E%B1%E5%9D%9E%E4%B9%8B%E4%B8%A4%E4%B8%AA%E5%A9%9A%E7%A4%BC.mp4";
    public static final String qicheren = "http://olnvi6ek1.bkt.clouddn.com/%E6%B1%BD%E8%BD%A6%E4%BA%BA%E5%9B%A2%E8%81%9A%E7%9A%84%E5%9C%BA%E6%99%AF-%E6%97%B6%E4%BB%A3%E7%81%AD%E7%BB%9D2015%E5%B9%B4.mp4";
    public static final String snow = "http://olnvi6ek1.bkt.clouddn.com/%E4%B8%8B%E5%A4%A7%E9%9B%AA%E5%95%A6.mp4";


    public static List<VideoModel> getVideoDataList() {
        List<VideoModel> list = new ArrayList<>();
        list.add(new VideoModel("迪拜宣传片", dibai));
        list.add(new VideoModel("超震撼的Magic Leap视频", magic_leap));
        list.add(new VideoModel("TED改变你看法的事情", ted));
        list.add(new VideoModel("超萌小猫咪", cat));
        list.add(new VideoModel("空间站", space));
        list.add(new VideoModel("超动感舞蹈", dance));
        list.add(new VideoModel("美女热舞", girl_dance));
        list.add(new VideoModel("飞机喷绘过程", plane));
        list.add(new VideoModel("桌面上的萌物", animal));
        list.add(new VideoModel("三大傻大闹宝莱坞", baolaiwu));
        list.add(new VideoModel("汽车人的战争", qicheren));
        list.add(new VideoModel("世界都在下雪", snow));
        return list;
    }
}
