package com.hm.iou.pay.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.pay.bean.BindBankCardReqBean;
import com.hm.iou.pay.bean.FourElementsVerifyStatus;
import com.hm.iou.pay.bean.HistoryItemBean;
import com.hm.iou.pay.bean.PayTestBean;
import com.hm.iou.pay.bean.SearchTimeCardListResBean;
import com.hm.iou.pay.bean.WelfareAdvertiseBean;
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

    public static Flowable<PayTestBean> payTest() {
        return getService().payTest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<List<HistoryItemBean>>> getHistory() {
        return getService().getHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

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

    public static Flowable<BaseResponse<FourElementsVerifyStatus>> checkFourElementsVerifyStatus() {
        return getService().checkFourElementStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}