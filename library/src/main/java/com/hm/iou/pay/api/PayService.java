package com.hm.iou.pay.api;

import com.hm.iou.pay.bean.PayTestReqBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by syl on 2018/7/12.
 */

public interface PayService {

    @GET("/pay/iou/v1/test")
    Flowable<PayTestReqBean> payTest();

}
