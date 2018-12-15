package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.CommonEntityBaseK

/**
 * <p>
 * 商户信息表
 * </p>
 *

 * @since 2018-09-15
 */
@DatabaseTable(tableName = "commercial")
class Commercial : CommonEntityBaseK<Long>() {

    companion object {
        const val _commercialID = "commercialID"
        const val _commercialName = "commercialName"
        const val _commercialContact = "commercialContact"
        const val _commercialPhone = "commercialPhone"
        const val _commercialAdress = "commercialAdress"
        const val _commercialDesc = "commercialDesc"
        const val _commercialLogo = "commercialLogo"
        const val _status = "status"
        const val _brandID = "brandID"
        const val _branchName = "branchName"
        const val _opentime = "opentime"
        const val _consumePerson = "consumePerson"
        const val _deviceType = "deviceType"
        const val _longitude = "longitude"
        const val _latitude = "latitude"
    }

    override fun pkValue(): Long {
        return commercialID!!
    }

    /**
     * 主键id
     */
    @DatabaseField(columnName = Commercial._commercialID, id = true, canBeNull = false)
    var commercialID: Long? = null
    /**
     * 门店名称
     */
    @DatabaseField(columnName = Commercial._commercialName)
    var commercialName: String? = null
    /**
     * 联系人
     */
    @DatabaseField(columnName = Commercial._commercialContact)
    var commercialContact: String? = null
    /**
     * 联系电话
     */
    @DatabaseField(columnName = Commercial._commercialPhone)
    var commercialPhone: String? = null
    /**
     * 门店地址
     */
    @DatabaseField(columnName = Commercial._commercialAdress)
    var commercialAdress: String? = null
    /**
     * 商户描述
     */
    @DatabaseField(columnName = Commercial._commercialDesc)
    var commercialDesc: String? = null
    /**
     * LOGO对应的URL
     */
    @DatabaseField(columnName = Commercial._commercialLogo)
    var commercialLogo: String? = null
    /**
     * 商户状态 0-可用 -1-不可用,1-预装
     */
    @DatabaseField(columnName = Commercial._status)
    var status: Integer? = null
    /**
     * 所属品牌id
     */
    @DatabaseField(columnName = Commercial._brandID)
    var brandID: Long? = null
    /**
     * 分店名称
     */
    @DatabaseField(columnName = Commercial._branchName)
    var branchName: String? = null
    /**
     * 营业时间
     */
    @DatabaseField(columnName = Commercial._opentime)
    var opentime: String? = null
    /**
     * 人均消费
     */
    @DatabaseField(columnName = Commercial._consumePerson)
    var consumePerson: String? = null
    /**
     * 商家设备类型，0:pad 1:phone
     */
    @DatabaseField(columnName = Commercial._deviceType)
    var deviceType: Integer? = null
    /**
     * 经度
     */
    @DatabaseField(columnName = Commercial._longitude)
    var longitude: String? = null
    /**
     * 纬度
     */
    @DatabaseField(columnName = Commercial._latitude)
    var latitude: String? = null


    override fun toString(): String {
        return "Commercial{" +
                ", commercialID=" + commercialID +
                ", commercialName=" + commercialName +
                ", commercialContact=" + commercialContact +
                ", commercialPhone=" + commercialPhone +
                ", commercialAdress=" + commercialAdress +
                ", commercialDesc=" + commercialDesc +
                ", commercialLogo=" + commercialLogo +
                ", status=" + status +
                ", brandID=" + brandID +
                ", branchName=" + branchName +
                ", opentime=" + opentime +
                ", consumePerson=" + consumePerson +
                ", deviceType=" + deviceType +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", statusFlag=" + statusFlag +
                ", creatorId=" + creatorId +
                ", creatorName=" + creatorName +
                ", updatorId=" + updatorId +
                ", updatorName=" + updatorName +
                ", serverCreateTime=" + serverCreateTime +
                ", serverUpdateTime=" + serverUpdateTime +
                "}"
    }
}
