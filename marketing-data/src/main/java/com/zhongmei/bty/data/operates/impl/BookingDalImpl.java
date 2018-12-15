package com.zhongmei.bty.data.operates.impl;

import android.annotation.SuppressLint;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.booking.bean.BookingPeriodPopupVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingSetting;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.booking.operates.BookingDal;
import com.zhongmei.bty.basemodule.commonbusiness.bean.TablesCommercialAreaVo;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialOrderSource;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.Period;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.PeriodType;
import com.zhongmei.yunfu.context.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 预订单数据查询
 */
public class BookingDalImpl extends AbstractOpeartesImpl implements BookingDal {
    private static final String TAG = BookingDalImpl.class.getSimpleName();

    public BookingDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public Tables findTableById(String tableId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, String> tableDao = helper.getDao(Tables.class);
            QueryBuilder<Tables, String> qb = tableDao.queryBuilder();
            qb.where().eq(Tables.$.uuid, tableId);
            List<Tables> tableList = qb.query();
            if (tableList != null && !tableList.isEmpty()) {
                return tableList.get(0);
            }
            return null;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    public Period findPeriodById(Long periodId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Period, String> periodDao = helper.getDao(Period.class);
            QueryBuilder<Period, String> qb = periodDao.queryBuilder();
            qb.where().eq(Period.$.id, periodId);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<BookingVo> findAllBookingVoList() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().notIn(Booking.$.orderStatus,
                    BookingOrderStatus.CANCEL,
                    BookingOrderStatus.LEAVE,
                    BookingOrderStatus.REFUSED);
            qb.orderBy(Booking.$.orderTime, false);
            List<Booking> bookingList = qb.query();
            List<BookingVo> voList = new ArrayList<BookingVo>();
            if (bookingList != null && !bookingList.isEmpty()) {
                Dao<BookingTable, String> bookingtableDao = helper.getDao(BookingTable.class);
                Dao<Tables, String> tableDao = helper.getDao(Tables.class);
                QueryBuilder<BookingTable, String> qbBookingTable = bookingtableDao.queryBuilder();
                QueryBuilder<Tables, String> qbTable = tableDao.queryBuilder();
                for (Booking booking : bookingList) {
                    BookingVo vo = new BookingVo();
                    vo.setBooking(booking);
                    qbBookingTable.where().eq(BookingTable.$.orderID, booking.getId()).and().eq(BookingTable.$.status, 0);
                    List<BookingTable> bookingTableList = qbBookingTable.query();
                    vo.setBookingTableList(bookingTableList);
                    if (bookingTableList != null && !bookingTableList.isEmpty()) {
                        Set<String> tableIdList = new LinkedHashSet<String>();
                        for (BookingTable bookingTable : bookingTableList) {
                            tableIdList.add(bookingTable.getTableID());
                        }
                        qbTable.where().in(Tables.$.uuid, tableIdList);
                        vo.setTablesList(qbTable.query());
                    }
                    voList.add(vo);
                }
            }
            return voList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public long findBookingByDate(Date date) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            String dateStr = DateTimeUtils.formatDate(date);
            String begin = dateStr + " 00:00:00";
            String end = dateStr + " 23:59:59";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date beginTime = format.parse(begin);
            Date endTime = format.parse(end);
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().between(Booking.$.orderTime, beginTime.getTime(), endTime.getTime()).and().notIn(
                    Booking.$.orderStatus, BookingOrderStatus.CANCEL, BookingOrderStatus.LEAVE, BookingOrderStatus.REFUSED);
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    public List<Period> listPeriod() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(Period.class).queryBuilder().orderBy(Period.$.periodType, true).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Period getPeriod(Long orderTime) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String hh_mm = simpleDateFormat.format(new Date(orderTime));
            List<Period> result = helper.getDao(Period.class).queryBuilder()
                    .where()
                    .le(Period.$.periodStartTime, hh_mm)
                    .and()
                    .ge(Period.$.periodEndTime, hh_mm)
                    .query();

            Period period = null;
            if (result.size() > 1) {
                for (Period it : result) {
                    if (it.getPeriodType() != PeriodType.ALLDAY) {
                        period = it;
                        break;
                    }
                }
            } else {
                period = result.isEmpty() ? null : result.get(0);
            }

            return period;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<BookingTable> listBookingTables(Date date, Long periodId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            date = DateTimeUtils.onlyDate(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Long stratTime = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Long endTime = calendar.getTimeInMillis();
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);

            List<Booking> bookingList = bookingDao.queryBuilder()
                    .where()
                    .ge(Booking.$.orderTime, stratTime)
                    .and()
                    .le(Booking.$.orderTime, endTime)
                    .and()
                    .notIn(Booking.$.orderStatus,
                            BookingOrderStatus.CANCEL,
                            BookingOrderStatus.LEAVE,
                            BookingOrderStatus.REFUSED)
                    .query();

            Dao<BookingTable, String> bookingTableDao = helper.getDao(BookingTable.class);
            List<BookingTable> bookingTableList = new ArrayList<BookingTable>();
            for (Booking booking : bookingList) {
                /*
                 * 由于服务器的booking_table表中没有记录booking.uuid，
                 *
                 * booking_table表在同步前order_id字段为null
                 * ，同步后booking_uuid被置为null， 所以要比对这两个字段。
                 */
                List<BookingTable> list;

                if (booking.getId() != null) {
                    list = bookingTableDao.queryBuilder()
                            .where()
                            .isNotNull(BookingTable.$.tableID)
                            .and()
                            .eq(BookingTable.$.status, 0)
                            .and()
                            .eq(BookingTable.$.orderID, booking.getId())
                            .query();
                    bookingTableList.addAll(list);
                }
                if (booking.getUuid() != null) {
                    list = bookingTableDao.queryBuilder()
                            .where()
                            .isNotNull(BookingTable.$.bookingUuid)
                            .and()
                            .eq(BookingTable.$.status, 0)
                            .and()
                            .eq(BookingTable.$.bookingUuid, booking.getUuid())
                            .query();

                    bookingTableList.addAll(list);
                }
            }
            return bookingTableList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public boolean addBooking(final Booking booking) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Dao<Booking, String> dao = helper.getDao(Booking.class);
                    dao.create(booking);
                    return null;
                }
            };
            helper.callInTransaction(callable);

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return true;
    }

