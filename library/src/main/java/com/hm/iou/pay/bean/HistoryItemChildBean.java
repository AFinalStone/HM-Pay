package com.hm.iou.pay.bean;

import com.hm.iou.pay.business.history.view.IHistoryItemChild;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class HistoryItemChildBean implements IHistoryItemChild {

    private String time;
    private String type;

    public HistoryItemChildBean(String time, String type) {
        this.time = time;
        this.type = type;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public String getType() {
        return type;
    }
}
