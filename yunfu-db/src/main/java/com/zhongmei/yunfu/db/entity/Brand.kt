package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase

/**
 * <p>
 * 品牌表
 * </p>
 *

 * @since 2018-09-15
 */
@DatabaseTable(tableName = "brand")
class Brand : IdCommonEntityBase() {

    companion object {
        const val _name = "name"
        const val _logo = "logo"
        const val _contacts = "contacts"
        const val _contactsPhone = "contactsPhone"
        const val _contactsMail = "contactsMail"
        const val _commercialGroupAdress = "commercialGroupAdress"
        const val _parentId = "parentId"
        const val _type = "type"
    }

    /**
     * 商户组名称
     */
    @DatabaseField(columnName = Brand._name)
    var name: String? = null
    /**
     * 商户组logo
     */
    @DatabaseField(columnName = Brand._logo)
    var logo: String? = null
    /**
     * 商户组联系人
     */
    @DatabaseField(columnName = Brand._contacts)
    var contacts: String? = null
    /**
     * 联系人电话
     */
    @DatabaseField(columnName = Brand._contactsPhone)
    var contactsPhone: String? = null
    /**
     * 联系人邮箱
     */
    @DatabaseField(columnName = Brand._contactsMail)
    var contactsMail: String? = null
    /**
     * 商户组地址
     */
    @DatabaseField(columnName = Brand._commercialGroupAdress)
    var commercialGroupAdress: String? = null
    /**
     * 父ID=0时，代表的是集团
     */
    @DatabaseField(columnName = Brand._parentId)
    var parentId: Long? = null
    /**
     * 类型 0代表集团 1代表公司
     */
    @DatabaseField(columnName = Brand._type)
    var type: Integer? = null


    override fun toString(): String {
        return "Brand{" +
                ", id=" + id +
                ", name=" + name +
                ", logo=" + logo +
                ", contacts=" + contacts +
                ", contactsPhone=" + contactsPhone +
                ", contactsMail=" + contactsMail +
                ", commercialGroupAdress=" + commercialGroupAdress +
                ", parentId=" + parentId +
                ", type=" + type +
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
