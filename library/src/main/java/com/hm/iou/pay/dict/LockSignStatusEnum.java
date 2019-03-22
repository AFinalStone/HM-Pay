package com.hm.iou.pay.dict;

import java.io.Serializable;

public enum LockSignStatusEnum implements Serializable {

    SIGNING(1, "正在签署", 0xFF2782E2),
    HAVE_BACK(2, "未签退回", 0xFFEF5050),
    HAVE_PAY(3, "签完已付", 0xFF9B9B9B);

    int value;
    String name;
    int color;

    LockSignStatusEnum(int value, String name, int color) {
        this.value = value;
        this.name = name;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public static LockSignStatusEnum getInstance(int value) {
        if (SIGNING.value == value) {
            return SIGNING;
        } else if (HAVE_BACK.value == value) {
            return HAVE_BACK;
        }
        return HAVE_PAY;
    }
}
