package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidBasePlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class SampleFragment extends BaseFragment {

    @BindView(R.id.basePlayerView)
    DroidBasePlayerView basePlayerView;
    @BindView(R.id.tv_base_play)
    TextView tvBasePlay;
    @BindView(R.id.playerView)
    DroidPlayerView playerView;
    @BindView(R.id.tv_base_full_screen)
    TextView tvBaseFullScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, null);
        ButterKnife.bind(this, view);
        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        int randomNum = new Random().nextInt(mList.size());
        basePlayerView.setVideoUrl(mList.get(randomNum).video_url);
        basePlayerView.setImageUrl(mList.get(randomNum).image_url);

        randomNum = new Random().nextInt(mList.size());
        playerView.setVideoTitle(randomNum % 2 == 0 ? null : mList.get(randomNum).title);
        playerView.setVideoUrl(mList.get(randomNum).video_url);
        playerView.setImageUrl(mList.get(randomNum).image_url);
    }

    private void initView() {

    }

    private void initListener() {
        tvBasePlay.setOnClickListener(v -> basePlayerView.play());
        tvBaseFullScreen.setOnClickListener(v -> basePlayerView.enterFullScreen());
    }

    @Override
    public void onResume() {
        DroidMediaPlayer.getInstance().resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        DroidMediaPlayer.getInstance().pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        DroidMediaPlayer.getInstance().release();
        super.onDestroy();
    }

    @Override
    protected boolean onBackPressed() {
        if (basePlayerView.isFullScreen()) {
            basePlayerView.quitFullScreen();
            return true;
        }
        if (playerView.isFullScreen()) {
            playerView.quitFullScreen();
            return true;
        }
        return false;
    }
}
