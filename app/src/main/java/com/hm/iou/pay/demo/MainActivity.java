package com.hm.iou.pay.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.pay.business.fourelements.RealNameActivity;
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
                        .withString("time_card_num", "1次卡")
                        .withString("time_card_money", "¥10")
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
                startActivity(new Intent(MainActivity.this, RealNameActivity.class));
            }
        });
    }
}
