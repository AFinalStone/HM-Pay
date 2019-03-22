package com.hm.iou.pay.business.history;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.HistoryItemChildBean;
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
public class HistoryPresenter extends MvpActivityPresenter<HistoryContract.View> implements HistoryContract.Presenter {

    private Disposable mListDisposable;
    private Disposable mTimerDisposable;

    private List<HistoryItemBean> mListData;

    public HistoryPresenter(@NonNull Context context, @NonNull HistoryContract.View view) {
        super(context, view);
        mListData = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void init() {
        mView.showInitLoading();
        PayApi.getHistory()
                .compose(getProvider().<BaseResponse<List<HistoryItemBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<HistoryItemBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<HistoryItemBean>>(mView) {

                    @Override
                    public void handleResult(List<HistoryItemBean> list) {
                        mView.hideInitLoading();
                        mView.enableRefresh(true);
                        mListData.clear();
                        mListData.addAll(list);
                        if (mListData.isEmpty()) {
                            mView.showDataEmpty();
                        } else {
                            mView.showList((ArrayList) mListData);
                            startTimer();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String errorMsg) {
                        mView.hideInitLoading();
                        mView.showInitFailed(errorMsg);
                        mView.enableRefresh(false);
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

    @Override
    public void refresh() {
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mListDisposable = PayApi.getHistory()
                .compose(getProvider().<BaseResponse<List<HistoryItemBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<HistoryItemBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<HistoryItemBean>>(mView) {

                    @Override
                    public void handleResult(List<HistoryItemBean> list) {
                        mView.hidePullDownRefresh();
                        mView.enableRefresh(true);
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty();
                        } else {
                            mView.showList((ArrayList) list);
                            startTimer();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.hidePullDownRefresh();
                        mView.showInitFailed("数据异常");
                        mView.enableRefresh(false);
                    }
                });
    }


    @Override
    public void getMore() {

    }

    /**
     * 开启定时器
     */
    private void startTimer() {
        if (mTimerDisposable != null && !mTimerDisposable.isDisposed()) {
            mTimerDisposable.dispose();
        }
        HistoryItemChildBean.mTimerCount = 0;
        mTimerDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Logger.d("along" + aLong);
                        HistoryItemChildBean.mTimerCount++;
                        for (int i = 0; i < mListData.size(); i++) {
                            HistoryItemBean itemBean = mListData.get(i);
                            List<HistoryItemChildBean> listChild = itemBean.getRecords();
                            if (listChild != null) {
                                for (HistoryItemChildBean itemChild : listChild) {
                                    if (2 == itemChild.getRecordStatus()) {
                                        mView.updateItem(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

}
