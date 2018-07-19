package com.hm.iou.pay.business.timecard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.business.expend.view.ExpendListFooterHelp;
import com.hm.iou.pay.business.timecard.TimeCardRechargeContract;
import com.hm.iou.pay.business.timecard.TimeCardRechargePresenter;
import com.hm.iou.pay.comm.ITimeCardItem;
import com.hm.iou.pay.comm.TimeCardListAdapter;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

public class TimeCardRechargeActivity extends BaseActivity<TimeCardRechargePresenter> implements TimeCardRechargeContract.View {

    private static final int REQ_OPEN_SELECT_PAY_TYPE = 100;

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.rv_timeCardList)
    RecyclerView mRvTimeCardList;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.loading_init)
    HMLoadingView mLoadingInitView;

    TimeCardListAdapter mAdapter;
    TimeCardListHeaderHelp mTimeCardListHeaderHelp;
    TimeCardListFooterHelp mTimeCardListFooterHelp;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_time_card_recharge;
    }

    @Override
    protected TimeCardRechargePresenter initPresenter() {
        return new TimeCardRechargePresenter(this, this);
    }


    @Override
    protected void initEventAndData(Bundle bundle) {
        initView();
        mPresenter.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_OPEN_SELECT_PAY_TYPE == requestCode && RESULT_OK == resultCode) {
            mPresenter.refresh();
        }
    }

    private void initView() {
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/history")
                        .navigation(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new TimeCardListAdapter();
        mRvTimeCardList.setLayoutManager(new GridLayoutManager(mContext, 3));
        //头部
        mTimeCardListHeaderHelp = new TimeCardListHeaderHelp(mRvTimeCardList);
        mAdapter.addHeaderView(mTimeCardListHeaderHelp.getHeaderView());

        mRvTimeCardList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.toAddTimeCardNum(false, position);
            }
        });
        //设置下拉刷新监听
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.refresh();
            }
        });
    }

    @Override
    public void showRemainNum(String num) {
        mTimeCardListHeaderHelp.setRemainderNum(num);
    }

    @Override
    public void showList(List<ITimeCardItem> list) {
        mAdapter.setNewData(list);
    }


    @Override
    public void showInitLoading() {
        mLoadingInitView.setVisibility(View.VISIBLE);
        mLoadingInitView.showDataLoading();
    }

    @Override
    public void hideInitLoading() {
        mLoadingInitView.setVisibility(View.GONE);
    }

    @Override
    public void enableRefresh(boolean enable) {
        mRefreshLayout.setEnableRefresh(enable);
    }


    @Override
    public void hidePullDownRefresh() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showInitFailed(String errorMsg) {
        mLoadingInitView.setVisibility(View.VISIBLE);
        mLoadingInitView.showDataFail(errorMsg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.init();
            }
        });
    }

    @Override
    public void showAdvertisement(String adImageUrl, String adLinkUrl) {
        mTimeCardListFooterHelp.showAdvertisement(adImageUrl, adLinkUrl);
    }

    @Override
    public void showFirstTry(ITimeCardItem firstTryBean) {
        if (mTimeCardListFooterHelp == null) {
            mTimeCardListFooterHelp = new TimeCardListFooterHelp(mRvTimeCardList);
            mAdapter.addFooterView(mTimeCardListFooterHelp.getFooterView());
        }
        mTimeCardListFooterHelp.showFirstTry(firstTryBean, this);
    }

    @Override
    public void toFirstTry() {
        mPresenter.toAddTimeCardNum(true, 0);
    }

    @Override
    public void refresh() {
        mRefreshLayout.autoRefresh();
    }
}
