package com.zhongmei.bty.basemodule.commonbusiness.operates;

import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;



public interface ReasonDal extends IOperates {


    boolean isReasonSwitchOpen(ReasonType reasonType);


    List<ReasonSetting> findReasonSetting(ReasonSource reasonSource, ReasonType reasonType) throws Exception;

}
