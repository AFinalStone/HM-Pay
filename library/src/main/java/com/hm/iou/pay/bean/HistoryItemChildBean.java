package com.hm.iou.pay.bean;

import com.hm.iou.pay.business.history.view.IHistoryItemChild;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class HistoryItemChildBean implements IHistoryItemChild {

    /**
     * showDate : 2018-07-19T02:12:47.525Z
     * showDateStr : string
     * showState : string
     */

    private String showDate;
    private String showDateStr;
    private String showState;

    private int textColor;

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public void setShowDateStr(String showDateStr) {
        this.showDateStr = showDateStr;
    }

    public void setShowState(String showState) {
        this.showState = showState;
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    @Override
    public String getTime() {
        return showDateStr;
    }

    @Override
    public String getType() {
        return showState;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }
}
