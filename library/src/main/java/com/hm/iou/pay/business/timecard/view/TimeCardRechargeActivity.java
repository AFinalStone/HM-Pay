package com.hm.iou.pay.business.timecard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.bean.VipCardUseBean;
import com.hm.iou.pay.business.timecard.TimeCardRechargeContract;
import com.hm.iou.pay.business.timecard.TimeCardRechargePresenter;
import com.hm.iou.pay.comm.ITimeCardItem;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.List;

import butterknife.BindView;

/**
 * 我的签章页面
 */
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

    private TimeCardListAdapter mAdapter;
    private TimeCardListHeaderHelper mTimeCardListHeaderHelper;
    private TimeCardListFooterHelper mTimeCardListFooterHelper;

    private boolean mIsNeedRefresh = false;

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
    protected void onResume() {
        super.onResume();
        if (mIsNeedRefresh) {
            mRefreshLayout.autoRefresh();
            mIsNeedRefresh = false;
        }
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
                TraceUtil.onEvent(mContext, "my_charge_history_click");
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/login/customer_service")
                        .navigation(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new TimeCardListAdapter(mContext);
        mRvTimeCardList.setLayoutManager(new GridLayoutManager(mContext, 3));
        //头部
        mTimeCardListHeaderHelper = new TimeCardListHeaderHelper(this, mPresenter);
        mAdapter.addHeaderView(mTimeCardListHeaderHelper.getHeaderView());

        mRvTimeCardList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    TraceUtil.onEvent(mContext, "my_gold_click");
                } else if (position == 1) {
                    TraceUtil.onEvent(mContext, "my_silver_click");
                } else if (position == 2) {
                    TraceUtil.onEvent(mContext, "my_silver_click");
                }
                mPresenter.toAddTimeCardByPosition(position);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ITimeCardItem item = (ITimeCardItem) adapter.getItem(position);
                if (item == null)
                    return false;
                mPresenter.getInwardPackage(item.getPackageId());
                return true;
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
        mTimeCardListHeaderHelper.setCanUseSignNum(num);
    }

    @Override
    public void showLockSignNum(String num) {
        mTimeCardListHeaderHelper.setDisableSignNum(num);
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
    public void refresh() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void showSignCountMoreThanTen() {
        new HMAlertDialog.Builder(this)
                .setMessage("剩余签章数量已经超过10个，请用完再购买。")
                .setPositiveButton("购买联系")
                .setNegativeButton("感谢提醒")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/helper_center")
                                .navigation(TimeCardRechargeActivity.this);
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .create().show();
    }

    @Override
    public void showVipCardPackage(List<IVipCardItem> list) {
        if (mTimeCardListFooterHelper == null) {
            mTimeCardListFooterHelper = new TimeCardListFooterHelper(this);
            mAdapter.addFooterView(mTimeCardListFooterHelper.getFooterView());
        }
        mTimeCardListFooterHelper.showVipCardPackage(list);
    }

    @Override
    public void showVipCardUsage(VipCardUseBean data) {
        if (mTimeCardListFooterHelper == null) {
            mTimeCardListFooterHelper = new TimeCardListFooterHelper(this);
            mAdapter.addFooterView(mTimeCardListFooterHelper.getFooterView());
        }
        mTimeCardListFooterHelper.showPersonalVipCardInfo(data);
    }
}