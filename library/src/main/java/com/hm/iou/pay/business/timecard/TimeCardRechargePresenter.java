package com.hm.iou.pay.business.timecard;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.comm.CommApi;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxJavaStopException;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.Constants;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.AdBean;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.TimeCardBean;
import com.hm.iou.pay.event.PaySuccessEvent;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.PersonalCenterInfo;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.tools.MoneyFormatUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/5/17 下午5:24
 */
public class TimeCardRechargePresenter extends MvpActivityPresenter<TimeCardRechargeContract.View> implements TimeCardRechargeContract.Presenter {

    private Disposable mListDisposable;
    private long mSignUnitPrice; //单价
    private List<TimeCardBean> mListData = new ArrayList<>();
    private SearchTimeCardListResBean mTimeCardInfo;
    private boolean mIsNeedRefreshAd = true;

    public TimeCardRechargePresenter(@NonNull Context context, @NonNull TimeCardRechargeContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
    }

    @Override
    public void init() {
        mView.showInitLoading();
        PayApi.getLockedSignNum()
                .compose(getProvider().<BaseResponse<Integer>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Integer>handleResponse())
                .flatMap(new Function<Integer, Publisher<BaseResponse<SearchTimeCardListResBean>>>() {
                    @Override
                    public Publisher<BaseResponse<SearchTimeCardListResBean>> apply(Integer num) throws Exception {
                        mView.showLockSignNum(String.valueOf(num));
                        return PayApi.searchTimeCardPackageList();
                    }
                })
                .compose(getProvider().<BaseResponse<SearchTimeCardListResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<SearchTimeCardListResBean>handleResponse())
                .subscribeWith(new CommSubscriber<SearchTimeCardListResBean>(mView) {
                    @Override
                    public void handleResult(SearchTimeCardListResBean searchTimeCardListResBean) {
                        mView.hideInitLoading();
                        mTimeCardInfo = searchTimeCardListResBean;
                        if (searchTimeCardListResBean == null) {
                            mView.enableRefresh(false);
                            mView.showInitFailed("数据异常");
                            return;
                        }
                        mSignUnitPrice = searchTimeCardListResBean.getSignUnitPrice();
                        //套餐列表
                        List<TimeCardBean> list = searchTimeCardListResBean.getPackageRespList();
                        mListData.clear();
                        if (list != null) {
                            mListData.addAll(list);
                            mView.showList((ArrayList) mListData);
                        }
                        //剩余次数
                        long countSign = searchTimeCardListResBean.getCountSign();
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
        mListDisposable = PayApi.getLockedSignNum()
                .compose(getProvider().<BaseResponse<Integer>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Integer>handleResponse())
                .flatMap(new Function<Integer, Publisher<BaseResponse<SearchTimeCardListResBean>>>() {
                    @Override
                    public Publisher<BaseResponse<SearchTimeCardListResBean>> apply(Integer num) throws Exception {
                        mView.showLockSignNum(String.valueOf(num));
                        return PayApi.searchTimeCardPackageList();
                    }
                })
                .compose(getProvider().<BaseResponse<SearchTimeCardListResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<SearchTimeCardListResBean>handleResponse())
                .subscribeWith(new CommSubscriber<SearchTimeCardListResBean>(mView) {
                    @Override
                    public void handleResult(SearchTimeCardListResBean searchTimeCardListResBean) {
                        mTimeCardInfo = searchTimeCardListResBean;
                        mView.hidePullDownRefresh();
                        if (searchTimeCardListResBean == null) {
                            mView.showInitFailed("数据异常");
                            mView.enableRefresh(false);
                            return;
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
    public void toAddTimeCardByPosition(int position) {
        if (mTimeCardInfo != null && mTimeCardInfo.getCountSign() > 10) {
            mView.showSignCountMoreThanTen();
            return;
        }

        TimeCardBean timeCardBean;
        String strTimeCardName;
        if (position >= mListData.size()) {
            return;
        }
        timeCardBean = mListData.get(position);
        strTimeCardName = timeCardBean.getTimeCardContent();
        String strPackageId = timeCardBean.getPackageId();
        String strTimeCardPayMoney = null;
        try {
            strTimeCardPayMoney = MoneyFormatUtil.changeF2Y(timeCardBean.getActualPrice());
        } catch (Exception e) {
            e.printStackTrace();
            mView.toastMessage("发生异常，请稍后再试");
            return;
        }
        String strTimeCardAddNum = timeCardBean.getRechargeSign();
        toSelectPayType(strTimeCardName, strTimeCardPayMoney, strTimeCardAddNum, strPackageId);
    }


    @Override
    public void getBottomAd() {
        if (mIsNeedRefreshAd) {
            CommApi.getPersonalCenter()
                    .compose(getProvider().<BaseResponse<PersonalCenterInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.<PersonalCenterInfo>handleResponse())
                    .flatMap(new Function<PersonalCenterInfo, Publisher<BaseResponse<List<AdBean>>>>() {
                        @Override
                        public Publisher<BaseResponse<List<AdBean>>> apply(PersonalCenterInfo personalCenterInfo) throws Exception {
                            //存储个人中心摘要信息
                            UserManager userManager = UserManager.getInstance(mContext);
                            UserExtendInfo userExtendInfo = userManager.getUserExtendInfo();
                            userExtendInfo.setPersonalCenterInfo(personalCenterInfo);
                            if (personalCenterInfo != null && personalCenterInfo.getBankCardResp() != null && personalCenterInfo.getBankCardResp().isHasBinded()) {
                                throw new RxJavaStopException();
                            }
                            return PayApi.getAdvertiseList(Constants.AD_POSITION_CARD_CHARGE);
                        }
                    })
                    .compose(getProvider().<BaseResponse<List<AdBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.<List<AdBean>>handleResponse())
                    .subscribeWith(new CommSubscriber<List<AdBean>>(mView) {
                        @Override
                        public void handleResult(List<AdBean> list) {
                            mIsNeedRefreshAd = false;
                            if (list != null && !list.isEmpty()) {
                                AdBean bean = list.get(0);
                                mView.showAdvertisement(bean.getUrl(), bean.getLinkUrl());
                            } else {
                                mView.hideAdvertisement();
                            }
                        }

                        @Override
                        public void handleException(Throwable throwable, String s, String s1) {
                            if (throwable instanceof RxJavaStopException) {
                                mIsNeedRefreshAd = false;
                            }
                            mView.hideAdvertisement();
                        }

                        @Override
                        public boolean isShowCommError() {
                            return false;
                        }

                        @Override
                        public boolean isShowBusinessError() {
                            return false;
                        }
                    });
        }

    }

    @Override
    public void getInwardPackage(String packageId) {
        if (mTimeCardInfo != null && mTimeCardInfo.getCountSign() > 10) {
            mView.showSignCountMoreThanTen();
            return;
        }

        mView.showLoadingView();
        PayApi.getInwardPackage(packageId)
                .compose(getProvider().<BaseResponse<TimeCardBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<TimeCardBean>handleResponse())

                .subscribeWith(new CommSubscriber<TimeCardBean>(mView) {
                    @Override
                    public void handleResult(TimeCardBean data) {
                        mView.dismissLoadingView();
                        if (data != null) {
                            String cardName = data.getTimeCardContent();
                            String packageId = data.getPackageId();
                            String addNum = data.getRechargeSign();

                            String payMoney = "";
                            try {
                                payMoney = MoneyFormatUtil.changeF2Y(data.getActualPrice());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            toSelectPayType(cardName, payMoney, addNum, packageId);
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    /**
     * 选择支付方式
     *
     * @param timeCardName
     * @param timeCardPayMoney
     * @param timeCardAddNum
     * @param packageId
     */
    private void toSelectPayType(String timeCardName, String timeCardPayMoney
            , String timeCardAddNum, String packageId) {
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/pay/select_pay_type")
                .withString("time_card_name", timeCardName)
                .withString("time_card_pay_money", timeCardPayMoney)
                .withString("time_card_add_num", timeCardAddNum)
                .withString("package_id", packageId)
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
     * 银行卡绑定成功，重新刷新广告页面
     *
     * @param bindBankSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusBindBankCard(BindBankSuccessEvent bindBankSuccessEvent) {
        mIsNeedRefreshAd = true;
    }

}
