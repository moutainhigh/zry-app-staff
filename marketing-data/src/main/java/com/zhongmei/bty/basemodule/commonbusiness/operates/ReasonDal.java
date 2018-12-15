package com.zhongmei.bty.basemodule.commonbusiness.operates;

import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface ReasonDal extends IOperates {

    /**
     * 获取理由开关是否开启
     *
     * @param reasonType
     * @return true为开启 | false为关闭
     */
    boolean isReasonSwitchOpen(ReasonType reasonType);

    /**
     * 获取指定类型的理由记录
     *
     * @param reasonSource
     * @param reasonType
     * @return
     * @throws Exception
     */
    List<ReasonSetting> findReasonSetting(ReasonSource reasonSource, ReasonType reasonType) throws Exception;

}
