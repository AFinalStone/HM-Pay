package com.hm.iou.pay.bean;

import java.util.List;

import lombok.Data;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
@Data
public class SearchTimeCardListResBean {

    /**
     * countSign : 0
     * firstPackage : {"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0}
     * packageRespList : [{"actualPrice":0,"concessions":"string","content":"string","goodsId":"string","originalPrice":0,"packageId":0}]
     * signUnitPrice : 0
     */

    private long countSign;
    private TimeCardBean firstPackage;
    private long signUnitPrice;
    private List<TimeCardBean> packageRespList;

}
