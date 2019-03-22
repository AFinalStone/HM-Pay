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


    /**
     * 获取时间文本字体的颜色
     *
     * @return
     */
    int getTimeTextColor();

    /**
     * 获取类型文本字体的颜色
     *
     * @return
     */
    int getTypeTextColor();
}
