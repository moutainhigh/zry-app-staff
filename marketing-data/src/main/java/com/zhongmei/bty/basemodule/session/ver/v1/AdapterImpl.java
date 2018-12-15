package com.zhongmei.bty.basemodule.session.ver.v1;

import android.content.Context;
import android.net.Uri;

import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.ver.v1.db.AuthDataPermission;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
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
        //list.add(DBHelperManager.getUri(AuthUserPermission.class));
        list.add(DBHelperManager.getUri(AuthDataPermission.class));
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
