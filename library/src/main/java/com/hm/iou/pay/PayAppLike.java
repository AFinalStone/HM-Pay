//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hm.iou.pay;

import android.content.Context;

import com.hm.iou.pay.comm.PaySPUtil;
import com.hm.iou.sharedata.event.LogoutEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PayAppLike {


    private Context mContext;

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
        PaySPUtil.clearCache(mContext);
    }


}
