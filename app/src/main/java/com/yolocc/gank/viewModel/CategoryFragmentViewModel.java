package com.yolocc.gank.viewModel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.text.TextUtils;
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

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 */

public class CategoryFragmentViewModel implements ViewModel {

    public String mCategory;
    private Context mContext;
    private Subscription mSubscription;
    private int page = 1;
    public ObservableInt recyclerViewVisibility;

    public CategoryFragmentViewModel(String category, Context context) {
        mCategory = category;
        mContext = context;
        recyclerViewVisibility = new ObservableInt(View.VISIBLE);
        getCategoryData();
    }

    public void getCategoryData() {
        unSubscribe(mSubscription);
        mSubscription = GankService.defaultInstance().create(GankApi.class)
                .getCategoryGankData(mCategory, Constants.COUNT, page)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<GankWrap, Observable<List<DataGank>>>() {
                    @Override
                    public Observable<List<DataGank>> call(GankWrap gankWrap) {
                        if (gankWrap.results.size() > 0) {
                            String dateStr = null;
                            List<DataGank> mDataGanks = new ArrayList<>();
                            List<GankInfo> mGankInfo = new ArrayList<>();
                            for (GankInfo gankInfo : gankWrap.results) {
                                String publicDate = DateUtil.toDate(gankInfo.getPublishedAt());
                                if(TextUtils.equals(dateStr,publicDate)) {
                                    mGankInfo.add(gankInfo);
                                } else {
                                    if(!TextUtils.isEmpty(dateStr)) {
                                        DataGank dataGank = new DataGank();
                                        dataGank.setDate(publicDate);
                                        dataGank.setGankInfos(mGankInfo);
                                        mGankInfo.clear();
                                        mDataGanks.add(dataGank);
                                    }
                                    dateStr = publicDate;

                                }
                            }
                            return mDataGanks;
                        } else {
                            return null;
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
                        System.out.println("error++++++++++++");
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onNext(List<DataGank> dataGanks) {
                        for (DataGank dataGank : dataGanks) {
                            System.out.println("date:"+dataGank.getDate());
                        }
                    }
                });
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
}
