package com.hm.iou.pay.business.bindbank.view;

import android.content.DialogInterface;
import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.business.bindbank.BindBinkContract;
import com.hm.iou.pay.business.bindbank.presenter.BindBankPresenter;
import com.hm.iou.uikit.dialog.IOSAlertDialog;

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
        new IOSAlertDialog.Builder(mContext)
                .setTitle("管家提醒")
                .setMessage("银行卡或预留银行的手机号有误。每日三次银行卡认证已达上限。")
                .setPositiveButton("明日再试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
}