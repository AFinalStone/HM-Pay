package com.hm.iou.pay.api

import com.hm.iou.pay.bean.*
import com.hm.iou.pay.bean.req.CreateLawyerLetterReqBean
import com.hm.iou.pay.bean.req.CreatePublishQJCodeOrderReqBean
import com.hm.iou.pay.bean.req.QJCodeLenderConfirmReqBean
import com.hm.iou.sharedata.model.BaseResponse
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by syl on 2019/8/5.
 */
interface PayV2Service {


    @GET("/pay/iou/signOrder/v1/getVipCardUseInfo")
    fun userIsHaveElecReceiveVipCardInfo(@Query("scene") scene: Int): Flowable<BaseResponse<UserIsHaveElecReceiveVipCardResBean>>

    @GET("/api/iou/consumer/v1/getVipCardUseInfoItems")
    fun getElecReceiveVipCardConsumerListData(): Flowable<BaseResponse<List<ElecReceiveVipCardConsumerBean>>>

    @GET("/api/square/v1/order/publish/getApplyStatus")
    fun getPublishQJCodeStatus(@Query("squareApplyId") squareApplyId: String): Flowable<BaseResponse<Int>>

    @POST("/api/square/v1/order/publish/createOrder")
    fun createPublishQJCodeOrder(@Body reqBean: CreatePublishQJCodeOrderReqBean, @Header("hmpop") flag: String): Flowable<BaseResponse<CreatePublishQJCodeOrderResBean>>

    @POST("/api/square/v1/moneyV2/loaner/loanerConfirm")
    fun qjCodeLenderConfirm(@Body reqBean: QJCodeLenderConfirmReqBean, @Header("hmpop") flag: String): Flowable<BaseResponse<QJCodeLenderConfirmResBean>>

    @GET("/api/square/v1/moneyV2/getContentStep")
    fun getQjCodeLenderConfirmStatus(@Query("contentId") contentId: Int): Flowable<BaseResponse<Int>>

    @POST("/api/lawyer/v1/order/createOrder")
    suspend fun createLawyerLetterOrder(@Body reqBean: CreateLawyerLetterReqBean, @Header("hmpop") flag: String): BaseResponse<CreateLawyerLetterOrderResBean>

    @GET("/api/lawyer/v1/order/getBillStatus")
    suspend fun getLawyerLetterOrderStatus(@Query("billId") billId: String): BaseResponse<Int>

    @GET("/pay/iou/v1/queryOrderWhilePaying")
    suspend fun queryOrderPayState(@Query("orderId") orderId: String): BaseResponse<String>

}