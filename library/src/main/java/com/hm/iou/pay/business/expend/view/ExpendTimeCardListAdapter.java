package com.hm.iou.pay.business.expend.view;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.R;
import com.hm.iou.pay.comm.ITimeCardItem;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class ExpendTimeCardListAdapter extends BaseQuickAdapter<ITimeCardItem, BaseViewHolder> {

    private int mContentColorUnSelect;
    private int mMoneyColorUnSelect;
    private int mColorSelect;

    public ExpendTimeCardListAdapter(Context context) {
        super(R.layout.pay_item_expend_recharge);
        mContentColorUnSelect = context.getResources().getColor(R.color.uikit_text_auxiliary);
        mMoneyColorUnSelect = context.getResources().getColor(R.color.uikit_text_hint);
        mColorSelect = 0xfff8f8f9;
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
