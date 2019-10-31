package com.hm.iou.pay.business.qjcode

import android.annotation.SuppressLint
import android.content.Context
import com.hm.iou.base.event.OpenWxResultEvent
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.create.api.PayV2Api
import com.hm.iou.pay.api.PayApi
import com.hm.iou.pay.bean.QJCodeLenderConfirmResBean
import com.hm.iou.pay.bean.WxPayAppParamResp
import com.hm.iou.pay.bean.req.QJCodeLenderConfirmReqBean
import com.hm.iou.pay.dict.ChannelEnumBean
import com.hm.iou.pay.dict.OrderPayStatusEnumBean
import com.hm.iou.pay.event.PaySuccessEvent
import com.hm.iou.tools.SystemUtil
import com.hm.iou.wxapi.WXEntryActivity
import com.hm.iou.wxapi.WXPayEntryActivity
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * Created by syl on 2019/8/14.
 */
class QJCodeLenderConfirmPayPresenter(context: Context, view: QJCodeLenderConfirmPayContract.View) : MvpActivityPresenter<QJCodeLenderConfirmPayContract.View>(context, view), QJCodeLenderConfirmPayContract.Presenter {
    companion object {
        private val PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm"
        private val KEY_WX_PAY_CODE = "qjcodelenderconfirmpay.wxpay"
    }

    private val mCountDownTime: Long = 1800
    private var mTimeCountDownDisposable: Disposable? = null
    private var mWxPayBean: WxPayAppParamResp? = null
    private var mChannel: ChannelEnumBean? = null
    private var mOrderId: String? = null//订单编号

    private var mWXApi: IWXAPI? = null

    init {
        EventBus.getDefault().register(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (mWXApi != null) {
            mWXApi?.detach()
            mWXApi = null
            WXEntryActivity.cleanWXLeak()
        }
    }

    @SuppressLint("CheckResult")
    override fun createPayOrderByWx(reqBean: QJCodeLenderConfirmReqBean) {
        val flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT)
        if (flag) {
            mView.showLoadingView("创建订单...")
            mChannel = ChannelEnumBean.PayByWx
            PayV2Api.qjCodeLenderConfirm(reqBean)
                    .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.handleResponse())
                    .subscribeWith(object : CommSubscriber<QJCodeLenderConfirmResBean>(mView) {
                        override fun handleResult(data: QJCodeLenderConfirmResBean?) {
                            mView.dismissLoadingView()
                            val contentId = data?.contentId
                            contentId?.let {
                                getResultStatus(it)
                            }
                            mOrderId = data?.wxPayAppParamResp?.orderId
                            mWxPayBean = data?.wxPayAppParamResp
                            payAgain()
                            startCountDown()
                        }

                        override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                            mView.dismissLoadingView()
                        }

                        override fun isShowBusinessError(): Boolean {
                            return false
                        }

                        override fun isShowCommError(): Boolean {
                            return false
                        }
                    })
        } else {
            mView.toastMessage("当前手机未安装微信")
            mView.closeCurrPage()
        }
    }


    override fun payAgain() {
        if (ChannelEnumBean.PayByWx == mChannel) {
            payByWx()
        }
    }

    private fun payByWx() {
        if (mWxPayBean != null) {
            val partnerId = mWxPayBean?.partnerid
            val prepayId = mWxPayBean?.prepayid
            val packageValue = mWxPayBean?.packageValue
            val nonceStr = mWxPayBean?.noncestr
            val timeStamp = mWxPayBean?.timestamp ?: ""
            val sign = mWxPayBean?.sign
            if (mWXApi == null) {
                mWXApi = WXPayEntryActivity.createWXApi(mContext)
            }
            WXPayEntryActivity.wxPay(mWXApi, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, KEY_WX_PAY_CODE)
        }
    }

    @SuppressLint("CheckResult")
    private fun getResultStatus(contentId: Int) {
        PayV2Api.getQjCodeLenderConfirmStatus(contentId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<Int>(mView) {

                    override fun handleResult(status: Int?) {
                        mView.dismissLoadingView()
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }

                    override fun isShowBusinessError(): Boolean {
                        return false
                    }

                    override fun isShowCommError(): Boolean {
                        return false
                    }
                })
    }

    @SuppressLint("CheckResult")
    override fun checkPayResult() {
        mView.showLoadingView("请稍等...")
        val orderId = mOrderId ?: ""
        mView.showLoadingView("校验结果...")
        PayApi.queryOrderPayState(orderId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<String>(mView) {
                    override fun handleResult(code: String) {
                        mView.dismissLoadingView()
                        if (OrderPayStatusEnumBean.PaySuccess.status == code) {
                            EventBus.getDefault().post(PaySuccessEvent())
                            mView.closePageByPaySuccess()
                        } else if (OrderPayStatusEnumBean.PayFailed.status == code) {
                            mTimeCountDownDisposable?.let {
                                if (!it.isDisposed) {
                                    it.dispose()
                                }
                            }
                            mView.setPayFailedBtnVisible(true)
                        } else if (OrderPayStatusEnumBean.PayWait.status == code || OrderPayStatusEnumBean.Paying.status == code) {
                            payAgain()
                        } else if (OrderPayStatusEnumBean.PayFinish.status == code) {
                            mView.toastMessage("订单已经关闭...")
                            mView.closeCurrPage()
                        } else if (OrderPayStatusEnumBean.RefundMoney.status == code) {
                            mView.toastMessage("订单已经退款...")
                            mView.closeCurrPage()
                        } else {
                            mView.toastMessage("发生未知异常...")
                            mView.closeCurrPage()
                        }
                    }

                    override fun handleException(throwable: Throwable, code: String, errorMsg: String) {
                        mView.dismissLoadingView()
                    }
                })
    }

    /**
     * 开启倒计时
     */
    fun startCountDown() {
        mTimeCountDownDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mView.setPayByWxBtnVisible(false)
        mView.setCheckPayResultBtnVisible(true)
        mTimeCountDownDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(mCountDownTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map { time ->
                    val timeRemain = mCountDownTime - time - 1
                    val timeSeconds = timeRemain / 60
                    var strTimeSeconds = timeSeconds.toString()
                    if (timeSeconds < 10) {
                        strTimeSeconds = "0$timeSeconds"
                    }
                    val timeMinutes = timeRemain % 60
                    var strTimeMinutes = timeMinutes.toString()
                    if (timeMinutes < 10) {
                        strTimeMinutes = "0$timeMinutes"
                    }
                    "手动校验  $strTimeSeconds:$strTimeMinutes"
                }
                .doOnComplete {
                    if (mView != null) {
                        mView.setCheckPayResultBtnEnable(false)
                        mView.setCheckPayResultBtnText("刷新订单 00:00")
                    }
                }
                .subscribe({ desc ->
                    if (mView != null) {
                        mView.setCheckPayResultBtnText(desc)
                    }
                }, {
                    if (mView != null) {
                        mView.setCheckPayResultBtnEnable(false)
                        mView.setCheckPayResultBtnText("刷新订单 00:00")
                    }
                })
    }


    /**
     * 微信支付成功，关闭当前页面
     *
     * @param openWxResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvenBusOpenWXResult(openWxResultEvent: OpenWxResultEvent) {
        if (KEY_WX_PAY_CODE == openWxResultEvent.key) {
            if (openWxResultEvent.ifPaySuccess) {
                checkPayResult()
            }
        }
    }


}