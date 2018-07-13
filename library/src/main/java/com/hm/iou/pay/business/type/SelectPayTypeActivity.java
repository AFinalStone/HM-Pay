package com.hm.iou.pay.business.type;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectPayTypeActivity extends BaseActivity<SelectPayTypePresenter> implements SelectPayTypeContract.View {


    public static final String EXTRA_TIME_CARD_NUM = "time_card_num";
    public static final String EXTRA_TIME_CARD_MONEY = "time_card_money";

    @BindView(R2.id.tv_payTimeCardNum)
    TextView mTvPayTimeCardNum;

    @BindView(R2.id.tv_payTimeCardMoney)
    TextView mTvPayTimeCardMoney;

    @BindView(R2.id.tv_payTimeCardDesc)
    TextView mTvPayTimeCardDesc;

    @BindView(R2.id.rl_payByWX)
    RelativeLayout mRlPayByWX;
    @BindView(R2.id.ll_checkPayResult)
    LinearLayout mLlCheckPayResult;
    @BindView(R2.id.btn_checkPayResult)
    Button mBtnCheckPayResult;

    private String mPayTimeCareNum;
    private String mPayTimeCardMoney;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_select_pay_type;
    }

    @Override
    protected SelectPayTypePresenter initPresenter() {
        return new SelectPayTypePresenter(this, this);
    }


    @Override
    protected void initEventAndData(Bundle bundle) {
        mPayTimeCareNum = getIntent().getStringExtra(EXTRA_TIME_CARD_NUM);
        mPayTimeCardMoney = getIntent().getStringExtra(EXTRA_TIME_CARD_MONEY);
        if (bundle != null) {
            mPayTimeCareNum = bundle.getString(EXTRA_TIME_CARD_NUM);
            mPayTimeCardMoney = bundle.getString(EXTRA_TIME_CARD_MONEY);
        }
        mTvPayTimeCardNum.setText("充值" + mPayTimeCareNum + "次");
        mTvPayTimeCardMoney.setText(getString(R.string.uikit_money) + mPayTimeCardMoney);
        mTvPayTimeCardDesc.setText(getString(R.string.uikit_money) + mPayTimeCardMoney + "签约/次，包含以下服务：");
    }

    //关闭Activity的切换动画
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @OnClick({R2.id.rl_payByWX, R2.id.iv_close})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.rl_payByWX == id) {
            mPresenter.payByWx();
        } else if (R.id.iv_close == id) {
            onBackPressed();
        }
    }


    @Override
    public void setPayByWxBtnVisible(boolean visible) {
        if (visible) {
            mRlPayByWX.setVisibility(View.VISIBLE);
        } else {
            mRlPayByWX.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCheckPayResultBtnVisible(boolean visible) {
        if (visible) {
            mLlCheckPayResult.setVisibility(View.VISIBLE);
        } else {
            mLlCheckPayResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCheckPayResultBtnText(String text) {
        mBtnCheckPayResult.setText(text);
    }

    @Override
    public void setCheckPayResultBtnEnable(boolean enable) {
        mBtnCheckPayResult.setEnabled(enable);
    }
}
