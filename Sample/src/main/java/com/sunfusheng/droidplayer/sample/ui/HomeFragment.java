package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sunfusheng.droidplayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.adapter.HomeVideoAdapter;
import com.sunfusheng.droidplayer.sample.http.Api;
import com.sunfusheng.droidplayer.sample.http.ApiService;
import com.sunfusheng.droidplayer.sample.model.VideoEntity;
import com.sunfusheng.droidplayer.sample.util.AppUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.playerView)
    DroidPlayerView playerView;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.fl_player_container)
    FrameLayout flPlayerContainer;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private HomeVideoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);

        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        getVideoList();
    }

    private void initView() {

    }

    private void initListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            initPlayerView(position);
            playerView.play();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> true);
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
                        flPlayerContainer.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        rlEmpty.setVisibility(View.GONE);

                        mAdapter = new HomeVideoAdapter(getContext(), list);
                        listView.setAdapter(mAdapter);

                        int randomNum = new Random().nextInt(list.size());
                        initPlayerView(randomNum);
                    } else {
                        flPlayerContainer.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                }, Throwable::printStackTrace);
    }

    private void initPlayerView(int position) {
        VideoEntity entity = mAdapter.getItem(position);
        playerView.setVideoTitle(entity.getTitle());
        playerView.setVideoUrl(entity.getMp4_url());
        playerView.setImageUrl(entity.getCover());
    }

}
