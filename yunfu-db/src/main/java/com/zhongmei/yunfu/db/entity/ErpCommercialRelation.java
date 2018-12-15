package com.zhongmei.yunfu.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.IEntity;

/**
 * @Date：2016-2-16 下午4:40:32
 * @Description: Erp配置费率表记录
 */
@DatabaseTable(tableName = "erp_commercial_relation")
public class ErpCommercialRelation extends EntityBase<Long> {

    private static final long serialVersionUID = 1L;

    public interface $ {

        /**
         * id
         */
        String id = "id";

        /**
         * localCreateDateTime
         */
        String localCreateDateTime = "localCreateDateTime";

        /**
         * localModifyDateTime
         */
        String localModifyDateTime = "localModifyDateTime";

        /**
         * 商户id
         */
        String commercialId = "commercial_id";

        /**
         * 部门Id
         */
        String deptId = "dept_id";

        /**
         * 商户状态：1:已开通,2:服务中,3:超租期，-1:已禁用'
         */
        String commercialStatus = "commercial_status";

        /**
         * crm商户id
         */
        String crmCommercialId = "crm_commercial_id";

        /**
         * 是否开通熟客
         */
        String isShuke = "is_shuke";
    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;
	
	/*@DatabaseField(columnName = "_id", canBeNull = false)
	private Long _id;*/

    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "commercial_type")
    private Long commercialType;

    @DatabaseField(columnName = "station_id")
    private Long stationId;

    @DatabaseField(columnName = "dept_id")
    private Long deptId;

    @DatabaseField(columnName = "commercial_status")
    private Long commercialStatus;

    @DatabaseField(columnName = "install_status")
    private Long installStatus;

    @DatabaseField(columnName = "sales_id")
    private Long salesId;

    @DatabaseField(columnName = "real_inst_time")
    private Long realInstTime;

    @DatabaseField(columnName = "operation_user_id")
    private Long operationUserId;

    @DatabaseField(columnName = "contract_first_time")
    private Long contractFirstTime;

    @DatabaseField(columnName = "contract_last_time")
    private Long contractLastTime;

    @DatabaseField(columnName = "last_operator_id")
    private Long lastOperatorId;

    @DatabaseField(columnName = "installer_user_id")
    private Long installerUserId;

    @DatabaseField(columnName = "is_free")
    private Long isFree;

    @DatabaseField(columnName = "crm_commercial_id")
    private Long crmCommercialId;

    @DatabaseField(columnName = "is_unionpay")
    private Long isUnionpay;

    @DatabaseField(columnName = "bank_channel_id")
    private Long bankChannelId;

    @DatabaseField(columnName = "weixin_rates")
    private Double weixinRates;

    @DatabaseField(columnName = "bank_rates")
    private Double bankRates;

    @DatabaseField(columnName = "alipay_rates")
    private Double alipayRates;

    @DatabaseField(columnName = "baidu_rates")
    private Double baiduRates;

    @DatabaseField(columnName = "localCreateDateTime")
    private Long localCreateDateTime;

    @DatabaseField(columnName = "localModifyDateTime")
    private Long localModifyDateTime;

    @DatabaseField(columnName = "is_shuke")
    private Long isShuke;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
	/*public Long get_id() {
		return _id;
	}
	
	public void set_id(Long _id) {
		this._id = _id;
	}*/

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return localModifyDateTime;
    }

    public Long getLocalCreateDateTime() {
        return localCreateDateTime;
    }

    public void setLocalCreateDateTime(Long localCreateDateTime) {
        this.localCreateDateTime = localCreateDateTime;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Long getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(Long commercialType) {
        this.commercialType = commercialType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getCommercialStatus() {
        return commercialStatus;
    }

    public void setCommercialStatus(Long commercialStatus) {
        this.commercialStatus = commercialStatus;
    }

    public Long getInstallStatus() {
        return installStatus;
    }

    public void setInstallStatus(Long installStatus) {
        this.installStatus = installStatus;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public Long getRealInstTime() {
        return realInstTime;
    }

    public void setRealInstTime(Long realInstTime) {
        this.realInstTime = realInstTime;
    }

    public Long getOperationUserId() {
        return operationUserId;
    }

    public void setOperationUserId(Long operationUserId) {
        this.operationUserId = operationUserId;
    }

    public Long getContractFirstTime() {
        return contractFirstTime;
    }

    public void setContractFirstTime(Long contractFirstTime) {
        this.contractFirstTime = contractFirstTime;
    }

    public Long getContractLastTime() {
        return contractLastTime;
    }

    public void setContractLastTime(Long contractLastTime) {
        this.contractLastTime = contractLastTime;
    }

    public Long getLastOperatorId() {
        return lastOperatorId;
    }

    public void setLastOperatorId(Long lastOperatorId) {
        this.lastOperatorId = lastOperatorId;
    }

    public Long getInstallerUserId() {
        return installerUserId;
    }

    public void setInstallerUserId(Long installerUserId) {
        this.installerUserId = installerUserId;
    }

    public Long getIsFree() {
        return isFree;
    }

    public void setIsFree(Long isFree) {
        this.isFree = isFree;
    }

    public Long getCrmCommercialId() {
        return crmCommercialId;
    }

    public void setCrmCommercialId(Long crmCommercialId) {
        this.crmCommercialId = crmCommercialId;
    }

    public Long getIsUnionpay() {
        return isUnionpay;
    }

    public void setIsUnionpay(Long isUnionpay) {
        this.isUnionpay = isUnionpay;
    }

    public Long getBankChannelId() {
        return bankChannelId;
    }

    public void setBankChannelId(Long bankChannelId) {
        this.bankChannelId = bankChannelId;
    }

    public Double getWeixinRates() {
        return weixinRates;
    }

    public void setWeixinRates(Double weixinRates) {
        this.weixinRates = weixinRates;
    }

    public Double getBankRates() {
        return bankRates;
    }

    public void setBankRates(Double bankRates) {
        this.bankRates = bankRates;
    }

    public Double getAlipayRates() {
        return alipayRates;
    }

    public void setAlipayRates(Double alipayRates) {
        this.alipayRates = alipayRates;
    }

    public Double getBaiduRates() {
        return baiduRates;
    }

    public void setBaiduRates(Double baiduRates) {
        this.baiduRates = baiduRates;
    }

    public Long getIsShuke() {
        return isShuke;
    }

    public void setIsShuke(Long isShuke) {
        this.isShuke = isShuke;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pkValue() == null) ? 0 : pkValue().hashCode());
        return result;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IEntity<?> other = (IEntity<?>) obj;
        if (pkValue() == null) {
            if (other.pkValue() != null)
                return false;
        } else if (!pkValue().equals(other.pkValue()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pkValue=" + pkValue() + "]";
    }
}
