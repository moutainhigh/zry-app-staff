package com.zhongmei.bty.basemodule.auth.permission.operates;

import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * <p>
 * 授权记录操作数据库的工具类
 */

public interface AuthLogDal extends IOperates {

    public void insert(AuthorizedLog authorizedLog);

    public void update(AuthorizedLog authorizedLog);

    public void delete(String uuid);

    public AuthorizedLog queryByUuid(String uuid);

    public List<AuthorizedLog> queryList(long count);

    //批量删除
    public void batchDelete(List<AuthorizedLog> authLogList);

    //批量保存
    public void batchSave(List<AuthorizedLog> authLogList);
}
