package com.hm.iou.pay.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.pay.bean.PayTestReqBean;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by syl on 2018/6/28.
 */

public class WeiXinPayApi {

    private static WeiXinPayService getService() {
        return HttpReqManager.getInstance().getService(WeiXinPayService.class);
    }

    public static Flowable<PayTestReqBean> payTest() {
        return getService().payTest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}