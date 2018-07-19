package com.hm.iou.pay.comm;

import android.content.Context;

import com.hm.iou.pay.Constants;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.tools.SPUtil;

/**
 * Created by syl on 2018/7/19.
 */

public class PaySPUtil {

    /**
     * 保存用户绑定银行卡成功的信息
     *
     * @param context
     */
    public static void saveUserBindBankSuccess(Context context) {
        //四要素认证已经成功，用SharedPreferences保存下来，采用userId拼接的字符串作为key来存储
        String userId = UserManager.getInstance(context).getUserId();
        SPUtil.put(context, Constants.SP_NAME, Constants.SP_KEY_FOUR_ELEMENTS + userId, true);
    }

    /**
     * 校验用户是否已经成功绑定银行卡
     *
     * @param context
     * @return
     */
    public static boolean checkUserHaveBindBankSuccess(Context context) {
        String userId = UserManager.getInstance(context).getUserId();
        return SPUtil.getBoolean(context, Constants.SP_NAME, Constants.SP_KEY_FOUR_ELEMENTS + userId, false);
    }


}
