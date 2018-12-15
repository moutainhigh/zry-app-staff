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

/**
 * @version: 1.0
 * @date 2015年7月27日
 */
public class BookingTablesManager {
    private static final String TAG = BookingTablesManager.class.getSimpleName();

    /**
     * 加载数据。调用此方法阻塞调用线程
     *
     * @return
     * @throws Exception
     */
    public List<Period> loadData() throws Exception {
        BookingDal dal = OperatesFactory.create(BookingDal.class);
        List<Period> periodList = dal.listPeriod();
        if (periodList != null && periodList.size() > 1) {
            // 如果有其它时段 去除全天
            for (Period p : periodList) {
                if (p.getPeriodType() == PeriodType.ALLDAY) {
                    periodList.remove(p);
                    break;
                }
            }
        }
        return periodList;
    }

    /**
     * 获取一个时段的桌台状态。调用此方法阻塞调用线程
     *
     * @param date
     * @param period
     * @return
     * @throws Exception
     */
    public List<TablesAreaVo> selectPeriod(Date date, Period period, boolean isEdit, Booking booking) throws Exception {
        // 获取时段已经预订的桌台
        BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        HashMap<String, BookingTable> bookingTables = new HashMap<String, BookingTable>();
        if (period != null) {
            List<BookingTable> bookingTableList = bookingDal.listBookingTables(date, period.getId());
            for (BookingTable bookingTable : bookingTableList) {
                bookingTables.put(bookingTable.getTableID(), bookingTable);
            }
        }
        // 获取所有桌台区域
        TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        List<CommercialArea> areas = tablesDal.listTableArea();
        Map<Long, TablesAreaVo> typeVoFinder = new LinkedHashMap<Long, TablesAreaVo>();
        for (CommercialArea area : areas) {
            TablesAreaVo tableTypeVo = new TablesAreaVo(area);
            tableTypeVo.setTablesVoList(new ArrayList<TablesVo>());
            typeVoFinder.put(area.getId(), tableTypeVo);
        }

        // 根据已经预订的桌台信息设置桌台的可用状态
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

    /**
     * 根据预订uuid查询预订单
     *
     * @param uuid
     * @return
     */
    public Booking getBookByUUId(String uuid) {
        BookingDal dal = OperatesFactory.create(BookingDal.class);
        try {
            return dal.findBookingByUUID(uuid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "", e);
        }
        return null;
    }

    /**
     * 通过id查找period
     *
     * @param periodId
     * @return
     */
    public Period findPeriodById(long periodId) {
        BookingDal dal = OperatesFactory.create(BookingDal.class);
        try {
            return dal.findPeriodById(periodId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "", e);
        }
        return null;
    }

    /**
     * 通过bookid查询预订桌台
     *
     * @param bookid
     * @return
     */
    public static List<BookingTable> findBookTablesById(Long bookid) {
        if (bookid == null) {
            return null;
        }
        BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        try {
            return bookingDal.listBookTablesById(bookid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "", e);
        }
        return null;
    }

    /**
     * 通过预订 与选择的桌台 得到vo
     *
     * @param booking
     * @param selectedTables
     * @return
     */
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

    /**
     * 桌台选取去重复
     *
     * @param booking
     * @param selectedTables
     * @return
     */
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

            // 去重复
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
                // 已选定列表去除重复
                for (int i = 0; i < beforeSelTables.size(); i++) {
                    for (BookingTable ta : tempList) {
                        if (beforeSelTables.get(i).getTableID() == ta.getTableID()) {
                            beforeSelTables.remove(i);
                            --i;
                        }
                    }
                }
                // 当前选择桌台去重复
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

    /**
     * 发送创建预订请求
     *
     * @param bookVo
     * @param listener
     */
    public static void addBooking(BookingVo bookVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {
        // 向服务器发送下单请求
        BookingOperates bookOperates = OperatesFactory.create(BookingOperates.class);
        bookOperates.submitBooking(bookVo, listener);
    }

    /**
     * 获取当前时段需要展示定字的桌台
     *
     * @return
     */
    public HashMap<String, BookingTable> getCurrentPeriodBookingTables() throws Exception {
        BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        BookingSetting setting = bookingDal.getBookingSetting();
        long now = System.currentTimeMillis();
        long stratTime;
        if (setting == null || setting.getKeepTime() == null || setting.getKeepTime().length() == 0) {
            //没有设置预留时长
            stratTime = now;
        } else {
            String str[] = setting.getKeepTime().split(":|\\.");
            //开始时间为当前时间往前推保留时间，如预订时间为19:00，保留时间为30分钟，则19:30之前都候需要展示19:00的预订
            stratTime = now - (Integer.parseInt(str[0]) * 60 * 60 + Integer.parseInt(str[1]) * 60 + Integer.parseInt(str[2])) * 1000L;
        }
        //结束时间为当前时间往后推1小时
        long endTime = now + 1 * 60 * 60 * 1000;
        List<BookingTable> bookingTableList = bookingDal.listCurrentPeriodBookingTables(stratTime, endTime);
        HashMap<String, BookingTable> bookingTables = new HashMap<String, BookingTable>();
        for (BookingTable table : bookingTableList) {
            bookingTables.put(table.getTableID(), table);
        }
        return bookingTables;
    }

    /**
     * 获取当前时段选择的桌台数据，通过时段开始，结束时间
     *
     * @param customerBookingTableList  顾客预定的桌台
     * @param bookingTablesListByPeriod 时间段内已经预定过的所有桌台 ， 来源于服务器
     * @param booking
     * @return
     * @throws Exception
     */
    public List<TablesAreaVo> selectPeriodByTime(List<BookingTable> customerBookingTableList, List<BookingTable> bookingTablesListByPeriod, Booking booking) throws Exception {
        // 时段已经预订的桌台
        HashMap<String, BookingTable> bookingTables = new HashMap<>();
        if (bookingTablesListByPeriod != null && bookingTablesListByPeriod.size() > 0) {
            for (BookingTable bookingTable : bookingTablesListByPeriod) {
                bookingTables.put(bookingTable.getTableID(), bookingTable);
            }
        }
        if (customerBookingTableList != null && customerBookingTableList.size() > 0) { // 判断当前顾客已经预定的桌台是否包含在全局桌台中，没有则添加
            for (BookingTable bookingTable : customerBookingTableList) {
                if (!bookingTables.containsKey(bookingTable.getTableID())) {
                    bookingTables.put(bookingTable.getTableID(), bookingTable);
                }
            }
        }
        // 获取所有桌台区域
        TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        List<CommercialArea> areas = tablesDal.listTableArea();
        Map<Long, TablesAreaVo> typeVoFinder = new LinkedHashMap<Long, TablesAreaVo>();
        for (CommercialArea area : areas) {
            TablesAreaVo tableTypeVo = new TablesAreaVo(area);
            tableTypeVo.setTablesVoList(new ArrayList<TablesVo>());
            typeVoFinder.put(area.getId(), tableTypeVo);
        }

        // 查询可用的物理桌台
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
                        tableTypeVo.setInitSelectedTableCount(tableTypeVo.getInitSelectedTableCount() + 1); // 设置保存初始桌台数
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

    /**
     * 获取当前时段选择的桌台数据，通过时段开始，结束时间
     *
     * @param currentPeroidBookingTables 当前时间段已经预定过的桌台 , 服务器查询出来的当前桌台选择数据
     * @param isEdit
     * @param booking
     * @return
     * @throws Exception
     */
    public List<TablesAreaVo> queryTablesAreaVo(List<BookingTable> currentPeroidBookingTables, boolean isEdit, Booking booking) throws Exception {
        // 获取时段已经预订的桌台
//		BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
        HashMap<String, BookingTable> bookingTablesMap = new HashMap<String, BookingTable>();
//		if (startPeriodTime != null && endPeriodTime != null) {
//			List<BookingTable> bookingTableList = bookingDal.listBookingTablesByTime(startPeriodTime , endPeriodTime);
        for (BookingTable bookingTable : currentPeroidBookingTables) {
            bookingTablesMap.put(bookingTable.getUuid(), bookingTable);
        }
//		}
        // 获取所有桌台区域
        TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        List<CommercialArea> areas = tablesDal.listTableArea();
        Map<Long, TablesAreaVo> typeVoFinder = new LinkedHashMap<Long, TablesAreaVo>();
        for (CommercialArea area : areas) {
            TablesAreaVo tableTypeVo = new TablesAreaVo(area);
            tableTypeVo.setTablesVoList(new ArrayList<TablesVo>());
            typeVoFinder.put(area.getId(), tableTypeVo);
        }

        // 根据已经预订的桌台信息设置桌台的可用状态
        List<Tables> tables = tablesDal.listTables();
        for (Tables table : tables) {
            TablesAreaVo tableTypeVo = typeVoFinder.get(table.getAreaId());
            if (tableTypeVo != null) {
                TablesVo tablesVo = null;
                if (bookingTablesMap.containsKey(table.getUuid())) {
                    BookingTable bTable = bookingTablesMap.get(table.getUuid());
                    if (isEdit && booking != null && (bTable.getOrderID().longValue() == booking.getId().longValue())) { // 当前已被选的桌台等与当前预定的订单号
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

    public static List<BookingTable> getUpdateTableListByTables(Booking booking, List<BookingTable> orderTable, List<Tables> selectedTables) {
		/*List<BookingTable> bTableList = new ArrayList<>();
		HashMap<String, BookingTable> map = new HashMap<>();
		if (!Utils.isEmpty(orderTable)) {
			for (BookingTable b : orderTable) {
				b.setStatus(-1);
				map.put(b.getTableID(), b);
			}
		}
		if (!Utils.isEmpty(selectedTables)) {
			for (TablesVo tVo : selectedTables) {
				BookingTable bookingTable = new BookingTable();
				bookingTable.setBookingUuid(booking.getUuid());
				bookingTable.setUuid(SystemUtils.genOnlyIdentifier());
				bookingTable.setStatus(0);
				bookingTable.setTableID(tVo.getTable().getUuid());
				map.put(bookingTable.getTableID(), bookingTable);
			}
		}
		bTableList.addAll(map.values());*/
        List<Tables> tables = new ArrayList<>();
        if (selectedTables != null) {
            for (Tables table : selectedTables) {
                tables.add(table);
            }
        }

        return getUpdateTableList1(booking, orderTable, tables);
    }

    public static List<BookingTable> getUpdateTableList(Booking booking, List<BookingTable> orderTable, List<TablesVo> selectedTables) {
		/*List<BookingTable> bTableList = new ArrayList<>();
		HashMap<String, BookingTable> map = new HashMap<>();
		if (!Utils.isEmpty(orderTable)) {
			for (BookingTable b : orderTable) {
				b.setStatus(-1);
				map.put(b.getTableID(), b);
			}
		}
		if (!Utils.isEmpty(selectedTables)) {
			for (TablesVo tVo : selectedTables) {
				BookingTable bookingTable = new BookingTable();
				bookingTable.setBookingUuid(booking.getUuid());
				bookingTable.setUuid(SystemUtils.genOnlyIdentifier());
				bookingTable.setStatus(0);
				bookingTable.setTableID(tVo.getTable().getUuid());
				map.put(bookingTable.getTableID(), bookingTable);
			}
		}
		bTableList.addAll(map.values());*/
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
                    //bookingTable.setBtid(tVo.getId());
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
