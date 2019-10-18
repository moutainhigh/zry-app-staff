package com.zhongmei.bty.basemodule.auth.permission.operates;

import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;



public interface AuthLogDal extends IOperates {

    public void insert(AuthorizedLog authorizedLog);

    public void update(AuthorizedLog authorizedLog);

    public void delete(String uuid);

    public AuthorizedLog queryByUuid(String uuid);

    public List<AuthorizedLog> queryList(long count);

        public void batchDelete(List<AuthorizedLog> authLogList);

        public void batchSave(List<AuthorizedLog> authLogList);
}
