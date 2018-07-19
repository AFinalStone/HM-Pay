package com.hm.iou.pay.business.history;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.HistoryItemChildBean;
import com.hm.iou.pay.business.history.view.IHistoryItem;
import com.hm.iou.pay.business.history.view.IHistoryItemChild;
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

    public HistoryPresenter(@NonNull Context context, @NonNull HistoryContract.View view) {
        super(context, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void init() {
        mView.showInitLoading();
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mListDisposable = PayApi.getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<BaseResponse<List<HistoryItemBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<HistoryItemBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<HistoryItemBean>>(mView) {

                    @Override
                    public void handleResult(List<HistoryItemBean> historyItemBeans) {
                        mView.hideInitLoading();
                        mView.enableRefresh(true);
                        if (historyItemBeans == null && historyItemBeans.isEmpty()) {
                            mView.showDataEmpty();
                        } else {
                            mView.showList((ArrayList) historyItemBeans);
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.hideInitLoading();
                        mView.showInitFailed("数据异常");
                        mView.enableRefresh(false);
                    }
                });
    }

    @Override
    public void refresh() {
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mListDisposable = PayApi.getHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<BaseResponse<List<HistoryItemBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<HistoryItemBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<HistoryItemBean>>(mView) {

                    @Override
                    public void handleResult(List<HistoryItemBean> historyItemBeans) {
                        mView.hideInitLoading();
                        mView.enableRefresh(true);
                        if (historyItemBeans == null && historyItemBeans.isEmpty()) {
                            mView.showDataEmpty();
                        } else {
                            mView.showList((ArrayList) historyItemBeans);
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

}
