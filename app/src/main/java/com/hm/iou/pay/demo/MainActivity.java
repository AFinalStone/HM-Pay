package com.hm.iou.pay.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ToastUtil;
import com.sina.weibo.sdk.utils.MD5;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btn_timeCardRecharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/time_card_recharge")
                        .navigation(MainActivity.this);
            }
        });
        findViewById(R.id.btn_timeCardHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/history")
                        .navigation(MainActivity.this);
            }
        });
        findViewById(R.id.btn_expendTimeCardNum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/expend_time_card_num")
                        .navigation(MainActivity.this);
            }
        });

        findViewById(R.id.btn_four_elements).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank")
                        .navigation(MainActivity.this);
            }
        });
    }

    private void login() {
        String pwd = MD5.hexdigest("111111".getBytes());
        MobileLoginReqBean reqBean = new MobileLoginReqBean();
//        reqBean.setMobile("13186975702");
        reqBean.setMobile("15967132742");
        reqBean.setQueryPswd(pwd);
        HttpReqManager.getInstance().getService(LoginService.class)
                .mobileLogin(reqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<UserInfo>>() {
                    @Override
                    public void accept(BaseResponse<UserInfo> userInfoBaseResponse) throws Exception {
                        if (userInfoBaseResponse.getErrorCode() != 0) {
                            ToastUtil.showMessage(MainActivity.this, userInfoBaseResponse.getMessage());
                            return;
                        }
                        ToastUtil.showMessage(MainActivity.this, "登录成功");
                        UserInfo userInfo = userInfoBaseResponse.getData();
                        UserManager.getInstance(MainActivity.this).updateOrSaveUserInfo(userInfo);
                        HttpReqManager.getInstance().setUserId(userInfo.getUserId());
                        HttpReqManager.getInstance().setToken(userInfo.getToken());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        t.printStackTrace();
                    }
                });
    }
}
