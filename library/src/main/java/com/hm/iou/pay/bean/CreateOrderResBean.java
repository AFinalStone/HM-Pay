package com.hm.iou.pay.bean;

public class CreateOrderResBean {

    public String orderId;
    public boolean pay;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }
}
