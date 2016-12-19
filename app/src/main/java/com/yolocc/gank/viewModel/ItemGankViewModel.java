package com.yolocc.gank.viewModel;

import android.databinding.BaseObservable;

import com.yolocc.gank.model.DataGank;

/**
 */

public class ItemGankViewModel extends BaseObservable{

    private DataGank mDataGank;

    public ItemGankViewModel(DataGank dataGank) {
        mDataGank = dataGank;
    }

    public String getDate() {
        return  mDataGank.getDate();
    }

    public void setDataGank(DataGank dataGank) {
        mDataGank = dataGank;
        notifyChange();
    }
}
