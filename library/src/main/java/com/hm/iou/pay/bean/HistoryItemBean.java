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

    private String title;
    private List<HistoryItemChildBean> list;

    public HistoryItemBean(String title, List<HistoryItemChildBean> list) {
        this.title = title;
        this.list = list;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<IHistoryItemChild> getChild() {
        return (ArrayList) list;
    }
}
