package com.hm.iou.pay.business.timecard

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.pay.bean.ElecReceiveVipCardConsumerBean
import com.hm.iou.pay.bean.ElecReceiveVipCardInfo

class VipCardDetailContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示初始化View
         */
        fun showInitView()

        /**
         * 隐藏初始化View
         */
        fun hideInitView()

        /**
         * 显示初始化失败
         */
        fun showInitFailed(msg: String?)

        /**
         * 显示消费记录
         */
        fun showRecordList(list: List<ElecReceiveVipCardConsumerBean>?)

        /**
         * 显示vip卡详情
         */
        fun showPayByVipCardView(vipCardInfo: ElecReceiveVipCardInfo?)
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取VIP贵宾卡信息
         */
        fun getVipCardInfo()

    }
}