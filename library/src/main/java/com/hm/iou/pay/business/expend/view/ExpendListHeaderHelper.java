package com.hm.iou.pay.business.expend.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.pay.Constants;
import com.hm.iou.pay.R;
import com.hm.iou.pay.business.expend.ExpendContract;
import com.hm.iou.router.Router;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class ExpendListHeaderHelper {

    private TextView mTvRemainderNum;
    private View mHeaderView;

    public ExpendListHeaderHelper(ViewGroup parentView, ExpendContract.View expendView, ExpendContract.Presenter expendPresenter) {
        mHeaderView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.pay_item_expend_header, parentView, false);
        mTvRemainderNum = mHeaderView.findViewById(R.id.tv_remainderNum);
        mHeaderView.findViewById(R.id.btn_expend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendPresenter.toExpendOnceTime();
            }
        });
        mHeaderView.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendView.closeCurrPage();
            }
        });
        mHeaderView.findViewById(R.id.tv_rechargeAgreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                        .withString("url", BaseBizAppLike.getInstance().getH5Server() + Constants.H5_URL_RECHARGE_AGREEMENT)
                        .navigation(parentView.getContext());
            }
        });
    }


    public View getHeaderView() {
        return mHeaderView;
    }


    public void setRemainderNum(String remainderNum) {
        mTvRemainderNum.setText(remainderNum);
    }
}