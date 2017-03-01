package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.util.ModelUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.playerView)
    DroidPlayerView playerView;
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        playerView.setVideoTitle(ModelUtil.getVideoDataList().get(0).title);
        playerView.setVideoUrl(ModelUtil.getVideoDataList().get(0).video_url);
//        playerView.play(ModelUtil.girl_dance);

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
