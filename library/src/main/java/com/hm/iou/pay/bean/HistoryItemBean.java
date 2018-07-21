package com.hm.iou.pay.bean;

import com.hm.iou.pay.business.history.view.IHistoryItem;
import com.hm.iou.pay.business.history.view.IHistoryItemChild;
import com.hm.iou.pay.dict.OrderStatusEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class HistoryItemBean implements IHistoryItem {

    //绿色：0xFF417505
    //蓝色：0xFF4A90E2
    //灰色：0xFFBBBBBB
    //红色：0xFFF43048

    private String sortDate;
    private String title;
    private int showStatus;
    private List<HistoryItemChildBean> records;

    private int textColor;
    private boolean childListInit;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<IHistoryItemChild> getChild() {
        if (records == null) {
            return new ArrayList<>();
        }
        if (childListInit) {
            return (ArrayList) records;
        }
        if (showStatus == OrderStatusEnum.WAIT_PAY.getStatus() ||
                showStatus == OrderStatusEnum.PAYING.getStatus()) {
            for (HistoryItemChildBean item : records) {
                item.setTextColor(0xFF417505);
            }
        } else if (showStatus == OrderStatusEnum.PAID_NOT_USED.getStatus() ||
                showStatus == OrderStatusEnum.GIFT_NOT_USED.getStatus()) {
            for (HistoryItemChildBean item : records) {
                item.setTextColor(0xFF4A90E2);
            }
        } else {
            int i = 0;
            for (HistoryItemChildBean item : records) {
                item.setTextColor(0xFFBBBBBB);
                i++;
                if (showStatus == OrderStatusEnum.RETURN_MONEY.getStatus() && i == records.size()) {
                    item.setTextColor(0xFFF43048);
                }
            }
        }
        childListInit = true;
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

    @Override
    public int getTextColor() {
        if (textColor != 0) {
            return textColor;
        }
        if (showStatus == OrderStatusEnum.WAIT_PAY.getStatus() ||
                showStatus == OrderStatusEnum.PAYING.getStatus()) {
            textColor = 0xFF417505;
        } else if (showStatus == OrderStatusEnum.PAID_NOT_USED.getStatus() ||
                showStatus == OrderStatusEnum.GIFT_NOT_USED.getStatus()) {
            textColor = 0xFF4A90E2;
        } else {
            textColor = 0xFFBBBBBB;
        }
        return textColor;
    }

}