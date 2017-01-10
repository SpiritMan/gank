package com.yolocc.gank.view;

import android.Manifest;
import android.content.Intent;
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
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yolocc.gank.Constants;
import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ActivityPictureBinding;
import com.yolocc.gank.viewModel.PictureViewModel;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.yolocc.gank.view.ShareBottomDialogFragment.OnShareClickListener;

public class PictureActivity extends AppCompatActivity implements PictureViewModel.DataListener {

    public static final String DESC = "desc";
    public static final String IMAGE_URL = "image_url";
    private static final int STORAGE_PERMISSION_REQUEST = 0x1;

    private ActivityPictureBinding mActivityPictureBinding;
    private PhotoViewAttacher attacher;
    private IWXAPI iwxapi;
    private Tencent mTencent;
    private int mExtarFlag = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPictureBinding = DataBindingUtil.setContentView(this, R.layout.activity_picture);
        String desc = getIntent().getStringExtra(DESC);
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        PictureViewModel pictureViewModel = new PictureViewModel(this, this);
        pictureViewModel.mImageUrl = imageUrl;
        pictureViewModel.desc = desc;
        mActivityPictureBinding.setViewModel(pictureViewModel);
        initToolbar(mActivityPictureBinding.toolbar, desc);
        initPhoto(pictureViewModel);
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, true);
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
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
                pictureViewModel.onImageClick();
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
        mActivityPictureBinding.getViewModel().isShare = false;
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
                    case ShareBottomDialogFragment.QQ_FRIEND_TARGET:
                        mActivityPictureBinding.getViewModel().isShare = true;
                        mActivityPictureBinding.getViewModel().type = 1;
                        //android 6.0后手动申请权限
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PackageManager.PERMISSION_DENIED == checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                        } else {
                            mActivityPictureBinding.getViewModel().downloadImage();
                        }
                        break;
                    case ShareBottomDialogFragment.QQ_ZONE_TARGET:
                        mActivityPictureBinding.getViewModel().isShare = true;
                        mActivityPictureBinding.getViewModel().type = 2;
                        //android 6.0后手动申请权限
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PackageManager.PERMISSION_DENIED == checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                        } else {
                            mActivityPictureBinding.getViewModel().downloadImage();
                        }
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


    /**
     * 分享图片给qq
     *
     * @param type     1代表分享给朋友，2代表分享到QQ空间
     * @param imageUrl 图片本地路劲
     */
    public void sharePictureQq(int type, String imageUrl) {
        final Bundle params = new Bundle();
        int shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE;
        if (type == 1) {
            mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        } else {
            mExtarFlag |= QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
        }
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "Gank");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);

        mTencent.shareToQQ(this, params, iUiListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == STORAGE_PERMISSION_REQUEST) {
            mActivityPictureBinding.getViewModel().downloadImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult:" + requestCode + "resultCode:" + resultCode);
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);
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

    @Override
    public void savePictureSuccess(String path, int type) {
        sharePictureQq(type, path);
    }

    IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            System.out.println("onComplete");
        }

        @Override
        public void onError(UiError uiError) {
            System.out.println("onError");
        }

        @Override
        public void onCancel() {
            System.out.println("onCancel");
        }
    };
}
