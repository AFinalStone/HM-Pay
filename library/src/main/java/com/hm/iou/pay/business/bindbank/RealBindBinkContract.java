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
         * 显示顶部广告
         *
         * @param adUrl
         * @param linkUrl
         */
        void showTopAd(String adUrl, String linkUrl);

        /**
         * 显示认证成功对话框
         */
        void showAuthSuccDialog();
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getUserRealName();

        /**
         * 获取顶部的广告
         */
        void getTopAd();

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

        boolean isCardNoInputError();

        boolean isMobileInputError();
    }
}