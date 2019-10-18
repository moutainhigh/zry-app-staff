package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade_buffet_people")
public class TradeBuffetPeople extends BasicEntityBase implements Cloneable, ICreator, IUpdator {

    public interface $ extends BasicEntityBase.$ {
        public static final String tradeId = "trade_id";
        public static final String shopIdenty = "shop_identy";
        public static final String carteNormsId = "carte_norms_id";
        public static final String carteNormsName = "carte_norms_name";
        public static final String cartePrice = "carte_price";
        public static final String peopleCount = "people_count";
        public static final String clientCreate_time = "client_create_time";
        public static final String clientUpdateTime = "client_update_time";
        public static final String creatorId = "creator_id";
        public static final String creatorName = "creator_name";
        public static final String updatorId = "updator_id";
        public static final String updatorName = "updator_name";
        public static final String tradeUuid = "trade_uuid";

    }

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "carte_norms_id")
    private Long carteNormsId;

    @DatabaseField(columnName = "carte_norms_name")
    private String carteNormsName;

    @DatabaseField(columnName = "carte_price")
    private BigDecimal cartePrice;

    @DatabaseField(columnName = "people_count")
    private BigDecimal peopleCount;

    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;

    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;


    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getCarteNormsId() {
        return carteNormsId;
    }

    public void setCarteNormsId(Long carteNormsId) {
        this.carteNormsId = carteNormsId;
    }

    public String getCarteNormsName() {
        return carteNormsName;
    }

    public void setCarteNormsName(String carteNormsName) {
        this.carteNormsName = carteNormsName;
    }

    public BigDecimal getCartePrice() {
        return cartePrice;
    }

    public void setCartePrice(BigDecimal cartePrice) {
        this.cartePrice = cartePrice;
    }

    public BigDecimal getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(BigDecimal peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public TradeBuffetPeople clone() {
        TradeBuffetPeople tradeBuffetPeople = null;
        try {
            tradeBuffetPeople = (TradeBuffetPeople) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tradeBuffetPeople;
    }

    @Override
    public void validateCreate() {
        super.validateCreate();
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(ShopInfoCfg.getInstance().getShopInfo().getBrandIdLong());
        setShopIdenty(ShopInfoCfg.getInstance().getShopInfo().getShopIdLong());
        setClientCreateTime(System.currentTimeMillis());
    }

    @Override
    public void validateUpdate() {
        super.validateUpdate();
        setClientUpdateTime(System.currentTimeMillis());
    }
}
