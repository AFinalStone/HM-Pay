package com.hm.iou.pay.business.timecard

import android.content.Context
import com.hm.iou.base.event.OpenWxResultEvent
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.pay.api.PayApi
import com.hm.iou.pay.bean.CreateOrderResBean
import com.hm.iou.pay.bean.TimeCardBean
import com.hm.iou.pay.bean.WxPayBean
import com.hm.iou.pay.dict.ChannelEnumBean
import com.hm.iou.pay.dict.OrderPayStatusEnumBean
import com.hm.iou.pay.event.PaySuccessEvent
import com.hm.iou.tools.SystemUtil
import com.hm.iou.wxapi.WXEntryActivity
import com.hm.iou.wxapi.WXPayEntryActivity
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 贵宾卡详情购买页面
 */
class VipCardPresenter(context: Context, view: VipCardContract.View) : MvpActivityPresenter<VipCardContract.View>(context, view), VipCardContract.Presenter {

    companion object {
        private const val PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm"
        private const val KEY_WX_PAY_CODE = "vipcard.wxpay"
    }

    private var mWXApi: IWXAPI? = null
    private var mWxPayBean: WxPayBean? = null

    private var mOrderId: String? = null
    private var mWeiXinCallBackPaySuccess = false

    init {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        mWXApi?.let {
            it.detach()
            mWXApi = null
            WXEntryActivity.cleanWXLeak()
        }
    }

    override fun createPayOrderByWx(packageCode: String) {
        if (mWeiXinCallBackPaySuccess) {
            checkPayResult()
            return
        }
        val flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT)
        if (flag) {
            mView.showLoadingView("创建订单...")
            PayApi.createOrderV2(packageCode, null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.handleResponse())
                    .subscribeWith(object : CommSubscriber<CreateOrderResBean>(mView) {
                        override fun handleResult(orderInfo: CreateOrderResBean?) {
                            mView.dismissLoadingView()
                            orderInfo?.let {
                                if (orderInfo.isPay) {
                                    createPrepareOrder(orderInfo.orderId)
                                } else {
                                    EventBus.getDefault().post(PaySuccessEvent())
                                    mView.closePageByPaySuccess()
                                }
                            }
                        }

                        override fun handleException(throwable: Throwable?, s: String?, s1: String?) {
                            mView.dismissLoadingView()
                        }
                    })
        } else {
            mView.toastMessage("当前手机未安装微信")
            mView.closeCurrPage()
        }
    }

    override fun innerPayOrderByWx(packageCode: String) {
        mView.showLoadingView()
        PayApi.getInwardPackageV2(packageCode)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<TimeCardBean>(mView) {
                    override fun handleResult(data: TimeCardBean?) {
                        mView.dismissLoadingView()
                        data?.let {
                            createPayOrderByWx(it.packageCode)
                        }
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

    /**
     * 创建微信预支付订单
     */
    private fun createPrepareOrder(orderId: String) {
        mView.showLoadingView("创建订单...")
        mOrderId = orderId
        PayApi.createPreparePayOrder(ChannelEnumBean.PayByWx, orderId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<WxPayBean>(mView) {
                    override fun handleResult(wxPayBean: WxPayBean?) {
                        mView.dismissLoadingView()
                        mWxPayBean = wxPayBean
                        payByWx()
                    }

                    override fun handleException(throwable: Throwable?, s: String?, s1: String?) {
                        mView.dismissLoadingView()
                    }
                })
    }

    /**
     * 通过微信进行付款
     */
    private fun payByWx() {
        mWxPayBean?.let {
            mWeiXinCallBackPaySuccess = false

            val partnerId = it.partnerid
            val prepayid = it.prepayid
            val packageValue = it.packageValue
            val nonceStr = it.noncestr
            val timeStamp = it.timestamp
            val sign = it.sign

            if (mWXApi == null) {
                mWXApi = WXPayEntryActivity.createWXApi(mContext)
            }
            WXPayEntryActivity.wxPay(mWXApi, partnerId, prepayid, packageValue, nonceStr, timeStamp, sign, KEY_WX_PAY_CODE)
        }
    }

    /**
     * 校验支付结果
     */
    private fun checkPayResult() {
        mView.showLoadingView("校验结果...")
        PayApi.queryOrderPayState(mOrderId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<String>(mView) {
                    override fun handleResult(code: String?) {
                        mView.dismissLoadingView()
                        if (OrderPayStatusEnumBean.PaySuccess.status == code) {
                            EventBus.getDefault().post(PaySuccessEvent())
                            mView.closePageByPaySuccess()
                        } else {
                            mView.showNotCheckResultDialog()
                        }
                    }

                    override fun handleException(throwable: Throwable?, code: String?, errorMsg: String?) {
                        mView.dismissLoadingView()
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
                mWeiXinCallBackPaySuccess = true
                checkPayResult()
            }
        }
    }

}