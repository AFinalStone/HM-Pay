package com.hm.iou.pay.business.fourelements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.Constants;
import com.hm.iou.pay.R;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.WelfareAdvertiseBean;
import com.hm.iou.pay.event.FourElementsAuthSuccEvent;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.SPUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hjy on 2018/7/16.
 */

public class RealNamePresenter extends MvpActivityPresenter<RealNameContract.View> implements RealNameContract.Presenter {

    private static final String ERR_CODE_CANNOT_AUTH= "300005";     //3次认证机会均失败，不能再认证了
    private static final String ERR_CODE_AUTH_FAIL = "300006";      //认证失败，剩余还剩尝试机会

    private boolean mInputMobileError;
    private boolean mInputCardNoError;

    private WelfareAdvertiseBean mWelfareAdData;

    public RealNamePresenter(@NonNull Context context, @NonNull RealNameContract.View view) {
        super(context, view);
    }

    @Override
    public void getUserRealName() {
        String userName = UserManager.getInstance(mContext).getUserInfo().getName();
        mView.showUserName(userName);
    }

    @Override
    public void getTopAd() {
        PayApi.getWelfareAdvertise()
                .compose(getProvider().<BaseResponse<WelfareAdvertiseBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<WelfareAdvertiseBean>handleResponse())
                .subscribeWith(new CommSubscriber<WelfareAdvertiseBean>(mView) {
                    @Override
                    public void handleResult(WelfareAdvertiseBean data) {
                        mWelfareAdData = data;
                        mView.showTopAd(data.getWelfareUrl());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }
                });
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
                        mView.toastMessage("认证成功");
                        //四要素认证已经成功，用SharedPreferences保存下来，采用userId拼接的字符串作为key来存储
                        String userId = UserManager.getInstance(mContext).getUserId();
                        SPUtil.put(mContext, Constants.SP_NAME, Constants.SP_KEY_FOUR_ELEMENTS + userId, true);

                        mView.closeCurrPage();
                        //4要素认证已经成功
                        EventBus.getDefault().post(new FourElementsAuthSuccEvent());
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
