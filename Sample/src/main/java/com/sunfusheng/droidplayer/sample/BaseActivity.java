package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by sunfusheng on 2017/3/2.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbar(Toolbar toolbar, @StringRes int resId, boolean showHomeAsUp) {
        initToolbar(toolbar, getString(resId), showHomeAsUp);
    }

    protected void initToolbar(Toolbar toolbar, String title, boolean showHomeAsUp) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }
}
