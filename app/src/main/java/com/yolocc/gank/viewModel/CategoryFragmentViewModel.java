package com.yolocc.gank.viewModel;

import android.content.Context;

/**
 */

public class CategoryFragmentViewModel implements ViewModel {

    public String mCategory;
    private Context mContext;

    public CategoryFragmentViewModel(String category, Context context) {
        mCategory = category;
        mContext = context;
    }

    @Override
    public void destroy() {
        mContext = null;
    }
}
