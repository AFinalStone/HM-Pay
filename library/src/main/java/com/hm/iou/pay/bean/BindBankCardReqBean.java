package com.hm.iou.pay.bean;

/**
 * Created by hjy on 2018/7/19.
 */

public class BindBankCardReqBean {

    private String bankcard;
    private String mobile;
    private int source;

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}