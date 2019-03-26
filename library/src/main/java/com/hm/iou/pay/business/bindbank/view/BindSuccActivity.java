package com.hm.iou.pay.business.bindbank.view;

import android.os.Bundle;
import android.view.View;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.Constants;
import com.hm.iou.pay.R;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.HMTopBarView;

/**
 * Created by hjy on 2019/3/26.
 */

public class BindSuccActivity extends BaseActivity {

    String mSource;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity_bind_card_succ;
    }

    @Override
    protected MvpActivityPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mSource = getIntent().getStringExtra(Constants.EXTRA_KEY_SOURCE);
        HMTopBarView topBarView = findViewById(R.id.topBar);
        topBarView.setOnBackClickListener(new HMTopBarView.OnTopBarBackClickListener() {
            @Override
            public void onClickBack() {
                closePage();
            }
        });
        findViewById(R.id.btn_bind_card_succ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TraceUtil.onEvent(mContext, "back_receive_awardreceive_award");
                closePage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        closePage();
    }

    private void closePage() {
        if (Constants.BIND_CARD_SOURCE_USERCENTER.equals(mSource)) {
            //如果是从个人中心进来，则跳转到绑卡详情页面
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/person/user_bind_bank_info")
                    .navigation(mContext);
        }
        finish();
    }

}
