package com.hm.iou.pay.business.history;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.HistoryItemChildBean;
import com.hm.iou.pay.bean.TimeCardBean;
import com.hm.iou.pay.business.history.view.IHistoryItem;
import com.hm.iou.pay.business.history.view.IHistoryItemChild;
import com.hm.iou.pay.business.timecard.view.ITimeCardItem;
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
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mView.showInitLoading();
        mListDisposable = Flowable.just(0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mView.hideInitLoading();
                        mView.enableRefresh(true);
                        //child
                        HistoryItemChildBean childBean = new HistoryItemChildBean("2018-12-03 12:00:00", "待支付");
                        List<IHistoryItemChild> childList = new ArrayList<>();
                        childList.add(childBean);
                        //item
                        List<IHistoryItem> itemList = new ArrayList<>();
                        HistoryItemBean itemBean = new HistoryItemBean("充值3次", (ArrayList) childList);
                        itemList.add(itemBean);

                        //child
                        childBean = new HistoryItemChildBean("2019-07-13 08:00:00", "已赠送");
                        childList.add(childBean);
                        childBean = new HistoryItemChildBean("2019-08-17 17:33:44", "已使用");
                        childList.add(childBean);
                        //item
                        itemBean = new HistoryItemBean("赠送5次", (ArrayList) childList);
                        itemList.add(itemBean);
                        mView.showList(itemList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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
        mListDisposable = Flowable.just(0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mView.hidePullDownRefresh();
                        mView.showDataEmpty();
                        List<IHistoryItem> itemList = new ArrayList<>();
                        mView.showList(itemList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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
