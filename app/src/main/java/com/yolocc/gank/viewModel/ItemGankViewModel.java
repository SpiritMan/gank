package com.yolocc.gank.viewModel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yolocc.gank.R;
import com.yolocc.gank.model.DataGank;

/**
 */

public class ItemGankViewModel extends BaseObservable{

    private static final String TAG = "ItemGankViewModel";

    private DataGank mDataGank;

    public ItemGankViewModel(DataGank dataGank) {
        mDataGank = dataGank;
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

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Log.i(TAG,imageUrl+"?imageView2/0/w/540");
        Glide.with(view.getContext())
                .load(imageUrl)
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(view);
    }
}
