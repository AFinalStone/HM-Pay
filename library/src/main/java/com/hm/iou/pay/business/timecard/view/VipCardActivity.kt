package com.hm.iou.pay.business.timecard.view

import android.os.Bundle
import android.view.Gravity
import com.hm.iou.base.BaseActivity
import com.hm.iou.pay.R.layout.pay_activity_vip_card
import com.hm.iou.pay.bean.VipCardPackageBean
import com.hm.iou.pay.business.timecard.VipCardContract
import com.hm.iou.pay.business.timecard.VipCardPresenter
import com.hm.iou.pay.comm.getVipCardBgResId
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.pay_activity_vip_card.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 贵宾卡详情购买页面
 */
class VipCardActivity : BaseActivity<VipCardPresenter>(), VipCardContract.View {

    companion object {
        const val EXTRA_KEY_PACKAGE_INFO = "package_info"
    }

    private var mVipCardPackageInfo: VipCardPackageBean? by extraDelegate(EXTRA_KEY_PACKAGE_INFO, null)

    override fun initPresenter(): VipCardPresenter = VipCardPresenter(this, this)

    override fun getLayoutId(): Int = pay_activity_vip_card

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mVipCardPackageInfo = bundle.getValue(EXTRA_KEY_PACKAGE_INFO)
        }

        if (mVipCardPackageInfo == null) {
            finish()
            return
        }

        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_PACKAGE_INFO, mVipCardPackageInfo)
    }

    private fun initViews() {
        tv_card_name.text = mVipCardPackageInfo?.content
        tv_card_record.text = "使用条件：最多签${mVipCardPackageInfo?.rechargeSign
                ?: 0}份 （最低${mVipCardPackageInfo?.yuanPer ?: ""}元/份）"
        val cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val startDate = sdf.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, mVipCardPackageInfo?.outOfDay ?: 0)
        val endDate = sdf.format(cal.time)
        tv_card_timerange.text = "使用期限：${startDate} - ${endDate}（${mVipCardPackageInfo?.outOfDay
                ?: 0}天）"
        tv_card_money.text = ((mVipCardPackageInfo?.actualPrice ?: 0) / 100f).toString()

        rl_vip_card_info.setBackgroundResource(getVipCardBgResId(mVipCardPackageInfo?.content
                ?: ""))

        btn_card_pay.clickWithDuration {
            mVipCardPackageInfo?.packageCode?.let {
                mPresenter.createPayOrderByWx(it)
            }
        }
    }

    override fun closePageByPaySuccess() {
        setResult(RESULT_OK)
        finish()
    }

    override fun showNotCheckResultDialog() {
        HMAlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("未检测到支付结果，请重新支付")
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton("重新支付")
                .setNegativeButton("取消")
                .setOnClickListener(object : HMAlertDialog.OnClickListener {
                    override fun onPosClick() {
                        mVipCardPackageInfo?.packageCode?.let {
                            mPresenter.createPayOrderByWx(it)
                        }
                    }

                    override fun onNegClick() {

                    }
                })
                .create();
    }
}