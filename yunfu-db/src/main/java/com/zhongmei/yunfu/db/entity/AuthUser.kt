package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase
import java.util.*

/**
 * <p>
 * 用户表
 * </p>
 *

 * @since 2018-09-15
 */
@DatabaseTable(tableName = "auth_user")
class AuthUser : IdCommonEntityBase() {

    companion object {
        const val _roleId = "roleId"
        const val _name = "name"
        const val _gender = "gender"
        const val _birthday = "birthday"
        const val _identityCard = "identityCard"
        const val _education = "education"
        const val _graduateSchool = "graduateSchool"
        const val _intoWorkDate = "intoWorkDate"
        const val _isMarry = "isMarry"
        const val _mobile = "mobile"
        const val _email = "email"
        const val _qq = "qq"
        const val _address = "address"
        const val _icon = "icon"
        const val _ecName = "ecName"
        const val _ecRelation = "ecRelation"
        const val _ecMobile = "ecMobile"
        const val _ecMobileReserve = "ecMobileReserve"
        const val _jobNumber = "jobNumber"
        const val _jobEmployeeType = "jobEmployeeType"
        const val _jobEntryTime = "jobEntryTime"
        const val _jobPositiveTime = "jobPositiveTime"
        const val _jobPosition = "jobPosition"
        const val _jobGrade = "jobGrade"
        const val _jobAddress = "jobAddress"
        const val _salaryCalcMode = "salaryCalcMode"
        const val _salaryBase = "salaryBase"
        const val _salaryPost = "salaryPost"
        const val _account = "account"
        const val _password = "password"
        const val _passwordNum = "passwordNum"
        const val _sourceFlag = "sourceFlag"
        const val _salt = "salt"
        const val _shopIdenty = "shopIdenty"
        const val _brandIdenty = "brandIdenty"
        const val _assignedGroup = "assignedGroup"
        const val _assignedId = "assignedId"
        const val _enabledFlag = "enabledFlag"
    }

