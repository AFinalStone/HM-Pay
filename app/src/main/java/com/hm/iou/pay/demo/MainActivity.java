package com.hm.iou.pay.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.pay.business.wxpay.WXPayActivity;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_wxPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WXPayActivity.class));
            }
        });
        findViewById(R.id.btn_checkPayResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
