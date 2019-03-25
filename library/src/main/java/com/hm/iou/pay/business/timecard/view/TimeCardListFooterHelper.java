package com.hm.iou.pay.business.timecard.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hm.iou.pay.R;
import com.hm.iou.router.Router;
import com.hm.iou.tools.DensityUtil;
import com.hm.iou.tools.ImageLoader;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListFooterHelper {

    private Context mContext;
    private ImageView mIvAdvertisement;
    private View mFooterView;

    public TimeCardListFooterHelper(ViewGroup parentView) {
        mContext = parentView.getContext();
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_time_card_recharge_footer, parentView, false);
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
                    if (adLinkUrl.startsWith("http")) {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                                .withString("url", adLinkUrl)
                                .navigation(mContext);
                    } else {
                        Router.getInstance()
                                .buildWithUrl(adLinkUrl)
                                .navigation(mContext);
                    }
                }
            });
        }
    }

    /**
     * 隐藏广告
     */
    public void hideAdvertisement() {
        mIvAdvertisement.setVisibility(View.GONE);
    }

}
