package com.hm.iou.create.api

import com.hm.iou.pay.bean.ElecReceiveVipCardConsumerBean
import com.hm.iou.pay.bean.UserIsHaveElecReceiveVipCardResBean
import com.hm.iou.sharedata.model.BaseResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by syl on 2019/8/5.
 */
interface PayV2Service {


    @GET("/pay/iou/signOrder/v1/getVipCardUseInfo")
    fun userIsHaveElecReceiveVipCardInfo(@Query("scene") scene: Int): Flowable<BaseResponse<UserIsHaveElecReceiveVipCardResBean>>

    @GET("/api/iou/consumer/v1/getVipCardUseInfoItems")
    fun getElecReceiveVipCardConsumerListData(): Flowable<BaseResponse<List<ElecReceiveVipCardConsumerBean>>>


}