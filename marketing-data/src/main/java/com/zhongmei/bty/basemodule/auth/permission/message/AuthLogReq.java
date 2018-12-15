package com.zhongmei.bty.basemodule.auth.permission.message;

import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class AuthLogReq {
    private List<AuthorizedLog> authorizedLogs;

    public List<AuthorizedLog> getAuthorizedLogs() {
        return authorizedLogs;
    }

    public void setAuthorizedLogs(List<AuthorizedLog> authorizedLogs) {
        this.authorizedLogs = authorizedLogs;
    }
}
