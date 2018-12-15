package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

/**
 * Created by demo on 2018/12/15
 */
public interface CommercialGroupSettingDal extends IOperates {

    /**
     * 获取默认语言
     *
     * @return
     */
    public String getDefaultLanguage();
}
