package com.yolocc.gank.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.yolocc.gank.Constants;
import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ActivityPictureBinding;
import com.yolocc.gank.viewModel.PictureViewModel;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.yolocc.gank.view.ShareBottomDialogFragment.OnShareClickListener;

public class PictureActivity extends AppCompatActivity {

    public static final String DESC = "desc";
    public static final String IMAGE_URL = "image_url";
    private static final int STORAGE_PERMISSION_REQUEST = 0x1;

    private ActivityPictureBinding mActivityPictureBinding;
    private PhotoViewAttacher attacher;
    private IWXAPI iwxapi;

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
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
    }

    private void initToolbar(Toolbar toolbar, String desc) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(desc);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initPhoto(final PictureViewModel pictureViewModel) {
        attacher = new PhotoViewAttacher(mActivityPictureBinding.pictureIv);
        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void save(View view) {
        //android 6.0后手动申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PackageManager.PERMISSION_DENIED == checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
        } else {
            mActivityPictureBinding.getViewModel().downloadImage();
        }
    }

    public void share(View view) {
        final ShareBottomDialogFragment shareBottomDialogFragment = new ShareBottomDialogFragment();
        shareBottomDialogFragment.show(getFragmentManager(), "shareFragment");
        shareBottomDialogFragment.setOnShareClickListener(new OnShareClickListener() {
            @Override
            public void onTargetChose(int target) {
                switch (target) {
                    case ShareBottomDialogFragment.WECHAT_FRIEND_TARGET:
                        shareWechat(mActivityPictureBinding.getViewModel().mImageUrl, 1);
                        break;
                    case ShareBottomDialogFragment.WECHAT_MOMENT_TARGET:
                        shareWechat(mActivityPictureBinding.getViewModel().mImageUrl, 2);
                        break;
                }
            }
        });
    }

    /**
     * 分享图片到微信
     *
     * @param imageUrl 图片的url
     * @param type     1表示分享给微信朋友，2表示分享到微信朋友圈
     */
    private void shareWechat(String imageUrl, final int type) {
        Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        WXImageObject wxImageObject = new WXImageObject(resource);
                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = wxImageObject;
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("image");
                        req.message = msg;
                        req.scene = type == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                        iwxapi.sendReq(req);
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == STORAGE_PERMISSION_REQUEST) {
            mActivityPictureBinding.getViewModel().downloadImage();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityPictureBinding.getViewModel().destroy();
        attacher.cleanup();
    }
}
