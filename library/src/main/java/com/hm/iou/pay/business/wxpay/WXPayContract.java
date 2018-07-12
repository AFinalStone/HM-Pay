package com.hm.iou.pay.business.wxpay;


import com.hm.iou.base.mvp.BaseContract;

/**
 * 选择登录方式
 *
 * @author syl
 * @time 2018/5/17 下午4:35
 */
public interface WXPayContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter {
        /**
         * 通过微信进行付款
         */
        void payByWx();

    }
}