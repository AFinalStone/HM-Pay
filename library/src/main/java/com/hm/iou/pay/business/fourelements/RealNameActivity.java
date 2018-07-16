package com.hm.iou.pay.business.fourelements;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.event.CancelFourElementsAuthEvent;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by hjy on 2018/7/16.
 */

public class RealNameActivity extends BaseActivity<RealNamePresenter> implements RealNameContract.View {

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
    protected RealNamePresenter initPresenter() {
        return new RealNamePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
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
            public void accept(Throwable throwable) throws Exception {

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
            new IOSAlertDialog.Builder(this)
                    .setMessage("信息必须是本人资料")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else if (v.getId() == R.id.iv_fourelement_cardno_i) {
            new IOSAlertDialog.Builder(this)
                    .setMessage("银行卡必须“62”或“60”开头16-19位纯数字")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else if (v.getId() == R.id.iv_fourelement_mobile_i) {
            new IOSAlertDialog.Builder(this)
                    .setMessage("必须为本人的11位纯数字手机号")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else if (v.getId() == R.id.btn_four_element_submit) {
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

    /**
     * 点击左上角叉叉放弃实名认证
     */
    private void doGiveUpRealName() {
        new IOSAlertDialog.Builder(this)
                .setTitle("放弃福利")
                .setMessage(getString(R.string.pay_give_up_real_name))
                .setPositiveButton("继续认证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        closeCurrPage();
                        EventBus.getDefault().post(new CancelFourElementsAuthEvent());
                    }
                }).show();
    }

}