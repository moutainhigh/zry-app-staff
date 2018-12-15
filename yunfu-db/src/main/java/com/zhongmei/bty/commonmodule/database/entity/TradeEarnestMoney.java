package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;

/**
 * @Date： 2018/6/12
 * @Description:预订金记录
 * @Version: 1.0
 */
@DatabaseTable(tableName = "trade_earnest_money")
public class TradeEarnestMoney extends IdEntityBase {
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "delivery_order"
     */
    public interface $ extends IdEntityBase.$ {
        String tradeId = "tradeId";
        String brandIdenty = "brandIdenty";
        String shopIdenty = "shopIdenty";
        String statusFlag = "statusFlag";
        String serverCreateTime = "serverCreateTime";
        String serverUpdateTime = "serverUpdateTime";
        String creatorId = "creatorId";
        String creatorName = "creatorName";
        String earnestMoney = "earnestMoney";
        String payModeId = "payModeId";
    }

    @DatabaseField(columnName = $.tradeId)
    private Long tradeId;  //是	订单ID

    @DatabaseField(columnName = $.brandIdenty)
    private Long brandIdenty;  //是	品牌ID

    @DatabaseField(columnName = $.shopIdenty)
    private Long shopIdenty;  //是	门店ID

    @DatabaseField(columnName = $.statusFlag)
    private Integer statusFlag;  //是	1:valid:有效；2:invalid:无效

    @DatabaseField(columnName = $.serverCreateTime)
    private Long serverCreateTime;  //是	服务端创建时间

    @DatabaseField(columnName = $.serverUpdateTime)
    private Long serverUpdateTime;  //是	服务端最后修改时间

    @DatabaseField(columnName = $.creatorId)
    private Long creatorId;  //是	创建者，创建此记录的系统用户

    @DatabaseField(columnName = $.creatorName)
    private String creatorName;  //是	创建者姓名

    @DatabaseField(columnName = $.earnestMoney)
    private BigDecimal earnestMoney;  //是	定金金额

    @DatabaseField(columnName = $.payModeId)
    private Long payModeId;  //是	支付方式ID(cashTypeId)：-1:会员卡余额,-2:优惠劵(废弃),-3:现金,-4:银行卡,-5:微信支付,-6:支付宝,-7:百度钱包,-8:百度直达号(停用),-9:积分抵现(废弃),-10:百度地图(停用),-11:银联刷卡,-12:百糯到店付(停用),-13:百度外卖(停用),-14:饿了么(停用),-15:实体卡支付,-16:大众点评(停用),-17:美团外卖(停用),-18:点评团购券(停用),-19:点评闪惠(停用)，-20:临时卡余额 -21:糯米点菜(停用),-22:第三方C端，-23:美团闪惠,-24:美团团购券,-25:钱包生活,-26:百度糯米团购券,-27:乐富支付,-28:熟客,-29:金诚,-30:金城充值卡,-31:支付通支付,-32:工行银行卡支付,-33:钱包生活银行卡支付,-34:锴乐,-35:京东到家c端,-36:口碑团购卷,-37:银联云闪付,-38:工商e支付,-39:美大点餐 ,-40:多码一扫

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(YesOrNo.YES, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public BigDecimal getEarnestMoney() {
        return earnestMoney;
    }

    public Long getPayModeId() {
        return payModeId;
    }


    public Long getTradeId() {
        return tradeId;
    }
}
