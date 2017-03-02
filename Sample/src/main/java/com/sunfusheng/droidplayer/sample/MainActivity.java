package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.adapter.VideoAdapter;
import com.sunfusheng.droidplayer.sample.model.VideoModel;
import com.sunfusheng.droidplayer.sample.util.ModelUtil;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.playerView)
    DroidPlayerView playerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    ListView listView;

    private List<VideoModel> mData = ModelUtil.getVideoList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar(toolbar, R.string.app_name, false);

        VideoAdapter adapter = new VideoAdapter(this, mData);
        listView.setAdapter(adapter);

        int randomNum = new Random().nextInt(mData.size());
        initPlayerView(mData.get(randomNum));

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
