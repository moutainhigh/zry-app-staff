package com.zhongmei.bty.basemodule.session.support;

import com.zhongmei.yunfu.context.session.auth.IAuthUser;



public class IAuthUserProxy implements IAuthUser {

    private final Long id;
    private final String name;

    public IAuthUserProxy(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
