package com.hm.iou.pay.business.bindbank.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.FourElementsVerifyStatus;
import com.hm.iou.pay.business.bindbank.BindBinkContract;
import com.hm.iou.pay.business.bindbank.view.RealBindBankActivity;
import com.hm.iou.pay.comm.PaySPUtil;
import com.hm.iou.pay.dict.FourElementStatusEnumBean;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.CustomerTypeEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by hjy on 2018/7/16.
 */

public class BindBankPresenter extends MvpActivityPresenter<BindBinkContract.View> implements BindBinkContract.Presenter {

    public BindBankPresenter(@NonNull Context context, @NonNull BindBinkContract.View view) {
        super(context, view);
    }

    /**
     * 校验用户今日是否还可以绑定银行卡
     */
    @Override
    public void checkCanBindBank() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int type = userInfo.getType();
        if (type == CustomerTypeEnum.CSub.getValue() || type == CustomerTypeEnum.CPlus.getValue()) {
            //如果是绑定4要素银行卡，需要先做实名认证。
            mView.showAuthDialog();
            return;
        }
        UserThirdPlatformInfo userThirdPlatformInfo = UserManager.getInstance(mContext).getUserExtendInfo().getThirdPlatformInfo();
        if (userThirdPlatformInfo != null) {
            UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = userThirdPlatformInfo.getBankInfoResp();
            if (bankInfoRespBean != null && 0 != bankInfoRespBean.getIsBinded()) {
                //已绑定过银行卡
                mView.showBinkBankInfo(bankInfoRespBean.getBankName(), bankInfoRespBean.getBankCard(), bankInfoRespBean.getBankCardType(), bankInfoRespBean.getBankPhone());
                return;
            }
        }

        if (PaySPUtil.checkUserHaveBindBankSuccess(mContext)) {
            mView.closeCurrPage();
            return;
        }

        mView.showLoadingView();
        PayApi.checkFourElementsVerifyStatus()
                .compose(getProvider().<BaseResponse<FourElementsVerifyStatus>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<FourElementsVerifyStatus>handleResponse())
                .subscribeWith(new CommSubscriber<FourElementsVerifyStatus>(mView) {
                    @Override
                    public void handleResult(FourElementsVerifyStatus status) {
                        mView.dismissLoadingView();
                        TraceUtil.onEvent(mContext, "back_succ_count");
                        if (FourElementStatusEnumBean.NoBindBank.getStatus() == status.getStatus()) {
                            if (0 == status.getRetryTimes()) {
                                mView.warnNoTimeToBindToday();
                            } else {
                                mContext.startActivity(new Intent(mContext, RealBindBankActivity.class));
                                mView.closeCurrPage();
                            }
                        } else {
                            PaySPUtil.saveUserBindBankSuccess(mContext);
                            mView.closeCurrPage();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errMsg) {
                        TraceUtil.onEvent(mContext, "bank_fail_count");
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }
                });
    }
}
