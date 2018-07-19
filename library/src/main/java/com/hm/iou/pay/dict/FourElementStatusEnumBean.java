package com.hm.iou.pay.dict;

import java.io.Serializable;

public enum FourElementStatusEnumBean implements Serializable {

    HaveBindBank(1, "已认证，绑定银行卡"), NoBindBank(0, "未认证，没有绑定银行卡");

    int status;
    String name;

    FourElementStatusEnumBean(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

}
