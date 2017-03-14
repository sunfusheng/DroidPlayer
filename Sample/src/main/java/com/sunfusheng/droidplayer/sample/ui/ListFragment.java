package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class ListFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ButterKnife.bind(this, view);
        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {

    }

    private void initView() {

    }

    private void initListener() {

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
        if (DroidMediaPlayer.getInstance().onBackPressed()) {
            return true;
        }
        return false;
    }

}
