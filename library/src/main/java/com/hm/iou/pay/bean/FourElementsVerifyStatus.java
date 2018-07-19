package com.hm.iou.pay.bean;

/**
 * Created by hjy on 2018/7/19.
 */

public class FourElementsVerifyStatus {

    private int retryTimes;     //未认证：当日剩余次数；已认证，当日剩余次数0
    private int status;         //1:已认证，0:未认证

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}