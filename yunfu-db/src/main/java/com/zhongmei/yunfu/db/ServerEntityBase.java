package com.zhongmei.yunfu.db;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


public class ServerEntityBase extends BasicEntityBase {


    private static final long serialVersionUID = 1L;

    protected interface $ extends BasicEntityBase.$ {


        String deviceIdenty = "device_identy";

    }



    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public void validateCreate() {
        validateCreate1(BaseApplication.sInstance);
    }

    public void validateCreate1(BaseApplication application) {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(application.getBrandIdenty());
        setShopIdenty(application.getShopIdenty());
        setDeviceIdenty(application.getDeviceIdenty());
        setChanged(true);
        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
    }

    public void validateUpdate() {
        setChanged(true);
        if (this instanceof IUpdator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull();
    }
}
