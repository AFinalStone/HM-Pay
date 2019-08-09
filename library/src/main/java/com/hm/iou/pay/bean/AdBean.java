package com.hm.iou.pay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjy on 2018/7/21.
 */

public class AdBean implements Parcelable {

    String url;
    String linkUrl;
    String beginTime;
    String endTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.linkUrl);
        dest.writeString(this.beginTime);
        dest.writeString(this.endTime);
    }

    public AdBean() {
    }

    protected AdBean(Parcel in) {
        this.url = in.readString();
        this.linkUrl = in.readString();
        this.beginTime = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<AdBean> CREATOR = new Parcelable.Creator<AdBean>() {
        @Override
        public AdBean createFromParcel(Parcel source) {
            return new AdBean(source);
        }

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }
    };
}
