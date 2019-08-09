@file:JvmName("NavigationHelper")

package com.hm.iou

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.hm.iou.pay.bean.VipCardPackageBean
import com.hm.iou.pay.business.timecard.view.VipCardPackageDetailActivity

/**
 * 贵宾卡支付页面
 */
fun toVipCardPayPage(context: Context, vipCard: VipCardPackageBean) {
    var vipCardJson: String? = null
    try {
        vipCardJson = Gson().toJson(vipCard)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    vipCardJson?.let {
        val intent = Intent(context, VipCardPackageDetailActivity::class.java)
        intent.putExtra(VipCardPackageDetailActivity.EXTRA_KEY_PACKAGE_INFO_JSON, vipCardJson)
        context.startActivity(intent)
    }
}