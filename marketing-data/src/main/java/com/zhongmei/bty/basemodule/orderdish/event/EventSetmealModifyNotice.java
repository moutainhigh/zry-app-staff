package com.zhongmei.bty.basemodule.orderdish.event;

import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager.ModifyResult;

/**
 * 通知修改套餐明细数量的结果
 *
 * @version: 1.0
 * @date 2015年7月16日
 */
public class EventSetmealModifyNotice {

    /**
     * 操作时传入的UUID
     */
    public final String uuid;
    /**
     *
     */
    public final DishSetmealGroupVo groupdVo;
    /**
     * 修改后的套餐明细
     */
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

    /**
     * 如果操作成功返回true
     *
     * @return
     */
    public boolean isSuccessful() {
        return modifyResult == ModifyResult.SUCCESSFUL;
    }
}
