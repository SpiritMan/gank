package com.yolocc.gank.viewModel;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yolocc.gank.Constants;

/**
 */

public class MainViewModel implements ViewModel {

    private Context mContext;

    public MainViewModel(Context context) {
        mContext = context;
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID,true);
        iwxapi.registerApp(Constants.APP_ID);
    }

    @Override
    public void destroy() {
        mContext = null;
    }
}
