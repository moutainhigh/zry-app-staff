package com.zhongmei.bty.basemodule.database.operates;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.sql.SQLException;
import java.util.List;


public interface EcCardDal extends IOperates {


    EcCardLevelSetting findEcCardLevelSetting(Long levelId) throws SQLException;


    List<EcCardSettingDetail> findEcCardSettingDetail(Long levelId) throws SQLException;
}
