package com.zhongmei.bty.basemodule.commonbusiness.operates;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSettingSwitch;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.List;



public class ReasonDalImpl extends AbstractOpeartesImpl implements ReasonDal {

    private static final String TAG = ReasonDalImpl.class.getName();

    public ReasonDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public boolean isReasonSwitchOpen(ReasonType reasonType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ReasonSettingSwitch, Long> dao = helper.getDao(ReasonSettingSwitch.class);
            QueryBuilder<ReasonSettingSwitch, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(ReasonSettingSwitch.$.reasonValue, reasonType)
                    .and()
                    .eq(ReasonSettingSwitch.$.statusFlag, StatusFlag.VALID);
            ReasonSettingSwitch reasonSettingSwitch = qb.queryForFirst();
            return reasonSettingSwitch == null || reasonSettingSwitch.getEnableSwitch() == ReasonSettingSwitch.SWITCH_ON;
        } catch (Exception e) {
            Log.e(TAG, "findByPaymentItemId error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return true;
    }

    @Override
    public List<ReasonSetting> findReasonSetting(ReasonSource reasonSource, ReasonType reasonType)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ReasonSetting, String> reasonSettingDao = helper.getDao(ReasonSetting.class);
            QueryBuilder<ReasonSetting, String> qb = reasonSettingDao.queryBuilder();
            qb.where().eq(ReasonSetting.$.source, reasonSource).and().eq(ReasonSetting.$.type, reasonType).and().eq(
                    ReasonSetting.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(ReasonSetting.$.sort, true);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}
