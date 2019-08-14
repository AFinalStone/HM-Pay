package com.hm.iou.pay.business.timecard.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.create.api.PayV2Api
import com.hm.iou.pay.bean.ElecReceiveVipCardConsumerBean
import com.hm.iou.pay.bean.UserIsHaveElecReceiveVipCardResBean
import com.hm.iou.pay.business.timecard.VipCardDetailContract
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by syl on 2019/8/14.
 */
class VipCardDetailPresenter(context: Context, view: VipCardDetailContract.View) : MvpActivityPresenter<VipCardDetailContract.View>(context, view), VipCardDetailContract.Presenter {

    override fun getVipCardInfo() {
        mView.showInitView()
        PayV2Api.userIsHaveElecReceiveVipCardInfo(7)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<UserIsHaveElecReceiveVipCardResBean>(mView) {

                    override fun handleResult(bean: UserIsHaveElecReceiveVipCardResBean?) {
                        bean?.let {
                            if (bean.hasValidCard != null && bean.hasValidCard) {
                                if (bean.useInfo == null) {
                                    mView.closeCurrPage()
                                } else {
                                    mView.showPayByVipCardView(bean.useInfo)
                                    getElecReceiveVipCardConsumerListData()
                                }
                            } else {
                                mView.closeCurrPage()
                            }
                        }
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.showInitFailed(p2)
                    }

                    override fun isShowBusinessError(): Boolean {
                        return false
                    }

                    override fun isShowCommError(): Boolean {
                        return false
                    }
                })

    }

    /**
     * 获取vip消费记录
     */
    fun getElecReceiveVipCardConsumerListData() {
        PayV2Api.getElecReceiveVipCardConsumerListData()
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<List<ElecReceiveVipCardConsumerBean>>(mView) {

                    override fun handleResult(list: List<ElecReceiveVipCardConsumerBean>?) {
                        mView.hideInitView()
                        mView.showRecordList(list)
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.showInitFailed(p2)
                    }

                    override fun isShowBusinessError(): Boolean {
                        return false
                    }

                    override fun isShowCommError(): Boolean {
                        return false
                    }
                })
    }
}