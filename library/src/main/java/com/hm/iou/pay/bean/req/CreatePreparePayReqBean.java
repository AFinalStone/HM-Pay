package com.hm.iou.pay.bean.req;

import lombok.Data;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
@Data
public class CreatePreparePayReqBean {

    /**
     * channel : 0
     * orderId : 0
     */

    private int channel;
    private String orderId;

}