    @Override
    public boolean addBookingTable(final BookingTable table) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Dao<BookingTable, String> dao = helper.getDao(BookingTable.class);
                    dao.create(table);
                    return null;
                }
            };
            helper.callInTransaction(callable);

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return true;

    }

    @Override
    public Booking findBookingByUUID(String uuid) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().eq(Booking.$.uuid, uuid)/*.and().notIn(Booking.$.orderStatus,
                BookingOrderStatus.CANCEL,
				BookingOrderStatus.LEAVE,
				BookingOrderStatus.REFUSED,
				BookingOrderStatus.UNPROCESS)*/;
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Period findPeriodByUuid(String periodServerId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Period, String> period = helper.getDao(Period.class);
            return period.queryForId(periodServerId);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public BookingVo findBookingVoByid(Long bookingId) throws Exception {

        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, Long> bookingDao = helper.getDao(Booking.class);
            Booking booking = bookingDao.queryForId(bookingId);
            BookingVo bookVo = new BookingVo();
            Dao<BookingTable, String> bookingtableDao = helper.getDao(BookingTable.class);
            QueryBuilder<BookingTable, String> qbBookingTable = bookingtableDao.queryBuilder();
            qbBookingTable.where().eq(BookingTable.$.orderID, booking.getId()).and().eq(BookingTable.$.status, 0);
            List<BookingTable> bookingTableList = qbBookingTable.query();
            bookVo.setBookingTableList(bookingTableList);
            bookVo.setBooking(booking);
            QueryBuilder<BookingPeriod, String> qbBookingPeriodDao = helper.getDao(BookingPeriod.class); //v7.16添加预定时段
            BookingPeriod period = qbBookingPeriodDao.where().eq(BookingPeriod.$.bookingId, booking.getId()).queryForFirst();
            bookVo.setBookingPeriod(period);
            Dao<Tables, String> tableDao = helper.getDao(Tables.class);
            QueryBuilder<Tables, String> qbTable = tableDao.queryBuilder();
            if (bookingTableList != null && !bookingTableList.isEmpty()) {
                Set<String> tableIdList = new LinkedHashSet<String>();
                for (BookingTable bookingTable : bookingTableList) {
                    tableIdList.add(bookingTable.getTableID());
                }
                qbTable.where().in(Tables.$.uuid, tableIdList);
                bookVo.setTablesList(qbTable.query());
            }
            return bookVo;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }

    @Override
    public List<BookingTable> listBookTablesById(Long bookingId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<BookingTable, String> bookingtableDao = helper.getDao(BookingTable.class);

            QueryBuilder<BookingTable, String> qbBookingTable = bookingtableDao.queryBuilder();
            qbBookingTable.where().eq(BookingTable.$.orderID, bookingId).and().eq(BookingTable.$.status, 0);

            return qbBookingTable.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<BookingPeriodPopupVo> findAllPeriodList() throws Exception {

        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Period, String> bookingDao = helper.getDao(Period.class);
            QueryBuilder<Period, String> qb = bookingDao.queryBuilder();
            qb.where().eq(Period.$.status, 0);
            List<Period> periodList = qb.query();
            List<BookingPeriodPopupVo> voList = new ArrayList<BookingPeriodPopupVo>();
            if (periodList != null && !periodList.isEmpty()) {
                for (Period period : periodList) {
                    BookingPeriodPopupVo vo = new BookingPeriodPopupVo();
                    vo.setPeriod(period);
                    vo.setSelect(false);
                    voList.add(vo);
                }
            }

            return voList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }

    @Override
    public List<BookingVo> findBookingVoByDate(Date date) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            long beginTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().between(Booking.$.orderTime, beginTime, endTime);
            qb.orderBy(Booking.$.orderTime, false);
            List<Booking> bookingList = qb.query();
            List<BookingVo> voList = new ArrayList<BookingVo>();
            if (bookingList != null && !bookingList.isEmpty()) {
                Dao<BookingTable, String> bookingtableDao = helper.getDao(BookingTable.class);
                Dao<Tables, String> tableDao = helper.getDao(Tables.class);
                QueryBuilder<BookingTable, String> qbBookingTable = bookingtableDao.queryBuilder();
                QueryBuilder<Tables, String> qbTable = tableDao.queryBuilder();
                for (Booking booking : bookingList) {
                    BookingVo vo = new BookingVo();
                    vo.setBooking(booking);
                    qbBookingTable.where().eq(BookingTable.$.orderID, booking.getId()).and().eq(BookingTable.$.status, 0);
                    List<BookingTable> bookingTableList = qbBookingTable.query();
                    vo.setBookingTableList(bookingTableList);
                    if (bookingTableList != null && !bookingTableList.isEmpty()) {
                        Set<String> tableIdList = new LinkedHashSet<String>();
                        for (BookingTable bookingTable : bookingTableList) {
                            tableIdList.add(bookingTable.getTableID());
                        }
                        qbTable.where().in(Tables.$.uuid, tableIdList);
                        vo.setTablesList(qbTable.query());
                    }
                    voList.add(vo);
                }
            }
            return voList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<BookingVo> findBookingVoByStr(String s) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().like(Booking.$.customerMobile, "%" + s + "%").or().like(Booking.$.customerName, "%" + s + "%");
            qb.orderBy(Booking.$.orderTime, false);
            List<Booking> bookingList = qb.query();
            List<BookingVo> voList = new ArrayList<BookingVo>();
            if (bookingList != null && !bookingList.isEmpty()) {
                Dao<BookingTable, String> bookingtableDao = helper.getDao(BookingTable.class);
                Dao<Tables, String> tableDao = helper.getDao(Tables.class);
                QueryBuilder<BookingTable, String> qbBookingTable = bookingtableDao.queryBuilder();
                QueryBuilder<Tables, String> qbTable = tableDao.queryBuilder();
                for (Booking booking : bookingList) {
                    BookingVo vo = new BookingVo();
                    vo.setBooking(booking);
                    qbBookingTable.where().eq(BookingTable.$.orderID, booking.getId()).and().eq(BookingTable.$.status, 0);
                    List<BookingTable> bookingTableList = qbBookingTable.query();
                    vo.setBookingTableList(bookingTableList);
                    if (bookingTableList != null && !bookingTableList.isEmpty()) {
                        Set<String> tableIdList = new LinkedHashSet<String>();
                        for (BookingTable bookingTable : bookingTableList) {
                            tableIdList.add(bookingTable.getTableID());
                        }
                        qbTable.where().in(Tables.$.uuid, tableIdList);
                        vo.setTablesList(qbTable.query());
                    }
                    voList.add(vo);
                }
            }
            return voList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public List<Booking> findBookingByDate(Date beginDate, Date endDate) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            String beginDateStr = DateTimeUtils.formatDate(beginDate);
            String endDateStr = DateTimeUtils.formatDate(endDate);
            String begin = beginDateStr + " 00:00:00";
            String end = endDateStr + " 23:59:59";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date beginTime = format.parse(begin);
            Date endTime = format.parse(end);
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().between(Booking.$.orderTime, beginTime.getTime(), endTime.getTime()).and().notIn(
                    Booking.$.orderStatus, BookingOrderStatus.CANCEL, BookingOrderStatus.LEAVE, BookingOrderStatus.REFUSED);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    /*public List<BookingTradeItemVo> findTradeItemVoByBooking(Booking booking) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List<BookingTradeItem> tradeItemList;
            List<BookingTradeItemProperty> tradeItemPropertyList;
            List<BookingTradeItemVo> tradeItemVoList = new ArrayList<>();
            Dao<BookingTradeItem, String> tradeItemDao = helper.getDao(BookingTradeItem.class);
            Dao<BookingTradeItemProperty, String> tradeItemPropertyDao = helper.getDao(BookingTradeItemProperty.class);
            QueryBuilder<BookingTradeItem, String> qbTradeItem = tradeItemDao.queryBuilder();
            QueryBuilder<BookingTradeItemProperty, String> qbTradeItemProperty = tradeItemPropertyDao.queryBuilder();
            tradeItemList = qbTradeItem.where().eq(BookingTradeItem.$.bookingUuid, booking.getUuid()).query();
            if (tradeItemList != null && tradeItemList.size() > 0) {
                BookingTradeItemVo tradeItemVo;
                for (BookingTradeItem tradeItem : tradeItemList) {
                    tradeItemPropertyList = qbTradeItemProperty.where().eq(BookingTradeItemProperty.$.bookingTradeItemUuid, tradeItem.getUuid()).query();
                    tradeItemVo = new BookingTradeItemVo();
                    tradeItemVo.setTradeItem(tradeItem);
                    tradeItemVo.setTradeItemPropertyList(tradeItemPropertyList);
                    tradeItemVoList.add(tradeItemVo);
                }
            }
            return tradeItemVoList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }*/

    @Override
    public List<CommercialOrderSource> listOrderSource() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(CommercialOrderSource.class).queryForAll();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Booking> listUnProcess() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where()
                    .eq(Booking.$.orderStatus, BookingOrderStatus.UNPROCESS);
            qb.orderBy(Booking.$.serverCreateTime, false);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long getCountUnProcess() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            QueryBuilder<Booking, String> qb = bookingDao.queryBuilder();
            qb.where().eq(Booking.$.orderStatus, BookingOrderStatus.UNPROCESS);
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public BookingSetting getBookingSetting() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<BookingSetting, Long> dao = helper.getDao(BookingSetting.class);
            QueryBuilder<BookingSetting, Long> qb = dao.queryBuilder();
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<BookingTable> listCurrentPeriodBookingTables(Long stratTime, Long endTime) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            List<Booking> bookingList = bookingDao.queryBuilder()
                    .where()
                    .ge(Booking.$.orderTime, stratTime)
                    .and()
                    .le(Booking.$.orderTime, endTime)
                    .and()
                    .in(Booking.$.orderStatus,
                            BookingOrderStatus.TIMEOUT,
                            BookingOrderStatus.UNARRIVED
                    )
                    .query();

            Dao<BookingTable, String> bookingTableDao = helper.getDao(BookingTable.class);
            List<BookingTable> bookingTableList = new ArrayList<BookingTable>();
            for (Booking booking : bookingList) {
                /*
                 * 由于服务器的booking_table表中没有记录booking.uuid，
                 *
                 * booking_table表在同步前order_id字段为null
                 * ，同步后booking_uuid被置为null， 所以要比对这两个字段。
                 */
                List<BookingTable> list;

                if (booking.getId() != null) {
                    list = bookingTableDao.queryBuilder()
                            .where()
                            .isNotNull(BookingTable.$.tableID)
                            .and()
                            .eq(BookingTable.$.orderID, booking.getId())
                            .and()
                            .eq(BookingTable.$.status, 0)
                            .query();
                    bookingTableList.addAll(list);
                }
                if (booking.getUuid() != null) {
                    list = bookingTableDao.queryBuilder()
                            .where()
                            .isNotNull(BookingTable.$.bookingUuid)
                            .and()
                            .eq(BookingTable.$.bookingUuid, booking.getUuid())
                            .and()
                            .eq(BookingTable.$.status, 0)
                            .query();

                    bookingTableList.addAll(list);
                }
            }
            return bookingTableList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<BookingTable> listBookingTablesByTime(Long stratPeriodTime, Long endPeriodTime) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<BookingPeriod, String> bookingPeriodDao = helper.getDao(BookingPeriod.class);

            List<BookingPeriod> bookingPeriodList = bookingPeriodDao.queryBuilder()
                    .where()
                    .ge(BookingPeriod.$.startTime, stratPeriodTime)
                    .and()
                    .le(BookingPeriod.$.endTime, endPeriodTime)
                    .query();

            Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
            List<Booking> bookingList = new ArrayList<>();
            for (BookingPeriod period : bookingPeriodList) {
                Booking booking = bookingDao.queryBuilder()
                        .where()
                        .eq(Booking.$.id, period.getBookingId())
                        .and()
                        .notIn(Booking.$.orderStatus,
                                BookingOrderStatus.CANCEL,
                                BookingOrderStatus.LEAVE,
                                BookingOrderStatus.REFUSED)
                        .queryForFirst();
                bookingList.add(booking);
            }
            Dao<BookingTable, String> bookingTableDao = helper.getDao(BookingTable.class);
            List<BookingTable> bookingTableList = new ArrayList<>();
            for (Booking booking : bookingList) {
                /*
                 * 由于服务器的booking_table表中没有记录booking.uuid，
                 *
                 * booking_table表在同步前order_id字段为null
                 * ，同步后booking_uuid被置为null， 所以要比对这两个字段。
                 */
                List<BookingTable> list;

                if (booking.getId() != null) {
                    list = bookingTableDao.queryBuilder()
                            .where()
                            .isNotNull(BookingTable.$.tableID)
                            .and()
                            .eq(BookingTable.$.status, 0)
                            .and()
                            .eq(BookingTable.$.orderID, booking.getId())
                            .query();
                    bookingTableList.addAll(list);
                }
                if (booking.getUuid() != null) {
                    list = bookingTableDao.queryBuilder()
                            .where()
                            .isNotNull(BookingTable.$.bookingUuid)
                            .and()
                            .eq(BookingTable.$.status, 0)
                            .and()
                            .eq(BookingTable.$.bookingUuid, booking.getUuid())
                            .query();

                    bookingTableList.addAll(list);
                }
            }
            return bookingTableList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Tables> listOrderTables(List<BookingTable> bookingTableList) throws Exception {
        if (Utils.isEmpty(bookingTableList)) {
            return null;
        } else {
            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                List<String> tablesUuidList = new ArrayList<>();
                Dao<Tables, String> tablesDao = helper.getDao(Tables.class);
                for (BookingTable b : bookingTableList) {
                    tablesUuidList.add(b.getTableID());
                }
                return tablesDao.queryBuilder().where().in(Tables.$.uuid, tablesUuidList).query();
            } finally {
                DBHelperManager.releaseHelper(helper);
            }
        }
    }

    @Override
    public void setCommercialAreaVoList(List<BookingVo> bookingVoList) {
        Map<String, BookingVo> bookingUuidMap = new HashMap<>();
        for (BookingVo bookingVo : bookingVoList) {
            bookingUuidMap.put(bookingVo.getBooking().getUuid(), bookingVo);
        }

        setCommercialAreaVoListByUuid(bookingUuidMap);
    }

    private void setCommercialAreaVoListByUuid(Map<String, BookingVo> bookingVoMap) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List<String> bookingTableUuidList = new ArrayList<>();
            /*Dao<BookingTable, ?> bookingTableDao = helper.getDao(BookingTable.class);
            List<BookingTable> bookingTableList = bookingTableDao.queryBuilder().where().in(BookingTable.$.bookingUuid, bookingVoMap.keySet()).query();
            for (BookingTable bookingTable : bookingTableList) {
                bookingTableUuidList.add(bookingTable.getUuid());
                BookingVo bookingVo = bookingVoMap.get(bookingTable.getBookingUuid());
                if (bookingVo != null) {
                    List<BookingTable> bTableList = bookingVo.getBookingTableList();
                    if (bTableList == null) {
                        bTableList = new ArrayList<>();
                        bookingVo.setBookingTableList(bTableList);
                    }
                    bTableList.add(bookingTable);
                }
            }*/

            for (BookingVo bookingVo : bookingVoMap.values()) {
                bookingVo.tableAreaVoList = null;
                List<BookingTable> bookingTableList = bookingVo.getBookingTableList();
                if (bookingTableList != null) {
                    for (BookingTable bookingTable : bookingTableList) {
                        bookingTableUuidList.add(bookingTable.getTableID());
                    }
                }
            }

            Dao<Tables, ?> tablesDao = helper.getDao(Tables.class);
            List<Tables> tablesList = tablesDao.queryBuilder().where().in(Tables.$.uuid, bookingTableUuidList).query();
            List<Long> areaIdList = new ArrayList<>();
            for (Tables tables : tablesList) {
                areaIdList.add(tables.getAreaId());
            }

            TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
            List<CommercialArea> commercialAreaList = tablesDal.findAreaById(areaIdList);
            for (BookingVo bookingVo : bookingVoMap.values()) {
                List<BookingTable> bookingTableList = bookingVo.getBookingTableList();
                if (bookingTableList != null) {
                    for (BookingTable bookingTable : bookingTableList) {
                        TablesCommercialAreaVo commercialAreaVo = getCommercialAreaVo(tablesList, commercialAreaList, bookingTable);
                        if (commercialAreaVo != null) {
                            if (bookingVo.tableAreaVoList == null) {
                                bookingVo.tableAreaVoList = new ArrayList<>();
                            }
                            bookingVo.tableAreaVoList.add(commercialAreaVo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private TablesCommercialAreaVo getCommercialAreaVo(List<Tables> tablesList, List<CommercialArea> commercialAreaList, BookingTable bookingTable) {
        for (Tables tables : tablesList) {
            if (bookingTable.getTableID().equals(tables.getUuid())) {
                TablesCommercialAreaVo commercialAreaVo = new TablesCommercialAreaVo();
                commercialAreaVo.commercialArea = getCommercialAreaWithTableList(commercialAreaList, tables);
                commercialAreaVo.tables = tables;
                return commercialAreaVo;
            }
        }
        return null;
    }

    private CommercialArea getCommercialAreaWithTableList(List<CommercialArea> commercialAreaList, Tables tables) {
        if (commercialAreaList != null) {
            for (CommercialArea commercialArea : commercialAreaList) {
                if (commercialArea.getId() != null && commercialArea.getId().equals(tables.getAreaId())) {
                    return commercialArea;
                }
            }
        }
        return null;
    }

    @Override
    public List<CommercialArea> getCommercialAreaList(List<BookingVo> bookingVoList) {
        List<CommercialArea> areaList = new ArrayList<>();
        for (BookingVo bookingVo : bookingVoList) {
            List<TablesCommercialAreaVo> tableAreaVoList = bookingVo.tableAreaVoList;
            if (tableAreaVoList != null) {
                for (TablesCommercialAreaVo areaVo : tableAreaVoList) {
                    if (!areaList.contains(areaVo.commercialArea))
                        areaList.add(areaVo.commercialArea);
                }
            }
        }
        return areaList;
    }

    @Override
    public List<BookingVo> getBookingListByArea(List<BookingVo> bookingVoList, List<CommercialArea> areaList) {
        List<BookingVo> resultList = new ArrayList<>();
        Map<String, CommercialArea> areaMap = new HashMap<>();
        for (CommercialArea area : areaList) {
            areaMap.put(area.getAreaCode(), area);
        }
        for (BookingVo bookingVo : bookingVoList) {
            List<TablesCommercialAreaVo> tableAreaVoList = bookingVo.tableAreaVoList;
            if (tableAreaVoList != null) {
                for (TablesCommercialAreaVo areaVo : tableAreaVoList) {
                    if (areaMap.get(areaVo.commercialArea.getAreaCode()) != null) {
                        resultList.add(bookingVo);
                        break;
                    }
                }
            }
        }
        return resultList;
    }
}

