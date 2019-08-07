package com.hm.iou.pay.business.timecard

import com.hm.iou.base.mvp.BaseContract

/**
 * 贵宾卡详情购买
 */
interface VipCardContract {

    interface View : BaseContract.BaseView {

        /**
         * 支付成功关闭页面
         */
        fun closePageByPaySuccess()

        /**
         * 显示未检测到支付结果对话框
         */
        fun showNotCheckResultDialog()
    }

    interface Presenter : BaseContract.BasePresenter {

        fun createPayOrderByWx(packageCode: String)

        fun innerPayOrderByWx(packageCode: String)

    }

}