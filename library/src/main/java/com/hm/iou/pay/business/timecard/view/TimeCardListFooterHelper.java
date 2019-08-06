package com.hm.iou.pay.business.timecard.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.NavigationHelper;
import com.hm.iou.pay.R;
import com.hm.iou.pay.bean.VipCardPackageBean;
import com.hm.iou.pay.bean.VipCardUseBean;
import com.hm.iou.pay.bean.VipCardUseInfo;
import com.hm.iou.pay.comm.CommUtilKt;
import com.hm.iou.tools.DensityUtil;

import java.util.List;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListFooterHelper {

    private Context mContext;
    private View mFooterView;

    private LinearLayout mLlVipCardPackage;

    public TimeCardListFooterHelper(Context context) {
        mContext = context;
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_time_card_recharge_footer, null, false);
        mLlVipCardPackage = mFooterView.findViewById(R.id.ll_vip_card_package_content);
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 显示VIP贵宾卡套餐信息
     *
     * @param list
     */
    public void showVipCardPackage(List<IVipCardItem> list) {
        mLlVipCardPackage.removeAllViews();
        if (list == null || list.isEmpty())
            return;
        for (int i = 0; i < 3 && i < list.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.pay_item_vip_card_package, null);
            TextView tvName = view.findViewById(R.id.tv_card_name);
            TextView tvMoney = view.findViewById(R.id.tv_card_money);
            TextView tvDesc = view.findViewById(R.id.tv_card_desc);
            TextView tvTag = view.findViewById(R.id.tv_card_tag);

            IVipCardItem item = list.get(i);
            tvName.setText(item.getName());
            tvMoney.setText(item.getAmountPerOnce());
            tvDesc.setText(item.getDesc());
            tvTag.setVisibility(item.isOverBalance() ? View.VISIBLE : View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            mLlVipCardPackage.addView(view, params);

            if (i < 2) {
                View tmpView = new View(mContext);
                params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 10), 10);
                mLlVipCardPackage.addView(tmpView, params);
            }

            view.setTag(item.getData());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VipCardPackageBean data = (VipCardPackageBean) v.getTag();
                    NavigationHelper.toVipCardPayPage(mContext, data);
                }
            });
        }
    }

    /**
     * 显示贵宾卡使用情况
     *
     * @param data
     */
    public void showPersonalVipCardInfo(VipCardUseBean data) {
        mLlVipCardPackage.removeAllViews();
        VipCardUseInfo useInfo = data.getUseInfo();
        if (useInfo == null)
            return;

        View view = LayoutInflater.from(mContext).inflate(R.layout.pay_item_vip_card_info, null);
        TextView tvName = view.findViewById(R.id.tv_card_name);
        TextView tvRecord = view.findViewById(R.id.tv_card_record);
        TextView tvTimeRange = view.findViewById(R.id.tv_card_timerange);

        view.setBackgroundResource(CommUtilKt.getVipCardBgResId(useInfo.getContent()));
        tvName.setText(useInfo.getContent());
        tvRecord.setText(String.format("使用记录（%d/%d）", useInfo.getUsedCount(), useInfo.getAllCount()));

        String startDate = useInfo.getBeginTime();
        String endDate = useInfo.getEndTime();
        startDate = getFormatDateTime(startDate);
        endDate = getFormatDateTime(endDate);
        tvTimeRange.setText(String.format("使用期限：%s - %s（%d天）", startDate, endDate, useInfo.getOutOfDay()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlVipCardPackage.addView(view, params);
    }

    private String getFormatDateTime(String date) {
        if (date != null && date.length() >= 10) {
            date.replace("-", ".").subSequence(0, 10);
        }
        return date;
    }

}