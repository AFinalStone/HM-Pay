package com.hm.iou.pay.business.timecard.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.pay.R;
import com.hm.iou.router.Router;
import com.hm.iou.tools.ImageLoader;

/**
 * @author syl
 * @time 2018/7/16 上午11:40
 */
public class TimeCardListFooterHelp {

    private Context mContext;
    TextView mTvFirstTry;
    ImageView mIvAdvertisement;
    View.OnClickListener mOnClickListener;
    private View mFooterView;

    public TimeCardListFooterHelp(ViewGroup parentView) {
        mContext = parentView.getContext();
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.pay_item_time_card_recharge_footer, parentView, false);
        mTvFirstTry = mFooterView.findViewById(R.id.tv_firstTry);
        mIvAdvertisement = mFooterView.findViewById(R.id.iv_advertisement);
        mTvFirstTry.setOnClickListener(mOnClickListener);
    }

    public void setOnFirstTryBtnClickListener(@NonNull View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    public View getFooterView() {
        return mFooterView;
    }

    public void showAdvertisement(String adImageUrl, String adLinkUrl) {
        ImageLoader.getInstance(mContext)
                .displayImage(adImageUrl, mIvAdvertisement, R.drawable.uikit_bg_pic_loading_place
                        , R.drawable.uikit_bg_pic_loading_error);
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
