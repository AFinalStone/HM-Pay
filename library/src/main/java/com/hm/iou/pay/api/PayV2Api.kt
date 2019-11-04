package com.hm.iou.pay.api

import com.hm.iou.network.HttpReqManager
import com.hm.iou.pay.bean.CreatePublishQJCodeOrderResBean
import com.hm.iou.pay.bean.ElecReceiveVipCardConsumerBean
import com.hm.iou.pay.bean.QJCodeLenderConfirmResBean
import com.hm.iou.pay.bean.UserIsHaveElecReceiveVipCardResBean
import com.hm.iou.pay.bean.req.CreatePublishQJCodeOrderReqBean
import com.hm.iou.pay.bean.req.QJCodeLenderConfirmReqBean
import com.hm.iou.sharedata.model.BaseResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by syl on 2019/8/5.
 */
class PayV2Api {

    companion object {

        private fun getService(): PayV2Service {
            return HttpReqManager.getInstance().getService(PayV2Service::class.java)
        }

        /**
         * 判断用户是否拥有可用的贵宾卡
         *
         * @return
         */
        fun userIsHaveElecReceiveVipCardInfo(scene: Int): Flowable<BaseResponse<UserIsHaveElecReceiveVipCardResBean>> {
            return getService().userIsHaveElecReceiveVipCardInfo(scene).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 获取吕约收条贵宾卡消费记录
         *
         * @return
         */
        fun getElecReceiveVipCardConsumerListData(): Flowable<BaseResponse<List<ElecReceiveVipCardConsumerBean>>> {
            return getService().getElecReceiveVipCardConsumerListData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 创建求借码发布订单
         */
        fun createPublishQJCodeOrder(reqBean: CreatePublishQJCodeOrderReqBean): Flowable<BaseResponse<CreatePublishQJCodeOrderResBean>> {
            return getService().createPublishQJCodeOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 查询求借码是否发布成功
         */
        fun getPublishQJCodeStatus(squareApplyId: String): Flowable<BaseResponse<Int>> {
            return getService().getPublishQJCodeStatus(squareApplyId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 出借人输入签约密码/支付签章之后 确认签署
         */
        fun qjCodeLenderConfirm(reqBean: QJCodeLenderConfirmReqBean): Flowable<BaseResponse<QJCodeLenderConfirmResBean>> {
            return getService().qjCodeLenderConfirm(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 查询求借码出借人是否确认成功
         */
        fun getQjCodeLenderConfirmStatus(contentId: Int): Flowable<BaseResponse<Int>> {
            return getService().getQjCodeLenderConfirmStatus(contentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }


}