    /**
     * 角色id : 角色id
     */
    @DatabaseField(columnName = AuthUser._roleId)
    var roleId: Long? = null
    /**
     * 用户名称 : 用户名称
     */
    @DatabaseField(columnName = AuthUser._name)
    var name: String? = null
    /**
     * 性别 : 未知：-1 ；女：0； 男：1
     */
    @DatabaseField(columnName = AuthUser._gender)
    var gender: Integer? = null
    /**
     * 生日 : 生日
     */
    @DatabaseField(columnName = AuthUser._birthday)
    var birthday: Long? = null
    /**
     * 身份证
     */
    @DatabaseField(columnName = AuthUser._identityCard)
    var identityCard: String? = null
    /**
     * 学历
     */
    @DatabaseField(columnName = AuthUser._education)
    var education: String? = null
    /**
     * 毕业学校
     */
    @DatabaseField(columnName = AuthUser._graduateSchool)
    var graduateSchool: String? = null
    /**
     * 参数工作时间
     */
    @DatabaseField(columnName = AuthUser._intoWorkDate)
    var intoWorkDate: String? = null
    /**
     * 婚姻状况 1：已婚  2：未婚
     */
    @DatabaseField(columnName = AuthUser._isMarry)
    var isMarry: String? = null
    /**
     * 手机号
     */
    @DatabaseField(columnName = AuthUser._mobile)
    var mobile: String? = null
    /**
     * 邮箱
     */
    @DatabaseField(columnName = AuthUser._email)
    var email: String? = null
    /**
     * QQ : QQ账号
     */
    @DatabaseField(columnName = AuthUser._qq)
    var qq: Long? = null
    /**
     * 地址
     */
    @DatabaseField(columnName = AuthUser._address)
    var address: String? = null
    /**
     * 头像
     */
    @DatabaseField(columnName = AuthUser._icon)
    var icon: String? = null
    /**
     * 紧急联系人-姓名
     */
    @DatabaseField(columnName = AuthUser._ecName)
    var ecName: String? = null
    /**
     * 紧急联系人-关系
     */
    @DatabaseField(columnName = AuthUser._ecRelation)
    var ecRelation: String? = null
    /**
     * 紧急联系人-电话
     */
    @DatabaseField(columnName = AuthUser._ecMobile)
    var ecMobile: String? = null
    /**
     * 紧急联系人-备用电话
     */
    @DatabaseField(columnName = AuthUser._ecMobileReserve)
    var ecMobileReserve: String? = null
    /**
     * 工号
     */
    @DatabaseField(columnName = AuthUser._jobNumber)
    var jobNumber: String? = null
    /**
     * 员工类型   1.正式员工  2、试用期员工 3、外聘员工  4、停用
     */
    @DatabaseField(columnName = AuthUser._jobEmployeeType)
    var jobEmployeeType: String? = null
    /**
     * 入职时间
     */
    @DatabaseField(columnName = AuthUser._jobEntryTime)
    var jobEntryTime: String? = null
    /**
     * 转正时间
     */
    @DatabaseField(columnName = AuthUser._jobPositiveTime)
    var jobPositiveTime: String? = null
    /**
     * 职位
     */
    @DatabaseField(columnName = AuthUser._jobPosition)
    var jobPosition: String? = null
    /**
     * 职级
     */
    @DatabaseField(columnName = AuthUser._jobGrade)
    var jobGrade: String? = null
    /**
     * 工作地址
     */
    @DatabaseField(columnName = AuthUser._jobAddress)
    var jobAddress: String? = null
    /**
     * 薪资-计算方式
     */
    @DatabaseField(columnName = AuthUser._salaryCalcMode)
    var salaryCalcMode: String? = null
    /**
     * 薪资-基本工资
     */
    @DatabaseField(columnName = AuthUser._salaryBase)
    var salaryBase: String? = null
    /**
     * 薪资-岗位工资
     */
    @DatabaseField(columnName = AuthUser._salaryPost)
    var salaryPost: String? = null
    /**
     * 登陆账号 : 登陆账号 限制未数字
     */
    @DatabaseField(columnName = AuthUser._account)
    var account: String? = null
    /**
     * 密码 限制为数字
     */
    @DatabaseField(columnName = AuthUser._password)
    var password: String? = null
    /**
     * 数字型密码
     */
    @DatabaseField(columnName = AuthUser._passwordNum)
    var passwordNum: String? = null
    /**
     * 1:用户(b.kry用户创建,) 2:系统(b.kry不可见,系统自动创建,如营销发布员) 3:品牌(只读模式,系统自动创建如admin) 4:erp同步用户
     */
    @DatabaseField(columnName = AuthUser._sourceFlag)
    var sourceFlag: Integer? = null
    /**
     * 盐值 : 用户创建时随机生成
     */
    @DatabaseField(columnName = AuthUser._salt)
    var salt: String? = null
    /**
     * 如果归属门店，则为门店id；如果归属品牌，则为品牌id.
    新的权限体系下，全部为品牌id
    就是登录标示!!仅登录使用
     */
    @DatabaseField(columnName = AuthUser._shopIdenty)
    var shopIdenty: Long? = null
    /**
     * 品牌标识
     */
    @DatabaseField(columnName = AuthUser._brandIdenty)
    var brandIdenty: Long? = null
    /**
     * 账号归属：1：品牌，2：门店，3：配送站
     */
    @DatabaseField(columnName = AuthUser._assignedGroup)
    var assignedGroup: Integer? = null
    /**
     * 账号归属ID
     */
    @DatabaseField(columnName = AuthUser._assignedId)
    var assignedId: Long? = null
    /**
     * 启用停用标识 : 1:启用;2:停用
     */
    @DatabaseField(columnName = AuthUser._enabledFlag)
    var enabledFlag: Integer? = null


    override fun toString(): String {
        return "AuthUser{" +
                ", id=" + id +
                ", roleId=" + roleId +
                ", name=" + name +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", identityCard=" + identityCard +
                ", education=" + education +
                ", graduateSchool=" + graduateSchool +
                ", intoWorkDate=" + intoWorkDate +
                ", isMarry=" + isMarry +
                ", mobile=" + mobile +
                ", email=" + email +
                ", qq=" + qq +
                ", address=" + address +
                ", icon=" + icon +
                ", ecName=" + ecName +
                ", ecRelation=" + ecRelation +
                ", ecMobile=" + ecMobile +
                ", ecMobileReserve=" + ecMobileReserve +
                ", jobNumber=" + jobNumber +
                ", jobEmployeeType=" + jobEmployeeType +
                ", jobEntryTime=" + jobEntryTime +
                ", jobPositiveTime=" + jobPositiveTime +
                ", jobPosition=" + jobPosition +
                ", jobGrade=" + jobGrade +
                ", jobAddress=" + jobAddress +
                ", salaryCalcMode=" + salaryCalcMode +
                ", salaryBase=" + salaryBase +
                ", salaryPost=" + salaryPost +
                ", account=" + account +
                ", password=" + password +
                ", passwordNum=" + passwordNum +
                ", sourceFlag=" + sourceFlag +
                ", salt=" + salt +
                ", shopIdenty=" + shopIdenty +
                ", brandIdenty=" + brandIdenty +
                ", assignedGroup=" + assignedGroup +
                ", assignedId=" + assignedId +
                ", enabledFlag=" + enabledFlag +
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
