package com.hm.iou.pay.business.history.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.pay.R;

import java.util.List;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class HistoryListAdapter extends BaseQuickAdapter<IHistoryItem, BaseViewHolder> {

    private int mColorBlue;
    private int mColorGreen;
    private int mColorRed;

    public HistoryListAdapter(Context context) {
        super(R.layout.pay_item_history);
        mColorBlue = context.getResources().getColor(R.color.uikit_blue);
        mColorGreen = context.getResources().getColor(R.color.uikit_green);
        mColorRed = context.getResources().getColor(R.color.uikit_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, IHistoryItem item) {
        helper.setText(R.id.tv_title, item.getTitle());
        LinearLayout llChild = helper.getView(R.id.ll_childList);
        List<IHistoryItemChild> list = item.getChild();
        for (IHistoryItemChild child : list) {
            HistoryItemChildHelp help = new HistoryItemChildHelp(llChild, child);
            llChild.addView(help.getChildView());
        }
    }


}
