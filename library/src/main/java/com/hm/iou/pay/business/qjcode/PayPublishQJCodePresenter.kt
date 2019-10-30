package com.hm.iou.pay.business.qjcode

import android.annotation.SuppressLint
import android.content.Context
import com.hm.iou.base.event.OpenWxResultEvent
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.create.api.PayV2Api
import com.hm.iou.pay.bean.CreatePublishQJCodeOrderResBean
import com.hm.iou.pay.bean.req.CreatePublishQJCodeOrderReqBean
import com.hm.iou.pay.dict.ChannelEnumBean
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
class PayPublishQJCodePresenter(context: Context, view: PayPublishQJCodeContract.View) : MvpActivityPresenter<PayPublishQJCodeContract.View>(context, view), PayPublishQJCodeContract.Presenter {

    companion object {
        private val PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm"
        private val KEY_WX_PAY_CODE = "paypublishqjcode.wxpay"
    }

    private val mCountDownTime: Long = 1800
    private var mQJCodePublishId: String? = null//求借码发布id
    private var mTimeCountDownDisposable: Disposable? = null
    private var mWxPayBean: CreatePublishQJCodeOrderResBean? = null
    private var mChannel: ChannelEnumBean? = null
    private var mOrderId: String? = null

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
    override fun createPayOrderByWx(qjCodePublishId: String, publishType: Int) {
        mQJCodePublishId = qjCodePublishId
        val flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT)
        if (flag) {
            mView.showLoadingView("创建订单...")
            mChannel = ChannelEnumBean.PayByWx
            val req = CreatePublishQJCodeOrderReqBean(publishType, qjCodePublishId)
            PayV2Api.createPublishQJCodeOrder(req)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.handleResponse())
                    .subscribeWith(object : CommSubscriber<CreatePublishQJCodeOrderResBean>(mView) {
                        override fun handleResult(res: CreatePublishQJCodeOrderResBean?) {
                            mView.dismissLoadingView()
                            mWxPayBean = res
                            payAgain()
                            startCountDown()
                        }

                        override fun handleException(throwable: Throwable?, code: String?, msg: String?) {
                            mView.dismissLoadingView()
                            if ("601003" == code || "2401005" == code) {
                                EventBus.getDefault().post(PaySuccessEvent())
                                mView.closePageByPaySuccess()
                            } else {
                                mView.toastErrorMessage(msg)
                            }
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
            val timeStamp = mWxPayBean?.timestamp
            val sign = mWxPayBean?.sign
            if (mWXApi == null) {
                mWXApi = WXPayEntryActivity.createWXApi(mContext)
            }
            WXPayEntryActivity.wxPay(mWXApi, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, KEY_WX_PAY_CODE)
        }
    }

    @SuppressLint("CheckResult")
    override fun checkPayResult() {
        mView.showLoadingView("请稍等...")
        val qjCodePublishId = mQJCodePublishId ?: ""
        PayV2Api.getPublishQJCodeStatus(qjCodePublishId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<Int>(mView) {

                    override fun handleResult(status: Int?) {
                        mView.dismissLoadingView()
                        status?.let {
                            if (status == 0) {
                                payAgain()
                            } else {
                                EventBus.getDefault().post(PaySuccessEvent())
                                mView.closePageByPaySuccess()
                            }
                        }

                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
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