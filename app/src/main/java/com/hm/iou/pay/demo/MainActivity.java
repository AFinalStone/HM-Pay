package com.hm.iou.pay.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.pay.business.type.SelectPayTypeActivity;
import com.hm.iou.router.Router;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_wxPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/pay/select_pay_type")
                        .withString("time_card_num", "1")
                        .withString("time_card_money", "10")
                        .navigation(MainActivity.this);
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
    }
}
