package com.hm.iou.pay.business.bindbank;


import com.hm.iou.base.mvp.BaseContract;

/**
 * 四要素实名认证
 */
public interface RealBindBinkContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示用户名称
         *
         * @param userName
         */
        void showUserName(CharSequence userName);

        void enableSubmitButton(boolean enabled);

        void updateCardNoAboutIcon(int iconResId);

        void updateMobileAboutIcon(int iconResId);

        /**
         * 认证失败，弹出重试提醒对话框
         *
         * @param msg
         */
        void showAuthFailRetryDialog(String msg);

        /**
         * 认证失败，并且已经超过当日次数限制
         *
         * @param msg
         */
        void showAuthFailExceedCount(String msg);

        /**
         * 认证成功，并跳转到赠送签章页面
         */
        void bindSuccAndJump2GiveSignaturePage();
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
         * @param source
         * @param cardNo
         * @param mobile
         */
        void doFourElementsRealName(String source, String cardNo, String mobile);

        boolean isCardNoInputError();

        boolean isMobileInputError();
    }
}