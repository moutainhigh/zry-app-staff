package com.zhongmei.bty.basemodule.booking.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingSetting;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.booking.message.BookingResp;
import com.zhongmei.bty.basemodule.booking.operates.BookingDal;
import com.zhongmei.bty.basemodule.booking.operates.BookingOperates;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.trade.bean.TablesAreaVo;
import com.zhongmei.bty.basemodule.trade.bean.TablesVo;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.commonmodule.database.entity.Period;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.bty.commonmodule.database.enums.PeriodType;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BookingTablesManager {
    private static final String TAG = BookingTablesManager.class.getSimpleName();


    public List<Period> loadData() throws Exception {
        BookingDal dal = OperatesFactory.create(BookingDal.class);
        List<Period> periodList = dal.listPeriod();
        if (periodList != null && periodList.size() > 1) {
                        for (Period p : periodList) {
                if (p.getPeriodType() == PeriodType.ALLDAY) {
                    periodList.remove(p);
                    break;
                }
            }
        }
        return periodList;
    }


    public List<TablesAreaVo> selectPeriod(Date date, Period period, boolean isEdit, Booking booking) throws Exception {
                BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        HashMap<String, BookingTable> bookingTables = new HashMap<String, BookingTable>();
        if (period != null) {
            List<BookingTable> bookingTableList = bookingDal.listBookingTables(date, period.getId());
            for (BookingTable bookingTable : bookingTableList) {
                bookingTables.put(bookingTable.getTableID(), bookingTable);
            }
        }
                TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        List<CommercialArea> areas = tablesDal.listTableArea();
        Map<Long, TablesAreaVo> typeVoFinder = new LinkedHashMap<Long, TablesAreaVo>();
        for (CommercialArea area : areas) {
            TablesAreaVo tableTypeVo = new TablesAreaVo(area);
            tableTypeVo.setTablesVoList(new ArrayList<TablesVo>());
            typeVoFinder.put(area.getId(), tableTypeVo);
        }

                List<Tables> tables = tablesDal.listTables();
        for (Tables table : tables) {
            TablesAreaVo tableTypeVo = typeVoFinder.get(table.getAreaId());
            if (tableTypeVo != null) {
                TablesVo tablesVo = null;
                if (bookingTables.containsKey(table.getUuid())) {
                    BookingTable bTable = bookingTables.get(table.getUuid());
                    if (isEdit && booking != null && (bTable.getOrderID().longValue() == booking.getId().longValue())) {
                        Log.i("BookingTablesManager", "edit");
                        tablesVo = new TablesVo(table, true);
                        tablesVo.setSelected(true);
                    } else {
                        tablesVo = new TablesVo(table, false);
                    }
                } else {
                    tablesVo = new TablesVo(table, true);
                }
                if (tablesVo != null)
                    tableTypeVo.getTablesVoList().add(tablesVo);
            }
        }

        List<TablesAreaVo> areaVoList = new ArrayList<TablesAreaVo>(typeVoFinder.values());
        Collections.sort(areaVoList, new Comparator<TablesAreaVo>() {

            @Override
            public int compare(TablesAreaVo lhs, TablesAreaVo rhs) {
                int v = 0;
                if (lhs.getTablesArea().getAreaCode() != null && rhs.getTablesArea().getAreaCode() != null) {
                    v = lhs.getTablesArea().getAreaCode().compareTo(rhs.getTablesArea().getAreaCode());
                } else if (lhs.getTablesArea().getAreaCode() != null) {
                    return 1;
                } else if (rhs.getTablesArea().getAreaCode() != null) {
                    return -1;
                }
                if (v == 0) {
                    v = lhs.getTablesArea().getId().compareTo(rhs.getTablesArea().getId());
                }
                return v;
            }
        });

        return areaVoList;

    }


    public Booking getBookByUUId(String uuid) {
        BookingDal dal = OperatesFactory.create(BookingDal.class);
        try {
            return dal.findBookingByUUID(uuid);
        } catch (Exception e) {
                        Log.e(TAG, "", e);
        }
        return null;
    }


    public Period findPeriodById(long periodId) {
        BookingDal dal = OperatesFactory.create(BookingDal.class);
        try {
            return dal.findPeriodById(periodId);
        } catch (Exception e) {
                        Log.e(TAG, "", e);
        }
        return null;
    }


    public static List<BookingTable> findBookTablesById(Long bookid) {
        if (bookid == null) {
            return null;
        }
        BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        try {
            return bookingDal.listBookTablesById(bookid);
        } catch (Exception e) {
                        Log.e(TAG, "", e);
        }
        return null;
    }


    public static BookingVo getBookingVo(Booking booking, List<TablesVo> selectedTables) {
        BookingVo bookVo = new BookingVo();
        bookVo.setBooking(booking);
        List<BookingTable> bTableList = new ArrayList<BookingTable>();
        List<BookingTable> beforeSelTables = BookingTablesManager.findBookTablesById(booking.getId());

        if (beforeSelTables != null) {
            for (BookingTable tb : beforeSelTables) {
                tb.setStatus(-1);
            }
            bTableList.addAll(beforeSelTables);
        }
        if (selectedTables != null) {
            for (TablesVo tVo : selectedTables) {
                BookingTable bookingTable = new BookingTable();
                bookingTable.setBookingUuid(booking.getUuid());
                bookingTable.setUuid(SystemUtils.genOnlyIdentifier());
                bookingTable.setStatus(0);
                bookingTable.setTableID(tVo.getTable().getUuid());
                bTableList.add(bookingTable);
            }
        }
        bookVo.setBookingTableList(bTableList);
        return bookVo;
    }


    public static BookingVo getBookingVoNoRepeat(Booking booking, List<TablesVo> selectedTables) {
        BookingVo bookVo = new BookingVo();
        bookVo.setBooking(booking);
        List<BookingTable> bTableList = new ArrayList<BookingTable>();
        List<BookingTable> beforeSelTables = BookingTablesManager.findBookTablesById(booking.getId());
        List<BookingTable> tempList = new ArrayList<BookingTable>();

        if (selectedTables != null) {
            for (TablesVo tVo : selectedTables) {
                BookingTable bookingTable = new BookingTable();
                bookingTable.setBookingUuid(booking.getUuid());
                bookingTable.setUuid(SystemUtils.genOnlyIdentifier());
                bookingTable.setStatus(0);
                bookingTable.setTableID(tVo.getTable().getUuid());
                bTableList.add(bookingTable);
            }
        }

        if (beforeSelTables != null && beforeSelTables.size() > 0) {
            for (BookingTable tb : beforeSelTables) {
                tb.setStatus(-1);
            }

                        if (bTableList.size() > 0) {
                for (BookingTable bTable : bTableList) {
                    for (BookingTable bfTable : beforeSelTables) {
                        if (TextUtils.equals(bTable.getTableID(), bfTable.getTableID())) {
                            tempList.add(bTable);
                        }
                    }
                }
            }

            if (tempList.size() > 0) {
                                for (int i = 0; i < beforeSelTables.size(); i++) {
                    for (BookingTable ta : tempList) {
                        if (beforeSelTables.get(i).getTableID() == ta.getTableID()) {
                            beforeSelTables.remove(i);
                            --i;
                        }
                    }
                }
                                for (int i = 0; i < bTableList.size(); i++) {
                    for (BookingTable ta : tempList) {
                        if (bTableList.get(i).getTableID() == ta.getTableID()) {
                            bTableList.remove(i);
                            --i;
                        }
                    }
                }
            }
            bTableList.addAll(beforeSelTables);
        }

        bookVo.setBookingTableList(bTableList);
        return bookVo;
    }


    public static void addBooking(BookingVo bookVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {
                BookingOperates bookOperates = OperatesFactory.create(BookingOperates.class);
        bookOperates.submitBooking(bookVo, listener);
    }


    public HashMap<String, BookingTable> getCurrentPeriodBookingTables() throws Exception {
        BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        BookingSetting setting = bookingDal.getBookingSetting();
        long now = System.currentTimeMillis();
        long stratTime;
        if (setting == null || setting.getKeepTime() == null || setting.getKeepTime().length() == 0) {
                        stratTime = now;
        } else {
            String str[] = setting.getKeepTime().split(":|\\.");
                        stratTime = now - (Integer.parseInt(str[0]) * 60 * 60 + Integer.parseInt(str[1]) * 60 + Integer.parseInt(str[2])) * 1000L;
        }
                long endTime = now + 1 * 60 * 60 * 1000;
        List<BookingTable> bookingTableList = bookingDal.listCurrentPeriodBookingTables(stratTime, endTime);
        HashMap<String, BookingTable> bookingTables = new HashMap<String, BookingTable>();
        for (BookingTable table : bookingTableList) {
            bookingTables.put(table.getTableID(), table);
        }
        return bookingTables;
    }


    public List<TablesAreaVo> selectPeriodByTime(List<BookingTable> customerBookingTableList, List<BookingTable> bookingTablesListByPeriod, Booking booking) throws Exception {
                HashMap<String, BookingTable> bookingTables = new HashMap<>();
        if (bookingTablesListByPeriod != null && bookingTablesListByPeriod.size() > 0) {
            for (BookingTable bookingTable : bookingTablesListByPeriod) {
                bookingTables.put(bookingTable.getTableID(), bookingTable);
            }
        }
        if (customerBookingTableList != null && customerBookingTableList.size() > 0) {             for (BookingTable bookingTable : customerBookingTableList) {
                if (!bookingTables.containsKey(bookingTable.getTableID())) {
                    bookingTables.put(bookingTable.getTableID(), bookingTable);
                }
            }
        }
                TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        List<CommercialArea> areas = tablesDal.listTableArea();
        Map<Long, TablesAreaVo> typeVoFinder = new LinkedHashMap<Long, TablesAreaVo>();
        for (CommercialArea area : areas) {
            TablesAreaVo tableTypeVo = new TablesAreaVo(area);
            tableTypeVo.setTablesVoList(new ArrayList<TablesVo>());
            typeVoFinder.put(area.getId(), tableTypeVo);
        }

                List<Tables> tables = tablesDal.listAllPhysicalLayoutTables();
        for (Tables table : tables) {
            TablesAreaVo tableTypeVo = typeVoFinder.get(table.getAreaId());
            if (tableTypeVo != null) {
                TablesVo tablesVo = null;
                if (bookingTables.containsKey(table.getUuid())) {
                    BookingTable bTable = bookingTables.get(table.getUuid());
                    if (booking != null && booking.getId() != null && bTable.getOrderID() != null && (bTable.getOrderID().longValue() == booking.getId().longValue())) {
                        Log.i("BookingTablesManager", "edit");
                        tablesVo = new TablesVo(table, true);
                        tablesVo.setSelected(true);
                        tableTypeVo.setInitSelectedTableCount(tableTypeVo.getInitSelectedTableCount() + 1);                     } else {
                        tablesVo = new TablesVo(table, false);
                    }
                } else {
                    tablesVo = new TablesVo(table, true);
                }
                if (tablesVo != null)
                    tableTypeVo.getTablesVoList().add(tablesVo);
            }
        }

        List<TablesAreaVo> areaVoList = new ArrayList<TablesAreaVo>(typeVoFinder.values());
        Collections.sort(areaVoList, new Comparator<TablesAreaVo>() {

            @Override
            public int compare(TablesAreaVo lhs, TablesAreaVo rhs) {
                int v = 0;
                if (lhs.getTablesArea().getAreaCode() != null && rhs.getTablesArea().getAreaCode() != null) {
                    v = lhs.getTablesArea().getAreaCode().compareTo(rhs.getTablesArea().getAreaCode());
                } else if (lhs.getTablesArea().getAreaCode() != null) {
                    return 1;
                } else if (rhs.getTablesArea().getAreaCode() != null) {
                    return -1;
                }
                if (v == 0) {
                    v = lhs.getTablesArea().getId().compareTo(rhs.getTablesArea().getId());
                }
                return v;
            }
        });

        return areaVoList;

    }


    public List<TablesAreaVo> queryTablesAreaVo(List<BookingTable> currentPeroidBookingTables, boolean isEdit, Booking booking) throws Exception {
                HashMap<String, BookingTable> bookingTablesMap = new HashMap<String, BookingTable>();
        for (BookingTable bookingTable : currentPeroidBookingTables) {
            bookingTablesMap.put(bookingTable.getUuid(), bookingTable);
        }
                TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        List<CommercialArea> areas = tablesDal.listTableArea();
        Map<Long, TablesAreaVo> typeVoFinder = new LinkedHashMap<Long, TablesAreaVo>();
        for (CommercialArea area : areas) {
            TablesAreaVo tableTypeVo = new TablesAreaVo(area);
            tableTypeVo.setTablesVoList(new ArrayList<TablesVo>());
            typeVoFinder.put(area.getId(), tableTypeVo);
        }

                List<Tables> tables = tablesDal.listTables();
        for (Tables table : tables) {
            TablesAreaVo tableTypeVo = typeVoFinder.get(table.getAreaId());
            if (tableTypeVo != null) {
                TablesVo tablesVo = null;
                if (bookingTablesMap.containsKey(table.getUuid())) {
                    BookingTable bTable = bookingTablesMap.get(table.getUuid());
                    if (isEdit && booking != null && (bTable.getOrderID().longValue() == booking.getId().longValue())) {                         Log.i("BookingTablesManager", "edit");
                        tablesVo = new TablesVo(table, true);
                        tablesVo.setSelected(true);
                    } else {
                        tablesVo = new TablesVo(table, false);
                    }
                } else {
                    tablesVo = new TablesVo(table, true);
                }
                if (tablesVo != null)
                    tableTypeVo.getTablesVoList().add(tablesVo);
            }
        }

        List<TablesAreaVo> areaVoList = new ArrayList<TablesAreaVo>(typeVoFinder.values());
        Collections.sort(areaVoList, new Comparator<TablesAreaVo>() {

            @Override
            public int compare(TablesAreaVo lhs, TablesAreaVo rhs) {
                int v = 0;
                if (lhs.getTablesArea().getAreaCode() != null && rhs.getTablesArea().getAreaCode() != null) {
                    v = lhs.getTablesArea().getAreaCode().compareTo(rhs.getTablesArea().getAreaCode());
                } else if (lhs.getTablesArea().getAreaCode() != null) {
                    return 1;
                } else if (rhs.getTablesArea().getAreaCode() != null) {
                    return -1;
                }
                if (v == 0) {
                    v = lhs.getTablesArea().getId().compareTo(rhs.getTablesArea().getId());
                }
                return v;
            }
        });

        return areaVoList;

    }

    public static List<BookingTable> getUpdateTableListByTables(Booking booking, List<BookingTable> orderTable, List<Tables> selectedTables) {

        List<Tables> tables = new ArrayList<>();
        if (selectedTables != null) {
            for (Tables table : selectedTables) {
                tables.add(table);
            }
        }

        return getUpdateTableList1(booking, orderTable, tables);
    }

    public static List<BookingTable> getUpdateTableList(Booking booking, List<BookingTable> orderTable, List<TablesVo> selectedTables) {

        List<Tables> tables = new ArrayList<>();
        if (selectedTables != null) {
            for (TablesVo tablesVo : selectedTables) {
                tables.add(tablesVo.getTable());
            }
        }

        return getUpdateTableList1(booking, orderTable, tables);
    }

    public static List<BookingTable> getUpdateTableList1(Booking booking, List<BookingTable> orderTable, List<Tables> selectedTables) {
        List<BookingTable> bTableList = new ArrayList<>();
        HashMap<String, BookingTable> map = new HashMap<>();
        if (!Utils.isEmpty(orderTable)) {
            for (BookingTable b : orderTable) {
                if (!isBookingTableSelected(b, selectedTables)) {
                    b.setStatus(-1);
                }
                map.put(b.getTableID(), b);
            }
        }
        if (!Utils.isEmpty(selectedTables)) {
            for (Tables tVo : selectedTables) {
                BookingTable bookingTable = map.get(tVo.getUuid());
                if (bookingTable == null) {
                    bookingTable = new BookingTable();
                    bookingTable.setBookingUuid(booking.getUuid());
                    bookingTable.setUuid(SystemUtils.genOnlyIdentifier());
                    bookingTable.setStatus(0);
                    bookingTable.setTableID(tVo.getUuid());
                                        map.put(bookingTable.getTableID(), bookingTable);
                }
            }
        }
        bTableList.addAll(map.values());
        return bTableList;
    }

    private static boolean isBookingTableSelected(BookingTable b, List<Tables> selectedTables) {
        for (Tables tables : selectedTables) {
            if (tables.getUuid().equals(b.getTableID())) {
                return true;
            }
        }
        return false;
    }
}
