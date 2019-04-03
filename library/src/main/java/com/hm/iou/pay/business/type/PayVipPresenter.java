package com.hm.iou.pay.business.type;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.WxPayBean;
import com.hm.iou.pay.dict.ChannelEnumBean;
import com.hm.iou.pay.dict.OrderPayStatusEnumBean;
import com.hm.iou.pay.event.PaySuccessEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.wxapi.WXEntryActivity;
import com.hm.iou.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
public class PayVipPresenter extends MvpActivityPresenter<PayVipContract.View> implements PayVipContract.Presenter {

    private static final String PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm";
    private static final String KEY_WX_PAY_CODE = "selecttype.wxpay";
    private long mCountDownTime = 1800;
    private String mTimeCareOrderId;//次卡充值订单Id
    private Disposable mTimeCountDownDisposable;
    private WxPayBean mWxPayBean;
    private ChannelEnumBean mChannel;

    private IWXAPI mWXApi;

    public PayVipPresenter(@NonNull Context context, @NonNull PayVipContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mWXApi != null) {
            mWXApi.detach();
            mWXApi = null;
            WXEntryActivity.cleanWXLeak();
        }
    }

    @Override
    public void createPayOrderByWx(String packageId) {
        boolean flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT);
        if (flag) {
            createTimeCardOrder(packageId);
        } else {
            mView.toastMessage("当前手机未安装微信");
            mView.closeCurrPage();
        }
    }

    @Override
    public void payAgain() {
        if (ChannelEnumBean.PayByWx.equals(mChannel)) {
            payByWx();
        }
    }

    @Override
    public void checkPayResult() {
        mView.showLoadingView("校验结果...");
        PayApi.queryOrderPayState(mTimeCareOrderId)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String code) {
                        mView.dismissLoadingView();
                        if (OrderPayStatusEnumBean.PaySuccess.getStatus().equals(code)) {
                            EventBus.getDefault().post(new PaySuccessEvent());
                            mView.closePageByPaySuccess();
                        } else if (OrderPayStatusEnumBean.PayFailed.getStatus().equals(code)) {
                            if (mTimeCountDownDisposable != null && !mTimeCountDownDisposable.isDisposed()) {
                                mTimeCountDownDisposable.dispose();
                            }
                            mView.setPayFailedBtnVisible(true);
                        } else if (OrderPayStatusEnumBean.PayWait.getStatus().equals(code)
                                || OrderPayStatusEnumBean.Paying.getStatus().equals(code)) {
                            payAgain();
                        } else if (OrderPayStatusEnumBean.PayFinish.getStatus().equals(code)) {
                            mView.toastMessage("订单已经关闭...");
                            mView.closeCurrPage();
                        } else if (OrderPayStatusEnumBean.RefundMoney.getStatus().equals(code)) {
                            mView.toastMessage("订单已经退款...");
                            mView.closeCurrPage();
                        } else {
                            mView.toastMessage("发生未知异常...");
                            mView.closeCurrPage();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.dismissLoadingView();
                    }
                });
    }

    /**
     * 开启倒计时
     */
    public void startCountDown() {
        if (mTimeCountDownDisposable != null && !mTimeCountDownDisposable.isDisposed()) {
            mTimeCountDownDisposable.dispose();
        }
        mView.setPayByWxBtnVisible(false);
        mView.setCheckPayResultBtnVisible(true);
        mTimeCountDownDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(mCountDownTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Long>bindUntilEvent(ActivityEvent.DESTROY))
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
        mView.showLoadingView("创建订单...");
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
                        mView.dismissLoadingView();
                    }
                });
    }


    /**
     * 创建微信预支付订单
     */
    private void createPayByWxPrepareOrder() {
        mView.showLoadingView("创建订单...");
        mChannel = ChannelEnumBean.PayByWx;
        PayApi.createPreparePayOrder(mChannel, mTimeCareOrderId)
                .compose(getProvider().<BaseResponse<WxPayBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<WxPayBean>handleResponse())
                .subscribeWith(new CommSubscriber<WxPayBean>(mView) {
                    @Override
                    public void handleResult(WxPayBean wxPayBean) {
                        mView.dismissLoadingView();
                        mWxPayBean = wxPayBean;
                        payByWx();
                        startCountDown();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    /**
     * 通过微信进行付款
     */
    private void payByWx() {
        if (mWxPayBean != null) {
            String partnerId = mWxPayBean.getPartnerid();
            String prepayid = mWxPayBean.getPrepayid();
            String packageValue = mWxPayBean.getPackageValue();
            String nonceStr = mWxPayBean.getNoncestr();
            String timeStamp = mWxPayBean.getTimestamp();
            String sign = mWxPayBean.getSign();

            if (mWXApi == null) {
                mWXApi = WXPayEntryActivity.createWXApi(mContext);
            }
            WXPayEntryActivity.wxPay(mWXApi, partnerId, prepayid, packageValue
                    , nonceStr, timeStamp, sign, KEY_WX_PAY_CODE);
        }
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
                checkPayResult();
            }
        }
    }

}
