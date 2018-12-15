package com.zhongmei.yunfu.orm;

import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;

import java.util.ArrayList;

/**
 * 美业独有数据库类
 * Created by demo on 2018/12/15
 */

public class BeautyDbHelperUtil {

    public static void initTables(ArrayList<Class<? extends IEntity<?>>> tables) {
//        tables.add(TradeItemUser.class);
        tables.add(TradePrivilegeLimitNumCard.class);
        tables.add(TradePrivilegeApplet.class);
    }
}
