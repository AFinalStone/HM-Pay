@file:JvmName("NavigationHelper")

package com.hm.iou

import android.content.Context
import com.google.gson.Gson
import com.hm.iou.pay.bean.VipCardPackageBean
import com.hm.iou.pay.business.timecard.view.VipCardPackageDetailActivity
import com.hm.iou.tools.kt.startActivity

/**
 * 贵宾卡支付页面
 */
fun toVipCardPayPage(context: Context, vipCard: VipCardPackageBean) {
    val vipCardPackageBeanJson = Gson().toJson(vipCard)
    context.startActivity<VipCardPackageDetailActivity>(
            VipCardPackageDetailActivity.EXTRA_KEY_PACKAGE_INFO_JSON to vipCardPackageBeanJson
    )
}