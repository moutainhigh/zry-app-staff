package com.zhongmei.yunfu.orm;

import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;

import java.util.ArrayList;



public class BeautyDbHelperUtil {

    public static void initTables(ArrayList<Class<? extends IEntity<?>>> tables) {
        tables.add(TradePrivilegeLimitNumCard.class);
        tables.add(TradePrivilegeApplet.class);
    }
}
