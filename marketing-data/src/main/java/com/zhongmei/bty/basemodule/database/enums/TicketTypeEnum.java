package com.zhongmei.bty.basemodule.database.enums;

import android.content.res.Resources;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;


public enum TicketTypeEnum {

    CUSTOMER(1, 1, "客看单"),
    PRECASH(2, 1, "预结单"),
    CASH(3, 1, "结账单"),
    CHECK_OUT(4, 1, "消费清单"),
    DEPOSIT(11, 1, "押金单"),
    REFUND(19, 1, "退货单"),
    CANCEL(20, 1, "作废单"),
    STORE(5, 1, "储值单"),
    STORE_OUT(22, 1, "储值消费单"),
    HANDOVER(6, 1, "交接单"),
    CLOSING(7, 1, "关账单"),
    SALERANK(13, 1, "商品销售统计单"),
    BANK(8, 1, "银联POS签购单"),
    QUEUE(10, 1, "排队单"),
    BOOKING(9, 1, "预订单"),
    INVOICE(21, 1, "发票申请单"),

    KITCHENALL(14, 2, "厨总单"),

    TRANSFERTABLE(16, 2, "转台单"),
    MERGETABLE(18, 2, "合台单"),
    MOVEDISH(17, 2, "移菜单"),
    KITCHENCELL(15, 2, "堂口单");

    public static Resources mResources = BaseApplication.sInstance.getResources();

    public static final int FRONT_CODE = 0x1;
    public static final int KITCHEN_CODE = 0x2;

    private Integer code;
    private String name;
    private Integer type;
    TicketTypeEnum(Integer code, Integer type, String name) {
        this.code = code;
        this.type = type;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        switch (this) {
            case CUSTOMER:
                return mResources.getString(R.string.ticket_note);
            case PRECASH:
                return mResources.getString(R.string.ticket_ad_closing);
            case CASH:
                return mResources.getString(R.string.ticket_closed);
            case CHECK_OUT:
                return mResources.getString(R.string.ticket_xiaofeiqingdan);
            case DEPOSIT:
                return mResources.getString(R.string.ticket_deposit);
            case REFUND:
                return mResources.getString(R.string.ticket_refundnote);
            case CANCEL:
                return mResources.getString(R.string.ticket_invalidorder);
            case STORE:
                return mResources.getString(R.string.ticket_store);
            case STORE_OUT:
                return mResources.getString(R.string.ticket_store_out);
            case HANDOVER:
                return mResources.getString(R.string.ticket_handover);
            case CLOSING:
                return mResources.getString(R.string.ticket_closing);
            case SALERANK:
                return mResources.getString(R.string.ticket_sale_rank);
            case BANK:
                return mResources.getString(R.string.ticket_bank);
            case QUEUE:
                return mResources.getString(R.string.ticket_queue);
            case BOOKING:
                return mResources.getString(R.string.ticket_booking);
            case INVOICE:
                return mResources.getString(R.string.ticket_invoice);
            case KITCHENALL:
                return mResources.getString(R.string.ticket_kitchenorder);
            case TRANSFERTABLE:
                return mResources.getString(R.string.ticket_transfer_table);
            case MERGETABLE:
                return mResources.getString(R.string.ticket_merge_table);
            case MOVEDISH:
                return mResources.getString(R.string.ticket_move_dsih);
            case KITCHENCELL:
                return mResources.getString(R.string.ticket_hallticket);
        }
        return name;
    }

    private Integer getType() {
        return type;
    }





    public static TicketTypeEnum toEnum(Integer ticketType) {
        if (ticketType == null) {
            return null;
        }

        for (TicketTypeEnum ticketTypeEnum : TicketTypeEnum.values()) {
            if (Utils.equals(ticketTypeEnum.code, ticketType)) {
                return ticketTypeEnum;
            }
        }

        return null;
    }

}