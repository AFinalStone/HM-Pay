package com.hm.iou.pay.business.bindbank.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.business.bindbank.BindBinkContract;
import com.hm.iou.pay.business.bindbank.presenter.BindBankPresenter;
import com.hm.iou.router.Router;
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

    @Override
    public void showAuthDialog() {
        Dialog dialog = new IOSAlertDialog.Builder(mContext)
                .setTitle("银行卡认证")
                .setMessage("通过银行卡认证，您可以免费获得1次签章的机会，目前您未通过实名认证，是否立即认证实名信息？")
                .setGravity(Gravity.LEFT)
                .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(mContext);
                        dialog.dismiss();
                    }
                })
                .show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    @Override
    public void showBinkBankInfo(String bankCardName, String bankCardCode, String bankCardType, String phoneCode) {
        StringBuffer sbMsg = new StringBuffer();
        sbMsg.append("当前绑定的银行卡为（");
        if (!TextUtils.isEmpty(bankCardName)) {
            sbMsg.append(bankCardName);
        }
        if (!TextUtils.isEmpty(bankCardCode)) {
            if (!TextUtils.isEmpty(bankCardName)) {
                sbMsg.append("/");
            }
            sbMsg.append(bankCardCode);
        }
        if (!TextUtils.isEmpty(bankCardType)) {
            if (!TextUtils.isEmpty(bankCardName) || !TextUtils.isEmpty(bankCardCode)) {
                sbMsg.append("/");
            }
            sbMsg.append(bankCardType);
        }
        if (TextUtils.isEmpty(phoneCode)) {
            sbMsg.append("），如需更换信息，请联系客服，服务费¥2。");
        } else {
            sbMsg.append("），手机号尾号" + phoneCode + "，如需更换信息，请联系客服，服务费¥2。");
        }
        Dialog dialog = new IOSAlertDialog.Builder(mContext)
                .setTitle("银行卡认证")
                .setMessage(sbMsg.toString())
                .setGravity(Gravity.LEFT)
                .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }
}