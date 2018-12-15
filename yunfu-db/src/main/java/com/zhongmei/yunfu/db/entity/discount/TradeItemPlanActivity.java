package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.Option;

/**
 * Model class of trade_item_plan_activity.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "trade_item_plan_activity")
public class TradeItemPlanActivity extends DataEntityBase implements Option {

    /**
     * @date：2016-5-21 下午2:26:02
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void onOption() {
        if (getUuid() == null) {
            setUuid(String.valueOf(getId()));
        }
    }

    public interface $ extends DataEntityBase.$ {
        public static final String tradeId = "trade_id";

        public static final String tradeItemId = "trade_item_id";

        public static final String ruleId = "rule_id";

        public static final String planId = "plan_id";

        public static final String tradeItemUuid = "tradeItemUuid";

        public static final String relUuid = "rel_uuid";

        public static final String relId = "rel_id";

        public static final String tradeUuid = "trade_uuid";
    }

    /**
     * 关联tradeID.
     */
    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    /**
     * 关联tradeItemID.
     */
    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    /**
     * tradeItem的uuid
     **/
    @DatabaseField(columnName = "tradeItemUuid")
    private String tradeItemUuid;

    /**
     * 营销活动ID.
     */
    @DatabaseField(columnName = "rule_id", canBeNull = false)
    private Long ruleId;

    /**
     * trade_plan_activity表的uuid
     */
    @DatabaseField(columnName = "rel_uuid")
    private String relUuid;

    /**
     * trade_plan_activity 的id
     */
    @DatabaseField(columnName = "rel_id")
    private Long relId;
    /**
     * 营销方案id. 本地使用
     */
    private Long planId;

    //只和同步请求接口时使用，服务端表未存储
    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuid;

    public String getRelUuid() {
        return relUuid;
    }

    public void setRelUuid(String relUuid) {
        this.relUuid = relUuid;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    @Override
    public String pkValue() {
        return super.pkValue() == null ? String.valueOf(getId()) : super.pkValue();
    }

    /**
     * @Title: copyTradeItemPlanActivity
     * @Description: coupy
     * @Param @param fromTradeItemPlanActivity
     * @Param @param toTradeItemPlanActivity
     * @Param @return TODO
     * @Return TradeItemPlanActivity 返回类型
     */
    public TradeItemPlanActivity copyTradeItemPlanActivity(TradeItemPlanActivity fromTradeItemPlanActivity, TradeItemPlanActivity toTradeItemPlanActivity) {
        toTradeItemPlanActivity.setId(fromTradeItemPlanActivity.getId());
        toTradeItemPlanActivity.setTradeId(fromTradeItemPlanActivity.getTradeId());
        toTradeItemPlanActivity.setTradeUuid(fromTradeItemPlanActivity.getTradeUuid());
        toTradeItemPlanActivity.setTradeItemId(fromTradeItemPlanActivity.getTradeItemId());
        toTradeItemPlanActivity.setTradeItemUuid(fromTradeItemPlanActivity.getTradeItemUuid());
        toTradeItemPlanActivity.setRuleId(fromTradeItemPlanActivity.getRuleId());
        toTradeItemPlanActivity.setPlanId(fromTradeItemPlanActivity.getPlanId());
        toTradeItemPlanActivity.setBrandIdenty(fromTradeItemPlanActivity.getBrandIdenty());
        toTradeItemPlanActivity.setShopIdenty(fromTradeItemPlanActivity.getShopIdenty());
        toTradeItemPlanActivity.setRelId(fromTradeItemPlanActivity.getRelId());
        toTradeItemPlanActivity.setRelUuid(fromTradeItemPlanActivity.getRelUuid());
        toTradeItemPlanActivity.setStatusFlag(fromTradeItemPlanActivity.getStatusFlag());
        toTradeItemPlanActivity.setServerCreateTime(fromTradeItemPlanActivity.getServerCreateTime());
        toTradeItemPlanActivity.setServerUpdateTime(fromTradeItemPlanActivity.getServerUpdateTime());
        return toTradeItemPlanActivity;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(getShopIdenty(), ruleId);
    }
}
