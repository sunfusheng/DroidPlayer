package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.http.Api;
import com.sunfusheng.droidplayer.sample.http.ApiService;
import com.sunfusheng.droidplayer.sample.model.VideoEntity;
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

    List<Object> mList = new ArrayList<>();
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

    private void getVideoList(final boolean isLoadMore) {
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
                .map(it -> flatVideoEntity2Obj(it.get(ApiService.ID)))
                .subscribe(list -> {
                    if (list != null && list.size() > 0) {
                        mList.addAll(list);
                        onSuccess(mList, isLoadMore);
                    } else {
                        if (isLoadMore) {
                            onSuccess(mList, isLoadMore);
                        } else {
                            onEmpty();
                        }
                    }
                }, e -> {
                    startPage--;
                    e.printStackTrace();
                    onError();
                });
    }

    private List<Object> flatVideoEntity2Obj(List<VideoEntity> videos) {
        if (videos == null || videos.size() == 0) return null;
        List<Object> list = new ArrayList<>();
        for (VideoEntity entity : videos) {
            boolean isRepeat = false;
            if (mList != null && mList.size() > 0) {
                for (Object obj : mList) {
                    if (obj instanceof VideoEntity) {
                        VideoEntity item = (VideoEntity) obj;
                        if (item.getVid().equals(entity.getVid())) {
                            isRepeat = true;
                        }
                    }
                }
            }
            if (!isRepeat) {
                list.add(entity);
            }
        }
        return list;
    }

    public void onLoading() {
        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.LOADING);
    }

    public void onSuccess(List<Object> list, boolean isLoadMore) {
        recyclerViewWrapper.setData(list);
        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
    }

    public void onError() {
        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.ERROR);
    }

    public void onEmpty() {
        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.EMPTY);
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
