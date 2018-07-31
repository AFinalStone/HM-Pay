package com.hm.iou.pay.business.bindbank.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.FourElementsVerifyStatus;
import com.hm.iou.pay.business.bindbank.BindBinkContract;
import com.hm.iou.pay.business.bindbank.view.RealBindBankActivity;
import com.hm.iou.pay.comm.PaySPUtil;
import com.hm.iou.pay.dict.FourElementStatusEnumBean;
import com.hm.iou.sharedata.model.BaseResponse;
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
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }
                });
    }
}
