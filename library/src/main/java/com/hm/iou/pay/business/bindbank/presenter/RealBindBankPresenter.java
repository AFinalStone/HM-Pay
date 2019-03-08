package com.hm.iou.pay.business.bindbank.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.R;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.WelfareAdvertiseBean;
import com.hm.iou.pay.business.bindbank.RealBindBinkContract;
import com.hm.iou.pay.comm.PaySPUtil;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by hjy on 2018/7/16.
 */

public class RealBindBankPresenter extends MvpActivityPresenter<RealBindBinkContract.View> implements RealBindBinkContract.Presenter {

    private static final String ERR_CODE_CANNOT_AUTH = "700005";     //3次认证机会均失败，不能再认证了
    private static final String ERR_CODE_AUTH_FAIL = "700006";      //认证失败，剩余还剩尝试机会

    private boolean mInputMobileError;
    private boolean mInputCardNoError;

    private WelfareAdvertiseBean mWelfareAdData;

    public RealBindBankPresenter(@NonNull Context context, @NonNull RealBindBinkContract.View view) {
        super(context, view);
    }

    @Override
    public void getUserRealName() {
        String userName = UserManager.getInstance(mContext).getUserInfo().getName();
        mView.showUserName(userName);
    }

    @Override
    public void checkUserInputValue(String cardNo, String mobile) {
        Logger.d("cardNo = " + cardNo + ", mobile = " + mobile);
        if (TextUtils.isEmpty(cardNo) || TextUtils.isEmpty(mobile) || mobile.length() < 11) {
            mView.enableSubmitButton(false);
            return;
        }
        cardNo = cardNo.replace(" ", "");
        if (cardNo.length() < 16) {
            mView.enableSubmitButton(false);
            return;
        }
        mView.enableSubmitButton(true);
    }

    @Override
    public boolean isCardNoInputError() {
        return mInputCardNoError;
    }

    @Override
    public boolean isMobileInputError() {
        return mInputMobileError;
    }

    @Override
    public void doFourElementsRealName(String cardNo, String mobile) {
        cardNo = cardNo.replace(" ", "");
        if (!cardNo.startsWith("60") && !cardNo.startsWith("62")) {
            mView.updateCardNoAboutIcon(R.mipmap.uikit_icon_warn_red);
            mInputCardNoError = true;
        } else {
            mView.updateCardNoAboutIcon(R.mipmap.uikit_icon_warn_gray);
            mInputCardNoError = false;
        }
        if (!mobile.startsWith("1")) {
            mView.updateMobileAboutIcon(R.mipmap.uikit_icon_warn_red);
            mInputMobileError = true;
        } else {
            mView.updateMobileAboutIcon(R.mipmap.uikit_icon_warn_gray);
            mInputMobileError = false;
        }
        if (mInputMobileError || mInputCardNoError) {
            return;
        }
        mView.showLoadingView();
        PayApi.bindBankCard(cardNo, mobile)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        //四要素认证已经成功，用SharedPreferences保存下来，
                        PaySPUtil.saveUserBindBankSuccess(mContext);
                        mView.showAuthSuccDialog();
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errMsg) {
                        mView.dismissLoadingView();
                        if (!TextUtils.isEmpty(code)) {
                            if (code.equals(ERR_CODE_CANNOT_AUTH)) {
                                mView.showAuthFailExceedCount(errMsg);
                                return;
                            } else if (code.equals(ERR_CODE_AUTH_FAIL)) {
                                mView.showAuthFailRetryDialog(errMsg);
                                return;
                            }
                            mView.toastMessage(errMsg);
                            return;
                        }
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
    }
}
