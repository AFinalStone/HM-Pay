package com.hm.iou.pay.business.expend.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.pay.R;
import com.hm.iou.pay.business.expend.ExpendContract;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class ExpendListFooterHelp {

    private Context mContext;
    TextView mTvFirstTry;
    private View mFooterView;

    public ExpendListFooterHelp(ViewGroup parentView, ExpendContract.View expendView) {
        mContext = parentView.getContext();
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_expend_footer, parentView, false);
        mTvFirstTry = mFooterView.findViewById(R.id.tv_firstTry);
        mTvFirstTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendView.toSelectPayType("1", "5");
            }
        });
    }

    public View getFooterView() {
        return mFooterView;
    }

}
