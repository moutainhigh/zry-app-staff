package com.zhongmei.bty.booking.utils;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.operates.message.content.BookingToUnionSubmitReq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class BookingOpenTableUtils {

    public static List<BookingToUnionSubmitReq.BookingToUnionTable> createBookingUnionTableList(List<BookingTable> bookingTableList) {
        if (Utils.isNotEmpty(bookingTableList)) {
            List<BookingToUnionSubmitReq.BookingToUnionTable> bookingUnionTableList = new ArrayList<>();
            for (BookingTable bookingTable : bookingTableList) {
                BookingToUnionSubmitReq.BookingToUnionTable bookingUnionTable = new BookingToUnionSubmitReq.BookingToUnionTable();
                bookingUnionTable.id = bookingTable.getBtid();
                bookingUnionTable.status = bookingTable.getStatus();
                bookingUnionTable.tableId = bookingTable.getTableID();
                bookingUnionTable.uuid = bookingTable.getUuid();
                bookingUnionTable.serverUpdateTime = bookingTable.getServerUpdateTime();
                bookingUnionTableList.add(bookingUnionTable);
            }

            return bookingUnionTableList;
        }

        return null;
    }

    public static List<Trade> createBookingUnionSubTradeList(BookingVo bookingVo, List<Tables> tableList) {
        List<Trade> subTradeList = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Trade trade = new Trade();
            trade.validateCreate();
            trade.setDomainType(DomainType.RESTAURANT);
            trade.setBusinessType(BusinessType.DINNER);
            trade.setTradeTime(new Date().getTime());
            trade.setDeliveryType(DeliveryType.HERE);
            trade.setSource(SourceId.POS);
            trade.setSourceChild(SourceChild.ANDROID);
            trade.setTradeNo(SystemUtils.getBillNumber());
            trade.setDishKindCount(0);
            trade.setClientCreateTime(System.currentTimeMillis());
            trade.setUuid(SystemUtils.genOnlyIdentifier());
            subTradeList.add(trade);
        }

        return subTradeList;
    }

    public static Trade createBookingUnionMainTrade(BookingVo bookingVo, List<Tables> tableList) {
        Trade mainTrade = new Trade();
        mainTrade.setTradeNo(SystemUtils.getBillNumber());
        mainTrade.validateCreate();
        BigDecimal unionMainTradeAmount = calculateBookingUnionMainTradeAmount(bookingVo, tableList);
        mainTrade.setSaleAmount(unionMainTradeAmount);
        mainTrade.setTradeAmount(unionMainTradeAmount);
        mainTrade.setTradeAmountBefore(unionMainTradeAmount);
        mainTrade.setDishKindCount(calculateSkuKindCount(bookingVo.getTradeItemVoList()));

        return mainTrade;
    }

    private static BigDecimal calculateBookingUnionMainTradeAmount(BookingVo bookingVo, List<Tables> tableList) {
        BigDecimal tradeAmount = BigDecimal.ZERO;
        if (Utils.isNotEmpty(bookingVo.getTradeItemVoList())) {
            for (BookingTradeItemVo bookingTradeItemVo : bookingVo.getTradeItemVoList()) {
                BookingTradeItem bookingTradeItem = bookingTradeItemVo.getTradeItem();
                BigDecimal itemAmount = bookingTradeItem.getAmount();
                if (itemAmount != null) {
                    tradeAmount.add(itemAmount);
                }
                if (Utils.isNotEmpty(bookingTradeItemVo.getTradeItemPropertyList())) {
                    for (BookingTradeItemProperty bookingTradeItemProperty : bookingTradeItemVo.getTradeItemPropertyList()) {
                        BigDecimal propertyAmount = bookingTradeItemProperty.getAmount();
                        if (propertyAmount != null) {
                            tradeAmount.add(propertyAmount);
                        }
                    }
                }
            }
        }
        tradeAmount = tradeAmount.multiply(new BigDecimal(tableList.size()));

        return tradeAmount;
    }

    private static Integer calculateSkuKindCount(List<BookingTradeItemVo> bookingTradeItemVos) {
        if (Utils.isNotEmpty(bookingTradeItemVos)) {
            Set<String> skuKindSet = new HashSet<>();
            for (BookingTradeItemVo bookingTradeItemVo : bookingTradeItemVos) {
                BookingTradeItem bookingTradeItem = bookingTradeItemVo.getTradeItem();
                if (TextUtils.isEmpty(bookingTradeItem.getParentUuid())
                        && (bookingTradeItem.getType() == DishType.SINGLE || bookingTradeItem.getType() == DishType.COMBO)) {
                    skuKindSet.add(bookingTradeItem.getDishUuid());
                }
            }

            return new Integer(skuKindSet.size());
        }

        return Integer.valueOf(0);
    }

}
