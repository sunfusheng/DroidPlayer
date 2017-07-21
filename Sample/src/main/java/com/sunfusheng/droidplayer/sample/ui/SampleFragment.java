package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.droidplayer.DroidBasePlayerView;
import com.sunfusheng.droidplayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.http.Api;
import com.sunfusheng.droidplayer.sample.http.ApiService;
import com.sunfusheng.droidplayer.sample.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class SampleFragment extends BaseFragment {

    @BindView(R.id.basePlayerView)
    DroidBasePlayerView basePlayerView;
    @BindView(R.id.tv_base_play)
    TextView tvBasePlay;
    @BindView(R.id.tv_base_full_screen)
    TextView tvBaseFullScreen;
    @BindView(R.id.tv_base_tiny_screen)
    TextView tvBaseTinyScreen;
    @BindView(R.id.playerView1)
    DroidPlayerView playerView1;
    @BindView(R.id.playerView2)
    DroidPlayerView playerView2;
    @BindView(R.id.ll_players_container)
    LinearLayout llPlayersContainer;
    @BindView(R.id.fl_tiny_screen_container)
    FrameLayout flTinyScreenContainer;

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
        llPlayersContainer.setVisibility(View.GONE);
        getVideoList();
    }

    private void initView() {

    }

    private void initListener() {
        tvBasePlay.setOnClickListener(v -> basePlayerView.play());
        tvBaseFullScreen.setOnClickListener(v -> basePlayerView.enterFullScreen());
    }

    // 获取视频数据
    protected void getVideoList() {
        Api.getInstance().getApiService().getVideoList(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(it -> it != null && it.containsKey(ApiService.ID))
                .map(it -> it.get(ApiService.ID))
                .compose(bindToLifecycle())
                .subscribe(list -> {
                    if (AppUtil.notEmpty(list)) {
                        llPlayersContainer.setVisibility(View.VISIBLE);

                        basePlayerView.setVideoUrl(list.get(0).getMp4_url());
                        basePlayerView.setImageUrl(list.get(0).getCover());

                        playerView1.setVideoTitle(list.get(1).getTitle());
                        playerView1.setVideoUrl(list.get(1).getMp4_url());
                        playerView1.setImageUrl(list.get(1).getCover());

                        playerView2.setRatio(1, 1);
                        playerView2.setVideoTitle(list.get(2).getTitle());
                        playerView2.setVideoUrl(list.get(2).getMp4_url());
                        playerView2.setImageUrl(list.get(2).getCover());
                    }
                }, Throwable::printStackTrace);
    }

}
