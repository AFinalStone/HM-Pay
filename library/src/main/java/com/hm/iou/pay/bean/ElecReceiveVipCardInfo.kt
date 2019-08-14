package com.hm.iou.pay.bean

import java.io.Serializable

data class ElecReceiveVipCardInfo(
        val allCount: Int?,//套餐卡总数
        val beginTime: String?,
        val content: String?,
        val endTime: String?,
        val outOfDay: Int,//时效天数
        val surplusCount: Int,//套餐卡剩余次数
        val usedCount: Int,//使用次数
        val yuanPer: String,//单价
        val actualPrice: Int,//总价,单位分
        val actualPriceY: String//总价字符串
) : Serializable