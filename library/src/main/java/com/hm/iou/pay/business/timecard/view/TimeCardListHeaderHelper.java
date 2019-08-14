package com.hm.iou.pay.business.timecard.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hm.iou.pay.Constants;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.business.timecard.presenter.TimeCardRechargePresenter;
import com.hm.iou.router.Router;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListHeaderHelper {

    @BindView(R2.id.tv_can_use_num)
    TextView mTvCanUseNum;
    @BindView(R2.id.tv_disabled_num)
    TextView mTvDisabledNum;

    private Context mContext;
    private TimeCardRechargePresenter mPresenter;
    private View mHeaderView;

    public TimeCardListHeaderHelper(Context context, TimeCardRechargePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_time_card_recharge_header, null, false);
        ButterKnife.bind(this, mHeaderView);
    }

    @OnClick({R2.id.ll_can_use_num, R2.id.iv_arrow_can_use_num, R2.id.ll_disabled_num, R2.id.iv_arrow_disabled_num, R2.id.btn_recharge, R2.id.tv_recharge_agreement})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.ll_can_use_num == id || R.id.iv_arrow_can_use_num == id) {
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/pay/history")
                    .navigation(mContext);
        } else if (R.id.ll_disabled_num == id || R.id.iv_arrow_disabled_num == id) {
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/pay/lock_sign_list")
                    .navigation(mContext);
        } else if (R.id.btn_recharge == id) {
            mPresenter.toAddTimeCardByPosition(1);
        } else if (R.id.tv_recharge_agreement == id) {
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                    .withString("url", Constants.H5_URL_RECHARGE_AGREEMENT)
                    .navigation(mContext);
        }
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 可用签章
     *
     * @param num
     */
    public void setCanUseSignNum(String num) {
        mTvCanUseNum.setText(num);
    }

    /**
     * 占用签章
     *
     * @param num
     */
    public void setDisableSignNum(String num) {
        mTvDisabledNum.setText(num);
    }


}
