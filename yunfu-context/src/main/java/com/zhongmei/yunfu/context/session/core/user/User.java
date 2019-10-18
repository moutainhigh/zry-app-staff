package com.zhongmei.yunfu.context.session.core.user;

import java.io.Serializable;



public class User implements Serializable {

    private Long id;
    private Long accountId;
    private String account;
    private String name;
    private String salt;
    private String password;
    private String mobile;
    private String faceCode;
    private boolean specialAccount = false;
    private Long roleId;
    private String roleName;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.name = user.getName();
        this.salt = user.getSalt();
        this.password = user.getPassword();
        this.mobile = user.getMobile();
        this.accountId = user.getAccountId();
        this.faceCode = user.faceCode;
        this.roleId = user.getRoleId();
        this.roleName = user.getRoleName();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (name != null && name.length() > 5) {
            return name.substring(0, 4) + "...";
        } else {
            return name;
        }
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isSpecialAccount() {
        return specialAccount;
    }

    public void setSpecialAccount(boolean specialAccount) {
        this.specialAccount = specialAccount;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }

    public String getFaceCode() {
        return faceCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
