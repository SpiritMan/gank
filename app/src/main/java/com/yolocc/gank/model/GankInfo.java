package com.yolocc.gank.model;

import java.util.Date;
import java.util.List;

/**
 */

public class GankInfo {


    /**
     * desc : Facebook 开源文本布局库。
     * publishedAt : 2016-12-19T11:57:16.232Z
     * url : https://github.com/facebookincubator/TextLayoutBuilder
     * who : Jason
     * images
     */

    private String desc;
    private Date publishedAt;
    private String url;
    private String who;
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
