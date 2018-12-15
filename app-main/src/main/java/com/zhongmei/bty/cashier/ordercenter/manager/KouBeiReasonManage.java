package com.zhongmei.bty.cashier.ordercenter.manager;

import static com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType.REFUSE_RETURN;
import static com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType.TRADE_REFUSED;
import static com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType.TRADE_REPEATED;
import static com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType.TRADE_RETURNED;

import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 2018.06.09.
 */
public class KouBeiReasonManage {

    public static KouBeiReasonManage newInstance() {
        return new KouBeiReasonManage();
    }

    private KouBeiReasonManage() {
    }

    public List<ReasonSetting> load(ReasonType reasonType) {
        ArrayList<ReasonSetting> reasonSettings = new ArrayList<>();
        if (reasonType == TRADE_REFUSED) {
            for (KouBeiReason entry : KouBeiReason.values()) {
                ReasonSetting reasonSetting = new ReasonSetting();
                reasonSetting.setContent(entry.content);
                reasonSetting.setSort(ReasonSource.KOUBEI.value());
                reasonSetting.setSource(ReasonSource.KOUBEI);
                reasonSetting.setContentCode(entry.code);
                reasonSettings.add(reasonSetting);
            }
        } else if (reasonType == TRADE_REPEATED || reasonType == TRADE_RETURNED) {
            for (KouBeiReason entry : KouBeiReason.values()) {
                if (entry == KouBeiReason.RECEIVE_TIMEOUT) {
                    continue;
                }
                ReasonSetting reasonSetting = new ReasonSetting();
                reasonSetting.setContent(entry.content);
                reasonSetting.setSort(ReasonSource.KOUBEI.value());
                reasonSetting.setSource(ReasonSource.KOUBEI);
                reasonSetting.setContentCode(entry.code);
                reasonSettings.add(reasonSetting);
            }
        } else if (reasonType == REFUSE_RETURN) {
            ReasonSetting reasonSetting = new ReasonSetting();
            reasonSetting.setContent("用户已取餐");
            reasonSetting.setSort(ReasonSource.KOUBEI.value());
            reasonSetting.setSource(ReasonSource.KOUBEI);
            reasonSetting.setContentCode("RECEIVE_TIMEOUT");
            reasonSettings.add(reasonSetting);
            reasonSetting = new ReasonSetting();
            reasonSetting.setContent("和用户协商一致，线下解决");
            reasonSetting.setSort(ReasonSource.KOUBEI.value());
            reasonSetting.setSource(ReasonSource.KOUBEI);
            reasonSetting.setContentCode("UNDER_LINE_NEGOTIATION");
            reasonSettings.add(reasonSetting);
            reasonSetting = new ReasonSetting();
            reasonSetting.setContent("其他原因");
            reasonSetting.setSort(ReasonSource.KOUBEI.value());
            reasonSetting.setSource(ReasonSource.KOUBEI);
            reasonSetting.setContentCode("OTHER_REASON");
            reasonSettings.add(reasonSetting);
        }
        return reasonSettings;
    }

    enum KouBeiReason {

        RECEIVE_TIMEOUT("RECEIVE_TIMEOUT", "超时未接单"),
        BUSY("BUSY", "店铺太忙，无法接单"),
        DUPLICATE_ORDER("DUPLICATE_ORDER", "重复订单"),
        SHOP_CLOSE("SHOP_CLOSE", "店铺已打烊"),
        SELL_OUT("SELL_OUT", "菜品售完"),
        OTHER_REASON("OTHER_REASON", "其他原因");

        String code;
        String content;

        KouBeiReason(String code, String content) {
            this.code = code;
            this.content = content;
        }
    }

}
