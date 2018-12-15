package com.zhongmei.bty.basemodule.database.operates;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.sql.SQLException;
import java.util.List;

/**
 * 会员卡
 * <p>
 * Created by demo on 2018/12/15
 */
public interface EcCardDal extends IOperates {

    /**
     * 根据实体卡等级id查询实体卡等级对象
     *
     * @param levelId
     * @return
     * @throws SQLException
     */
    EcCardLevelSetting findEcCardLevelSetting(Long levelId) throws SQLException;

    /**
     * 根据实体卡等级id查询实体卡设置项
     *
     * @param levelId
     * @return
     * @throws SQLException
     */
    List<EcCardSettingDetail> findEcCardSettingDetail(Long levelId) throws SQLException;
}
