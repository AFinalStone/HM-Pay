package com.hm.iou.pay.business.locksign.view;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.pay.R;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class LockSignListAdapter extends BaseQuickAdapter<ILockSignItem, BaseViewHolder> {

    public LockSignListAdapter(Context context) {
        super(R.layout.pay_item_lock_sign_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ILockSignItem item) {
        helper.setText(R.id.tv_name_value, item.getITitle());
        helper.setText(R.id.tv_contract_id_value, item.getIContractId());
        helper.setText(R.id.tv_start_time_value, item.getIStartTime());
        helper.setText(R.id.tv_end_time_value, item.getIEndTime());
        helper.setText(R.id.tv_lock_sign_num_value, item.getILockSignNum());
        helper.setText(R.id.tv_status_desc, item.getIStatusDesc());
        helper.setTextColor(R.id.tv_status_desc, item.getIStatusDescTextColor());
    }

}
