package com.yolocc.gank.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Toast;

import com.yolocc.gank.model.GankInfo;

/**
 */

public class ItemGankTitleViewModel extends BaseObservable{

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
//        Intent intent = new Intent(mContext,);
//        mContext.startActivity(intent);
        Toast.makeText(mContext, mGankInfo.getDesc(), Toast.LENGTH_SHORT).show();
    }


    public void setGankInfo(GankInfo gankInfo) {
        mGankInfo = gankInfo;
        notifyChange();
    }
}
