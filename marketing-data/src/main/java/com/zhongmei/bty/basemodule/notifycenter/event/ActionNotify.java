package com.zhongmei.bty.basemodule.notifycenter.event;

import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.database.queue.SuccessOrFaild;
import com.zhongmei.bty.basemodule.notifycenter.enums.NotificationType;

import static android.R.attr.data;

/**
 * 通知类，用来包装主界面通知中心的新订单和打印失败等通知
 */
public class ActionNotify {

    private EntranceType entranceType;

    private NotificationType notificationType;

    private String ticketName;

    private String printerName;

    private String ip;

    public ActionNotify(NotificationType notificationType) {
        entranceType = EntranceType.DINNER;// 只有正餐有后厨打印失败，暂定为默认正餐入口
        this.notificationType = notificationType;
    }

    public ActionNotify(EntranceType entranceType, NotificationType type) {
        this.entranceType = entranceType;
        this.notificationType = type;
    }

    public EntranceType getEntranceType() {
        return entranceType;
    }

    public void setEntranceType(EntranceType entranceType) {
        this.entranceType = entranceType;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType type) {
        this.notificationType = type;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
