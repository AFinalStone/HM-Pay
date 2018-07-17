package com.hm.iou.pay.business.timecard;


import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.pay.comm.ITimeCardItem;

import java.util.List;

/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/7/13 下午3:19
 */
public interface TimeCardRechargeContract {

    interface View extends BaseContract.BaseView {
        /**
         * 显示初始化动画
         */
        void showInitLoading();

        /**
         * 隐藏初始化动画
         */
        void hideInitLoading();

        /**
         * 允许刷新
         */
        void enableRefresh(boolean enable);

        /**
         * 隐藏下拉刷新View
         */
        void hidePullDownRefresh();

        /**
         * 初始化失败
         */
        void showInitFailed(String errorMsg);

        /**
         * 显示剩余次数
         */
        void showRemainNum(String num);

        /**
         * 显示充值列表
         */
        void showList(List<ITimeCardItem> list);

        /**
         * 显示首次体验的信息描述
         *
         * @param desc
         */
        void showFirstTry(String desc);

        /**
         * 显示广告
         *
         * @param adImageUrl 广告图片
         * @param adLinkUrl  广告链接
         */
        void showAdvertisement(String adImageUrl, String adLinkUrl);

        /**
         * 充值
         *
         * @param num
         * @param money
         */
        void toSelectPayType(String num, String money);
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 初始化
         */
        void init();

        /**
         * 刷新数据
         */
        void refresh();
    }
}