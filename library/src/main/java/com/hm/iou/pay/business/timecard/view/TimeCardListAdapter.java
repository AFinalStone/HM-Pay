package com.hm.iou.pay.business.timecard.view;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.pay.R;
import com.hm.iou.pay.comm.ITimeCardItem;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListAdapter extends BaseQuickAdapter<ITimeCardItem, BaseViewHolder> {


    public TimeCardListAdapter(Context context) {
        super(R.layout.pay_item_time_card_recharge);
    }

    @Override
    protected void convert(BaseViewHolder helper, ITimeCardItem item) {
        helper.setText(R.id.tv_package_name, item.getTimeCardContent());
        helper.setText(R.id.tv_package_desc, item.getTimeCardDiscounts());
    }


}
