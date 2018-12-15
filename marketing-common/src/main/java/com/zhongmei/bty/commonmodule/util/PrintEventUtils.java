package com.zhongmei.bty.commonmodule.util;

import com.zhongmei.bty.commonmodule.database.entity.PrinterCashierTicket;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.bty.commonmodule.event.ActionPrinterCashierTicketChanged;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */

public class PrintEventUtils {

    public static <T extends IEntity<?>> void printConfigChangeEventBus(T entity) {
        if (entity instanceof PrinterCashierTicket) {
            ActionPrinterCashierTicketChanged action = new ActionPrinterCashierTicketChanged();
            EventBus.getDefault().post(action);
        }
    }
}
