package com.hm.iou.pay.business.history.view;

import java.util.List;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface IHistoryItem {

    /**
     * 获取当前充值条目的描述
     *
     * @return
     */
    String getTitle();

    /**
     * 获取当前充值条目的消费历史记录
     *
     * @return
     */
    List<IHistoryItemChild> getChild();


}
