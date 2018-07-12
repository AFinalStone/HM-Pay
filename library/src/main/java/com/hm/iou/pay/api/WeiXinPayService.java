package com.hm.iou.pay.api;

import com.hm.iou.pay.bean.PayTestReqBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by syl on 2018/7/12.
 */

public interface WeiXinPayService {

    @GET("/pay/iou/wx/v1/payTest")
    Flowable<PayTestReqBean> payTest();

}
