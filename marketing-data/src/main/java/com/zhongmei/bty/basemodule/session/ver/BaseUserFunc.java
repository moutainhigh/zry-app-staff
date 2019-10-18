package com.zhongmei.bty.basemodule.session.ver;

import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.core.security.Password;



public abstract class BaseUserFunc implements UserFunc {

    @Override
    public boolean checkPassword(User user, String password) {
        String firstPasswordNum = Password.create().generate(user.getAccount(), password);
        String secondPasswordNum = user.getPassword();
        return secondPasswordNum.equals(firstPasswordNum);
    }

}
