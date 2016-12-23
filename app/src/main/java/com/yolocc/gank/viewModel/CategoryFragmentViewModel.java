package com.yolocc.gank.viewModel;

import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.yolocc.gank.Constants;
import com.yolocc.gank.model.DataGank;
import com.yolocc.gank.model.GankApi;
import com.yolocc.gank.model.GankInfo;
import com.yolocc.gank.model.GankService;
import com.yolocc.gank.model.GankWrap;
import com.yolocc.gank.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 */

public class CategoryFragmentViewModel implements ViewModel {

    private static final String TAG = "CategoryFragmentViewModel";

    public String mCategory;
    private Subscription mSubscription;
    private DataListener mDataListener;
    public ObservableInt recyclerViewVisibility;

    public CategoryFragmentViewModel(String category, DataListener dataListener) {
        mCategory = category;
        this.mDataListener = dataListener;
        recyclerViewVisibility = new ObservableInt(View.VISIBLE);
        getCategoryData(1);
    }

    public void getCategoryData(final int page) {
        unSubscribe(mSubscription);
        mSubscription = GankService.defaultInstance().create(GankApi.class)
                .getCategoryGankData(mCategory, Constants.COUNT, page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<GankWrap, List<DataGank>>() {
                    @Override
                    public List<DataGank> call(GankWrap gankWrap) {
                        List<DataGank> mDataGanks = new ArrayList<>();
                        if (gankWrap.results.size() > 0) {
                            String dateStr = null;
                            List<GankInfo> mGankInfo = new ArrayList<>();
                            List<GankInfo> mResultGanks = gankWrap.results;
                            for (int i = 0; i < mResultGanks.size(); i++) {
                                String publicDate = DateUtil.toDate(mResultGanks.get(i).getPublishedAt());
                                String nextPublicDate;
                                if (i < (mResultGanks.size() - 1)) {
                                    nextPublicDate = DateUtil.toDate(mResultGanks.get(i + 1).getPublishedAt());
                                } else {
                                    nextPublicDate = DateUtil.toDate(mResultGanks.get(i - 1).getPublishedAt());
                                }
                                dateStr = publicDate;
                                mGankInfo.add(mResultGanks.get(i));
                                if (!TextUtils.equals(publicDate, nextPublicDate)) {
                                    DataGank dataGank = new DataGank();
                                    dataGank.setDate(dateStr);
                                    List<GankInfo> temp = new ArrayList<>();
                                    temp.addAll(mGankInfo);
                                    dataGank.setGankInfos(temp);
                                    mGankInfo.clear();
                                    mDataGanks.add(dataGank);
                                }
                            }
                            if (mGankInfo.size() > 0) {
                                DataGank dataGank = new DataGank();
                                dataGank.setDate(dateStr);
                                List<GankInfo> temp = new ArrayList<>();
                                temp.addAll(mGankInfo);
                                dataGank.setGankInfos(temp);
                                mGankInfo.clear();
                                mDataGanks.add(dataGank);
                            }
                            return mDataGanks;
                        } else {
                            return mDataGanks;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DataGank>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        if (mDataListener != null) {
                            mDataListener.onGankDateError();
                        }
                    }

                    @Override
                    public void onNext(List<DataGank> dataGanks) {
                        boolean isRefresh = false;
                        if (page == 1) {
                            isRefresh = true;
                        }
                        for (DataGank dataGank : dataGanks) {
                            System.out.println(dataGank.getDate());
                            System.out.println(dataGank.getGankInfos().get(0).getUrl());
                        }
                        mDataListener.onGankDataChanged(dataGanks, isRefresh);
                    }
                });
    }

    @Override
    public void destroy() {
        unSubscribe(mSubscription);
        mSubscription = null;
    }

    private void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public interface DataListener {
        void onGankDataChanged(List<DataGank> dataGanks, boolean isRefresh);

        void onGankDateError();
    }
}
