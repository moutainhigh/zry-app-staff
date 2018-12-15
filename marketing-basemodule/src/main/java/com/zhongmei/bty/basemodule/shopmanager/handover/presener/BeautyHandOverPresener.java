package com.zhongmei.bty.basemodule.shopmanager.handover.presener;

import com.zhongmei.bty.basemodule.shopmanager.handover.operators.HandoverOperates;
import com.zhongmei.bty.commonmodule.data.operate.BeautyOperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyHandOverPresener implements IHandOverPresener {

    @Override
    public boolean isShowPrePayInfo() {
        return false;
    }

    @Override
    public HandoverOperates createHandoverOperates() {
        return BeautyOperatesFactory.create(HandoverOperates.class);
    }
}
