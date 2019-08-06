package com.hm.iou.pay.business.timecard.view

import com.hm.iou.pay.bean.VipCardPackageBean

/**
 * 贵宾卡信息
 */
interface IVipCardItem {

    fun getName(): String?

    fun getAmountPerOnce(): String?

    fun getDesc(): String?

    fun isOverBalance(): Boolean

    fun getData(): VipCardPackageBean
}