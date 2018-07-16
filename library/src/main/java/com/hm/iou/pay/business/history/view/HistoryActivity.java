package com.hm.iou.pay.business.history.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.business.history.HistoryContract;
import com.hm.iou.pay.business.history.HistoryPresenter;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMLoadMoreView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 充值历史
 *
 * @author syl
 * @time 2018/7/16 下午5:30
 */
public class HistoryActivity extends BaseActivity<HistoryPresenter> implements HistoryContract.View {

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.rv_timeCardList)
    RecyclerView mRvTimeCardList;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.loading_init)
    HMLoadingView mLoadingInitView;
    HMLoadMoreView mLoadMoreView;
    HistoryListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_history;
    }

    @Override
    protected HistoryPresenter initPresenter() {
        return new HistoryPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        initView();
        mPresenter.init();
    }

    private void initView() {
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/login/customer_service")
                        .navigation(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new HistoryListAdapter(mContext);
        mLoadMoreView = new HMLoadMoreView();
        mAdapter.setLoadMoreView(mLoadMoreView);
        mRvTimeCardList.setLayoutManager(new LinearLayoutManager(mContext));

        mRvTimeCardList.setAdapter(mAdapter);
        //设置下拉刷新监听
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.refresh();
            }
        });
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
    public void enableLoadMore(boolean enable) {
        mRefreshLayout.setEnableLoadMore(enable);
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
    public void showDataEmpty() {
        mLoadingInitView.setVisibility(View.VISIBLE);
        mLoadingInitView.showDataEmpty("");
    }

    @Override
    public void showList(List<IHistoryItem> list) {
        mAdapter.setNewData(list);
    }
}
