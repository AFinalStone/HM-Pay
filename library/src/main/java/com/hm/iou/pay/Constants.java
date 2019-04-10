package com.hm.iou.pay;

/**
 * Created by hjy on 18/5/15.<br>
 */

public class Constants {

    /**
     * 用户充值协议
     */
    public static final String H5_URL_RECHARGE_AGREEMENT = "https://app.54jietiao.com/xieyi/3.html";

    public static final String SP_NAME = "hm_pay";
    //存储用户是否成功绑定过银行卡
    public static final String SP_KEY_BIND_BANK_SUCCESS = "key_bind_bank_success_";
    //存储用户今日是否有剩余次数去绑定银行卡
    public static final String SP_KEY_CAN_BIND_BANK_TODAY = "key_can_bind_bank_today_";


    public static final String AD_POSITION_CARD_CHARGE = "banner001";
    public static final String AD_POSITION_FOUR_ELEMENTS = "banner002";

    public static int colorGreen = 0xFF07C160;
    public static int colorBlue = 0xFF2782E2;
    public static int colorGray = 0xFF575B62;
    public static int colorRed = 0xFFEF5050;
    public static int colorBlack = 0xFF111111;

    //绑卡页面来源出处
    public static final String EXTRA_KEY_SOURCE = "source";
    public static final String BIND_CARD_SOURCE_BANNER = "banner";       //绑卡来源，从banner里进
    public static final String BIND_CARD_SOURCE_USERCENTER = "usercenter";  //绑卡来源，个人中心侧滑
    public static final String BIND_CARD_SOURCE_UPDATE = "updatebank";      //绑卡来源，更新银行卡地址

}
