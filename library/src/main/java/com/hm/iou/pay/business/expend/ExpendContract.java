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
         * 显示首次体验的信息描述
         *
         * @param firstBean
         */
        void showFirstTry(ITimeCardItem firstBean);

        /**
         * 增加签章次数
         */
        void toFirstTry();

        /**
         * 消耗一次次卡数量
         */
        void toExpendOnceTime();

        /**
         * 刷新页面数据
         */
        void refresh();
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
         * @param isFirstTry
         * @param position
         */
        void toAddTimeCardNum(boolean isFirstTry, int position);

        /**
         * 查询用户银行卡四要素认证的状态
         */
        void checkFourElementsVerifyStatus();
    }
}