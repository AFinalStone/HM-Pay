package com.hm.iou.pay.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.pay.business.timecard.view.TimeCardRechargeActivity;
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
                /*Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/time_card_recharge")
                        .navigation(MainActivity.this);
                */

                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/pay_vip?time_card_pay_money=9&time_card_name=VIP会员&package_id=123")
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

        findViewById(R.id.btn_my_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimeCardRechargeActivity.class));
            }
        });
        findViewById(R.id.btn_pay_publish_qj_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/pay_publish_qj_code")
                        .withString("qj_code_apply_id", "293162632238706688")
                        .withString("qj_code_apply_publish_type", "1")
                        .withString("package_title", "AA借款")
                        .withString("package_money", "10元")
                        .withString("package_sub_title", "送1次签章，每次签章包含：")
                        .withString("package_content", " CFCA数字证书签章1个；\n国家授时中心可信时间戳2个； \n电子合同多地同步备份； \n国立公证处合同公证服务； \n\n如果同意，这笔费用不支持退款！\n如果不同意，请想清楚后再支付。")
                        .navigation(MainActivity.this);
            }
        });

        test();
    }

    private void login() {
        String pwd = MD5.hexdigest("123456".getBytes());
        MobileLoginReqBean reqBean = new MobileLoginReqBean();
//        reqBean.setMobile("13186975702");
//        reqBean.setMobile("15967132742");
        reqBean.setMobile("15267163669");
//        reqBean.setMobile("18337150117");
//        reqBean.setMobile("17681832816");

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


    private void test() {
        System.out.println("BOARD=" + Build.BOARD);
        System.out.println("BRAND=" + Build.BRAND);
        System.out.println("CPU_ABI=" + Build.CPU_ABI);
        System.out.println("DEVICE=" + Build.DEVICE);
        System.out.println("DISPLAY=" + Build.DISPLAY);
        System.out.println("HOST=" + Build.HOST);
        System.out.println("ID=" + Build.ID);
        System.out.println("MANUFACTURER=" + Build.MANUFACTURER);
        System.out.println("MODEL=" + Build.MODEL);
        System.out.println("PRODUCT=" + Build.PRODUCT);
        System.out.println("TAGS=" + Build.TAGS);
        System.out.println("TYPE=" + Build.TYPE);
        System.out.println("USER=" + Build.USER);
    }

}
