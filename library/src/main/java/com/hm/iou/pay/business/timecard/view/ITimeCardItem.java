package com.hm.iou.pay.business.timecard.view;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface ITimeCardItem {

    /**
     * 获取充值次数
     *
     * @return
     */
    String getTimeCardNum();

    /**
     * 获取优惠金额
     *
     * @return
     */
    String getDiscountsMoney();

}
