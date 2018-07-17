package com.hm.iou.pay.comm;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.pay.R;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListAdapter extends BaseQuickAdapter<ITimeCardItem, BaseViewHolder> {


    public TimeCardListAdapter() {
        super(R.layout.pay_item_time_card_recharge);
    }

    @Override
    protected void convert(BaseViewHolder helper, ITimeCardItem item) {
        helper.setText(R.id.tv_timeCardNum, item.getTimeCardNum());
        helper.setText(R.id.tv_discountsMoney, item.getDiscountsMoney());
    }


}
