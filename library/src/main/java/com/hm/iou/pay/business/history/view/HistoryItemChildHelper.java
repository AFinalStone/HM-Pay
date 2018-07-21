package com.hm.iou.pay.business.history.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hm.iou.pay.R;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class HistoryItemChildHelper {

    private TextView mTvTime;
    private TextView mTvType;
    private View mChildView;

    public HistoryItemChildHelper(ViewGroup parentView, IHistoryItemChild itemChild) {
        Context context = parentView.getContext();
        mChildView = LayoutInflater.from(context).inflate(R.layout.pay_item_history_child, parentView, false);
        mTvTime = mChildView.findViewById(R.id.tv_time);
        mTvType = mChildView.findViewById(R.id.tv_type);
        mTvTime.setText(itemChild.getTime());
        mTvType.setText(itemChild.getType());

        mTvTime.setTextColor(itemChild.getTextColor());
        mTvType.setTextColor(itemChild.getTextColor());
    }

    public View getChildView() {
        return mChildView;
    }

}