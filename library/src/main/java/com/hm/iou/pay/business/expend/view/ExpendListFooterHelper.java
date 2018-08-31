package com.hm.iou.pay.business.expend.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hm.iou.pay.R;
import com.hm.iou.pay.business.expend.ExpendContract;
import com.hm.iou.pay.comm.ITimeCardItem;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class ExpendListFooterHelper {

    private Context mContext;
    private TextView mTvFirstTry;
    private View mFooterView;

    public ExpendListFooterHelper(ViewGroup parentView) {
        mContext = parentView.getContext();
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_expend_footer, parentView, false);
        mTvFirstTry = mFooterView.findViewById(R.id.tv_firstTry);
    }

    public void showFirstTry(final ITimeCardItem item, final ExpendContract.Presenter expendPresenter) {
        mTvFirstTry.setVisibility(View.VISIBLE);
        mTvFirstTry.setText(item.getTimeCardContent());
        mTvFirstTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendPresenter.toFirstTry();
            }
        });
        mTvFirstTry.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                expendPresenter.getInwardPackage(item.getPackageId());
                return true;
            }
        });
    }

    public void hideFirstTry() {
        mTvFirstTry.setVisibility(View.GONE);
    }

    public View getFooterView() {
        return mFooterView;
    }

}
