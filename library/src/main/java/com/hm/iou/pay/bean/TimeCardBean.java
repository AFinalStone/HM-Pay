package com.hm.iou.pay.bean;

import com.hm.iou.pay.comm.ITimeCardItem;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
public class TimeCardBean implements ITimeCardItem {


    /**
     * actualPrice : 0
     * concessions : string
     * content : string
     * goodsId : string
     * originalPrice : 0
     * packageId : 0
     */

    private int actualPrice;
    private String concessions;
    private String content;
    private String goodsId;
    private int originalPrice;
    private int packageId;

    @Override
    public String getTimeCardNum() {
        return content;
    }

    @Override
    public String getDiscountsMoney() {
        return concessions;
    }

}
