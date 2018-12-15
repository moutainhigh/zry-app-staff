package com.zhongmei.bty.basemodule.shopmanager.handover.presener;

import com.zhongmei.bty.basemodule.shopmanager.handover.operators.HandoverOperates;

/**
 * Created by demo on 2018/12/15
 */

public interface IHandOverPresener {

    boolean isShowPrePayInfo();//是否显示预付金信息

    HandoverOperates createHandoverOperates();

}
