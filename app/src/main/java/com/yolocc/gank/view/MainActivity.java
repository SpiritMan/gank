package com.yolocc.gank.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yolocc.gank.R;
import com.yolocc.gank.adapter.CategoryFragmentPagerAdapter;
import com.yolocc.gank.databinding.ActivityMainBinding;
import com.yolocc.gank.utils.StatusBarCompat;
import com.yolocc.gank.viewModel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainViewModel = new MainViewModel(this);
        mActivityMainBinding.setViewModel(mMainViewModel);
        initViews(mActivityMainBinding.categoryTabLayout, mActivityMainBinding.categoryViewPager);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark), false);
    }

    private void initViews(TabLayout tabLayout, ViewPager viewPager) {
        CategoryFragmentPagerAdapter adapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainViewModel.destroy();
    }
}
