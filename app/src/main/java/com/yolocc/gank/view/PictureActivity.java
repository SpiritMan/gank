package com.yolocc.gank.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ActivityPictureBinding;
import com.yolocc.gank.viewModel.PictureViewModel;

public class PictureActivity extends AppCompatActivity {

    public static final String DESC = "desc";
    public static final String IMAGE_URL = "image_url";

    private ActivityPictureBinding mActivityPictureBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPictureBinding = DataBindingUtil.setContentView(this, R.layout.activity_picture);
        String desc = getIntent().getStringExtra(DESC);
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        PictureViewModel pictureViewModel = new PictureViewModel();
        pictureViewModel.mImageUrl = imageUrl;
        mActivityPictureBinding.setViewModel(pictureViewModel);
        initToolbar(mActivityPictureBinding.toolbar, desc);
    }

    private void initToolbar(Toolbar toolbar, String desc) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(desc);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
