package com.hm.iou.pay.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by syl on 2019/9/10.
 */
data class CreatePublishQJCodeOrderResBean(
        val appid: String,
        val orderId: String,
        val noncestr: String,
        @SerializedName("package")
        val packageValue: String,
        val partnerid: String,
        val prepayid: String,
        val sign: String,
        val timestamp: String
)