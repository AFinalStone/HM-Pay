package com.hm.iou.pay.bean.req;

import lombok.Data;

@Data
public class CreateOrderReqBean {

    private String couponId;
    private String packageCode;

}
