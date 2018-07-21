package com.hm.iou.pay;

/**
 * Created by hjy on 18/5/15.<br>
 */

public class Constants {

    /**
     * 用户充值协议
     */
    public static final String H5_URL_RECHARGE_AGREEMENT = "/rechargeAgreement/index.html";

    public static final String SP_NAME = "hm_pay";
    //存储用户是否成功绑定过银行卡
    public static final String SP_KEY_BIND_BANK_SUCCESS = "key_bind_bank_success_";
    //存储用户今日是否有剩余次数去绑定银行卡
    public static final String SP_KEY_CAN_BIND_BANK_TODAY = "key_can_bind_bank_today_";


    public static final String AD_POSITION_CARD_CHARGE = "banner001";
    public static final String AD_POSITION_FOUR_ELEMENTS = "banner002";
}
