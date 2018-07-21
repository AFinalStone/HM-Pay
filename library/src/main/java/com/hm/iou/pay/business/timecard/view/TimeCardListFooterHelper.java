package com.hm.iou.pay.business.timecard.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.pay.R;
import com.hm.iou.pay.business.timecard.TimeCardRechargeContract;
import com.hm.iou.pay.comm.ITimeCardItem;
import com.hm.iou.router.Router;
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
    public void showFirstTry(ITimeCardItem item, TimeCardRechargeContract.Presenter timeCardRechargePresenter) {
        mTvFirstTry.setVisibility(View.VISIBLE);
        mTvFirstTry.setText(item.getTimeCardContent());
        mTvFirstTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCardRechargePresenter.toFirstTry();
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
    public void showAdvertisement(String adImageUrl, String adLinkUrl) {
        mIvAdvertisement.setVisibility(View.VISIBLE);
        ImageLoader.getInstance(mContext)
                .displayImage(adImageUrl, mIvAdvertisement, R.drawable.uikit_bg_pic_loading_place
                        , R.drawable.uikit_bg_pic_loading_error);
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
