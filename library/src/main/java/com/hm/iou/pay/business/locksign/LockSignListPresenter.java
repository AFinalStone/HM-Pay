package com.hm.iou.pay.business.locksign;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.pay.CacheDataUtil;
import com.hm.iou.pay.bean.LockedSignItemBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/5/17 下午5:24
 */
public class LockSignListPresenter extends MvpActivityPresenter<LockSignListContract.View> implements LockSignListContract.Presenter {

    private Disposable mListDisposable;

    public LockSignListPresenter(@NonNull Context context, @NonNull LockSignListContract.View view) {
        super(context, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
    }

    @Override
    public void init() {
        if (mListDisposable != null && !mListDisposable.isDisposed()) {
            mListDisposable.dispose();
        }
        mView.showInitLoading();
        mListDisposable = CacheDataUtil.refreshLockSignListToCache(mContext, new CommSubscriber<Boolean>(mView) {
            @Override
            public void handleResult(Boolean aBoolean) {
                CacheDataUtil.readLockSignListFromCache(new Consumer<List<LockedSignItemBean>>() {
                    @Override
                    public void accept(List<LockedSignItemBean> list) throws Exception {
                        mView.hideInitLoading();
                        mView.enableRefresh(true);
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty();
                        } else {
                            mView.showList((ArrayList) list);
                        }
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
        mListDisposable = CacheDataUtil.refreshLockSignListToCache(mContext, new CommSubscriber<Boolean>(mView) {
            @Override
            public void handleResult(Boolean aBoolean) {
                CacheDataUtil.readLockSignListFromCache(new Consumer<List<LockedSignItemBean>>() {
                    @Override
                    public void accept(List<LockedSignItemBean> list) throws Exception {
                        mView.hidePullDownRefresh();
                        mView.enableRefresh(true);
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty();
                        } else {
                            mView.showList((ArrayList) list);
                        }
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
            public void handleException(Throwable throwable, String code, String error) {
                mView.hidePullDownRefresh();
                mView.showInitFailed(error);
                mView.enableRefresh(false);
            }
        });

    }


    @Override
    public void getMore() {

    }

}
