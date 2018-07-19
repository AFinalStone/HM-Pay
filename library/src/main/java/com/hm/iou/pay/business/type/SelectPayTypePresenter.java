package com.hm.iou.pay.business.type;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.PayTestBean;
import com.hm.iou.pay.bean.req.ChannelEnumReqBean;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.wxapi.WXPayEntryActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 选择付款方式
 *
 * @author syl
 * @time 2018/5/17 下午5:24
 */
public class SelectPayTypePresenter extends MvpActivityPresenter<SelectPayTypeContract.View> implements SelectPayTypeContract.Presenter {

    private static final String PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm";
    private static final String KEY_WX_PAY_CODE = "selecttype.wxpay";
    private long mCountDownTime = 1800;
    private String mTimeCareOrderId;//次卡充值订单Id

    public SelectPayTypePresenter(@NonNull Context context, @NonNull SelectPayTypeContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开启倒计时
     */
    public void startCountDown() {
        mView.setPayByWxBtnVisible(false);
        mView.setCheckPayResultBtnVisible(true);
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(mCountDownTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().bindUntilEvent(ActivityEvent.DESTROY))
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long time) throws Exception {
                        long timeRemain = mCountDownTime - time - 1;
                        long timeSeconds = timeRemain / 60;
                        String strTimeSeconds = String.valueOf(timeSeconds);
                        if (timeSeconds < 10) {
                            strTimeSeconds = "0" + timeSeconds;
                        }
                        long timeMinutes = timeRemain % 60;
                        String strTimeMinutes = String.valueOf(timeMinutes);
                        if (timeMinutes < 10) {
                            strTimeMinutes = "0" + timeMinutes;
                        }
                        return "手动校验  " + strTimeSeconds + ":" + strTimeMinutes;
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mView != null) {
                            mView.setCheckPayResultBtnEnable(false);
                            mView.setCheckPayResultBtnText("刷新订单 00:00");
                        }
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String desc) throws Exception {
                        if (mView != null) {
                            mView.setCheckPayResultBtnText(desc);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mView != null) {
                            mView.setCheckPayResultBtnEnable(false);
                            mView.setCheckPayResultBtnText("刷新订单 00:00");
                        }
                    }
                });

    }

    /**
     * 创建次卡充值订单
     */
    private void createTimeCardOrder(String packageId) {
        mView.showLoadingView();
        PayApi.createOrder(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String orderId) {
                        mView.dismissLoadingView();
                        mTimeCareOrderId = orderId;
                        createPayByWxPrepareOrder();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }
                });
    }

    /**
     * 创建微信预支付订单
     */
    private void createPayByWxPrepareOrder() {
        mView.showLoadingView();
        PayApi.createPreparePayOrder(ChannelEnumReqBean.PayByWx, mTimeCareOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<BaseResponse<PayTestBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<PayTestBean>handleResponse())
                .subscribeWith(new CommSubscriber<PayTestBean>(mView) {
                    @Override
                    public void handleResult(PayTestBean payTestBean) {
                        mView.dismissLoadingView();
                        String partnerId = payTestBean.getPartnerid();
                        String prepayid = payTestBean.getPrepayid();
                        String packageValue = payTestBean.getPackageValue();
                        String nonceStr = payTestBean.getNoncestr();
                        String timeStamp = payTestBean.getTimestamp();
                        String sign = payTestBean.getSign();
                        WXPayEntryActivity.wxPay(mContext, partnerId, prepayid, packageValue
                                , nonceStr, timeStamp, sign, KEY_WX_PAY_CODE);
                        startCountDown();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }


    @Override
    public void payByWx(String packageId) {
        boolean flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT);
        if (flag) {
            createTimeCardOrder(packageId);
        } else {
            mView.toastMessage("当前手机未安装微信");
            mView.closeCurrPage();
        }
    }

    @Override
    public void checkPayResult() {

    }

    /**
     * 微信支付成功，关闭当前页面
     *
     * @param openWxResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusOpenWXResult(OpenWxResultEvent openWxResultEvent) {
        if (KEY_WX_PAY_CODE.equals(openWxResultEvent.getKey())) {
            if (openWxResultEvent.getIfPaySuccess()) {
                mView.setResultOK();
                mView.closeCurrPage();
            }
        }
    }

}
