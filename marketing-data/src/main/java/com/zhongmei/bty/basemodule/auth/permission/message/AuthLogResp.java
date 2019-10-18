package com.zhongmei.bty.basemodule.auth.permission.message;

import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;

import java.util.List;



public class AuthLogResp {
    List<AuthorizedLog> authorizedLogs;

    public List<AuthorizedLog> getAuthorizedLogs() {
        return authorizedLogs;
    }

    public void setAuthorizedLogs(List<AuthorizedLog> authorizedLogs) {
        this.authorizedLogs = authorizedLogs;
    }
}
