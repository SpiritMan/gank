package com.yolocc.gank.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yolocc.gank.view.CategoryFragment;

/**
 */

public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] category = new String[]{"福利","Android","iOS","休息视频","拓展资源","前端"};

    public CategoryFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(category[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return category[position];
    }

    @Override
    public int getCount() {
        return category.length;
    }
}
