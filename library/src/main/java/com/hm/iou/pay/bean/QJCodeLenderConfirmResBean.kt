package com.hm.iou.pay.bean

import com.google.gson.annotations.SerializedName

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-10-30 19:22
 * @E-Mail : afinalstone@foxmail.com
 */
data class QJCodeLenderConfirmResBean(
        val contentId: Int?,
        val wxPayAppParamResp: WxPayAppParamResp?
)

data class WxPayAppParamResp(
        val appid: String?,
        val noncestr: String?,
        val orderId: String?,
        @SerializedName("package")
        val packageValue: String?,
        val partnerid: String?,
        val prepayid: String?,
        val sign: String?,
        val timestamp: String?
)