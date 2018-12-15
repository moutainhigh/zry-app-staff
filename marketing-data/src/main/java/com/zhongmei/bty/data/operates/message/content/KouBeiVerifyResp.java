package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;

public class KouBeiVerifyResp {
    public int status;//1:成功 2:失败
    public String message;
    public long tradeId;
    public VerifyKoubeiOrder verifyInfo;

    public static boolean isOk(KouBeiVerifyResp kouBeiVerifyResp) {
        return kouBeiVerifyResp.status == 1;
    }
}
