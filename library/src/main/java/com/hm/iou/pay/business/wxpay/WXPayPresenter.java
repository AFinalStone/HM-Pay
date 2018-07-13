package com.hm.iou.pay.business.wxpay;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.PayTestReqBean;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.wxapi.WXPayEntryActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 选择登录方式
 *
 * @author syl
 * @time 2018/5/17 下午5:24
 */
public class WXPayPresenter extends MvpActivityPresenter<WXPayContract.View> implements WXPayContract.Presenter {

    private static final String PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm";
    private static final String KEY_WX_PAY_COE = "wxmodule.wxpay";

    public WXPayPresenter(@NonNull Context context, @NonNull WXPayContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void wxPay() {
        mView.showLoadingView();
        PayApi.payTest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<PayTestReqBean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new CommSubscriber<PayTestReqBean>(mView) {
                    @Override
                    public void handleResult(PayTestReqBean payTestReqBean) {
                        mView.dismissLoadingView();
                        String partnerId = payTestReqBean.getPartnerid();
                        String prepayid = payTestReqBean.getPrepayid();
                        String packageValue = payTestReqBean.getPackageValue();
                        String nonceStr = payTestReqBean.getNoncestr();
                        String timeStamp = payTestReqBean.getTimestamp();
                        String sign = payTestReqBean.getSign();
                        WXPayEntryActivity.wxPay(mContext, partnerId, prepayid, packageValue
                                , nonceStr, timeStamp, sign, KEY_WX_PAY_COE);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String msg) {
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }
                });
    }


    @Override
    public void payByWx() {
        boolean flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT);
        if (flag) {
            wxPay();
        } else {
            mView.toastMessage("当前手机未安装微信");
            mView.closeCurrPage();
        }
    }

    /**
     * 打开微信进行支付操作
     *
     * @param openWxResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusOpenWXResult(OpenWxResultEvent openWxResultEvent) {
        if (KEY_WX_PAY_COE.equals(openWxResultEvent.getKey())) {
            if (openWxResultEvent.getIfPaySuccess()) {
                mView.toastMessage("微信支付成功");
            } else {
                mView.toastMessage("微信支付失败");
            }
            mView.closeCurrPage();
        }
    }

}
