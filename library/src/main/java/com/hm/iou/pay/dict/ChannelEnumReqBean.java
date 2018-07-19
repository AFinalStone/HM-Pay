package com.hm.iou.pay.dict;

import java.io.Serializable;

public enum ChannelEnumReqBean implements Serializable {

    PayByWx(1, "微信支付"), PayByApple(2, "苹果支付");

    int channel;
    String name;

    ChannelEnumReqBean(int channel, String name) {
        this.channel = channel;
        this.name = name;
    }

    public int getValue() {
        return channel;
    }

    public String getName() {
        return name;
    }

}
