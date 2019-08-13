package com.hm.iou.pay.business.timecard;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.TimeCardBean;
import com.hm.iou.pay.bean.VipCardPackageBean;
import com.hm.iou.pay.bean.VipCardUseBean;
import com.hm.iou.pay.business.timecard.view.IVipCardItem;
import com.hm.iou.pay.event.PaySuccessEvent;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.MoneyFormatUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Publisher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;


/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/5/17 下午5:24
 */
public class TimeCardRechargePresenter extends MvpActivityPresenter<TimeCardRechargeContract.View> implements TimeCardRechargeContract.Presenter {

    private SearchTimeCardListResBean mTimeCardInfo;
    private List<TimeCardBean> mListData = new ArrayList<>();

    public TimeCardRechargePresenter(@NonNull Context context, @NonNull TimeCardRechargeContract.View view) {
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
        mView.showInitLoading();
        PayApi.getLockedSignNum()//获取被占用的签章
                .compose(getProvider().<BaseResponse<Integer>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Integer>handleResponse())
                .flatMap(new Function<Integer, Publisher<BaseResponse<SearchTimeCardListResBean>>>() {
                    @Override
                    public Publisher<BaseResponse<SearchTimeCardListResBean>> apply(Integer num) throws Exception {
                        mView.showLockSignNum(String.valueOf(num));
                        return PayApi.searchTimeCardPackageList();//获取可用签章，
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

                        //获取贵宾卡套餐
                        getVipCardInfo();
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
        Logger.d("========刷新数据======");
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
                        Logger.d("========成功获取套餐，开始获取底部广告======");
                        getVipCardInfo();
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
        if (mTimeCardInfo == null)
            return;
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
    public void getInwardPackage(String packageId) {
        if (mTimeCardInfo == null)
            return;
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
     * 获取用户VIP卡的信息
     */
    private void getVipCardInfo() {
        PayApi.getVipCardUserInfo(7)
                .compose(getProvider().<BaseResponse<VipCardUseBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<VipCardUseBean>handleResponse())
                .subscribeWith(new CommSubscriber<VipCardUseBean>(mView) {

                    @Override
                    public void handleResult(VipCardUseBean vipCardBean) {
                        if (vipCardBean.getHasValidCard()) {
                            //有有效的VIP卡片信息
                            mView.showVipCardUsage(vipCardBean);
                        } else {
                            //无有效的VIP卡片信息
                            getVipCardPackageList();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                    }
                });
    }

    /**
     * 获取VIP卡套餐信息
     */
    private void getVipCardPackageList() {
        PayApi.getVipPackages(1, 7)
                .compose(getProvider().<BaseResponse<List<VipCardPackageBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<VipCardPackageBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<VipCardPackageBean>>(mView) {
                    @Override
                    public void handleResult(List<VipCardPackageBean> list) {
                        List<IVipCardItem> dataList = new ArrayList<>();
                        if (list != null && !list.isEmpty()) {
                            for (final VipCardPackageBean bean : list) {
                                IVipCardItem data = new IVipCardItem() {
                                    @Nullable
                                    @Override
                                    public String getName() {
                                        return bean.getContent();
                                    }

                                    @Nullable
                                    @Override
                                    public String getAmountPerOnce() {
                                        return bean.getYuanPer();
                                    }

                                    @Nullable
                                    @Override
                                    public String getDesc() {
                                        float totalPrice = bean.getActualPrice() / 100f;
                                        DecimalFormat df = new DecimalFormat("###.###");
                                        String strTotalPrice;
                                        try {
                                            strTotalPrice = df.format(totalPrice);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            strTotalPrice = " ";
                                        }
                                        return String.format("%d次=%s元", bean.getRechargeSign(), strTotalPrice);
                                    }

                                    @Override
                                    public boolean isOverBalance() {
                                        if ("年卡".equals(bean.getContent())) {
                                            return true;
                                        }
                                        return false;
                                    }

                                    @NotNull
                                    @Override
                                    public VipCardPackageBean getData() {
                                        return bean;
                                    }
                                };
                                dataList.add(data);
                            }
                        }
                        mView.showVipCardPackage(dataList);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                    }
                });
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

}
