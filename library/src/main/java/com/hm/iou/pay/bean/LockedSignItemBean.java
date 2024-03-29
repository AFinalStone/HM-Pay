package com.hm.iou.pay.bean;

import com.hm.iou.pay.business.locksign.view.ILockSignItem;
import com.hm.iou.pay.dict.LockSignStatusEnum;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * @author syl
 * @time 2019/3/22 9:13 AM
 */
@Data
public class LockedSignItemBean implements ILockSignItem, Serializable {


    /**
     * content : string
     * endDateStr : string
     * genDateStr : string
     * justiceId : string
     * lockedStatus : 0
     * signNum : 0
     */

    private String content;//名称
    private String endDateStr;//占用的截止时间
    private String genDateStr;//生成时间
    private String justiceId;//合同id
    private int lockedStatus;   //1-签署中，2-未签退回，3-签完已付
    private int signNum;//占用的签章数量
    private int consumeBusinessType;//消费的业务类型，1-资金借条，9-电子收条，10-资金借条v2 ,

    @Override
    public String getITitle() {
        return content;
    }

    @Override
    public String getIContractId() {
        return justiceId;
    }

    @Override
    public String getIStartTime() {
        return genDateStr;
    }

    @Override
    public String getIEndTime() {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            Date date = sf.parse(endDateStr);
            sf = new SimpleDateFormat("yyyy-MM-dd");
            return sf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDateStr;
    }

    @Override
    public String getILockSignNum() {
        return String.format("%s个签章", signNum);
    }

    @Override
    public String getIStatusDesc() {
        return LockSignStatusEnum.getInstance(lockedStatus).getName();
    }

    @Override
    public int getIStatusDescTextColor() {
        return LockSignStatusEnum.getInstance(lockedStatus).getColor();
    }
}
