package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.adapter.VideoAdapter;
import com.sunfusheng.droidplayer.sample.model.VideoModel;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.playerView)
    DroidPlayerView playerView;
    @BindView(R.id.listView)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);

        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        int randomNum = new Random().nextInt(mList.size());
        initPlayerView(mList.get(randomNum));
    }

    private void initView() {
        VideoAdapter adapter = new VideoAdapter(mContext, mList);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            playerView.release();
            initPlayerView(mList.get(position));
            playerView.play();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> true);
    }

    private void initPlayerView(VideoModel model) {
        playerView.setVideoTitle(model.title);
        playerView.setVideoUrl(model.video_url);
        playerView.setImageUrl(model.image_url);
    }

    @Override
    public void onResume() {
        playerView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        playerView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        playerView.release();
        super.onDestroy();
    }
}
