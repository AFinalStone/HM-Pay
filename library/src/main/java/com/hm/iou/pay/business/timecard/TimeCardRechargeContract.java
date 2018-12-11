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
         * 显示广告
         *
         * @param adImageUrl 广告图片
         * @param adLinkUrl  广告链接
         */
        void showAdvertisement(String adImageUrl, String adLinkUrl);

        /**
         * 隐藏广告
         */
        void hideAdvertisement();

        /**
         * 显示首次体验的信息描述
         *
         * @param firstTryBean
         */
        void showFirstTry(ITimeCardItem firstTryBean);

        /**
         * 隐藏首次体验
         */
        void hideFirstTry();

        /**
         * 刷新页面数据
         */
        void refresh();

        /**
         * 提醒用户剩余签章数量已超过10个
         */
        void showSignCountMoreThanTen();
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

        /**
         * 增加次卡数量
         *
         * @param position
         */
        void toAddTimeCardNum(int position);

        /**
         * 首次体验
         */
        void toFirstTry();

        /**
         * 获取底部广告
         */
        void getBottomAd();

        /**
         * 获取内部用户充值信息
         */
        void getInwardPackage(String packageId);
    }
}