package com.hm.iou.pay.business.expend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.FourElementsVerifyStatus;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.TimeCardBean;
import com.hm.iou.pay.comm.PaySPUtil;
import com.hm.iou.pay.dict.FourElementStatusEnumBean;
import com.hm.iou.pay.event.BindBankSuccessEvent;
import com.hm.iou.pay.event.CancelBindBankEvent;
import com.hm.iou.pay.event.PaySuccessEvent;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.MoneyFormatUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;


/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/5/17 下午5:24
 */
public class ExpendPresenter extends MvpActivityPresenter<ExpendContract.View> implements ExpendContract.Presenter {

    private Disposable mListDisposable;
    private long mSignUnitPrice; //单价
    private TimeCardBean mFirstTryTimeCard; //首次体验
    private List<TimeCardBean> mListData = new ArrayList<>();
    private long mRemainNum;//剩余次数

    public ExpendPresenter(@NonNull Context context, @NonNull ExpendContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mView.showInitLoading();
        mListDisposable = PayApi.searchTimeCardPackageList()
                .compose(getProvider().<BaseResponse<SearchTimeCardListResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<SearchTimeCardListResBean>handleResponse())
                .subscribeWith(new CommSubscriber<SearchTimeCardListResBean>(mView) {
                    @Override
                    public void handleResult(SearchTimeCardListResBean searchTimeCardListResBean) {
                        mView.hideInitLoading();
                        if (searchTimeCardListResBean == null) {
                            mView.enableRefresh(false);
                            mView.showInitFailed("数据异常");
                            return;
                        }
                        mSignUnitPrice = searchTimeCardListResBean.getSignUnitPrice();
                        //初次体验
                        mFirstTryTimeCard = searchTimeCardListResBean.getFirstPackage();
                        if (mFirstTryTimeCard != null) {
                            mView.showFirstTryBtn(mFirstTryTimeCard);
                        } else {
                            mView.hideFirstTryBtn();
                        }
                        //套餐列表
                        List<TimeCardBean> list = searchTimeCardListResBean.getPackageRespList();
                        mListData.clear();
                        if (list != null) {
                            mListData.addAll(list);
                            mView.showList((ArrayList) mListData);
                        }
                        //剩余次数
                        long countSign = searchTimeCardListResBean.getCountSign();
                        mRemainNum = countSign;
                        mView.showRemainNum(String.valueOf(countSign));
                        mView.enableRefresh(true);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mListData.clear();
                        mView.hideInitLoading();
                        mView.showInitFailed(errorMsg);
                        mView.enableRefresh(false);
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
    public void refresh() {
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mListDisposable = PayApi.searchTimeCardPackageList()
                .compose(getProvider().<BaseResponse<SearchTimeCardListResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<SearchTimeCardListResBean>handleResponse())
                .subscribeWith(new CommSubscriber<SearchTimeCardListResBean>(mView) {
                    @Override
                    public void handleResult(SearchTimeCardListResBean searchTimeCardListResBean) {
                        mView.hidePullDownRefresh();
                        if (searchTimeCardListResBean == null) {
                            mView.showInitFailed("数据异常");
                            mView.enableRefresh(false);
                            return;
                        }
                        //初次体验
                        mFirstTryTimeCard = searchTimeCardListResBean.getFirstPackage();
                        if (mFirstTryTimeCard != null) {
                            mView.showFirstTryBtn(mFirstTryTimeCard);
                        } else {
                            mView.hideFirstTryBtn();
                        }
                        //套餐列表
                        List<TimeCardBean> list = searchTimeCardListResBean.getPackageRespList();
                        mListData.clear();
                        if (list != null) {
                            mListData.addAll(list);
                            mView.showList((ArrayList) mListData);
                        }
                        //剩余次数
                        long countSign = searchTimeCardListResBean.getCountSign();
                        mRemainNum = countSign;
                        mView.showRemainNum(String.valueOf(countSign));
                        mView.enableRefresh(true);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mListData.clear();
                        mView.hidePullDownRefresh();
                        mView.showInitFailed(errorMsg);
                        mView.enableRefresh(false);
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
    public void toAddTimeCardNum(int position) {
        TimeCardBean timeCardBean;
        String strTimeCardNum;
        if (position >= mListData.size()) {
            return;
        }
        timeCardBean = mListData.get(position);
        strTimeCardNum = timeCardBean.getTimeCardContent() + "：充" + timeCardBean.getRechargeSign() + "次";
        String strPackageId = timeCardBean.getPackageId();
        String strTimeCardPayMoney = null;
        try {
            strTimeCardPayMoney = MoneyFormatUtil.changeF2Y(timeCardBean.getActualPrice());
        } catch (Exception e) {
            e.printStackTrace();
            mView.toastMessage("发生异常，请稍后再试");
            return;
        }
        String strTimeCardUnitMoney = "0";
        try {
            strTimeCardUnitMoney = MoneyFormatUtil.changeF2Y(mSignUnitPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toSelectPayType(strTimeCardNum, strTimeCardPayMoney, strTimeCardUnitMoney, strPackageId);
    }

    @Override
    public void toExpendOnceTime() {
        if (0 == mRemainNum) {
            int lastPosition = mListData.size() - 1;
            toAddTimeCardNum(lastPosition);
        } else {
            checkFourElementsVerifyStatus();
        }
    }

    @Override
    public void toFirstTry() {
        TimeCardBean timeCardBean;
        String strTimeCardNum;
        if (mFirstTryTimeCard == null) {
            return;
        }
        timeCardBean = mFirstTryTimeCard;
        strTimeCardNum = "首次体验：充1次";

        String strPackageId = timeCardBean.getPackageId();
        String strTimeCardPayMoney = null;
        try {
            strTimeCardPayMoney = MoneyFormatUtil.changeF2Y(timeCardBean.getActualPrice());
        } catch (Exception e) {
            e.printStackTrace();
            mView.toastMessage("发生异常，请稍后再试");
            return;
        }
        String strTimeCardUnitMoney = "0";
        try {
            strTimeCardUnitMoney = MoneyFormatUtil.changeF2Y(mSignUnitPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toSelectPayType(strTimeCardNum, strTimeCardPayMoney, strTimeCardUnitMoney, strPackageId);
    }

    /**
     * 选择支付方式
     *
     * @param timeCardNum
     * @param timeCardPayMoney
     * @param timeCardUnitMoney
     * @param packageId
     */
    private void toSelectPayType(String timeCardNum, String timeCardPayMoney
            , String timeCardUnitMoney, String packageId) {
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/pay/select_pay_type")
                .withString("time_card_num", timeCardNum)
                .withString("time_card_pay_money", timeCardPayMoney)
                .withString("time_card_unit_price", timeCardUnitMoney)
                .withString("package_id", packageId)
                .navigation(mContext);
    }

    /**
     * 校验用户是否绑定银行卡的四要素
     */
    public void checkFourElementsVerifyStatus() {
        if (PaySPUtil.checkUserHaveBindBankSuccess(mContext)) {
            toCheckSignPsd();
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
                                toCheckSignPsd();
                            } else {
                                Router.getInstance()
                                        .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank")
                                        .navigation(mContext);
                            }
                        } else {
                            PaySPUtil.saveUserBindBankSuccess(mContext);
                            toCheckSignPsd();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errMsg) {
                        mView.dismissLoadingView();
                        toCheckSignPsd();
                    }
                });
    }


    /**
     * 校验签约密码，选择签章类型，然后创建电子PDF
     */
    private void toCheckSignPsd() {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/signature/check_sign_psd_from_bottom")
                .withString("url", "hmiou://m.54jietiao.com/signature/signature_list_select")
                .navigation(mContext);
    }

    /**
     * 支付成功
     *
     * @param paySuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusPaySuccess(PaySuccessEvent paySuccessEvent) {
        mView.refresh();
    }

    /**
     * 银行卡绑定成功，直接选择签章类型
     *
     * @param bindBankSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusBindBankSuccess(BindBankSuccessEvent bindBankSuccessEvent) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/signature/signature_list_select")
                .navigation(mContext);
    }

    /**
     * 用户取消银行卡绑定操作,进行签约密码校验，签章类型选择
     *
     * @param cancelBindBankEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusUserCancelBindBank(CancelBindBankEvent cancelBindBankEvent) {
        toCheckSignPsd();
    }

}
