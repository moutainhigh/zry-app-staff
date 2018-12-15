package com.zhongmei.bty.basemodule.devices.phone.operates;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.devices.phone.CallDBHelper;
import com.zhongmei.bty.basemodule.devices.phone.CallService;
import com.zhongmei.bty.basemodule.devices.phone.entity.CallHistory;
import com.zhongmei.bty.basemodule.devices.phone.utils.Lg;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class CallServiceImpl extends CallDBHelper.AbsService implements CallService {
    private static final String TAG = "CallServiceImpl--->";

    private static final long DEFAULT_PAGE_SIZE = 20;
    private static final long DEFAULT_PAGE_NUM = 1;

    private static CallHistory precallHistory;
    private static long pretime = 0;

    @Override
    public void insert(CallHistory callHistory) {
        //PLog.d(PLog.QUICK_SERVICE_KEY, "插入一条通话记录：" + callHistory.toString());
        try {
            long now = System.currentTimeMillis();
            if (precallHistory != null) {
                if (precallHistory.type == callHistory.type
                        && precallHistory.getNumber().equalsIgnoreCase(callHistory.getNumber())) {
                    if (now - pretime < 2000) {
                        pretime = now;
                        return;
                    }
                }
            }
            pretime = now;
            precallHistory = callHistory;

            getDao(CallHistory.class).create(callHistory);

            // 如果是未接, 则保存一下未读的"未接数"
            if (callHistory.getStatus() == CallHistory.STATUS_NO_ANSWER) {
                int count = SpHelper.getDefault().getUnreadNoAnswerCallHistoryCount() + 1;
                SpHelper.getDefault().saveUnreadNoAnswerCallHistoryCount(count);
            }
        } catch (SQLException e) {
            Lg.d("sql exception#insert", e.getMessage());
            //PLog.d(PLog.QUICK_SERVICE_KEY, "插入一条通话记录出现异常：" + e.getMessage());
        } finally {
            release();
        }
    }

    @Override
    public int countUnreadNoAnswer() {
        return SpHelper.getDefault().getUnreadNoAnswerCallHistoryCount();
    }

    @Override
    public void clearUnreadNoAnswer() {
        SpHelper.getDefault().saveUnreadNoAnswerCallHistoryCount(0);
    }

    @Override
    public void deleteTimeOutLog() {
        String sql = "delete from call_history where start_time < datetime('now','start of day','-"
                + 7 + " day')";
        try {
            exeSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            release();
        }
    }

    @Override
    public List<CallHistory> listNoAnswer() {
        return list(1, Integer.MAX_VALUE, null, CallHistory.STATUS_NO_ANSWER);
    }

    @Override
    public List<CallHistory> list() {
        return list(1, Integer.MAX_VALUE, null, -1);
    }

    @Override
    public List<CallHistory> list(String phoneNum) {
        return list(1, Integer.MAX_VALUE, phoneNum, -1);
    }

    @Override
    public List<CallHistory> list(long pageNum, long pageSize) {
        return list(pageNum, pageSize, null, 1);
    }

    @Override
    public List<CallHistory> list(long pageNum, long pageSize, String phoneNum) {
        return list(pageNum, pageSize, phoneNum, -1);
    }

    @Override
    public List<CallHistory> list(long pageNum, long pageSize, String phoneNum, int status) {
        //PLog.d(PLog.QUICK_SERVICE_KEY, "获取通话记录列表：" + "pageNum:" + pageNum + "  pageSize:" + pageSize + "  phoneNum:" + phoneNum + "  status:" + status);
        try {
            if (pageNum < 1) {
                pageNum = DEFAULT_PAGE_NUM;
            }
            if (pageSize < 1) {
                pageSize = DEFAULT_PAGE_SIZE;
            }

            Dao<CallHistory, Integer> mCallHistoryDao = getDao(CallHistory.class);

            QueryBuilder<CallHistory, Integer> builder = mCallHistoryDao.queryBuilder().limit(pageSize).offset((pageNum - 1) * pageSize); // 这里offset的值可能有问题, 还没验证

            // 没找到太合适的办法拼这个SQL 暂时这样了
            Calendar calendar = Calendar.getInstance();
            int showDate = SpHelper.getDefault().getInt(SpHelper.SP_CALLLOG_CLEAR_PERIOD, 2);
            calendar.add(Calendar.DATE, -showDate);
            Where<CallHistory, Integer> where = builder.where().gt(CallHistory.COL_END_TIME, calendar.getTime());
            if (isSelectPhoneNum(phoneNum) && isSelectStatus(status)) {
                where.and().eq(CallHistory.COL_STATUS, status).and().eq(CallHistory.COL_PHONE_NUM, phoneNum);
            } else if (isSelectPhoneNum(phoneNum)) {
                where.and().eq(CallHistory.COL_PHONE_NUM, phoneNum);
            } else if (isSelectStatus(status)) {
                where.and().eq(CallHistory.COL_STATUS, status);
            }
            builder.setWhere(where);
            builder.orderBy(CallHistory.COL_START_TIME, false);
            return builder.query();
        } catch (SQLException e) {
            Lg.d("sql exception#list", e.getMessage());
            //PLog.d(PLog.QUICK_SERVICE_KEY, "获取通话列表出现异常：" + e.getMessage());
        } finally {
            release();
        }

        return null;
    }

    private static boolean isSelectPhoneNum(String phoneNum) {
        return !TextUtils.isEmpty(phoneNum);
    }

    private static boolean isSelectStatus(int status) {
        return status == CallHistory.STATUS_NO_ANSWER
                || status == CallHistory.STATUS_NORMAL;
    }
}
