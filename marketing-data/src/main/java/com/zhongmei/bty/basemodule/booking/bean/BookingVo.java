package com.zhongmei.bty.basemodule.booking.bean;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.commonbusiness.bean.TablesCommercialAreaVo;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BookingVo implements Serializable {

    private static final long serialVersionUID = -3480118100975924976L;

    private Booking booking;

    private List<BookingTable> bookingTableList;
    @Deprecated
    private List<Tables> tablesList;

    public Trade trade;

    private BookingGroupInfo bookingGroupInfo;

    private BookingPeriod bookingPeriod;

    /**
     * 团餐模版
     */
    private BookingMealShellVo mealShellVo;


    private List<BookingTradeItemVo> tradeItemVoList;

    private Integer isPreOrder; // 1-是 2-否
    private BigDecimal oldDeskCount = null;

    /**
     * 销售员
     */
    private AuthUser saleAuthUser;

    //给打印使用的字段
    public List<TablesCommercialAreaVo> tableAreaVoList;

    private BookingDepositInfo bookingDepositInfo;

    public List<BookingTable> getBookingTableList() {
        return bookingTableList;
    }

    public void setBookingTableList(List<BookingTable> bookingTableList) {
        this.bookingTableList = bookingTableList;
    }

    public boolean isReservedTable() {
        return bookingTableList != null && bookingTableList.size() > 0;
    }

    public boolean isCancelOrRefused() {
        return booking.getOrderStatus() == BookingOrderStatus.CANCEL
                || booking.getOrderStatus() == BookingOrderStatus.REFUSED;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Deprecated
    public List<Tables> getTablesList() {
        return tablesList;
    }

    @Deprecated
    public void setTablesList(List<Tables> tablesList) {
        this.tablesList = tablesList;
    }

    public BookingGroupInfo getBookingGroupInfo() {
        return bookingGroupInfo;
    }

    public void setBookingGroupInfo(BookingGroupInfo bookingGroupInfo) {
        this.bookingGroupInfo = bookingGroupInfo;
    }

    public BookingPeriod getBookingPeriod() {
        return bookingPeriod;
    }

    public void setBookingPeriod(BookingPeriod bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

    public AuthUser getSaleAuthUser() {
        return saleAuthUser;
    }

    public BookingDepositInfo getBookingDepositInfo() {
        return bookingDepositInfo;
    }

    public void setBookingDepositInfo(BookingDepositInfo bookingDepositInfo) {
        this.bookingDepositInfo = bookingDepositInfo;
    }

    public boolean isBookingDeposit() {
        if (bookingDepositInfo != null) {
            Trade trade = bookingDepositInfo.getTrade();
            if (trade.isValid()
                    && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
                return true;
            }
        }
        return false;
    }

    public void setSaleAuthUser(AuthUser saleAuthUser) {
        this.saleAuthUser = saleAuthUser;
    }

    public List<BookingTradeItemVo> getTradeItemVoList() {
        return tradeItemVoList;
    }

    public void setTradeItemVoList(List<BookingTradeItemVo> tradeItemVoList) {
        this.tradeItemVoList = tradeItemVoList;
    }

    public BookingMealShellVo getMealShellVo() {
        return mealShellVo;
    }

    public void setMealShellVo(BookingMealShellVo mealShellVo) {
        this.mealShellVo = mealShellVo;
    }

    public BigDecimal getDeskCount() {
        if (Utils.isEmpty(bookingTableList)) {
            return BigDecimal.ZERO;
        }
        BigDecimal deskCount = BigDecimal.ZERO;
        for (BookingTable bookingTable : bookingTableList) {
            if (bookingTable.isValid()) {
                deskCount = deskCount.add(BigDecimal.ONE);
            }
        }
        return deskCount;
    }

    public void setOldDeskCount(BigDecimal oldDeskCount) {
        this.oldDeskCount = oldDeskCount;
    }

    public BigDecimal getOldDeskCount() {
        return oldDeskCount;
    }

    public boolean isPreOrder() {
        return (isPreOrder != null && isPreOrder == BookingOrderDishPopupVo.ORDER_DISH)
                || (tradeItemVoList != null && tradeItemVoList.size() > 0)
                || trade != null;
    }

    public void setIsPreOrder(Integer isPreOrder) {
        this.isPreOrder = isPreOrder;
    }

    public void setBookingVo(BookingVo bookingVo) {
        this.booking = bookingVo.getBooking();
        this.bookingTableList = bookingVo.getBookingTableList();
        this.tablesList = bookingVo.getTablesList();
        this.trade = bookingVo.trade;
        this.bookingGroupInfo = bookingVo.getBookingGroupInfo();
        this.bookingPeriod = bookingVo.getBookingPeriod();
        this.mealShellVo = bookingVo.getMealShellVo();
        this.tradeItemVoList = bookingVo.getTradeItemVoList();
        this.isPreOrder = bookingVo.isPreOrder() ? 1 : 2;
        this.saleAuthUser = bookingVo.saleAuthUser;
        this.bookingDepositInfo = bookingVo.bookingDepositInfo;
    }

    public String getTableName() {
        String result = null;
        StringBuilder tableName = new StringBuilder();
        if (tablesList != null && tablesList.size() > 0) {
            for (Tables table : tablesList) {
                tableName.append(table.getTableName());
                tableName.append("/");
            }
        }
        if (tableName.length() > 0) {
            result = tableName.subSequence(0, tableName.length() - 1).toString();
        }
        return result;
    }
}
