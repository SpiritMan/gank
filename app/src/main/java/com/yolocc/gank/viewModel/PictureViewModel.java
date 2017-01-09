package com.yolocc.gank.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yolocc.gank.R;
import com.yolocc.gank.model.GankApi;
import com.yolocc.gank.model.GankService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 */

public class PictureViewModel implements ViewModel {

    public String mImageUrl;

    public String desc;

    public int type;

    public boolean isShare;

    public ObservableInt mToolbarVisibility;

    private Subscription mSubscription;

    private Context mContext;

    private DataListener mDataListener;

    public PictureViewModel(Context context, DataListener dataListener) {
        mToolbarVisibility = new ObservableInt(View.VISIBLE);
        this.mContext = context;
        this.mDataListener = dataListener;
    }

    public void onImageClick() {
        if (mToolbarVisibility.get() == View.VISIBLE) {
            mToolbarVisibility.set(View.GONE);
        } else {
            mToolbarVisibility.set(View.VISIBLE);
        }
    }

    public void downloadImage() {
        unSubscribe(mSubscription);
        mSubscription = GankService.defaultInstance().create(GankApi.class)
                .downloadPic(mImageUrl)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Uri>() {
                    @Override
                    public Uri call(ResponseBody responseBody) {
                        final Bitmap bitmap =  BitmapFactory.decodeStream(responseBody.byteStream());
                        File appDir = new File(Environment.getExternalStorageDirectory(),"GankImage");
                        if(!appDir.exists()) {
                            appDir.mkdir();
                        }
                        String imageName = desc + ".jpg";
                        File file = new File(appDir,imageName);
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            assert bitmap != null;
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri uri = Uri.fromFile(file);
                        return uri;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "无法下载图片", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        // 通知图库更新
                        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                        mContext.sendBroadcast(scannerIntent);
                        File appDir = new File(Environment.getExternalStorageDirectory(), "GankImage");
                        String msg = "图片以保存到" + appDir.getAbsolutePath();
                        System.out.println("path:"+uri.getPath());
                        if(isShare) {
                            mDataListener.savePictureSuccess(uri.getPath(),type);
                        } else {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @BindingAdapter({"pictureImageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(view);
    }

    @Override
    public void destroy() {
        unSubscribe(mSubscription);
        mSubscription = null;
        mContext = null;
    }

    private void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public interface DataListener {
        void savePictureSuccess(String path,int type);
    }
}
