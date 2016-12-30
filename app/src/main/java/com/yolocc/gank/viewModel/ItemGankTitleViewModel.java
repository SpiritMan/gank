package com.yolocc.gank.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.yolocc.gank.model.GankInfo;
import com.yolocc.gank.view.WebActivity;

/**
 */

public class ItemGankTitleViewModel extends BaseObservable {

    private static final String TAG = "ItemGankTitleViewModel";

    private GankInfo mGankInfo;

    private Context mContext;

    public ItemGankTitleViewModel(Context context, GankInfo gankInfo) {
        mContext = context;
        mGankInfo = gankInfo;
    }

    public String getTitle() {
//        SpannableStringBuilder builder = new SpannableStringBuilder(mGankInfo.getDesc()).append(
//                StringStyles.format(mContext, " (by." +
//                        mGankInfo.getWho()+
//                        ")", R.style.ViaTextAppearance));
//        CharSequence gankText = builder.subSequence(0, builder.length());
        StringBuffer stringBuffer = new StringBuffer("· ").append(mGankInfo.getDesc()).append(" (by.").append(mGankInfo.getWho()).append(")");
        return stringBuffer.toString();
    }

    public void onItemClick(View view) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(WebActivity.URL, mGankInfo.getUrl());
        intent.putExtra(WebActivity.DESC, mGankInfo.getDesc());
        if (mGankInfo.getImages() != null && mGankInfo.getImages().size() > 0) {
            intent.putExtra(WebActivity.IMAGE, mGankInfo.getImages().get(0));
        }
        mContext.startActivity(intent);
    }


    public void setGankInfo(GankInfo gankInfo) {
        mGankInfo = gankInfo;
        notifyChange();
    }
}
