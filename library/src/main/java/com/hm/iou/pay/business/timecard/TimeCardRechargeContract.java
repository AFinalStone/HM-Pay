package com.hm.iou.pay.business.timecard;


import com.hm.iou.base.mvp.BaseContract;

/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/7/13 下午3:19
 */
public interface TimeCardRechargeContract {

    interface View extends BaseContract.BaseView {
        /**
         * 显示剩余次数
         */
        void showRemainNum(String num);

        /**
         * 显示充值列表
         */
        void showList();

        /**
         * 显示初始化动画
         */
        void showInitLoading();

        /**
         * 隐藏初始化动画
         */
        void hideInitLoading();

        /**
         * 显示广告
         *
         * @param adImageUrl 广告图片
         * @param adLinkUrl  广告链接
         */
        void showAdvertisemenr(String adImageUrl, String adLinkUrl);
    }

    interface Presenter extends BaseContract.BasePresenter {

    }
}