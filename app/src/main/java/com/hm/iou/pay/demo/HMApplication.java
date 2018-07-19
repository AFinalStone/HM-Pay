package com.hm.iou.pay.demo;

import android.app.Application;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.logger.Logger;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;


/**
 * @author syl
 * @time 2018/5/14 下午3:23
 */
public class HMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this);
        Logger.init(this, true);

        BaseBizAppLike baseBizAppLike = new BaseBizAppLike();
        baseBizAppLike.onCreate(this);
//        baseBizAppLike.initServer("http://192.168.1.82:8021", "http://192.168.1.254",
//                "http://192.168.1.254");
        baseBizAppLike.initServer("http://192.168.1.217", "http://192.168.1.217",
                "http://192.168.1.217");
//        baseBizAppLike.initServer("http://192.168.1.254:8021", "http://192.168.1.254",
//                "http://192.168.1.254");
        initNetwork();
    }


    private void initNetwork() {
        System.out.println("init-----------");
        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setDebug(true)
                .setAppChannel("yyb")
                .setToken(UserManager.getInstance(getApplicationContext()).getToken())
                .setUserId(UserManager.getInstance(getApplicationContext()).getUserId())
                .setAppVersion("1.0.2")
                .setDeviceId("123abc123")
                .setBaseUrl(BaseBizAppLike.getInstance().getApiServer())
                .build();
        HttpReqManager.init(config);
    }

}
