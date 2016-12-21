package com.yolocc.gank.viewModel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yolocc.gank.R;

/**
 */

public class PictureViewModel {

    public String mImageUrl;

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .into(view);
    }
}
