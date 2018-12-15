package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.dinner.vo.DinnertableVo;

/**
 * @version: 1.0
 * @date 2015年9月17日
 */
public class EventDinnertableVoNotice {

    public final DinnertableVo dinnertableVo;
    /**
     * 是否启用上餐功能
     */
    public final boolean enableServing;

    public EventDinnertableVoNotice(DinnertableVo dinnertableVo, boolean enableServing) {
        this.dinnertableVo = dinnertableVo;
        this.enableServing = enableServing;
    }

}
