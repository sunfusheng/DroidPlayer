package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.second.DroidBasePlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.second.DroidPlayerView2;

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
    DroidPlayerView2 playerView;

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
        playerView.setVideoTitle(mList.get(randomNum).title);
        playerView.setVideoUrl(mList.get(randomNum).video_url);
        playerView.setImageUrl(mList.get(randomNum).image_url);
    }

    private void initView() {

    }

    private void initListener() {
        tvBasePlay.setOnClickListener(v -> basePlayerView.play());
    }

    @Override
    public void onResume() {
        basePlayerView.resume();
        playerView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        basePlayerView.pause();
        playerView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        basePlayerView.release();
        playerView.release();
        super.onDestroy();
    }
}
