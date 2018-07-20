package com.hm.iou.pay.dict;

import java.io.Serializable;

public enum ChannelEnumBean implements Serializable {

    PayByWx(1, "微信支付");

    int channel;
    String name;

    ChannelEnumBean(int channel, String name) {
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
