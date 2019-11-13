package com.hm.iou.pay.bean

import com.google.gson.annotations.SerializedName

data class CreateLawyerLetterOrderResBean(

        val orderId: String,
        val noncestr: String,
        @SerializedName("package")
        val packageValue: String,
        val partnerid: String,
        val prepayid: String,
        val sign: String,
        val timestamp: String
)