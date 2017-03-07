package com.sunfusheng.droidplayer.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sunfusheng.droidplayer.sample.model.VideoModel;
import com.sunfusheng.droidplayer.sample.util.ModelUtil;

import java.util.List;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected Context mContext;
    protected List<VideoModel> mList = ModelUtil.getVideoList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();
    }
}
