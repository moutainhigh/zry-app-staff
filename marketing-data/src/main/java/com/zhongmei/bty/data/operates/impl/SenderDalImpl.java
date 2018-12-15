package com.zhongmei.bty.data.operates.impl;

import android.database.Cursor;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.context.util.peony.land.Extractable;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.bty.data.db.CalmDatabaseHelper;
import com.zhongmei.bty.data.operates.SenderDal;
import com.zhongmei.bty.entity.vo.SenderVo;
import com.zhongmei.bty.takeout.sender.vo.SenderSearcheVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Date：2015-5-28 下午1:28:08
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class SenderDalImpl extends AbstractOpeartesImpl implements SenderDal {
    private static final String TAG = SenderDalImpl.class.getSimpleName();

    public SenderDalImpl(ImplContext context) {
        super(context);
    }

    /*
     * (non-Javadoc) 查询所有的外卖员
     *
     * @see
     * com.zhongmei.bty.data.operates.UserDal#getAllSenderOffUser
     * ()
     */
    @Override
    public List<SenderVo> getAllSenderOffUser() {
        List<SenderVo> userVoList = new ArrayList<SenderVo>();
        CalmDatabaseHelper helper = OpenHelperManager.getHelper(getContext(), CalmDatabaseHelper.class);
        try {
            final UserDao userDao = new UserDao(helper);
            List<User> userList = Session.getFunc(UserFunc.class).listSender();

            userVoList = ArgsUtils.sucker(userList, new Extractable<SenderVo, User>() {
                @Override
                public SenderVo extract(User value) {
                    SenderVo sv = new SenderVo();
                    sv.setAuthUser(value);
                    userDao.setUnSquareUpCountAndAmountToVoByUserId(value.getAccount(), sv);// 未清账订单和金额
                    return sv;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return userVoList;
    }

    /**
     * @Date：2015-5-28 下午2:05:47
     * @Description: TODO
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
    private static class UserDao {

        private CalmDatabaseHelper mHelper;

        public UserDao(CalmDatabaseHelper helper) {
            mHelper = helper;
        }

        /**
         * @Title: setUnSquareUpCountAndAmountByUserId
         * @Description: TODO
         * @Param @param userid
         * @Param @param vo TODO
         * @Return void 返回类型
         */
        public void setUnSquareUpCountAndAmountToVoByUserId(String userid, SenderVo vo) {
            if (vo == null) {
                return;
            }
            Cursor cursor = null;
            int count = 0;
            double amount = 0.0;
            Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
            cad.add(Calendar.DAY_OF_MONTH, -2);
            StringBuilder sql =
                    new StringBuilder("SELECT count(*) as counts ,sum(trade.trade_amount) as amount "
                            + "from trade INNER JOIN  trade_extra " + "on trade.uuid = trade_extra.trade_uuid "
                            + "where trade.delivery_type = 2 " + "AND trade.trade_status in (3,4) "
                            + "AND trade.trade_type = 1 " + "AND trade.trade_pay_form = 1 "
                            + "AND trade_extra.delivery_status <> 3 " + "AND trade_extra.server_create_time > "
                            + cad.getTime().getTime() + " AND trade_extra.delivery_user_id ='");
            sql.append(userid);
            sql.append("'");

            try {
                cursor = mHelper.getReadableDatabase().rawQuery(sql.toString(), null);
                if (cursor != null) {

                    if (cursor.getCount() > 0 && cursor.moveToNext()) {

                        count = cursor.getInt(cursor.getColumnIndex("counts"));

                        amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                    }
                }

            } catch (Exception e) {

                Log.e(TAG, "", e);

            } finally {
                vo.setUnSquareUpCount(count);
                vo.setUnSquareUpAmount(new BigDecimal(amount));
                if (cursor != null) {
                    cursor.close();
                }
            }

        }

    }

    @Override
    public List<SenderSearcheVo> getSearchVoList() {
        String dateFormat = getContext().getString(R.string.booking_time_format);
        List<SenderSearcheVo> list = new ArrayList<SenderSearcheVo>();
        Cursor cursor = null;
        Calendar cad = Calendar.getInstance();
        cad.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
        cad.add(Calendar.DAY_OF_MONTH, -2);
        StringBuilder sql =
                new StringBuilder(
                        "SELECT trade.uuid as uuid,trade.trade_no as tradeNo,trade.server_create_time as createTime, "
                                + "trade_extra.delivery_user_id as userId,trade_extra.delivery_man as userName, trade.trade_pay_form, trade.trade_pay_status "
                                + "from trade LEFT JOIN  trade_extra " + "on trade.uuid = trade_extra.trade_uuid "
                                + "where trade.delivery_type = 2 " + "AND trade.status_flag = 1 "
                                + "AND trade.trade_status in (3,4) " + "AND trade.trade_type = 1 "
                                + "AND trade_extra.delivery_user_id is not null " + "AND trade_extra.server_create_time > "
                                + cad.getTime().getTime());

        CalmDatabaseHelper helper = OpenHelperManager.getHelper(getContext(), CalmDatabaseHelper.class);
        try {
            cursor = helper.getReadableDatabase().rawQuery(sql.toString(), null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex("trade_pay_form")) == (TradePayForm.ONLINE.value())
                            && cursor.getInt(cursor.getColumnIndex("trade_pay_status")) == (TradePayStatus.UNPAID.value())) {
                        continue;
                    }

                    SenderSearcheVo vo = new SenderSearcheVo();
                    vo.setTrandeNo(cursor.getString(cursor.getColumnIndex("tradeNo")));
                    vo.setTradeUuid(cursor.getString(cursor.getColumnIndex("uuid")));
                    String createTime =
                            DateTimeUtils.formatDateTime(cursor.getLong(cursor.getColumnIndex("createTime")), dateFormat);
                    vo.setOrderCreateTime(createTime);
                    vo.setSenderName(cursor.getString(cursor.getColumnIndex("userName")));
                    vo.setSenderId(cursor.getString(cursor.getColumnIndex("userId")));
                    list.add(vo);
                }
            }

        } catch (Exception e) {

            Log.e(TAG, "", e);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            OpenHelperManager.releaseHelper();
        }
        return list;
    }
}
