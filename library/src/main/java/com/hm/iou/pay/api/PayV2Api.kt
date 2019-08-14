package com.hm.iou.create.api

import com.hm.iou.network.HttpReqManager
import com.hm.iou.pay.bean.ElecReceiveVipCardConsumerBean
import com.hm.iou.pay.bean.UserIsHaveElecReceiveVipCardResBean
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

    }


}