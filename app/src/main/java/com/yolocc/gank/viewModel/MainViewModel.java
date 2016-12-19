package com.yolocc.gank.viewModel;

import android.content.Context;

/**
 */

public class MainViewModel implements ViewModel {

    private Context mContext;

    public MainViewModel(Context context) {
        mContext = context;
    }

    @Override
    public void destroy() {
        mContext = null;
    }
}
