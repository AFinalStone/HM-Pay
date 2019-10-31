package com.hm.iou.pay.bean.req

/**
 * @author : 借条管家-shilei
 * @version : 0.0.1
 * @Date : 2019-10-30 19:22
 * @E-Mail : afinalstone@foxmail.com
 */
data class QJCodeLenderConfirmReqBean(
        val loanerAccount: String,
        val loanerEmail: String,
        val loanerRemitWay: Int,
        val sealId: String,
        val squareApplyId: String,
        val transDeadLine: String,
        val transPswd: String
)