package com.hm.iou.pay.business.history.view;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface IHistoryItemChild {

    /**
     * 时间
     *
     * @return
     */
    String getTime();

    /**
     * 类型
     *
     * @return
     */
    String getType();


    int getTextColor();

}
