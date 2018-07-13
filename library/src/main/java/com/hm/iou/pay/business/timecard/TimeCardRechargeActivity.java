package com.hm.iou.pay.business.timecard;

import android.Manifest;
import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class TimeCardRechargeActivity extends BaseActivity<TimeCardRechargePresenter> implements TimeCardRechargeContract.View {


    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_select_pay_type;
    }

    @Override
    protected TimeCardRechargePresenter initPresenter() {
        return new TimeCardRechargePresenter(this, this);
    }


    @Override
    protected void initEventAndData(Bundle bundle) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE).
                subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        mPresenter.payByWx();
                    }
                });
    }

    @Override
    public void showRemainNum(String num) {

    }

    @Override
    public void showList() {

    }

    @Override
    public void showInitLoading() {

    }

    @Override
    public void hideInitLoading() {

    }

    @Override
    public void showAdvertisemenr(String adImageUrl, String adLinkUrl) {

    }
}
