package com.hm.iou.pay.business.qjcode

import android.os.Bundle
import android.view.View
import com.hm.iou.base.BaseActivity
import com.hm.iou.pay.R
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.pay_activity_pay_publish_qj_code.*

/**
 * Created by syl on 2019/8/14.
 */
class PayPublishQJCodeActivity : BaseActivity<PayPublishQJCodePresenter>(), PayPublishQJCodeContract.View {


    companion object {
        const val EXTRA_QJ_CODE_APPLY_ID = "qj_code_apply_id"         // 求借码广场申请id
        const val EXTRA_QJ_CODE_APPLY_PUBLISH_TYPE = "qj_code_apply_publish_type"         // 求借码广场申请发布的类型
        const val EXTRA_PACKAGE_TITLE = "package_title"
        const val EXTRA_PACKAGE_MONEY = "package_money"
        const val EXTRA_PACKAGE_SUB_TITLE = "package_sub_title"
        const val EXTRA_PACKAGE_CONTENT = "package_content"
        const val EXTRA_INNER_USER = "inner_user"                   //是否触发内部白名单支付判断，当且仅当为1时，才会触发

        const val FLAG_INNER_USER = "1"
    }

    private var mQJCodeApplyPublishId: String? by extraDelegate(EXTRA_QJ_CODE_APPLY_ID, null)
    private var mQJCodeApplyPublishType: String? by extraDelegate(EXTRA_QJ_CODE_APPLY_PUBLISH_TYPE, null)
    private var mPackageTitle: String? by extraDelegate(EXTRA_PACKAGE_TITLE, null)
    private var mPackageMoney: String? by extraDelegate(EXTRA_PACKAGE_MONEY, null)
    private var mPackageSubTitle: String? by extraDelegate(EXTRA_PACKAGE_SUB_TITLE, null)
    private var mPackageContent: String? by extraDelegate(EXTRA_PACKAGE_CONTENT, null)
    private var mInnerUser: String? by extraDelegate(EXTRA_INNER_USER, null)

    override fun initPresenter(): PayPublishQJCodePresenter = PayPublishQJCodePresenter(this, this)

    override fun getLayoutId(): Int = R.layout.pay_activity_pay_publish_qj_code

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mQJCodeApplyPublishId = bundle.getValue(EXTRA_QJ_CODE_APPLY_ID)
            mQJCodeApplyPublishType = bundle.getValue(EXTRA_QJ_CODE_APPLY_PUBLISH_TYPE)
            mPackageTitle = bundle.getValue(EXTRA_PACKAGE_TITLE)
            mPackageMoney = bundle.getValue(EXTRA_PACKAGE_MONEY)
            mPackageSubTitle = bundle.getValue(EXTRA_PACKAGE_SUB_TITLE)
            mPackageContent = bundle.getValue(EXTRA_PACKAGE_CONTENT)
            mInnerUser = bundle.getValue(EXTRA_INNER_USER)
        }
        tv_package_title.text = mPackageTitle ?: ""
        tv_package_money.text = getString(R.string.uikit_money) + (mPackageMoney ?: "")
        tv_package_sub_title.text = mPackageSubTitle ?: ""
        tv_package_content.text = mPackageContent ?: ""
        rl_payByWX.setOnClickListener {
            try {
                val qjCodeApplyPublishId = mQJCodeApplyPublishId ?: ""
                val qjCodeApplyPublishType: Int = mQJCodeApplyPublishType?.toInt() ?: 0
                mPresenter.createPayOrderByWx(qjCodeApplyPublishId, qjCodeApplyPublishType, mInnerUser == FLAG_INNER_USER)
            } catch (e: Exception) {
                toastErrorMessage("参数异常")
                e.printStackTrace()
            }
        }
        iv_close.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_QJ_CODE_APPLY_ID, mQJCodeApplyPublishId)
        outState?.putValue(EXTRA_QJ_CODE_APPLY_PUBLISH_TYPE, mQJCodeApplyPublishType)
        outState?.putValue(EXTRA_PACKAGE_TITLE, mPackageTitle)
        outState?.putValue(EXTRA_PACKAGE_MONEY, mPackageMoney)
        outState?.putValue(EXTRA_PACKAGE_SUB_TITLE, mPackageSubTitle)
        outState?.putValue(EXTRA_PACKAGE_CONTENT, mPackageContent)
        outState?.putValue(EXTRA_INNER_USER, mInnerUser)
    }

    //关闭Activity的切换动画
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    override fun setPayByWxBtnVisible(visible: Boolean) {
        if (visible) {
            rl_payByWX.visibility = View.VISIBLE
        } else {
            rl_payByWX.visibility = View.GONE
        }
    }

    override fun setCheckPayResultBtnVisible(visible: Boolean) {
        if (visible) {
            ll_checkPayResult.visibility = View.VISIBLE
            btn_checkPayResult.visibility = View.VISIBLE
            btn_payFailed.visibility = View.GONE
            btn_checkPayResult.setOnClickListener { mPresenter.checkPayResult() }
        } else {
            ll_checkPayResult.visibility = View.GONE
        }
    }

    override fun setPayFailedBtnVisible(visible: Boolean) {
        if (visible) {
            ll_checkPayResult.visibility = View.VISIBLE
            btn_payFailed.visibility = View.VISIBLE
            btn_checkPayResult.visibility = View.GONE
            btn_payFailed.setOnClickListener { mPresenter.payAgain() }
        } else {
            ll_checkPayResult.visibility = View.GONE
        }
    }

    override fun setCheckPayResultBtnText(text: String) {
        btn_checkPayResult.text = text
    }

    override fun setCheckPayResultBtnEnable(enable: Boolean) {
        btn_checkPayResult.isEnabled = enable
    }

    override fun closePageByPaySuccess() {
        setResult(RESULT_OK)
        finish()
    }

}