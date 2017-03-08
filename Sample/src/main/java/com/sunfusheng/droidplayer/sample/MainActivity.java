package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem lastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar(toolbar, R.string.app_name, false);
        showFragment(new SampleFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_more, menu);
        lastItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DroidMediaPlayer.getInstance().release();
        item.setCheckable(true);
        item.setChecked(true);
        if (lastItem != null && lastItem != item) {
            lastItem.setCheckable(false);
            lastItem.setChecked(false);
        }
        lastItem = item;
        switch (item.getItemId()) {
            case R.id.item_main:
                showFragment(new MainFragment());
                break;
            case R.id.item_sample:
                showFragment(new SampleFragment());
                break;
            case R.id.item_list:
                showFragment(new ListFragment());
                break;
            case R.id.item_recycler_view:
                showFragment(new RecyclerViewFragment());
                break;
            case R.id.item_about:
                showFragment(new AboutFragment());
                break;
        }
        return true;
    }

    private void showFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.commitAllowingStateLoss();
    }

}
