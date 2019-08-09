package com.hm.iou.pay.bean;

import com.hm.iou.pay.comm.ITimeCardItem;

import lombok.Data;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
@Data
public class TimeCardBean implements ITimeCardItem {


    /**
     * actualPrice : 0
     * concessions : string
     * content : string
     * goodsId : string
     * originalPrice : 0
     * packageId : 0
     */

    public long actualPrice;
    public String concessions;
    public String content;
    public String goodsId;
    public long originalPrice;
    public String packageId;
    public String rechargeSign;

    public String packageCode;

    @Override
    public String getTimeCardContent() {
        return content;
    }

    @Override
    public String getTimeCardDiscounts() {
        return concessions;
    }


}
