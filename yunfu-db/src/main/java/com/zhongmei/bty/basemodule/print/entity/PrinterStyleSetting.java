package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * PrinterStyleSetting is a ORMLite bean type. Corresponds to the database table "printer_style_setting"
 */
@DatabaseTable(tableName = "printer_style_setting")
public class PrinterStyleSetting extends UuidEntityBase {
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "printer_style_setting"
     */
    public interface $ extends UuidEntityBase.$ {

        /**
         * id
         */
        public static final String id = "id";

        /**
         * printer_type
         */
        public static final String printerType = "printer_type";

        /**
         * style_id
         */
        public static final String styleId = "style_id";

        /**
         * server_create_time
         */
        public static final String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        public static final String serverUpdateTime = "server_update_time";

        /**
         * status_flag
         */
        public static final String statusFlag = "status_flag";

        /**
         * brand_identy
         */
        public static final String brandIdenty = "brand_identy";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

        /**
         * device_identy
         */
        public static final String deviceIdenty = "device_identy";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * creator_name
         */
        // public static final String creatorName = "creator_name" ;

        /**
         * creator_id
         */
        //public static final String creatorId = "creator_id" ;

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * client_create_time
         */
        //  public static final String clientCreateTime = "client_create_time";

        /**
         * client_update_time
         */
        //  public static final String clientUpdateTime = "client_update_time";

    }

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;
    
 /*   @DatabaseField(columnName = "creator_name")
    private String creatorName;*/
    
   /* @DatabaseField(columnName = "creator_id")
    private String creatorId;*/

    @DatabaseField(columnName = "updator_id")
    private String updatorId;

    @DatabaseField(columnName = "id")
    private Long id;

    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    @DatabaseField(columnName = "printer_type", canBeNull = false)
    private Integer printerType;

    @DatabaseField(columnName = "style_id", canBeNull = false)
    private Integer styleId;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;
    
/*    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;

    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;*/

    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

	/*public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}*/

    public String getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Integer getPrinterType() {
        return printerType;
    }

    public void setPrinterType(Integer printerType) {
        this.printerType = printerType;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    /**
     * public Long getClientCreateTime() {
     * return clientCreateTime;
     * }
     * <p>
     * public void setClientCreateTime(Long clientCreateTime) {
     * this.clientCreateTime = clientCreateTime;
     * }
     * <p>
     * public Long getClientUpdateTime() {
     * return clientUpdateTime;
     * }
     * <p>
     * public void setClientUpdateTime(Long clientUpdateTime) {
     * this.clientUpdateTime = clientUpdateTime;
     * }
     **/

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID.value());
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        //setClientCreateTime(System.currentTimeMillis());
        if (this instanceof ICreator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
        validateUpdate();
    }

    public void validateUpdate() {
        //	setClientUpdateTime(System.currentTimeMillis());
        setChanged(true);
        if (this instanceof IUpdator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, statusFlag, printerType, styleId, brandIdenty);
    }
}
