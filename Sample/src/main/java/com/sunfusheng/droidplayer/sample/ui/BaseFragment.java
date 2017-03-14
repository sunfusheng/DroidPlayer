package com.sunfusheng.droidplayer.sample.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public abstract class BaseFragment extends RxFragment {

    protected Activity mActivity;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();
    }

    protected boolean onBackPressed() {
        return false;
    }

}
