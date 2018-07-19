package com.hm.iou.pay.bean;

import com.hm.iou.pay.business.history.view.IHistoryItem;
import com.hm.iou.pay.business.history.view.IHistoryItemChild;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class HistoryItemBean implements IHistoryItem {


    /**
     * records : [{"showDate":"2018-07-19T02:12:47.525Z","showDateStr":"string","showState":"string"}]
     * sortDate : 2018-07-19T02:12:47.525Z
     * title : string
     */

    private String sortDate;
    private String title;
    private List<HistoryItemChildBean> records;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<IHistoryItemChild> getChild() {
        return (ArrayList) records;
    }

    public String getSortDate() {
        return sortDate;
    }

    public void setSortDate(String sortDate) {
        this.sortDate = sortDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
