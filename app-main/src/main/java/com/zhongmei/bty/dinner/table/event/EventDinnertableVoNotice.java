package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.dinner.vo.DinnertableVo;


public class EventDinnertableVoNotice {

    public final DinnertableVo dinnertableVo;

    public final boolean enableServing;

    public EventDinnertableVoNotice(DinnertableVo dinnertableVo, boolean enableServing) {
        this.dinnertableVo = dinnertableVo;
        this.enableServing = enableServing;
    }

}
