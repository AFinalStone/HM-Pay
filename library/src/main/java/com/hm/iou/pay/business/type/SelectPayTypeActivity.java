package com.hm.iou.pay.business.type;

import android.os.Bundle;
import android.text.TextUtils;
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

    public static final String EXTRA_PACKAGE_ID = "package_id";
    public static final String EXTRA_TIME_CARD_NUM = "time_card_num";
    public static final String EXTRA_TIME_CARD_MONEY = "time_card_pay_money";
    public static final String EXTRA_TIME_CARD_UNIT_PRICE = "time_card_unit_price";

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

    private String mPackageId;//套餐Id
    private String mPayTimeCareNum;//次数
    private String mPayTimeCardMoney;//总金额
    private String mPayTimeCardUnitPrice; //单价

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
        mPackageId = getIntent().getStringExtra(EXTRA_PACKAGE_ID);
        mPayTimeCareNum = getIntent().getStringExtra(EXTRA_TIME_CARD_NUM);
        mPayTimeCardMoney = getIntent().getStringExtra(EXTRA_TIME_CARD_MONEY);
        mPayTimeCardUnitPrice = getIntent().getStringExtra(EXTRA_TIME_CARD_UNIT_PRICE);
        if (bundle != null) {
            mPackageId = bundle.getString(EXTRA_PACKAGE_ID);
            mPayTimeCareNum = bundle.getString(EXTRA_TIME_CARD_NUM);
            mPayTimeCardMoney = bundle.getString(EXTRA_TIME_CARD_MONEY);
            mPayTimeCardUnitPrice = bundle.getString(EXTRA_TIME_CARD_UNIT_PRICE);
        }
        if (!TextUtils.isEmpty(mPayTimeCareNum)) {
            mTvPayTimeCardNum.setText(mPayTimeCareNum);
        }
        if (!TextUtils.isEmpty(mPayTimeCardMoney)) {
            mTvPayTimeCardMoney.setText(getString(R.string.uikit_money) + mPayTimeCardMoney);
        }
        if (!TextUtils.isEmpty(mPayTimeCardUnitPrice)) {
            mTvPayTimeCardDesc.setText(getString(R.string.uikit_money) + mPayTimeCardUnitPrice + "签约/次，包含以下服务：");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PACKAGE_ID, mPackageId);
        outState.putString(EXTRA_TIME_CARD_NUM, mPayTimeCareNum);
        outState.putString(EXTRA_TIME_CARD_MONEY, mPayTimeCardMoney);
        outState.putString(EXTRA_TIME_CARD_UNIT_PRICE, mPayTimeCardUnitPrice);
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
            mPresenter.payByWx(mPackageId);
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

    @Override
    public void setResultOK() {
        setResult(RESULT_OK);
    }
}
