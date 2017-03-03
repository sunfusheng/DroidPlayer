package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.adapter.VideoAdapter;
import com.sunfusheng.droidplayer.sample.model.VideoModel;
import com.sunfusheng.droidplayer.sample.util.ModelUtil;

import java.util.List;
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

    private View rootView;
    private List<VideoModel> mData = ModelUtil.getVideoList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null);
            ButterKnife.bind(this, rootView);

            initData();
            initView();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initData() {
        int randomNum = new Random().nextInt(mData.size());
        initPlayerView(mData.get(randomNum));
    }

    private void initView() {
        VideoAdapter adapter = new VideoAdapter(mContext, mData);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            playerView.release();
            initPlayerView(mData.get(position));
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
