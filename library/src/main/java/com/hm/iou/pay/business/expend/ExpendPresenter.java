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
import com.hm.iou.sharedata.model.BaseResponse;
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
                        //初次体验
                        TimeCardBean firstBean = searchTimeCardListResBean.getFirstPackage();
                        if (firstBean != null) {
                            mView.showFirstTry(firstBean);
                        }
                        //套餐列表
                        List<TimeCardBean> list = searchTimeCardListResBean.getPackageRespList();
                        if (list != null) {
                            mView.showList((ArrayList) list);
                        }
                        //剩余次数
                        int countSign = searchTimeCardListResBean.getCountSign();
                        mView.showRemainNum(String.valueOf(countSign));
                        mView.enableRefresh(true);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.hideInitLoading();
                        mView.showInitFailed(errorMsg);
                        mView.enableRefresh(false);
                    }
                });

    }

    @Override
    public void refresh() {
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
                        mView.hidePullDownRefresh();
                        if (searchTimeCardListResBean == null) {
                            mView.showInitFailed("数据异常");
                            mView.enableRefresh(false);
                            return;
                        }
                        //初次体验
                        TimeCardBean firstBean = searchTimeCardListResBean.getFirstPackage();
                        if (firstBean != null) {
                            mView.showFirstTry(firstBean);
                        }
                        //套餐列表
                        List<TimeCardBean> list = searchTimeCardListResBean.getPackageRespList();
                        if (list != null) {
                            mView.showList((ArrayList) list);
                        }
                        //剩余次数
                        int countSign = searchTimeCardListResBean.getCountSign();
                        mView.showRemainNum(String.valueOf(countSign));
                        mView.enableRefresh(true);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.hidePullDownRefresh();
                        mView.showInitFailed(errorMsg);
                        mView.enableRefresh(false);
                    }
                });
    }

}
