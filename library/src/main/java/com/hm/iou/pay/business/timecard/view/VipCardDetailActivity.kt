package com.hm.iou.pay.business.timecard.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.base.BaseActivity
import com.hm.iou.pay.R
import com.hm.iou.pay.bean.ElecReceiveVipCardConsumerBean
import com.hm.iou.pay.bean.ElecReceiveVipCardInfo
import com.hm.iou.pay.business.timecard.VipCardDetailContract
import com.hm.iou.pay.business.timecard.presenter.VipCardDetailPresenter
import kotlinx.android.synthetic.main.pay_activity_vip_card_detail.*
import java.text.SimpleDateFormat

/**
 * Created by syl on 2019/8/14.
 */
class VipCardDetailActivity : BaseActivity<VipCardDetailPresenter>(), VipCardDetailContract.View {

    private var mAdapter: RecordAdapter = RecordAdapter()

    override fun initPresenter(): VipCardDetailPresenter = VipCardDetailPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.pay_activity_vip_card_detail

    override fun initEventAndData(p0: Bundle?) {
        rv_vip_card.layoutManager = LinearLayoutManager(mContext)
        rv_vip_card.adapter = mAdapter
        mPresenter.getVipCardInfo()
    }

    override fun showInitView() {
        loading.visibility = VISIBLE
        loading.showDataLoading()
    }

    override fun hideInitView() {
        loading.stopLoadingAnim()
        loading.visibility = GONE
    }

    override fun showInitFailed(msg: String?) {
        loading.visibility = VISIBLE
        loading.showDataFail(msg, {
            mPresenter.getVipCardInfo()
        })
    }


    override fun showRecordList(list: List<ElecReceiveVipCardConsumerBean>?) {
        mAdapter.setNewData(list)
    }

    override fun showPayByVipCardView(vipCardInfo: ElecReceiveVipCardInfo?) {
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.pay_layout_elec_receive_pay_by_vip_card_header, null)
        vipCardInfo?.let {
            viewHeader.findViewById<TextView>(R.id.tv_card_name).text = it.content
            viewHeader.findViewById<TextView>(R.id.tv_card_money).text = it.actualPriceY
            viewHeader.findViewById<TextView>(R.id.tv_card_limit).text = "使用条件：最多签%s份（最低%s元/份）".format(it.allCount, it.yuanPer)
            try {
                val sf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val sf02 = SimpleDateFormat("yyyy.MM.dd")
                val beginTime = sf02.format(sf01.parse(it.beginTime))
                val endTime = sf02.format(sf01.parse(it.endTime))
                viewHeader.findViewById<TextView>(R.id.tv_card_time_range).text = "使用期限：%s - %s（%d天）".format(beginTime, endTime, it.outOfDay)
            } catch (e: Exception) {

            }
            val usedCount = it.usedCount
            if (0 == usedCount) {
                viewHeader.findViewById<TextView>(R.id.tv_record_title).text = "使用记录：暂无"
            } else {
                viewHeader.findViewById<TextView>(R.id.tv_record_title).text = "使用记录（%d/%d）：".format(it.usedCount, it.allCount)
            }
        }
        mAdapter.addHeaderView(viewHeader)
    }

    class RecordAdapter : BaseQuickAdapter<ElecReceiveVipCardConsumerBean, BaseViewHolder>(R.layout.pay_layout_elec_receive_pay_by_vip_card_adapter) {
        private val sf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private val sf02 = SimpleDateFormat("yyyy.MM.dd HH:mm")

        override fun convert(helper: BaseViewHolder?, item: ElecReceiveVipCardConsumerBean?) {
            item?.let {
                //索引
                val order: String = if (item.order == null) " " else item.order.toString()
                helper?.setText(R.id.tv_index, order)
                //时间加签收人
                try {
                    val time = sf01.parse(item.consumeDate ?: "")
                    val content = sf02.format(time) + "       " + item.content
                    helper?.setText(R.id.tv_record, content)
                } catch (e: Exception) {

                }

            }
        }

    }
}