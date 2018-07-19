package com.hm.iou.pay.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.pay.bean.BindBankCardReqBean;
import com.hm.iou.pay.bean.FourElementsVerifyStatus;
import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.PayTestBean;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.WelfareAdvertiseBean;
import com.hm.iou.pay.dict.ChannelEnumReqBean;
import com.hm.iou.pay.bean.req.CreatePreparePayReqBean;
import com.hm.iou.sharedata.model.BaseResponse;

import java.util.List;

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

    /**
     * 微信支付测试接口
     *
     * @return
     */
    public static Flowable<PayTestBean> payTest() {
        return getService().payTest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取充值消费历史记录
     *
     * @return
     */
    public static Flowable<BaseResponse<List<HistoryItemBean>>> getHistory() {
        return getService().getHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 搜索次卡套餐列表
     *
     * @return
     */
    public static Flowable<BaseResponse<SearchTimeCardListResBean>> searchTimeCardPackageList() {
        return getService().searchTimeCardPackageList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 4要素认证，绑定银行卡
     *
     * @param bankCardNo 银行卡号
     * @param mobile     预留手机号
     * @return
     */
    public static Flowable<BaseResponse<Object>> bindBankCard(String bankCardNo, String mobile) {
        BindBankCardReqBean reqBean = new BindBankCardReqBean();
        reqBean.setBankcard(bankCardNo);
        reqBean.setMobile(mobile);
        return getService().bindBankCard(reqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取四要素实名认证页顶部广告图
     *
     * @return
     */
    public static Flowable<BaseResponse<WelfareAdvertiseBean>> getWelfareAdvertise() {
        return getService().getWelfareAdvertise()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询用户银行卡四要素认证的状态
     *
     * @return
     */
    public static Flowable<BaseResponse<FourElementsVerifyStatus>> checkFourElementsVerifyStatus() {
        return getService().checkFourElementStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建订单
     *
     * @param packageId
     * @return
     */
    public static Flowable<BaseResponse<String>> createOrder(String packageId) {
        return getService().createOrder(packageId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通过订单id查询订单状态
     *
     * @param orderId
     * @return
     */
    public static Flowable<BaseResponse<String>> queryOrderPayState(String orderId) {
        return getService().queryOrderPayState(orderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建预付款订单
     *
     * @param chanel  平台 1微信支付 2苹果支付
     * @param orderId
     * @return
     */
    public static Flowable<BaseResponse<PayTestBean>> createPreparePayOrder(ChannelEnumReqBean chanel, String orderId) {
        CreatePreparePayReqBean reqBean = new CreatePreparePayReqBean();
        reqBean.setChannel(chanel.getValue());
        reqBean.setOrderId(orderId);
        return getService().createPreparePayOrder(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}