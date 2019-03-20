package com.hm.iou.pay.business.bindbank.view;

import android.os.Bundle;
import android.view.Gravity;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.business.bindbank.BindBinkContract;
import com.hm.iou.pay.business.bindbank.presenter.BindBankPresenter;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.dialog.HMAlertDialog;

/**
 * @author syl
 * @time 2018/7/31 下午2:14
 */
public class BindBankActivity extends BaseActivity<BindBankPresenter> implements BindBinkContract.View {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected BindBankPresenter initPresenter() {
        return new BindBankPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mPresenter.checkCanBindBank();
    }

    @Override
    public void warnNoTimeToBindToday() {
        String title = "管家提醒";
        String msg = "银行卡或预留银行的手机号有误。每日三次银行卡认证已达上限。";
        new HMAlertDialog
                .Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("明日再试")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        finish();
                    }

                    @Override
                    public void onNegClick() {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showAuthDialog() {
        String title = "银行卡认证";
        String msg = "通过银行卡认证，您可以免费获得1次签章的机会，目前您未通过实名认证，是否立即认证实名信息？";
        new HMAlertDialog
                .Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setMessageGravity(Gravity.LEFT)
                .setNegativeButton("以后再说")
                .setPositiveButton("立即认证")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(mContext);
                        finish();
                    }

                    @Override
                    public void onNegClick() {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showBinkBankInfo(String bankCardName, String bankCardCode, String bankCardType, String phoneCode) {
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/person/user_bind_bank_info")
                .navigation(mContext);
        finish();
    }
}