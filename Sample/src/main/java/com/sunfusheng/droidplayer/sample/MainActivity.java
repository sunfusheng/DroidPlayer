package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.playerView)
    DroidPlayerView playerView;
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String dibai = "http://olnvi6ek1.bkt.clouddn.com/%E8%BF%AA%E6%8B%9C.mp4";
    private String magic_leap = "http://olnvi6ek1.bkt.clouddn.com/Maigc%20Leap.mp4";
    private String ted = "http://olnvi6ek1.bkt.clouddn.com/TED.mp4";
    private String cat = "http://olnvi6ek1.bkt.clouddn.com/%E5%91%86%E8%90%8C%E5%B0%8F%E5%A5%B6%E7%8C%AB.mp4";
    private String space = "http://olnvi6ek1.bkt.clouddn.com/%E7%A9%BA%E9%97%B4%E7%AB%99.mp4";
    private String dance = "http://olnvi6ek1.bkt.clouddn.com/%E8%B6%85%E5%8A%A8%E6%84%9F%E8%88%9E%E8%B9%88.mp4";
    private String plane = "http://olnvi6ek1.bkt.clouddn.com/plane.mp4";
    private String animal = "http://olnvi6ek1.bkt.clouddn.com/%E6%A1%8C%E9%9D%A2%E4%B8%8A%E7%9A%84%E8%90%8C%E7%89%A9.mp4";
    private String girl_dance = "http://olnvi6ek1.bkt.clouddn.com/%E7%BE%8E%E5%A5%B3%E6%80%A7%E6%84%9F%E8%88%9E%E8%B9%88.mp4";
    private String baolaiwu = "http://olnvi6ek1.bkt.clouddn.com/%E4%B8%89%E5%82%BB%E5%A4%A7%E9%97%B9%E5%AE%9D%E8%8E%B1%E5%9D%9E%E4%B9%8B%E4%B8%A4%E4%B8%AA%E5%A9%9A%E7%A4%BC.mp4";
    private String qicheren = "http://olnvi6ek1.bkt.clouddn.com/%E6%B1%BD%E8%BD%A6%E4%BA%BA%E5%9B%A2%E8%81%9A%E7%9A%84%E5%9C%BA%E6%99%AF-%E6%97%B6%E4%BB%A3%E7%81%AD%E7%BB%9D2015%E5%B9%B4.mp4";
    private String snow = "http://olnvi6ek1.bkt.clouddn.com/%E4%B8%8B%E5%A4%A7%E9%9B%AA%E5%95%A6.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        playerView.setDataSource(cat);
//        playerView.play(girl_dance);

        btnTest.setOnClickListener(v -> {
            playerView.play();
        });
    }

    @Override
    protected void onResume() {
        DroidMediaPlayer.getInstance().resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        DroidMediaPlayer.getInstance().pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DroidMediaPlayer.getInstance().release();
        super.onDestroy();
    }
}
