package com.hm.iou.pay.bean.req;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class CreatePreparePayReqBean {

    /**
     * channel : 0
     * orderId : 0
     */

    private int channel;
    private String orderId;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
