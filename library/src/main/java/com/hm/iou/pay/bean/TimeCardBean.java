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

    private long actualPrice;
    private String concessions;
    private String content;
    private String goodsId;
    private long originalPrice;
    private String packageId;
    private String rechargeSign;

    @Override
    public String getTimeCardNum() {
        return content;
    }

    @Override
    public String getTimeCardPackageId() {
        return packageId;
    }

    @Override
    public String getTimeCardDiscounts() {
        return concessions;
    }

    @Override
    public long getTimeCardPayMoney() {
        return actualPrice;
    }

}
