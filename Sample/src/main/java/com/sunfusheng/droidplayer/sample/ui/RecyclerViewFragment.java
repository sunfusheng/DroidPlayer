package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.http.Api;
import com.sunfusheng.droidplayer.sample.http.ApiService;
import com.sunfusheng.droidplayer.sample.model.VideoEntity;
import com.sunfusheng.droidplayer.sample.util.AppUtil;
import com.sunfusheng.droidplayer.sample.widget.RecyclerViewWrapper.LoadingStateDelegate;
import com.sunfusheng.droidplayer.sample.widget.RecyclerViewWrapper.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.schedulers.Schedulers;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class RecyclerViewFragment extends BaseFragment implements RecyclerViewWrapper.OnRequestListener {

    @BindView(R.id.recyclerViewWrapper)
    RecyclerViewWrapper recyclerViewWrapper;

    private List<VideoEntity> mList = new ArrayList<>();
    private int startPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, null);
        ButterKnife.bind(this, view);
        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.LOADING);
        getVideoList(false);
    }

    private void initView() {

    }

    private void initListener() {
        recyclerViewWrapper.setOnRequestListener(this);
    }

    @Override
    public void onRefresh() {
        getVideoList(false);
    }

    @Override
    public void onLoadingMore() {
        getVideoList(true);
    }

    // 获取视频数据
    protected void getVideoList(final boolean isLoadMore) {
        if (!isLoadMore) {
            mList = new ArrayList<>();
            startPage = 0;
        } else {
            startPage += 1;
        }
        Api.getInstance().getApiService().getVideoList(startPage)
                .subscribeOn(Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .filter(it -> it != null && it.containsKey(ApiService.ID))
                .map(it -> removeDuplicateData(it.get(ApiService.ID)))
                .compose(bindToLifecycle())
                .subscribe(list -> {
                    if (AppUtil.notEmpty(list)) {
                        mList.addAll(list);
                        recyclerViewWrapper.setData(mList);
                        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                    } else {
                        if (AppUtil.notEmpty(mList)) {
                            recyclerViewWrapper.setData(mList);
                            recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                        } else {
                            recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.EMPTY);
                        }
                    }
                }, e -> {
                    recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.ERROR);
                    e.printStackTrace();
                    startPage--;
                });
    }

    // 去重
    private List<VideoEntity> removeDuplicateData(List<VideoEntity> list) {
        if (AppUtil.isEmpty(list)) return mList;
        for (VideoEntity entity : list) {
            boolean isRepeat = false;
            if (AppUtil.notEmpty(mList)) {
                for (VideoEntity item : mList) {
                    if (item.getVid().equals(entity.getVid())) {
                        isRepeat = true;
                    }
                }
            }
            if (!isRepeat) {
                mList.add(entity);
            }
        }
        return list;
    }

}
