package com.yolocc.gank.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Toast;

import com.yolocc.gank.R;
import com.yolocc.gank.model.GankInfo;
import com.yolocc.gank.utils.StringStyles;

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
        SpannableStringBuilder builder = new SpannableStringBuilder(mGankInfo.getDesc()).append(
                StringStyles.format(mContext, " (by." +
                        mGankInfo.getWho()+
                        ")", R.style.ViaTextAppearance));
        CharSequence gankText = builder.subSequence(0, builder.length());
        return gankText.toString();
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
