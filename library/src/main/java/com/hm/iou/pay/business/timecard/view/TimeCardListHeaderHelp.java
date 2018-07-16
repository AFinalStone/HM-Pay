package com.hm.iou.pay.business.timecard.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.pay.R;
import com.hm.iou.router.Router;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListHeaderHelp {

    private TextView mTvRemainderNum;
    private ImageView mIvCheck;
    private boolean isCheck = true;
    private View mHeaderView;

    public TimeCardListHeaderHelp(ViewGroup parentView) {
        mHeaderView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.pay_item_time_card_recharge_header, parentView, false);
        mTvRemainderNum = mHeaderView.findViewById(R.id.tv_remainderNum);
        mIvCheck = mHeaderView.findViewById(R.id.iv_check);
        mIvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheck) {
                    mIvCheck.setImageResource(R.mipmap.uikit_icon_check_default);
                } else {
                    mIvCheck.setImageResource(R.mipmap.uikit_icon_check_green);
                }
                isCheck = !isCheck;
            }
        });
        mHeaderView.findViewById(R.id.tv_rechargeAgreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                        .withString("url", "https://www.baidu.com")
                        .navigation(parentView.getContext());
            }
        });
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    public void setRemainderNum(String remainderNum) {
        mTvRemainderNum.setText(remainderNum);
    }
}
