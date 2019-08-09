package com.hm.iou.pay.business.timecard.view

import android.os.Bundle
import android.view.Gravity
import com.google.gson.Gson
import com.hm.iou.base.BaseActivity
import com.hm.iou.pay.R.layout.pay_activity_vip_card
import com.hm.iou.pay.bean.VipCardPackageBean
import com.hm.iou.pay.business.timecard.VipCardPackageDetailContract
import com.hm.iou.pay.business.timecard.VipCardPackageDetailPresenter
import com.hm.iou.pay.comm.getVipCardBgResId
import com.hm.iou.tools.kt.*
import com.hm.iou.uikit.dialog.HMAlertDialog
import kotlinx.android.synthetic.main.pay_activity_vip_card.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 贵宾卡详情购买页面
 */
class VipCardPackageDetailActivity : BaseActivity<VipCardPackageDetailPresenter>(), VipCardPackageDetailContract.View {

    companion object {
        const val EXTRA_KEY_PACKAGE_INFO_JSON = "package_info"
    }

    private var mVipCardPackageInfoJson: String? by extraDelegate(EXTRA_KEY_PACKAGE_INFO_JSON, null)
    private var mVipCardPackageInfo: VipCardPackageBean? = null

    override fun initPresenter(): VipCardPackageDetailPresenter = VipCardPackageDetailPresenter(this, this)

    override fun getLayoutId(): Int = pay_activity_vip_card

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mVipCardPackageInfoJson = bundle.getValue(EXTRA_KEY_PACKAGE_INFO_JSON)
        }
        try {
            mVipCardPackageInfo = Gson().fromJson(mVipCardPackageInfoJson, VipCardPackageBean::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (mVipCardPackageInfo == null) {
            finish()
            return
        }
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_KEY_PACKAGE_INFO_JSON, mVipCardPackageInfoJson)
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
        val amount: Float = (mVipCardPackageInfo?.actualPrice ?: 0) / 100f
        tv_card_money.text = if (amount.toInt() < amount) amount.toString() else amount.toInt().toString()

        rl_vip_card_info.setBackgroundResource(getVipCardBgResId(mVipCardPackageInfo?.content
                ?: ""))

        btn_card_pay.clickWithDuration {
            mVipCardPackageInfo?.packageCode?.let {
                mPresenter.createPayOrderByWx(it)
            }
        }

        btn_card_pay.longClick {
            mVipCardPackageInfo?.packageCode?.let {
                mPresenter.innerPayOrderByWx(it)
            }
            return@longClick true
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