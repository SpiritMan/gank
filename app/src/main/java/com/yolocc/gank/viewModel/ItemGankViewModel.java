package com.yolocc.gank.viewModel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yolocc.gank.R;
import com.yolocc.gank.model.DataGank;

/**
 */

public class ItemGankViewModel extends BaseObservable{

    public DataGank mDataGank;

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
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(view);
    }
}
