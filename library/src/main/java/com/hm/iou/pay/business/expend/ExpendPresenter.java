package com.hm.iou.pay.business.expend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.TimeCardBean;
import com.hm.iou.pay.comm.ITimeCardItem;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.MoneyFormatUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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

    public ExpendPresenter(@NonNull Context context, @NonNull ExpendContract.View view) {
        super(context, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                            mView.showFirstTry(mFirstTryTimeCard);
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
                            mView.showFirstTry(mFirstTryTimeCard);
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
    public void toAddTimeCardNum(boolean isFirstTry, int position) {
        TimeCardBean timeCardBean;
        String strTimeCardNum;
        if (isFirstTry) {
            if (mFirstTryTimeCard == null) {
                return;
            }
            timeCardBean = mFirstTryTimeCard;
            strTimeCardNum = "1次卡";
        } else {
            if (position >= mListData.size()) {
                return;
            }
            timeCardBean = mListData.get(position);
            strTimeCardNum = timeCardBean.getTimeCardNum();
        }
        String strIsFirstTry = String.valueOf(isFirstTry);
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
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/pay/select_pay_type")
                .withString("is_first_try", strIsFirstTry)
                .withString("package_id", strPackageId)
                .withString("time_card_num", strTimeCardNum)
                .withString("time_card_pay_money", strTimeCardPayMoney)
                .withString("time_card_unit_price", strTimeCardUnitMoney)
                .navigation(mContext);
    }


}
