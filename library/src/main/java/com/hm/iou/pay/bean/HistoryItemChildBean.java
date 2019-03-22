package com.hm.iou.pay.bean;

import com.hm.iou.pay.Constants;
import com.hm.iou.pay.business.history.view.IHistoryItemChild;

import lombok.Data;

/**
 * @author syl
 * @time 2018/7/16 下午2:35
 */
@Data
public class HistoryItemChildBean implements IHistoryItemChild {

    public static int mTimerCount = 0;

    private int leftLockedTime;  //剩余冻结时间,单位秒
    private int recordStatus;    //历史状态0-已购买or已支付，1-已使用，2-冻结中
    private String showDateStr;     //左边展示的时间
    private String showState;       //右边显示的字符

    private int timeTextColor;
    private int typeTextColor;

    public static void setTimerCount(int timerCount) {
        mTimerCount = timerCount;
    }

    @Override
    public String getTime() {
        return showDateStr;
    }

    @Override
    public String getType() {
        if (2 == recordStatus && leftLockedTime != 0) {
            int afterTimeLockTime = leftLockedTime - mTimerCount;
            if (afterTimeLockTime != 0) {
                StringBuffer sb = new StringBuffer();
                int hour = afterTimeLockTime / 3600;
                sb.append(String.format("%02d", hour));
                sb.append(":");
                int temp = afterTimeLockTime % 3600;
                int minute = temp / 60;
                sb.append(String.format("%02d", minute));
                sb.append(":");
                int second = temp % 60;
                sb.append(String.format("%02d", second));
                return sb.toString();
            }
        }
        return showState;
    }

    @Override
    public int getTimeTextColor() {
        return timeTextColor;
    }

    @Override
    public int getTypeTextColor() {
        typeTextColor = timeTextColor;
        if (2 == recordStatus && leftLockedTime != 0) {
            int afterTimeLockTime = leftLockedTime - mTimerCount;
            if (afterTimeLockTime != 0) {
                typeTextColor = Constants.colorBlue;
            }
        }
        if ("退还中".equals(showState)) {
            typeTextColor = Constants.colorGreen;
        }
        if ("支付中".equals(showState)) {
            typeTextColor = Constants.colorRed;
        }
        return typeTextColor;
    }


    public void setTimeTextColor(int color) {
        timeTextColor = color;
    }


}
