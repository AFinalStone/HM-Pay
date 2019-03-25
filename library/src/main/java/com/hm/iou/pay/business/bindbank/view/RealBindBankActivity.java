package com.hm.iou.pay.business.bindbank.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.business.bindbank.BankCardTextWatcher;
import com.hm.iou.pay.business.bindbank.RealBindBinkContract;
import com.hm.iou.pay.business.bindbank.presenter.RealBindBankPresenter;
import com.hm.iou.pay.event.CancelBindBankEvent;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.event.UpdateUserInfoEvent;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by hjy on 2018/7/16.
 */

public class RealBindBankActivity extends BaseActivity<RealBindBankPresenter> implements RealBindBinkContract.View {

    @BindView(R2.id.topBar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.tv_fourelement_name)
    TextView mTvName;
    @BindView(R2.id.et_fourelement_cardno)
    EditText mEtCardNo;
    @BindView(R2.id.iv_fourelement_cardno_i)
    ImageView mIvCardNoAbout;
    @BindView(R2.id.et_fourelement_mobile)
    EditText mEtMobile;
    @BindView(R2.id.iv_fourelement_mobile_i)
    ImageView mIvMobileAbout;
    @BindView(R2.id.btn_four_element_submit)
    Button mBtnSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_four_elements_realname;
    }

    @Override
    protected RealBindBankPresenter initPresenter() {
        return new RealBindBankPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        TraceUtil.onEvent(this, "bank_page_show");
        mTopBarView.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/login/customer_service")
                        .navigation(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mTopBarView.setOnBackClickListener(new HMTopBarView.OnTopBarBackClickListener() {
            @Override
            public void onClickBack() {
                doGiveUpRealName();
            }
        });

        RxTextView.textChanges(mEtMobile).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mPresenter.checkUserInputValue(mEtCardNo.getText().toString(), mEtMobile.getText().toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {

            }
        });
        BankCardTextWatcher.bind(mEtCardNo, 23, new BankCardTextWatcher.OnTextChangedListener() {
            @Override
            public void onTextChanged() {
                mPresenter.checkUserInputValue(mEtCardNo.getText().toString(), mEtMobile.getText().toString());
            }
        });

        mPresenter.getUserRealName();
    }

    @Override
    public void onBackPressed() {
        doGiveUpRealName();
    }

    @OnClick(value = {R2.id.iv_fourelement_name_i, R2.id.iv_fourelement_cardno_i, R2.id.iv_fourelement_mobile_i, R2.id.btn_four_element_submit})
    void onClick(View v) {
        if (v.getId() == R.id.iv_fourelement_name_i) {
            new HMAlertDialog.Builder(this)
                    .setMessage("信息必须是本人资料")
                    .setMessageGravity(Gravity.CENTER)
                    .setNegativeButton("知道了")
                    .create().show();
        } else if (v.getId() == R.id.iv_fourelement_cardno_i) {
            new HMAlertDialog.Builder(this)
                    .setMessage(mPresenter.isCardNoInputError() ? "银行卡必须“62”或“60”开头" : "银行卡必须“62”或“60”开头16-19位纯数字")
                    .setNegativeButton("知道了").create().show();
        } else if (v.getId() == R.id.iv_fourelement_mobile_i) {
            new HMAlertDialog.Builder(this)
                    .setMessage(mPresenter.isMobileInputError() ? "请使用正常号段的手机号码" : "必须为本人的11位纯数字手机号")
                    .setMessageGravity(Gravity.CENTER)
                    .setNegativeButton("知道了").create().show();
        } else if (v.getId() == R.id.btn_four_element_submit) {
            TraceUtil.onEvent(this, "bank_submit_count");
            mPresenter.doFourElementsRealName(mEtCardNo.getText().toString(), mEtMobile.getText().toString());
        }
    }

    @Override
    public void showUserName(CharSequence userName) {
        mTvName.setText(userName);
    }

    @Override
    public void enableSubmitButton(boolean enabled) {
        mBtnSubmit.setEnabled(enabled);
    }

    @Override
    public void updateCardNoAboutIcon(int iconResId) {
        mIvCardNoAbout.setImageResource(iconResId);
    }

    @Override
    public void updateMobileAboutIcon(int iconResId) {
        mIvMobileAbout.setImageResource(iconResId);
    }

    @Override
    public void showAuthFailRetryDialog(String msg) {
        new HMAlertDialog.Builder(this)
                .setTitle("管家提醒")
                .setMessage(msg)
                .setPositiveButton("再给一次机会")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create()
                .show();
    }

    @Override
    public void showAuthFailExceedCount(String msg) {
        new HMAlertDialog.Builder(this)
                .setTitle("管家提醒")
                .setMessage(msg)
                .setPositiveButton("下一步")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        closeCurrPage();
                        EventBus.getDefault().post(new CancelBindBankEvent());
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create()
                .show();
    }

    @Override
    public void showAuthSuccDialog() {
        VerifySuccDialog dialog = new VerifySuccDialog(this);
        dialog.setConfirmListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                TraceUtil.onEvent(mContext, "back_receive_awardreceive_award");
                EventBus.getDefault().post(new BindBankSuccessEvent());
                EventBus.getDefault().post(new UpdateUserInfoEvent());
            }
        });
        dialog.show();
    }

    /**
     * 点击左上角叉叉放弃实名认证
     */
    private void doGiveUpRealName() {
        new HMAlertDialog.Builder(this)
                .setTitle("放弃福利")
                .setMessage(getString(R.string.pay_give_up_real_name))
                .setPositiveButton("继续认证")
                .setNegativeButton("以后再说")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {

                    }

                    @Override
                    public void onNegClick() {
                        closeCurrPage();
                        EventBus.getDefault().post(new CancelBindBankEvent());
                    }
                })
                .create()
                .show();
    }

}