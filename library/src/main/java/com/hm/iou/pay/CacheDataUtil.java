package com.hm.iou.pay;

import android.content.Context;

import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.database.LockSignDbHelper;
import com.hm.iou.database.table.LockSignDbData;
import com.hm.iou.pay.api.PayApi;
import com.hm.iou.pay.bean.GetLockedSignListResBean;
import com.hm.iou.pay.bean.LockedSignItemBean;
import com.hm.iou.pay.bean.req.GetLockSignListReqBean;
import com.hm.iou.tools.SPUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author syl
 * @time 2019/3/22 11:07 AM
 */
public class CacheDataUtil {

    private static final String SP_NAME = "pay_sp";
    //增量更新的时间
    private static final String KEY_LOCK_SIGN_LIST_LAST_PULL_DATE = "lock_sign_list_last_pull_date";

    /**
     * 设置上次增量更新的时间
     *
     * @param context
     */
    public static void setLastPullDate(Context context, String lastPullDate) {
        SPUtil.put(context, SP_NAME, KEY_LOCK_SIGN_LIST_LAST_PULL_DATE, lastPullDate);
    }

    /**
     * 获取上次增量更新的时间
     *
     * @param context
     * @return
     */
    public static String getLastPullDate(Context context) {
        return SPUtil.getString(context, SP_NAME, KEY_LOCK_SIGN_LIST_LAST_PULL_DATE);
    }

    /**
     * 刷新数据到本地缓存中
     *
     * @param context
     * @param subscriber
     * @return
     */
    public static Disposable refreshLockSignListToCache(final Context context, CommSubscriber<Boolean> subscriber) {
        String lastPullTime = CacheDataUtil.getLastPullDate(context);
        GetLockSignListReqBean reqBean = new GetLockSignListReqBean();
        reqBean.setLastReqDate(lastPullTime);
        Disposable disposable = PayApi.getLockedSignList(reqBean)
                .map(RxUtil.<GetLockedSignListResBean>handleResponse())
                .map(new Function<GetLockedSignListResBean, Boolean>() {
                    @Override
                    public Boolean apply(GetLockedSignListResBean resBean) throws Exception {
                        //保存缓存数据
                        LockSignDbHelper.saveOrUpdateDebtBookList(lockedSignItemBeanToLockSignDbData(resBean.getInfoList()));
                        //保存更新时间
                        String lastPullTime = resBean.getLastReqDate();
                        CacheDataUtil.setLastPullDate(context, lastPullTime);
                        return true;
                    }
                })
                .subscribeWith(subscriber);
        return disposable;
    }

    /**
     * 从缓存中获取数据
     *
     * @param subscriber
     * @return
     */
    public static Disposable readLockSignListFromCache(Consumer<List<LockedSignItemBean>> subscriber, Consumer<Throwable> throwableConsumer) {
        Disposable disposable = Flowable.create(new FlowableOnSubscribe<List<LockedSignItemBean>>() {
            @Override
            public void subscribe(FlowableEmitter<List<LockedSignItemBean>> flowableEmitter) throws Exception {
                List<LockSignDbData> listCache = LockSignDbHelper.queryDebtBookList();
                flowableEmitter.onNext(lockSignDbDataToLockedSignItemBean(listCache));
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, throwableConsumer);
        return disposable;
    }

    /**
     * 本地itemBean集合转换成数据缓存对象集合
     *
     * @param listData
     * @return
     */
    private static List<LockSignDbData> lockedSignItemBeanToLockSignDbData(List<LockedSignItemBean> listData) {
        List<LockSignDbData> listCache = new ArrayList<>();
        if (listData != null && !listData.isEmpty()) {
            for (LockedSignItemBean data : listData) {
                LockSignDbData dbData = new LockSignDbData();
                dbData.setJusticeId(data.getJusticeId());
                dbData.setContent(data.getContent());
                dbData.setGenDateStr(data.getGenDateStr());
                dbData.setEndDateStr(data.getEndDateStr());
                dbData.setLockedStatus(data.getLockedStatus());
                dbData.setSignNum(data.getSignNum());
                listCache.add(dbData);
            }
        }
        return listCache;
    }

    /**
     * 数据库缓存对象集合转换成本地itemBean集合
     *
     * @param listCache
     * @return
     */
    private static List<LockedSignItemBean> lockSignDbDataToLockedSignItemBean(List<LockSignDbData> listCache) {
        List<LockedSignItemBean> listData = new ArrayList<>();
        if (listCache != null && !listCache.isEmpty()) {
            for (LockSignDbData dbData : listCache) {
                LockedSignItemBean itemBean = new LockedSignItemBean();
                itemBean.setJusticeId(dbData.getJusticeId());
                itemBean.setContent(dbData.getContent());
                itemBean.setGenDateStr(dbData.getGenDateStr());
                itemBean.setEndDateStr(dbData.getEndDateStr());
                itemBean.setLockedStatus(dbData.getLockedStatus());
                itemBean.setSignNum(dbData.getSignNum());
                listData.add(itemBean);
            }
        }
        return listData;
    }

    /**
     * 清空缓存
     *
     * @param context
     * @return
     */

    public static void clearCache(Context context) {
        SPUtil.clear(context, SP_NAME);
        LockSignDbHelper.deleteAllLockSignDbData();
    }

}