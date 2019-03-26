package com.hm.iou.pay.business.bindbank;


import com.hm.iou.base.mvp.BaseContract;

/**
 * 四要素实名认证权限请求
 */
public interface BindBinkContract {

    interface View extends BaseContract.BaseView {
        /**
         * 警告当日已经没有剩余次数去进行银行卡绑定
         */
        void warnNoTimeToBindToday();

        /**
         * 显示实名认证对话框
         */
        void showAuthDialog();

        /**
         * 显示用户已绑定的银行卡信息
         *
         */
        void showBinkBankInfo();

        /**
         * 进入到真实绑卡页面
         */
        void toRealBindCardPage();

    }

    interface Presenter extends BaseContract.BasePresenter {
        /**
         * 检验是否有权限进行银行卡的绑定
         */
        void checkCanBindBank();
    }
}