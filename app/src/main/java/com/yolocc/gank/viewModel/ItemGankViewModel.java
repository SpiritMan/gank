package com.yolocc.gank.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yolocc.gank.R;
import com.yolocc.gank.model.DataGank;
import com.yolocc.gank.view.PictureActivity;

/**
 */

public class ItemGankViewModel extends BaseObservable{

    private static final String TAG = "ItemGankViewModel";

    private DataGank mDataGank;

    private Context mContext;

    public ItemGankViewModel(DataGank dataGank,Context context) {
        mDataGank = dataGank;
        this.mContext = context;
    }

    public String getDate() {
        return  mDataGank.getDate();
    }

    public void setDataGank(DataGank dataGank) {
        mDataGank = dataGank;
        notifyChange();
    }

    public String getFirstImageUrl() {
        return mDataGank.getGankInfos().get(0).getUrl();
    }

    public void onItemClick(View view) {
        Intent intent = new Intent(mContext, PictureActivity.class);
        intent.putExtra(PictureActivity.DESC,mDataGank.getGankInfos().get(0).getDesc());
        intent.putExtra(PictureActivity.IMAGE_URL,mDataGank.getGankInfos().get(0).getUrl());
        mContext.startActivity(intent);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(view);
    }
}
