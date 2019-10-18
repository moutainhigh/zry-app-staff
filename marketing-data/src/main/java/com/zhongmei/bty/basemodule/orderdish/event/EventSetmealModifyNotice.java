package com.zhongmei.bty.basemodule.orderdish.event;

import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager.ModifyResult;


public class EventSetmealModifyNotice {


    public final String uuid;

    public final DishSetmealGroupVo groupdVo;

    public final DishSetmealVo setmealVo;

    public final ModifyResult modifyResult;

    public EventSetmealModifyNotice(String uuid, ModifyResult modifyResult) {
        this.uuid = uuid;
        this.groupdVo = null;
        this.setmealVo = null;
        this.modifyResult = modifyResult;
    }

    public EventSetmealModifyNotice(String uuid, DishSetmealGroupVo groupdVo, DishSetmealVo setmealVo) {
        this.uuid = uuid;
        this.groupdVo = groupdVo;
        this.setmealVo = setmealVo;
        modifyResult = ModifyResult.SUCCESSFUL;
    }


    public boolean isSuccessful() {
        return modifyResult == ModifyResult.SUCCESSFUL;
    }
}
