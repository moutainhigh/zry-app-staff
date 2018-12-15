package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * Created by demo on 2018/12/15
 * 支付方式场景关联表
 */
@DatabaseTable(tableName = "payment_mode_scene")
public class PaymentModeScene extends BasicEntityBase implements ICreator, IUpdator {
    /* public static final Integer SCENE_CODE_CHARGE = 1;//充值
     public static final Integer SCENE_CODE_SHOP = 2;//购物*/
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "payment_mode_scene"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * brand_mode_id
         */
        public static final String shopErpModeId = "shop_erp_mode_id";

        public static final String sceneCode = "scene_code";

    }

    /**
     * 支付方式Id
     */
    @DatabaseField(columnName = "shop_erp_mode_id")//与payment_mode_shop表的erpModeId对应,
    private Long shopErpModeId;
    /**
     * 场景编码
     */
    @DatabaseField(columnName = "scene_code")//1 为储值充值 ；2  销售支付',
    private Integer sceneCode;


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getShopErpModeId() {
        return shopErpModeId;
    }

    public void setShopErpModeId(Long shopErpModeId) {
        this.shopErpModeId = shopErpModeId;
    }

    public Integer getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(Integer sceneCode) {
        this.sceneCode = sceneCode;
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
}
