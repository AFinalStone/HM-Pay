package com.hm.iou.pay.business.lawyer

import android.annotation.SuppressLint
import android.content.Context
import com.hm.iou.base.event.OpenWxResultEvent
import com.hm.iou.base.mvp.HMBasePresenter
import com.hm.iou.network.exception.ApiException
import com.hm.iou.pay.api.PayV2Api
import com.hm.iou.pay.bean.CreateLawyerLetterOrderResBean
import com.hm.iou.pay.dict.ChannelEnumBean
import com.hm.iou.pay.dict.OrderPayStatusEnumBean
import com.hm.iou.pay.event.PaySuccessEvent
import com.hm.iou.tools.SystemUtil
import com.hm.iou.wxapi.WXEntryActivity
import com.hm.iou.wxapi.WXPayEntryActivity
import com.tencent.mm.opensdk.openapi.IWXAPI
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PayLawyerLetterPresenter(context: Context, view: PayLawyerLetterContract.View) : HMBasePresenter<PayLawyerLetterContract.View>(context, view), PayLawyerLetterContract.Presenter {

    companion object {
        private val PACKAGE_NAME_OF_WX_CHAT = "com.tencent.mm"
        private val KEY_WX_PAY_CODE = "paylawyerletter.wxpay"
    }

    private val mCountDownTime: Long = 1800
    private var mCountDownJob: Job? = null

    private var mWxPayBean: CreateLawyerLetterOrderResBean? = null
    private var mChannel: ChannelEnumBean? = null

    private var mBillId: String? = null     //律师函订单id
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
    override fun createPayOrderByWx(billId: String, innerUser: Boolean) {
        mBillId = billId
        val flag = SystemUtil.isAppInstalled(mContext, PACKAGE_NAME_OF_WX_CHAT)
        if (flag) {
            mView.showLoadingView("创建订单...")
            launch {
                try {
                    mChannel = ChannelEnumBean.PayByWx
                    val data = handleResponse(PayV2Api.createLawyerLetterOrder(billId, innerUser))
                    mView.dismissLoadingView()
                    mOrderId = data?.orderId
                    mWxPayBean = data
                    payAgain()
                    startCountDown()
                } catch (e: Exception) {
                    mView.dismissLoadingView()
                    handleException(e, showCommError = false, showBusinessError = false)
                    if (e is ApiException) {
                        if ("601003" == e.code || "2401005" == e.code) {
                            EventBus.getDefault().post(PaySuccessEvent())
                            mView.closePageByPaySuccess()
                        } else {
                            mView.toastErrorMessage(e.message)
                        }
                    } else {
                        mView.toastErrorMessage(e.message)
                    }
                }
            }
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
    private fun getResultStatus() {
        val billId = mBillId
        if (billId != null) {
            mView.showLoadingView()
            launch {
                try {
                    val status = handleResponse(PayV2Api.getLawyerLetterOrderStatus(billId))
                    mView.dismissLoadingView()
                    if (status != 0) {
                        EventBus.getDefault().post(PaySuccessEvent())
                        mView.closePageByPaySuccess()
                    } else {
                        checkPayResult()
                    }
                } catch (e: Exception) {
                    mView.dismissLoadingView()
                    handleException(e, showBusinessError = false, showCommError = false)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun checkPayResult() {
        val orderId = mOrderId
        if (orderId != null) {
            mView.showLoadingView("校验结果...")
            launch {
                try {
                    val code = handleResponse(PayV2Api.queryOrderPayState(orderId))
                    mView.dismissLoadingView()
                    if (OrderPayStatusEnumBean.PaySuccess.status == code) {
                        EventBus.getDefault().post(PaySuccessEvent())
                        mView.closePageByPaySuccess()
                    } else if (OrderPayStatusEnumBean.PayFailed.status == code) {
                        mCountDownJob?.cancel()
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
                } catch (e: Exception) {
                    mView.dismissLoadingView()
                    handleException(e)
                }
            }
        }
    }

    /**
     * 开启倒计时
     */
    fun startCountDown() {
        mCountDownJob?.cancel()
        mView.setPayByWxBtnVisible(false)
        mView.setCheckPayResultBtnVisible(true)
        mCountDownJob = launch {
            var sec = 0
            while (isActive) {
                delay(1000)
                val timeRemain = mCountDownTime - sec - 1
                if (timeRemain <= 0) {
                    mView.setCheckPayResultBtnEnable(false)
                    mView.setCheckPayResultBtnText("刷新订单 00:00")
                    break
                }

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
                val text = "手动校验  $strTimeSeconds:$strTimeMinutes"
                mView.setCheckPayResultBtnText(text)
            }
        }
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
                getResultStatus()
            }
        }
    }


}