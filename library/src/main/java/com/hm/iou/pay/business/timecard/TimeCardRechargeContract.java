package com.hm.iou.pay.business.timecard;


import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.pay.bean.VipCardUseBean;
import com.hm.iou.pay.business.timecard.view.IVipCardItem;
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
         * 显示被占用的签章数量
         *
         * @param num
         */
        void showLockSignNum(String num);

        /**
         * 显示充值列表
         */
        void showList(List<ITimeCardItem> list);

        /**
         * 刷新页面数据
         */
        void refresh();

        /**
         * 提醒用户剩余签章数量已超过10个
         */
        void showSignCountMoreThanTen();

        /**
         * 显示贵宾卡套餐
         *
         * @param list
         */
        void showVipCardPackage(List<IVipCardItem> list);

        /**
         * 显示贵宾卡使用情况
         *
         * @param data
         */
        void showVipCardUsage(VipCardUseBean data);
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
        void toAddTimeCardByPosition(int position);

        /**
         * 获取内部用户充值信息
         */
        void getInwardPackage(String packageId);
    }
}