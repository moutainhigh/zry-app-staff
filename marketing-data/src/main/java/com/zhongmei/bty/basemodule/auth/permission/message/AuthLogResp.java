package com.zhongmei.bty.basemodule.auth.permission.message;

import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;

import java.util.List;

/**
 * 授权记录返回的数据列表
 * Created by demo on 2018/12/15
 */

public class AuthLogResp {
    List<AuthorizedLog> authorizedLogs;

    public List<AuthorizedLog> getAuthorizedLogs() {
        return authorizedLogs;
    }

    public void setAuthorizedLogs(List<AuthorizedLog> authorizedLogs) {
        this.authorizedLogs = authorizedLogs;
    }
}
