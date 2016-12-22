package com.yolocc.gank.viewModel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yolocc.gank.R;

/**
 */

public class PictureViewModel {

    public String mImageUrl;

    public ObservableInt mToolbarVisibility;

    public PictureViewModel() {
        mToolbarVisibility = new ObservableInt(View.VISIBLE);
    }

    public void onImageClick(View view) {
        if (mToolbarVisibility.get() == View.VISIBLE) {
            mToolbarVisibility.set(View.GONE);
        } else {
            mToolbarVisibility.set(View.VISIBLE);
        }
    }

    @BindingAdapter({"pictureImageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(view);
    }
}
