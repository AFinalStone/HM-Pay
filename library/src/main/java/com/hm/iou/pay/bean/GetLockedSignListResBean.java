package com.hm.iou.pay.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by syl on 2019/3/22.
 */
@Data
public class GetLockedSignListResBean {


    /**
     * lastReqDate : 2019-03-22 11:01:46
     * infoList : [{"content":"借条 ¥5566","justiceId":"190320162512000385","genDateStr":"2019.03.20 16:26","endDateStr":"2019.03.20 23:59","signNum":2,"lockedStatus":1},{"content":"借条 ¥556","justiceId":"190321100443000385","genDateStr":"2019.03.21 10:06","endDateStr":"2019.03.21 23:59","signNum":2,"lockedStatus":1},{"content":"借条 ¥4558","justiceId":"190321101554000385","genDateStr":"2019.03.21 10:17","endDateStr":"2019.03.21 23:59","signNum":2,"lockedStatus":1}]
     */

    private String lastReqDate;
    private List<LockedSignItemBean> infoList;

}
