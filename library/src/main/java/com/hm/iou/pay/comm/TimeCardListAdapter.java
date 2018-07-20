package com.hm.iou.pay.comm;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.R;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListAdapter extends BaseQuickAdapter<ITimeCardItem, BaseViewHolder> {

    private int mContentColorUnSelect;
    private int mMoneyColorUnSelect;
    private int mColorSelect;

    public TimeCardListAdapter(Context context) {
        super(R.layout.pay_item_time_card_recharge);
        mContentColorUnSelect = context.getResources().getColor(R.color.uikit_text_common_color);
        mMoneyColorUnSelect = context.getResources().getColor(R.color.uikit_text_hint_common_color);
        mColorSelect = context.getResources().getColor(R.color.uikit_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, ITimeCardItem item) {
        helper.setText(R.id.tv_timeCardNum, item.getTimeCardContent());
        helper.setText(R.id.tv_discountsMoney, item.getTimeCardDiscounts());
        //这里因为添加了请求头，所以不需要减一
        int lastPosition = getData().size();
        int layoutPosition = helper.getLayoutPosition();
        int adapterPosition = helper.getLayoutPosition();
        Logger.d("layoutPosition" + lastPosition + "---adapterPosition" + adapterPosition);
        if (lastPosition == helper.getAdapterPosition()) {
            helper.setBackgroundRes(R.id.ll_background, R.drawable.pay_selector_bg_item_time_card_recharge_select);
            helper.setTextColor(R.id.tv_timeCardNum, mColorSelect);
            helper.setTextColor(R.id.tv_discountsMoney, mColorSelect);
        } else {
            helper.setTextColor(R.id.tv_timeCardNum, mContentColorUnSelect);
            helper.setTextColor(R.id.tv_discountsMoney, mMoneyColorUnSelect);
            helper.setBackgroundRes(R.id.ll_background, R.drawable.pay_selector_bg_item_time_card_recharge_unselect);
        }
    }


}
