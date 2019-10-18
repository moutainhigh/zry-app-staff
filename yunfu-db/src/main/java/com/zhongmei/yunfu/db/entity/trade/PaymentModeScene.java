package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "payment_mode_scene")
public class PaymentModeScene extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String shopErpModeId = "shop_erp_mode_id";

        public static final String sceneCode = "scene_code";

    }


    @DatabaseField(columnName = "shop_erp_mode_id")    private Long shopErpModeId;

    @DatabaseField(columnName = "scene_code")    private Integer sceneCode;


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
