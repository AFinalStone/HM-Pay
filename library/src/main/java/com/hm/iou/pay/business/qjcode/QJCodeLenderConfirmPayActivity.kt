package com.hm.iou.pay.business.qjcode

import android.os.Bundle
import android.view.View
import com.hm.iou.base.BaseActivity
import com.hm.iou.pay.R
import com.hm.iou.pay.bean.req.QJCodeLenderConfirmReqBean
import com.hm.iou.tools.Md5Util
import com.hm.iou.tools.kt.extraDelegate
import com.hm.iou.tools.kt.getValue
import com.hm.iou.tools.kt.putValue
import kotlinx.android.synthetic.main.pay_activity_qj_code_lender_confirm_pay.*

/**
 * Created by syl on 2019/8/14.
 */
class QJCodeLenderConfirmPayActivity : BaseActivity<QJCodeLenderConfirmPayPresenter>(), QJCodeLenderConfirmPayContract.View {


    companion object {
        const val EXTRA_PACKAGE_TITLE = "package_title"
        const val EXTRA_PACKAGE_MONEY = "package_money"
        const val EXTRA_PACKAGE_SUB_TITLE = "package_sub_title"
        const val EXTRA_PACKAGE_CONTENT = "package_content"

        const val EXTRA_PAY_LOANER_EMAIL = "loaner_email"
        const val EXTRA_PAY_LOANER_ACCOUNT = "loaner_account"
        const val EXTRA_PAY_LOANER_REMIT_WAY = "loaner_remit_way"
        const val EXTRA_PAY_SEAL_ID = "seal_id"
        const val EXTRA_PAY_SQUARE_APPLY_ID = "square_apply_id"
        const val EXTRA_PAY_TRANS_DEAD_LINE = "trans_dead_line"
        const val EXTRA_PAY_TRANS_PSWD = "trans_pswd"
    }

    private var mPackageTitle: String? by extraDelegate(EXTRA_PACKAGE_TITLE, null)
    private var mPackageMoney: String? by extraDelegate(EXTRA_PACKAGE_MONEY, null)
    private var mPackageSubTitle: String? by extraDelegate(EXTRA_PACKAGE_SUB_TITLE, null)
    private var mPackageContent: String? by extraDelegate(EXTRA_PACKAGE_CONTENT, null)

    private var squareApplyId: String? by extraDelegate(EXTRA_PAY_SQUARE_APPLY_ID, null)
    private var loanerEmail: String? by extraDelegate(EXTRA_PAY_LOANER_EMAIL, null)
    private var loanerAccount: String? by extraDelegate(EXTRA_PAY_LOANER_ACCOUNT, null)
    private var loanerRemitWay: String? by extraDelegate(EXTRA_PAY_LOANER_REMIT_WAY, null)
    private var sealId: String? by extraDelegate(EXTRA_PAY_SEAL_ID, null)
    private var transDeadLine: String? by extraDelegate(EXTRA_PAY_TRANS_DEAD_LINE, null)
    private var transPswd: String? by extraDelegate(EXTRA_PAY_TRANS_PSWD, null)

    override fun initPresenter(): QJCodeLenderConfirmPayPresenter = QJCodeLenderConfirmPayPresenter(this, this)

    override fun getLayoutId(): Int = R.layout.pay_activity_qj_code_lender_confirm_pay

    override fun initEventAndData(bundle: Bundle?) {
        if (bundle != null) {
            mPackageTitle = bundle.getValue(EXTRA_PACKAGE_TITLE)
            mPackageMoney = bundle.getValue(EXTRA_PACKAGE_MONEY)
            mPackageSubTitle = bundle.getValue(EXTRA_PACKAGE_SUB_TITLE)
            mPackageContent = bundle.getValue(EXTRA_PACKAGE_CONTENT)

            squareApplyId = bundle.getValue(EXTRA_PAY_SQUARE_APPLY_ID)
            loanerEmail = bundle.getValue(EXTRA_PAY_LOANER_EMAIL)
            loanerAccount = bundle.getValue(EXTRA_PAY_LOANER_ACCOUNT)
            loanerRemitWay = bundle.getValue(EXTRA_PAY_LOANER_REMIT_WAY)
            sealId = bundle.getValue(EXTRA_PAY_SEAL_ID)
            transDeadLine = bundle.getValue(EXTRA_PAY_TRANS_DEAD_LINE)
            transPswd = bundle.getValue(EXTRA_PAY_TRANS_PSWD)
        }
        tv_package_title.text = mPackageTitle ?: ""
        tv_package_money.text = getString(R.string.uikit_money) + (mPackageMoney ?: "")
        tv_package_sub_title.text = mPackageSubTitle ?: ""
        tv_package_content.text = mPackageContent ?: ""
        rl_payByWX.setOnClickListener {
            try {
                val squareApplyId: String = squareApplyId ?: ""
                val loanerEmail: String = loanerEmail ?: ""
                val loanerAccount: String = loanerAccount ?: ""
                val loanerRemitWay: Int = loanerRemitWay?.toInt() ?: 0
                val transDeadLine: String = transDeadLine ?: ""
                val transPswd: String = Md5Util.getMd5ByString(transPswd)
                val sealId: String = sealId ?: ""
                val reqBean = QJCodeLenderConfirmReqBean(loanerAccount, loanerEmail, loanerRemitWay, sealId, squareApplyId, transDeadLine, transPswd)
                mPresenter.createPayOrderByWx(reqBean)
            } catch (e: Exception) {
                e.printStackTrace()
                toastErrorMessage("发生未知异常，请稍后重试")
            }

        }
        iv_close.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putValue(EXTRA_PACKAGE_TITLE, mPackageTitle)
        outState?.putValue(EXTRA_PACKAGE_MONEY, mPackageMoney)
        outState?.putValue(EXTRA_PACKAGE_SUB_TITLE, mPackageSubTitle)
        outState?.putValue(EXTRA_PACKAGE_CONTENT, mPackageContent)
        outState?.putValue(EXTRA_PAY_SQUARE_APPLY_ID, squareApplyId)
        outState?.putValue(EXTRA_PAY_LOANER_EMAIL, loanerEmail)
        outState?.putValue(EXTRA_PAY_LOANER_ACCOUNT, loanerAccount)
        outState?.putValue(EXTRA_PAY_LOANER_REMIT_WAY, loanerRemitWay)
        outState?.putValue(EXTRA_PAY_SEAL_ID, sealId)
        outState?.putValue(EXTRA_PAY_TRANS_DEAD_LINE, transDeadLine)
        outState?.putValue(EXTRA_PAY_TRANS_PSWD, transPswd)
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