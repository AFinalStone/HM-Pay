package com.hm.iou.pay.comm

import com.hm.iou.uikit.R

/**
 * 根据贵宾卡的名称，选择对应的背景图片
 */
fun getVipCardBgResId(name: String): Int {
    return when(name) {
        "季卡" -> R.drawable.uikit_bg_vip_card_quarter
        "半年卡" -> R.drawable.uikit_bg_vip_card_half_year
        "年卡" -> R.drawable.uikit_bg_vip_card_year
        else -> R.drawable.uikit_bg_vip_card_quarter
    }
}