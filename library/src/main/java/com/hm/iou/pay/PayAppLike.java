//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hm.iou.pay;

import android.content.Context;

import com.hm.iou.sharedata.event.LogoutEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.Disposable;

public class PayAppLike {

    public static final String EXTRA_KEY_GET_DEBT_TOTAL_NUM = "Debt_getDebtTotalNum";//获取记债本数量
    public static final String EXTRA_KEY_DEBT_NUM = "Debt_debtTotalNum";//记债本总的数量

    private Context mContext;
    private Disposable mDisposable;

    public void onCreate(Context context) {
        this.mContext = context;
        EventBus.getDefault().register(this);
    }

    /**
     * 用户登出
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventLogout(LogoutEvent event) {
        CacheDataUtil.clearCache(mContext);
    }


}
