package com.zhongmei.bty.basemodule.shopmanager.utils;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.db.entity.OpenTime;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class DateTimeUtil {

    public static Date getOpenTime() throws SQLException, ParseException {
                String openTimeStr = "00:00:00";        OpenTime openTime = null;

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<OpenTime, Long> openTimeDao = helper.getDao(OpenTime.class);
            openTime = openTimeDao.queryBuilder().where().eq(OpenTime.$.status, Status.VALID).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        if (openTime != null && !TextUtils.isEmpty(openTime.getClosingTime())) {
            openTimeStr = openTime.getClosingTime();
        }
                String dateTimeStr = DateTimeUtils.getCurrentDate() + " " + openTimeStr;
        SimpleDateFormat format = new SimpleDateFormat(DateTimeUtils.DATE_TIME_FORMAT3);
        Date date = format.parse(dateTimeStr);
                if (date.getTime() > System.currentTimeMillis()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
        }

        return date;
    }

    public static List<OpenTime> getBusinessTime() {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<OpenTime> openTimes = null;
        try {
            Dao<OpenTime, Long> openTimeDao = helper.getDao(OpenTime.class);
            openTimes = openTimeDao.queryBuilder().where().eq(OpenTime.$.status, Status.VALID).query();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return openTimes;
    }
}
