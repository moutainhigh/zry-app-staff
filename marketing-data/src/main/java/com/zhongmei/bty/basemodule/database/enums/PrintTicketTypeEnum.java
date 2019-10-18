package com.zhongmei.bty.basemodule.database.enums;

import android.text.TextUtils;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum PrintTicketTypeEnum {

    CUSTOMER(R.string.ticket_note),
    PRECASH(R.string.ticket_ad_closing),
    CASH(R.string.ticket_closed),
    CHECK_OUT(R.string.ticket_xiaofeiqingdan),
    DEPOSIT(R.string.ticket_deposit),
    REFUND(R.string.ticket_refundnote),
    CANCEL(R.string.ticket_invalidorder),
    STORE(R.string.ticket_store),
    STORE_OUT(R.string.ticket_store_out),
    HANDOVER(R.string.ticket_handover),
    CLOSING(R.string.ticket_closing),
    SALERANK(R.string.ticket_sale_rank),
    BANK(R.string.ticket_bank),
    QUEUE(R.string.ticket_queue),
    BOOKING(R.string.ticket_booking),
    INVOICE(R.string.ticket_invoice),

    KITCHENALL(R.string.ticket_kitchenorder),
    TRANSFERTABLE(R.string.ticket_transfer_table),
    MERGETABLE(R.string.ticket_merge_table),
    MOVEDISH(R.string.ticket_move_dsih),
    KITCHENCELL(R.string.ticket_hallticket),
    WAKEUP(R.string.ticket_kitchen_wakeup),
    RISE(R.string.ticket_kitchen_rise),
    REMIND(R.string.ticket_kitchen_remind),
    WAKEUP_CANCEL(R.string.ticket_kitchen_cancel_wakeup),
    RISE_CANCEL(R.string.ticket_kitchen_cancel_rise),

    LABEL(R.string.label);



    private int valueResId;

    PrintTicketTypeEnum(Integer valueResId) {
        this.valueResId = valueResId;
    }

    public String value() {
        if (valueResId > 0) {
            return BaseApplication.sInstance.getString(valueResId);
        } else {
            return BaseApplication.sInstance.getString(R.string.commonmodule_dialog_other);
        }
    }

    public static PrintTicketTypeEnum convert(TicketTypeEnum ticketType) {
        if (ticketType == null) {
            return null;
        }

        for (PrintTicketTypeEnum printTicketEnum : PrintTicketTypeEnum.values()) {
            if (TextUtils.equals(printTicketEnum.name(), ticketType.name())) {
                return printTicketEnum;
            }
        }

        return null;
    }

}
