@file:JvmName("NavigationHelper")
package com.hm.iou

import android.content.Context
import com.hm.iou.pay.bean.VipCardPackageBean
import com.hm.iou.pay.business.timecard.view.VipCardPackageDetailActivity
import com.hm.iou.tools.kt.startActivity

/**
 * 贵宾卡支付页面
 */
fun toVipCardPayPage(context: Context, vipCard: VipCardPackageBean) {
    context.startActivity<VipCardPackageDetailActivity>(
            VipCardPackageDetailActivity.EXTRA_KEY_PACKAGE_INFO to vipCard
    )
}