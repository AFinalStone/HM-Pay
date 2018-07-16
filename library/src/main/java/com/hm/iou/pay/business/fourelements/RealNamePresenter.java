package com.hm.iou.pay.business.fourelements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.logger.Logger;
import com.hm.iou.sharedata.UserManager;

/**
 * Created by hjy on 2018/7/16.
 */

public class RealNamePresenter extends MvpActivityPresenter<RealNameContract.View> implements RealNameContract.Presenter {

    public RealNamePresenter(@NonNull Context context, @NonNull RealNameContract.View view) {
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
        if (TextUtils.isEmpty(cardNo) || TextUtils.isEmpty(mobile) || mobile.length() < 11 || !mobile.startsWith("1")) {
            mView.enableSubmitButton(false);
            return;
        }
        cardNo = cardNo.replace(" ", "");
        if (!(cardNo.startsWith("62") || cardNo.startsWith("60")) || cardNo.length() < 16) {
            mView.enableSubmitButton(false);
            return;
        }
        mView.enableSubmitButton(true);
    }

    @Override
    public void doFourElementsRealName(String cardNo, String mobile) {
        cardNo = cardNo.replace(" ", "");

    }
}
