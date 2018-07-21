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

    public HistoryListAdapter(Context context) {
        super(R.layout.pay_item_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, IHistoryItem item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setTextColor(R.id.tv_title, item.getTextColor());

        LinearLayout llChild = helper.getView(R.id.ll_childList);
        llChild.removeAllViews();
        List<IHistoryItemChild> list = item.getChild();
        for (IHistoryItemChild child : list) {
            HistoryItemChildHelper help = new HistoryItemChildHelper(llChild, child);
            llChild.addView(help.getChildView());
        }
    }

}
