package com.hm.iou.pay.business.locksign;


import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.pay.business.locksign.view.ILockSignItem;

import java.util.List;

/**
 * 次卡充值
 *
 * @author syl
 * @time 2018/7/13 下午3:19
 */
public interface LockSignListContract {

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
         * 显示数据为空
         */
        void showDataEmpty();

        /**
         * 显示数据
         */
        void showList(List<ILockSignItem> list);
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
         * 获取更多数据
         */
        void getMore();
    }
}