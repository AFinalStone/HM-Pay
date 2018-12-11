package com.hm.iou.pay.business.expend;


import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.pay.comm.ITimeCardItem;

import java.util.List;

/**
 * 次卡消费
 *
 * @author syl
 * @time 2018/7/13 下午3:19
 */
public interface ExpendContract {

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
         * 显示首次体验的按钮
         *
         * @param firstBean
         */
        void showFirstTryBtn(ITimeCardItem firstBean);

        /**
         * 隐藏首次体验按钮
         */
        void hideFirstTryBtn();

        /**
         * 刷新页面数据
         */
        void refresh();

        /**
         * 埋点操作,消耗一次签章按钮
         */
        void traceByClickExpendBtn();

        /**
         * 埋点操作,退出
         */
        void traceByClickExitBtn();

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
         * 消耗一次次卡数量
         */
        void toExpendOnceTime();

        /**
         * 首次体验
         */
        void toFirstTry();

        /**
         * 获取内部用户充值信息
         */
        void getInwardPackage(String packageId);
    }
}