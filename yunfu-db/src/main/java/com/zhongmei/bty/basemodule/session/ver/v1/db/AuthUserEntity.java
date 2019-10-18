package com.zhongmei.bty.basemodule.session.ver.v1.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.Sex;


@DatabaseTable(tableName = "auth_user")
public class AuthUserEntity extends BasicEntityBase implements ICreator, IUpdator, IAuthUser {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String account = "account";


        public static final String address = "address";


        public static final String birthday = "birthday";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String email = "email";


        public static final String enabledFlag = "enabled_flag";


        public static final String gender = "gender";


        public static final String icon = "icon";


        public static final String mobile = "mobile";


        public static final String name = "name";


        public static final String passwordNum = "password_num";


        public static final String salt = "salt";


        public static final String shopIdenty = "shop_identy";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "account", canBeNull = false)
    private String account;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "birthday")
    private String birthday;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "email")
    private String email;

    @DatabaseField(columnName = "enabled_flag", canBeNull = false)
    private Integer enabledFlag;

    @DatabaseField(columnName = "gender")
    private Integer gender;

    @DatabaseField(columnName = "icon")
    private String icon;

    @DatabaseField(columnName = "mobile")
    private String mobile;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "password_num", canBeNull = false)
    private String passwordNum;

    @DatabaseField(columnName = "salt", canBeNull = false)
    private String salt;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    private String password;
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
    }

    public Sex getGender() {
        return ValueEnums.toEnum(Sex.class, gender);
    }

    public void setGender(Sex gender) {
        this.gender = ValueEnums.toValue(gender);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (name != null && name.length() > 5) {
            StringBuilder builder = new StringBuilder(name.substring(0, 4));
            builder.append("...");
            return builder.toString();
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordNum() {
        return passwordNum;
    }

    public void setPasswordNum(String passwordNum) {
        this.passwordNum = passwordNum;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(account, enabledFlag, name, passwordNum, salt, shopIdenty);
    }
}
