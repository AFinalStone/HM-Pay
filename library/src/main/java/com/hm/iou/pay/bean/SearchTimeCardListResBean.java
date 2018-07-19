package com.hm.iou.pay.bean;

import java.util.List;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class SearchTimeCardListResBean {


    /**
     * countSign : 0
     * firstPackage : {"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0}
     * packageRespList : [{"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0}]
     * signUnitPrice : 0
     */

    private int countSign;
    private TimeCardBean firstPackage;
    private int signUnitPrice;
    private List<TimeCardBean> packageRespList;

    public int getCountSign() {
        return countSign;
    }

    public void setCountSign(int countSign) {
        this.countSign = countSign;
    }

    public TimeCardBean getFirstPackage() {
        return firstPackage;
    }

    public void setFirstPackage(TimeCardBean firstPackage) {
        this.firstPackage = firstPackage;
    }

    public int getSignUnitPrice() {
        return signUnitPrice;
    }

    public void setSignUnitPrice(int signUnitPrice) {
        this.signUnitPrice = signUnitPrice;
    }

    public List<TimeCardBean> getPackageRespList() {
        return packageRespList;
    }

    public void setPackageRespList(List<TimeCardBean> packageRespList) {
        this.packageRespList = packageRespList;
    }

}
