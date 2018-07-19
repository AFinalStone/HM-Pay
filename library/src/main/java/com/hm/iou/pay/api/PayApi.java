package com.hm.iou.pay.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.PayTestBean;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.sharedata.model.BaseResponse;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by syl on 2018/6/28.
 */
public class PayApi {

    private static PayService getService() {
        return HttpReqManager.getInstance().getService(PayService.class);
    }

    public static Flowable<PayTestBean> payTest() {
        return getService().payTest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<HistoryItemBean>> getHistory() {
        return getService().getHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<SearchTimeCardListResBean>> searchTimeCardPackageList() {
        return getService().searchTimeCardPackageList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}