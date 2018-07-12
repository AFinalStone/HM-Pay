package com.hm.iou.pay.business.wxpay;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;

public class WXPayActivity extends BaseActivity<WXPayPresenter> implements WXPayContract.View {


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected WXPayPresenter initPresenter() {
        return new WXPayPresenter(this, this);
    }


    @Override
    protected void initEventAndData(Bundle bundle) {
        mPresenter.payByWx();
    }

}
