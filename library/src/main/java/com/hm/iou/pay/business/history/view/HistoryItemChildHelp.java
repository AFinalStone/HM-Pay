package com.hm.iou.pay.business.history.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.pay.R;
import com.hm.iou.router.Router;
import com.hm.iou.tools.ImageLoader;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class HistoryItemChildHelp {

    TextView mTvTime;
    TextView mTvType;
    private View mChildView;

    public HistoryItemChildHelp(ViewGroup parentView, IHistoryItemChild itemChild) {
        Context context = parentView.getContext();
        mChildView = LayoutInflater.from(context).inflate(R.layout.pay_item_history_child, parentView, false);
        mTvTime = mChildView.findViewById(R.id.tv_time);
        mTvType = mChildView.findViewById(R.id.tv_type);
        mTvTime.setText(itemChild.getTime());
        mTvType.setText(itemChild.getType());
    }

    public View getChildView() {
        return mChildView;
    }

}
