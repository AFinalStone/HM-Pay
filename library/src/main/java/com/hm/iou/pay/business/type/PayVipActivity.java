package com.hm.iou.pay.business.type;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;

import butterknife.BindView;
import butterknife.OnClick;

public class PayVipActivity extends BaseActivity<PayVipPresenter> implements PayVipContract.View {

    public static final String EXTRA_PACKAGE_ID = "package_id";
    public static final String EXTRA_TIME_CARD_NAME = "time_card_name";
    public static final String EXTRA_TIME_CARD_MONEY = "time_card_pay_money";

    @BindView(R2.id.tv_payTimeCardNum)
    TextView mTvPayTimeCardNum;
    @BindView(R2.id.tv_payTimeCardMoney)
    TextView mTvPayTimeCardMoney;

    @BindView(R2.id.rl_payByWX)
    RelativeLayout mRlPayByWX;
    @BindView(R2.id.ll_checkPayResult)
    LinearLayout mLlCheckPayResult;
    @BindView(R2.id.btn_checkPayResult)
    Button mBtnCheckPayResult;
    @BindView(R2.id.btn_payFailed)
    Button mBtnPayFailed;

    private String mPackageId;          //套餐Id
    private String mPayTimeCareName;    //套餐名称
    private String mPayTimeCardMoney;   //总金额

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_select_pay_vip;
    }

    @Override
    protected PayVipPresenter initPresenter() {
        return new PayVipPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mPackageId = getIntent().getStringExtra(EXTRA_PACKAGE_ID);
        mPayTimeCareName = getIntent().getStringExtra(EXTRA_TIME_CARD_NAME);
        mPayTimeCardMoney = getIntent().getStringExtra(EXTRA_TIME_CARD_MONEY);
        if (bundle != null) {
            mPackageId = bundle.getString(EXTRA_PACKAGE_ID);
            mPayTimeCareName = bundle.getString(EXTRA_TIME_CARD_NAME);
            mPayTimeCardMoney = bundle.getString(EXTRA_TIME_CARD_MONEY);
        }
        if (!TextUtils.isEmpty(mPayTimeCareName)) {
            mTvPayTimeCardNum.setText(mPayTimeCareName);
        }
        if (!TextUtils.isEmpty(mPayTimeCardMoney)) {
            mTvPayTimeCardMoney.setText(getString(R.string.uikit_money) + mPayTimeCardMoney);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PACKAGE_ID, mPackageId);
        outState.putString(EXTRA_TIME_CARD_NAME, mPayTimeCareName);
        outState.putString(EXTRA_TIME_CARD_MONEY, mPayTimeCardMoney);
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
            TraceUtil.onEvent(this, "pay_wx_submit_click");
            mPresenter.createPayOrderByWx(mPackageId);
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
            mBtnCheckPayResult.setVisibility(View.VISIBLE);
            mBtnPayFailed.setVisibility(View.GONE);
            mBtnCheckPayResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.checkPayResult();
                }
            });
        } else {
            mLlCheckPayResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPayFailedBtnVisible(boolean visible) {
        if (visible) {
            mLlCheckPayResult.setVisibility(View.VISIBLE);
            mBtnPayFailed.setVisibility(View.VISIBLE);
            mBtnCheckPayResult.setVisibility(View.GONE);
            mBtnPayFailed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.payAgain();
                }
            });
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

    @Override
    public void closePageByPaySuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
