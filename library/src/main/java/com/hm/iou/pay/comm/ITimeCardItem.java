package com.hm.iou.pay.comm;

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
    String getTimeCardDiscounts();

    /**
     * 获取支付金额
     *
     * @return
     */
    long getTimeCardPayMoney();

    /**
     * 获取套餐id
     *
     * @return
     */
    String getTimeCardPackageId();


}
