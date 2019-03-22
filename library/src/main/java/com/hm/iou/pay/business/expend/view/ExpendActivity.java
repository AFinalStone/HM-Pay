package com.hm.iou.pay.business.expend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.R;
import com.hm.iou.pay.R2;
import com.hm.iou.pay.business.expend.ExpendContract;
import com.hm.iou.pay.business.expend.ExpendPresenter;
import com.hm.iou.pay.comm.ITimeCardItem;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

public class ExpendActivity extends BaseActivity<ExpendPresenter> implements ExpendContract.View {

    public static final String EXTRA_KEY_TRACE_TYPE = "trace_type";
    //控制消费之后，验证签约密码时，不进行其他跳转，如签章选择等待，直接返回密码校验成功。只有当值为1时才生效，其他都是默认效果
    public static final String EXTRA_KEY_VERIFY_SIGNATURE_ONLY = "verify_sign_only";

    private static final int REQ_OPEN_SELECT_PAY_TYPE = 100;

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.rv_timeCardList)
    RecyclerView mRvTimeCardList;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.loading_init)
    HMLoadingView mLoadingInitView;

    private ExpendTimeCardListAdapter mAdapter;
    private ExpendListHeaderHelper mExpendListHeaderHelper;
    private ExpendListFooterHelper mExpendListFooterHelper;

    private String mTraceType;  //埋点类型
    private String mVerifyPwdType;      //只有当值为"1"时，才生效

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_expend_time_card_num;
    }

    @Override
    protected ExpendPresenter initPresenter() {
        return new ExpendPresenter(this, this);
    }


    @Override
    protected void initEventAndData(Bundle bundle) {
        mTraceType = getIntent().getStringExtra(EXTRA_KEY_TRACE_TYPE);
        mVerifyPwdType = getIntent().getStringExtra(EXTRA_KEY_VERIFY_SIGNATURE_ONLY);
        if (bundle != null) {
            mTraceType = bundle.getString(EXTRA_KEY_TRACE_TYPE);
            mVerifyPwdType = bundle.getString(EXTRA_KEY_VERIFY_SIGNATURE_ONLY);
        }
        initView();
        mPresenter.init(mVerifyPwdType);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_TRACE_TYPE, mTraceType);
        outState.putString(EXTRA_KEY_VERIFY_SIGNATURE_ONLY, mVerifyPwdType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_OPEN_SELECT_PAY_TYPE == requestCode && RESULT_OK == resultCode) {
            mPresenter.refresh();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ("draft".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "draft_pay_next_click");
        } else if ("elecb".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "elecb_pay_back_click");
        } else if ("elecr".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "elecr_pay_back_click");
        }
    }

    private void initView() {
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                if ("elecb".equals(mTraceType)) {
                    TraceUtil.onEvent(mContext, "elecb_pay_history_click");
                } else if ("elecr".equals(mTraceType)) {
                    TraceUtil.onEvent(mContext, "elecr_pay_history_click");
                }
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/history")
                        .navigation(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new ExpendTimeCardListAdapter(mContext);
        mRvTimeCardList.setLayoutManager(new GridLayoutManager(mContext, 3));
        //头部
        mExpendListHeaderHelper = new ExpendListHeaderHelper(mRvTimeCardList, this, mPresenter);
        mAdapter.addHeaderView(mExpendListHeaderHelper.getHeaderView());

        mRvTimeCardList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!TextUtils.isEmpty(mTraceType)) {
                    if (0 == position) {
                        TraceUtil.onEvent(mContext, mTraceType + "_pay_gold_click");
                    } else if (1 == position) {
                        TraceUtil.onEvent(mContext, mTraceType + "_pay_silver_click");
                    } else if (2 == position) {
                        TraceUtil.onEvent(mContext, mTraceType + "_pay_comm_click");
                    }
                }
                mPresenter.toAddTimeCardNum(position);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ITimeCardItem data = (ITimeCardItem) adapter.getItem(position);
                if (data == null)
                    return false;
                mPresenter.getInwardPackage(data.getPackageId());
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
    public void refresh() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void traceByClickExpendBtn() {
        if ("draft".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "draft_pay_one_click");
        } else if ("elecb".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "elecb_pay_sign_click");
        } else if ("elecr".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "elecr_pay_sign_click");
        }
    }

    @Override
    public void traceByClickExitBtn() {
        if ("draft".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "draft_pay_next_click");
        } else if ("elecb".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "elecb_pay_exit_click");
        } else if ("elecr".equals(mTraceType)) {
            TraceUtil.onEvent(mContext, "elecr_pay_exit_click");
        }
    }

    @Override
    public void showRemainNum(String num) {
        mExpendListHeaderHelper.setCanUseSignNum(num);
    }

    @Override
    public void showList(List<ITimeCardItem> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showFirstTryBtn(ITimeCardItem firstBean) {
        if (mExpendListFooterHelper == null) {
            mExpendListFooterHelper = new ExpendListFooterHelper(mRvTimeCardList);
            mAdapter.addFooterView(mExpendListFooterHelper.getFooterView());
        }
        mExpendListFooterHelper.showFirstTry(firstBean, mPresenter);
    }

    @Override
    public void hideFirstTryBtn() {
        if (mExpendListFooterHelper != null) {
            mExpendListFooterHelper.hideFirstTry();
        }
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
                mPresenter.init(mVerifyPwdType);
            }
        });
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
                                .navigation(ExpendActivity.this);
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .create().show();
    }
}
