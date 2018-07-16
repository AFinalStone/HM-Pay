package com.hm.iou.pay.business.fourelements;


import com.hm.iou.base.mvp.BaseContract;

/**
 * 四要素实名认证
 */
public interface RealNameContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示用户名称
         *
         * @param userName
         */
        void showUserName(CharSequence userName);

        void enableSubmitButton(boolean enabled);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getUserRealName();

        /**
         * 验证输入是否合法
         *
         * @param cardNo
         * @param mobile
         */
        void checkUserInputValue(String cardNo, String mobile);

        /**
         * 进行4要素实名认证
         *
         * @param cardNo
         * @param mobile
         */
        void doFourElementsRealName(String cardNo, String mobile);
    }
}