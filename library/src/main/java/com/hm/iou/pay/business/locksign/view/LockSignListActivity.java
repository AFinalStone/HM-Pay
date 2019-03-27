package com.hm.iou.pay.business.locksign.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.database.IouDbHelper;
import com.hm.iou.database.table.IouData;
import com.hm.iou.logger.Logger;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.business.locksign.LockSignListContract;
import com.hm.iou.pay.business.locksign.LockSignListPresenter;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.IOUKindEnum;
import com.hm.iou.uikit.HMLoadMoreView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 占用签章列表
 *
 * @author syl
 * @time 2018/7/16 下午5:30
 */
public class LockSignListActivity extends BaseActivity<LockSignListPresenter> implements LockSignListContract.View {

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.rv_timeCardList)
    RecyclerView mRvTimeCardList;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.loading_init)
    HMLoadingView mLoadingInitView;
    HMLoadMoreView mLoadMoreView;
    LockSignListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_lock_sign_list;
    }

    @Override
    protected LockSignListPresenter initPresenter() {
        return new LockSignListPresenter(this, this);
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
        mAdapter = new LockSignListAdapter(mContext);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ILockSignItem item = (ILockSignItem) adapter.getItem(position);
                if (item != null && !TextUtils.isEmpty(item.getIContractId())) {
                    toIOUDetailByJusticeId(item.getIContractId());
                }
            }
        });
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
    public void showList(List<ILockSignItem> list) {
        mAdapter.setNewData(list);
    }

    public void toIOUDetailByJusticeId(final String justiceId) {
        IouData iouData = IouDbHelper.queryIOUByJusticeId(justiceId);
        Logger.d("justiceId = " + justiceId);
        if (iouData == null) {//电子借条2.0已经被隐藏，收录电子借条2.0
            new HMAlertDialog.Builder(mContext)
                    .setMessage("借条已隐藏，是否需要收录此借条")
                    .setPositiveButton("收录")
                    .setNegativeButton("取消")
                    .setOnClickListener(new HMAlertDialog.OnClickListener() {
                        @Override
                        public void onPosClick() {
                            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/iou_search/include")
                                    .withString("iou_kind_type", "10")
                                    .withString("justId", justiceId)
                                    .navigation(mContext);

                        }

                        @Override
                        public void onNegClick() {

                        }
                    })
                    .create()
                    .show();

            return;
        }
        String iouId = iouData.getIouId();
        if (IOUKindEnum.ElecBorrowReceipt.getValue() == iouData.getIouKind()) {//吕约借条
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/iou/elec_borrow_detail")
                    .withString("iou_id", iouId)
                    .navigation(mContext);
            return;
        }
        if (IOUKindEnum.ElecReceiveReceipt.getValue() == iouData.getIouKind()) {//吕约收条
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/iou/elec_receive_detail")
                    .withString("iou_id", iouId)
                    .navigation(mContext);
            return;
        }
        //吕约借条2.0
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/iou/elec_borrow_detail_v2")
                .withString("iou_id", iouId)
                .navigation(mContext);
    }
}
