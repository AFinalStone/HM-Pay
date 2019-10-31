package com.hm.iou.pay.business.qjcode

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.pay.bean.req.QJCodeLenderConfirmReqBean

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-10-28 18:08
 * @E-Mail : afinalstone@foxmail.com
 */
class QJCodeLenderConfirmPayContract {

    interface View : BaseContract.BaseView {
        /**
         * 设置微信支付的按钮是否可见
         */
        fun setPayByWxBtnVisible(visible: Boolean)

        /**
         * 设置校验付款结果按钮是否可见
         *
         * @param visible
         */
        fun setCheckPayResultBtnVisible(visible: Boolean)


        /**
         * 设置支付失败按钮是否可见
         *
         * @param visible
         */
        fun setPayFailedBtnVisible(visible: Boolean)

        /**
         * 设置校验付款结果按钮的文字描述
         *
         * @param text
         */
        fun setCheckPayResultBtnText(text: String)

        /**
         * 设置校验付款结果按钮是否可以点击
         *
         * @param enable
         */
        fun setCheckPayResultBtnEnable(enable: Boolean)

        /**
         * 支付成功关闭页面
         */
        fun closePageByPaySuccess()
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 创建微信支付订单
         *
         */
        fun createPayOrderByWx(reqBean: QJCodeLenderConfirmReqBean)

        /**
         * 再次进行支付
         */
        fun payAgain()

        /**
         * 校验支付结果
         */
        fun checkPayResult()
    }
}