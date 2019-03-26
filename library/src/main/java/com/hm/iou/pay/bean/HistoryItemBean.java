package com.hm.iou.pay.bean;

import com.hm.iou.pay.Constants;
import com.hm.iou.pay.business.history.view.IHistoryItem;
import com.hm.iou.pay.business.history.view.IHistoryItemChild;
import com.hm.iou.pay.dict.OrderStatusEnum;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
@Data
public class HistoryItemBean implements IHistoryItem {

    private String title;//名称
    private int showStatus;//状态
    private List<HistoryItemChildBean> records;//记录

    private int textColor;
    private boolean childListInit;

    @Override
    public String getITitle() {
        return title;
    }

    @Override
    public List<IHistoryItemChild> getIChild() {
        if (records == null) {
            return new ArrayList<>();
        }
        if (childListInit) {
            return (ArrayList) records;
        }
        //待支付,支付中
        if (showStatus == OrderStatusEnum.WAIT_PAY.getStatus() || showStatus == OrderStatusEnum.PAYING.getStatus()) {
            for (HistoryItemChildBean item : records) {
                item.setTimeTextColor(Constants.colorBlack);
            }
            //已支付（未使用完)，已赠送（未使用完）
        } else if (showStatus == OrderStatusEnum.PAID_NOT_USED.getStatus() ||
                showStatus == OrderStatusEnum.GIFT_NOT_USED.getStatus()) {
            for (HistoryItemChildBean item : records) {
                if (item.getRecordStatus() == 2) {//被冻结
                    item.setTimeTextColor(Constants.colorBlack);
                } else {
                    item.setTimeTextColor(Constants.colorGray);
                }
            }
        } else {
            int i = 0;
            for (HistoryItemChildBean item : records) {
                item.setTimeTextColor(Constants.colorGray);
                i++;
                if (showStatus == OrderStatusEnum.RETURN_MONEY.getStatus() && i == records.size()) {
                    item.setTimeTextColor(Constants.colorRed);
                }
            }
        }
        childListInit = true;
        return (ArrayList) records;
    }

    @Override
    public int getITitleTextColor() {
        if (textColor != 0) {
            return textColor;
        }
        if (showStatus == OrderStatusEnum.WAIT_PAY.getStatus() ||
                showStatus == OrderStatusEnum.PAYING.getStatus()) {
            textColor = Constants.colorBlack;
        } else if (showStatus == OrderStatusEnum.PAID_NOT_USED.getStatus() ||
                showStatus == OrderStatusEnum.GIFT_NOT_USED.getStatus()) {
            textColor = Constants.colorBlack;
        } else {
            textColor = Constants.colorGray;
        }
        return textColor;
    }

}