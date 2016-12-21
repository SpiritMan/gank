package com.yolocc.gank.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yolocc.gank.R;
import com.yolocc.gank.model.DataGank;

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
//        Intent intent = new Intent(mContext,);
//        mContext.startActivity(intent);
        Snackbar.make(view,"jump",Snackbar.LENGTH_SHORT).show();
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
