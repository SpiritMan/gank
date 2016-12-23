package com.yolocc.gank.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ActivityPictureBinding;
import com.yolocc.gank.viewModel.PictureViewModel;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends AppCompatActivity {

    public static final String DESC = "desc";
    public static final String IMAGE_URL = "image_url";
    private static final int STORAGE_PERMISSION_REQUEST = 0x1;

    private ActivityPictureBinding mActivityPictureBinding;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPictureBinding = DataBindingUtil.setContentView(this, R.layout.activity_picture);
        String desc = getIntent().getStringExtra(DESC);
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        PictureViewModel pictureViewModel = new PictureViewModel(this);
        pictureViewModel.mImageUrl = imageUrl;
        pictureViewModel.desc = desc;
        mActivityPictureBinding.setViewModel(pictureViewModel);
        initToolbar(mActivityPictureBinding.toolbar, desc);
        initPhoto(pictureViewModel);
    }

    private void initToolbar(Toolbar toolbar, String desc) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(desc);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initPhoto(final PictureViewModel pictureViewModel) {
        mAttacher = new PhotoViewAttacher(mActivityPictureBinding.pictureIv);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                pictureViewModel.onImageClick(view);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                //android 6.0后手动申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PackageManager.PERMISSION_DENIED == checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                } else {
                    mActivityPictureBinding.getViewModel().downloadImage();
                }
                return true;
            case R.id.action_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == STORAGE_PERMISSION_REQUEST) {
            mActivityPictureBinding.getViewModel().downloadImage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityPictureBinding.getViewModel().destroy();
        mAttacher.cleanup();
    }
}
