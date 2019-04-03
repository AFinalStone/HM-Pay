package com.hm.iou.pay.business.type;


import com.hm.iou.base.mvp.BaseContract;

/**
 * 微信支付
 *
 * @author syl
 * @time 2018/5/17 下午4:35
 */
public interface PayVipContract {

    interface View extends BaseContract.BaseView {

        /**
         * 设置微信支付的按钮是否可见
         */
        void setPayByWxBtnVisible(boolean visible);

        /**
         * 设置校验付款结果按钮是否可见
         *
         * @param visible
         */
        void setCheckPayResultBtnVisible(boolean visible);


        /**
         * 设置支付失败按钮是否可见
         *
         * @param visible
         */
        void setPayFailedBtnVisible(boolean visible);

        /**
         * 设置校验付款结果按钮的文字描述
         *
         * @param text
         */
        void setCheckPayResultBtnText(String text);

        /**
         * 设置校验付款结果按钮是否可以点击
         *
         * @param enable
         */
        void setCheckPayResultBtnEnable(boolean enable);

        /**
         * 支付成功关闭页面
         */
        void closePageByPaySuccess();

    }

    interface Presenter extends BaseContract.BasePresenter {
        /**
         * 创建微信支付订单
         *
         * @param packageId 套餐id
         */
        void createPayOrderByWx(String packageId);

        /**
         * 再次进行支付
         */
        void payAgain();

        /**
         * 校验支付结果
         */
        void checkPayResult();

    }
}