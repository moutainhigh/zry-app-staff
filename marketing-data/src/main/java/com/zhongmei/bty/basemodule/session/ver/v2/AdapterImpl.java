package com.zhongmei.bty.basemodule.session.ver.v2;

import android.content.Context;
import android.net.Uri;

import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionDetailEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthPermissionEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthPermissionResourceEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthResourceEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRoleEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRolePermissionEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthUserEntity;
import com.zhongmei.yunfu.orm.DBHelperManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class AdapterImpl implements Session.Adapter {

    private Context context;

    public AdapterImpl(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public List<Uri> getDataObserverUris() {
        List<Uri> list = new ArrayList<>();
        list.add(DBHelperManager.getUri(AuthRolePermissionEntity.class));
        list.add(DBHelperManager.getUri(AuthResourceEntity.class));
        list.add(DBHelperManager.getUri(AuthPermissionEntity.class));
        list.add(DBHelperManager.getUri(AuthDataPermissionEntity.class));
        list.add(DBHelperManager.getUri(AuthDataPermissionDetailEntity.class));
        list.add(DBHelperManager.getUri(AuthPermissionResourceEntity.class));
        list.add(DBHelperManager.getUri(AuthAccountEntity.class));
        list.add(DBHelperManager.getUri(AuthRoleEntity.class));
        list.add(DBHelperManager.getUri(AuthUserEntity.class));
        return list;
    }

    @Override
    public Map<Class<? extends Session.SessionFunc>, Class> getDictionary() {
        Map<Class<? extends Session.SessionFunc>, Class> dictionary = new HashMap<>();
        dictionary.put(UserFunc.class, UserFuncImpl.class);
        return dictionary;
    }

    @Override
    public Auth getAuth() {
        return new AuthImpl();
    }

    @Override
    public void release() {

    }

}
