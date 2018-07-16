package com.hm.iou.pay.bean;

import com.hm.iou.pay.comm.ITimeCardItem;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class TimeCardBean implements ITimeCardItem {

    private String desc1;
    private String desc2;

    public TimeCardBean(String desc1, String desc2) {
        this.desc1 = desc1;
        this.desc2 = desc2;
    }

    @Override
    public String getTimeCardNum() {
        return desc1;
    }

    @Override
    public String getDiscountsMoney() {
        return  desc2;
    }
}
