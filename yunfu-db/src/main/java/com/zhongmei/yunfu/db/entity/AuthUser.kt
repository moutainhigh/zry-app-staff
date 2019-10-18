package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase
import java.util.*
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

    @DatabaseField(columnName = AuthUser._roleId)
    var roleId: Long? = null
    @DatabaseField(columnName = AuthUser._name)
    var name: String? = null
    @DatabaseField(columnName = AuthUser._gender)
    var gender: Integer? = null
    @DatabaseField(columnName = AuthUser._birthday)
    var birthday: Long? = null
    @DatabaseField(columnName = AuthUser._identityCard)
    var identityCard: String? = null
    @DatabaseField(columnName = AuthUser._education)
    var education: String? = null
    @DatabaseField(columnName = AuthUser._graduateSchool)
    var graduateSchool: String? = null
    @DatabaseField(columnName = AuthUser._intoWorkDate)
    var intoWorkDate: String? = null
    @DatabaseField(columnName = AuthUser._isMarry)
    var isMarry: String? = null
    @DatabaseField(columnName = AuthUser._mobile)
    var mobile: String? = null
    @DatabaseField(columnName = AuthUser._email)
    var email: String? = null
    @DatabaseField(columnName = AuthUser._qq)
    var qq: Long? = null
    @DatabaseField(columnName = AuthUser._address)
    var address: String? = null
    @DatabaseField(columnName = AuthUser._icon)
    var icon: String? = null
    @DatabaseField(columnName = AuthUser._ecName)
    var ecName: String? = null
    @DatabaseField(columnName = AuthUser._ecRelation)
    var ecRelation: String? = null
    @DatabaseField(columnName = AuthUser._ecMobile)
    var ecMobile: String? = null
    @DatabaseField(columnName = AuthUser._ecMobileReserve)
    var ecMobileReserve: String? = null
    @DatabaseField(columnName = AuthUser._jobNumber)
    var jobNumber: String? = null
    @DatabaseField(columnName = AuthUser._jobEmployeeType)
    var jobEmployeeType: String? = null
    @DatabaseField(columnName = AuthUser._jobEntryTime)
    var jobEntryTime: String? = null
    @DatabaseField(columnName = AuthUser._jobPositiveTime)
    var jobPositiveTime: String? = null
    @DatabaseField(columnName = AuthUser._jobPosition)
    var jobPosition: String? = null
    @DatabaseField(columnName = AuthUser._jobGrade)
    var jobGrade: String? = null
    @DatabaseField(columnName = AuthUser._jobAddress)
    var jobAddress: String? = null
    @DatabaseField(columnName = AuthUser._salaryCalcMode)
    var salaryCalcMode: String? = null
    @DatabaseField(columnName = AuthUser._salaryBase)
    var salaryBase: String? = null
    @DatabaseField(columnName = AuthUser._salaryPost)
    var salaryPost: String? = null
    @DatabaseField(columnName = AuthUser._account)
    var account: String? = null
    @DatabaseField(columnName = AuthUser._password)
    var password: String? = null
    @DatabaseField(columnName = AuthUser._passwordNum)
    var passwordNum: String? = null
    @DatabaseField(columnName = AuthUser._sourceFlag)
    var sourceFlag: Integer? = null
    @DatabaseField(columnName = AuthUser._salt)
    var salt: String? = null
    @DatabaseField(columnName = AuthUser._shopIdenty)
    var shopIdenty: Long? = null
    @DatabaseField(columnName = AuthUser._brandIdenty)
    var brandIdenty: Long? = null
    @DatabaseField(columnName = AuthUser._assignedGroup)
    var assignedGroup: Integer? = null
    @DatabaseField(columnName = AuthUser._assignedId)
    var assignedId: Long? = null
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
