package com.hm.iou.pay.business.timecard.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.pay.R;
import com.hm.iou.pay.business.timecard.TimeCardRechargeContract;
import com.hm.iou.pay.comm.ITimeCardItem;
import com.hm.iou.router.Router;
import com.hm.iou.tools.DensityUtil;
import com.hm.iou.tools.ImageLoader;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListFooterHelper {

    private Context mContext;
    private TextView mTvFirstTry;
    private ImageView mIvAdvertisement;
    private View mFooterView;

    public TimeCardListFooterHelper(ViewGroup parentView) {
        mContext = parentView.getContext();
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_time_card_recharge_footer, parentView, false);
        mTvFirstTry = mFooterView.findViewById(R.id.tv_firstTry);
        mIvAdvertisement = mFooterView.findViewById(R.id.iv_advertisement);
        float width = mContext.getResources().getDisplayMetrics().widthPixels - DensityUtil.dip2px(mContext, 20);
        float height = width * 5 / 12;
        ViewGroup.LayoutParams layoutParams = mIvAdvertisement.getLayoutParams();
        layoutParams.width = (int) width;
        layoutParams.height = (int) height;
        mIvAdvertisement.setLayoutParams(layoutParams);
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 显示首次体验
     *
     * @param item
     * @param timeCardRechargePresenter
     */
    public void showFirstTry(final ITimeCardItem item, final TimeCardRechargeContract.Presenter timeCardRechargePresenter) {
        mTvFirstTry.setVisibility(View.VISIBLE);
        mTvFirstTry.setText(item.getTimeCardContent());
        mTvFirstTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TraceUtil.onEvent(mContext, "my_experience_click");
                timeCardRechargePresenter.toFirstTry();
            }
        });
        mTvFirstTry.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timeCardRechargePresenter.getInwardPackage(item.getPackageId());
                return true;
            }
        });
    }

    /**
     * 隐藏首次体验
     */
    public void hideFirstTry() {
        mTvFirstTry.setVisibility(View.GONE);
    }

    /**
     * 显示广告
     *
     * @param adImageUrl
     * @param adLinkUrl
     */
    public void showAdvertisement(String adImageUrl, final String adLinkUrl) {
        mIvAdvertisement.setVisibility(View.VISIBLE);
        ImageLoader.getInstance(mContext)
                .displayImage(adImageUrl, mIvAdvertisement);
        if (!TextUtils.isEmpty(adLinkUrl)) {
            mIvAdvertisement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Router.getInstance()
                            .buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                            .withString("url", adLinkUrl)
                            .navigation(mContext);
                }
            });
        }
    }

    /**
     * 隐藏广告
     */
    public void hideAdvertisement() {
        mTvFirstTry.setVisibility(View.GONE);
    }

}
