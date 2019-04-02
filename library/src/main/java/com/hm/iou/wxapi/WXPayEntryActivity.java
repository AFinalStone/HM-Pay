package com.hm.iou.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.logger.Logger;
import com.hm.iou.tools.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;


/**
 * 微信支付的回调页面
 *
 * @author syl
 * @time 2018/7/13 上午9:50
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hm.iou.base.R.layout.base_activity_wx_pay);
        mIWXAPI = WXAPIFactory.createWXAPI(this, getAppId());
        mIWXAPI.handleIntent(getIntent(), this);


    }

    private static String getAppId() {
        PlatformConfig.APPIDPlatform weixin = (PlatformConfig.APPIDPlatform) PlatformConfig.configs.get(SHARE_MEDIA.WEIXIN);
        String appId = weixin.appId;
        return appId;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIWXAPI.detach();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.d("微信请求openId" + baseReq.openId);
        Logger.d("微信请求transaction" + baseReq.transaction);
    }

    /**
     * 微信回调方法
     *
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        Logger.d("微信回调ErrorCode" + baseResp.errCode);
        Logger.d("微信回调Type" + baseResp.getType());
        Logger.d("微信回调ErrStr" + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //微信支付
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Logger.d("WXPay", "用户支付成功");
                    String key = ((PayResp) baseResp).extData;
                    OpenWxResultEvent event = new OpenWxResultEvent();
                    event.setKey(key);
                    event.setIfPaySuccess(true);
                    EventBus.getDefault().post(event);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Logger.d("WXPay", "用户取消操作");
                    ToastUtil.showMessage(this, "用户取消操作");
                    break;
                default:
                    Logger.d("WXPay", "发生异常，请稍后再试");
                    ToastUtil.showMessage(this, "发生异常，请稍后再试");
            }
        }
        finish();
    }

    public static IWXAPI createWXApi(Context context) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, null);
        iwxapi.registerApp(getAppId());
        return iwxapi;
    }

    /**
     * 微信支付
     *
     * @param partnerId    商户号
     * @param packageValue
     * @param nonceStr
     * @param timeStamp
     * @param sign
     * @param key
     */
    public static boolean wxPay(IWXAPI wxApi, String partnerId, String prepayid
            , String packageValue, String nonceStr, String timeStamp, String sign, String key) {
        PayReq request = new PayReq();
        request.appId = getAppId();
        request.partnerId = partnerId;
        request.prepayId = prepayid;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        request.extData = key;
        return wxApi.sendReq(request);
    }

}
