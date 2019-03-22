package com.hm.iou.pay.business.locksign.view;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface ILockSignItem {

    /**
     * 获取名称
     *
     * @return
     */
    String getITitle();

    /**
     * 合同id
     *
     * @return
     */
    String getIContractId();

    /**
     * 获取开始时间
     *
     * @return
     */
    String getIStartTime();

    /**
     * 获取被占用的结束时间
     *
     * @return
     */
    String getIEndTime();

    /**
     * 获取被占用的签章数量
     *
     * @return
     */
    String getILockSignNum();

    /**
     * 获取右侧文字描述
     *
     * @return
     */
    String getIStatusDesc();

    /**
     * 获取右侧描述的文字颜色
     *
     * @return
     */
    int getIStatusDescTextColor();
}
