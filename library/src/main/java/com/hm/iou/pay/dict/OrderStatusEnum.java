package com.hm.iou.pay.dict;

/**
 * Created by hjy on 2018/7/21.
 */

public enum OrderStatusEnum {

    WAIT_PAY(1, "待支付"),
    PAYING(2, "支付中"),
    PAID_NOT_USED(3, "已支付（未使用完）"),
    GIFT_NOT_USED(4, "已赠送（未使用完）"),
    PAY_FAIL(5, "支付失败"),
    INVALID(6, "失效"),
    RETURN_MONEY(7, "已退款"),
    CLOSED(8, "交易关闭"),
    USED(9, "已使用完");

    int status;
    String value;

    OrderStatusEnum(int status, String value) {
        this.status = status;
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}