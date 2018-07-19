package com.hm.iou.pay.api;

import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.PayTestBean;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.req.CreatePreparePayReqBean;
import com.hm.iou.sharedata.model.BaseResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by syl on 2018/7/12.
 */

public interface PayService {

    @GET("/pay/iou/v1/test")
    Flowable<PayTestBean> payTest();

    @GET("/pay/iou/v1/getHistory")
    Flowable<BaseResponse<List<HistoryItemBean>>> getHistory();

    @POST("/pay/iou/package/v1/searchPackageList")
    Flowable<BaseResponse<SearchTimeCardListResBean>> searchTimeCardPackageList();

    @GET("/pay/iou/v1/createOrder")
    Flowable<BaseResponse<String>> createOrder(@Query("packageId") String packageId);

    @GET("/pay/iou/v1/queryOrderWhilePaying")
    Flowable<BaseResponse<String>> queryOrderPayState(@Query("orderId") String orderId);

    @POST("/pay/iou/v1/unifiedOrder")
    Flowable<BaseResponse<PayTestBean>> createPreparePayOrder(@Body CreatePreparePayReqBean reqBean);

}
