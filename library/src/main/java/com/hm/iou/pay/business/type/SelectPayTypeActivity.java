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

public class SelectPayTypeActivity extends BaseActivity<SelectPayTypePresenter> implements SelectPayTypeContract.View {

    public static final String EXTRA_PACKAGE_ID = "package_id";                     // packageId 或者是 packageCode
    public static final String EXTRA_TIME_CARD_NAME = "time_card_name";
    public static final String EXTRA_TIME_CARD_MONEY = "time_card_pay_money";
    public static final String EXTRA_TIME_CARD_ADD_NUM = "time_card_add_num";

    public static final String EXTRA_COUPON_ID = "coupon_id";       //优惠券id，为了兼容以前的版本，当该值不为空时，表示采用 packageCode。当couponId = "none" 字符串时，表示无优惠券
    public static final String EXTRA_COUPON_DESC = "coupon_desc";   //优惠信息描述文字

    @BindView(R2.id.tv_payTimeCardNum)
    TextView mTvPayTimeCardNum;
    @BindView(R2.id.tv_pay_coupon_info)
    TextView mTvPayCouponInfo;              //优惠信息

    @BindView(R2.id.tv_payTimeCardMoney)    //支付的金额
    TextView mTvPayTimeCardMoney;

    @BindView(R2.id.tv_payTimeCardDesc)
    TextView mTvPayTimeCardDesc;

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
    private String mPayTimeCardAddNum;  //增加的数量

    private String mCouponId;           //优惠券id
    private String mCouponDesc;         //优惠信息描述文字

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
        mPayTimeCareName = getIntent().getStringExtra(EXTRA_TIME_CARD_NAME);
        mPayTimeCardMoney = getIntent().getStringExtra(EXTRA_TIME_CARD_MONEY);
        mPayTimeCardAddNum = getIntent().getStringExtra(EXTRA_TIME_CARD_ADD_NUM);
        mCouponId = getIntent().getStringExtra(EXTRA_COUPON_ID);
        mCouponDesc = getIntent().getStringExtra(EXTRA_COUPON_DESC);
        if (bundle != null) {
            mPackageId = bundle.getString(EXTRA_PACKAGE_ID);
            mPayTimeCareName = bundle.getString(EXTRA_TIME_CARD_NAME);
            mPayTimeCardMoney = bundle.getString(EXTRA_TIME_CARD_MONEY);
            mPayTimeCardAddNum = bundle.getString(EXTRA_TIME_CARD_ADD_NUM);
            mCouponId = bundle.getString(EXTRA_COUPON_ID);
            mCouponDesc = bundle.getString(EXTRA_COUPON_DESC);
        }
        if (!TextUtils.isEmpty(mPayTimeCareName)) {
            mTvPayTimeCardNum.setText(mPayTimeCareName);
        }
        if (!TextUtils.isEmpty(mPayTimeCardMoney)) {
            mTvPayTimeCardMoney.setText(getString(R.string.uikit_money) + mPayTimeCardMoney);
        }
        if (!TextUtils.isEmpty(mPayTimeCardAddNum)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("含");
            stringBuffer.append(mPayTimeCardAddNum);
            stringBuffer.append("次签章，每次签章包含：");
            mTvPayTimeCardDesc.setText(stringBuffer.toString());
        }
        if (!TextUtils.isEmpty(mCouponDesc)) {
            mTvPayCouponInfo.setVisibility(View.VISIBLE);
            mTvPayCouponInfo.setText(mCouponDesc);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PACKAGE_ID, mPackageId);
        outState.putString(EXTRA_TIME_CARD_NAME, mPayTimeCareName);
        outState.putString(EXTRA_TIME_CARD_MONEY, mPayTimeCardMoney);
        outState.putString(EXTRA_TIME_CARD_ADD_NUM, mPayTimeCardAddNum);
        outState.putString(EXTRA_COUPON_ID, mCouponId);
        outState.putString(EXTRA_COUPON_DESC, mCouponDesc);
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
            mPresenter.createPayOrderByWx(mPackageId, mCouponId);
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
