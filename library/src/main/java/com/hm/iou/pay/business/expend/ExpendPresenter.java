package com.hm.iou.pay.business.expend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.pay.bean.TimeCardBean;
import com.hm.iou.pay.comm.ITimeCardItem;
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
        mListDisposable = Flowable.just(0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getProvider().<Integer>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mView.hideInitLoading();
                        List<ITimeCardItem> list = new ArrayList<>();
                        list.add(new TimeCardBean("5", "10"));
                        list.add(new TimeCardBean("2", "2"));
                        list.add(new TimeCardBean("1", "10"));
                        mView.showList(list);
                        mView.showRemainNum("5");
                        mView.enableRefresh(true);
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
                        List<ITimeCardItem> list = new ArrayList<>();
                        list.add(new TimeCardBean("5", "100"));
                        list.add(new TimeCardBean("2", "2"));
                        list.add(new TimeCardBean("1", "5"));
                        mView.showList(list);
                        mView.showRemainNum("5");
                        mView.enableRefresh(true);
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

}