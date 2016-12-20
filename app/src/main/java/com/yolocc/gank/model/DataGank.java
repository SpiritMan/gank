package com.yolocc.gank.model;

import java.util.List;

/**
 */

public class DataGank {

    private String date;
    private List<GankInfo> mGankInfos;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<GankInfo> getGankInfos() {
        return mGankInfos;
    }

    public void setGankInfos(List<GankInfo> gankInfos) {
        this.mGankInfos = gankInfos;
    }
}
