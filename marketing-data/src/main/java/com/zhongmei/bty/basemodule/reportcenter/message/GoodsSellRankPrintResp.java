package com.zhongmei.bty.basemodule.reportcenter.message;

import java.math.BigDecimal;
import java.util.List;


public class GoodsSellRankPrintResp {
    public Total total;
    public List<SaleItem> sale;
    public List<SaleItem> typeList;     public List<SaleItem> majorTypeList;    public List<PrivilegeItem> privilege;
    public int type;

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public List<SaleItem> getSale() {
        return sale;
    }

    public void setSale(List<SaleItem> sale) {
        this.sale = sale;
    }

    public List<PrivilegeItem> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<PrivilegeItem> privilege) {
        this.privilege = privilege;
    }

    public List<SaleItem> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<SaleItem> typeList) {
        this.typeList = typeList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SaleItem> getMajorTypeList() {
        return majorTypeList;
    }

    public void setMajorTypeList(List<SaleItem> majorTypeList) {
        this.majorTypeList = majorTypeList;
    }

    public static class Total {
        public BigDecimal sale;
        public BigDecimal privilege;
        public BigDecimal maling;
        public BigDecimal overFlow;
        public BigDecimal amount;
        public BigDecimal carry;
        public BigDecimal deposit;


        public BigDecimal getSale() {
            return sale;
        }

        public BigDecimal getPrivilege() {
            return privilege;
        }

        public BigDecimal getMaling() {
            return maling;
        }

        public BigDecimal getOverFlow() {
            return overFlow;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setSale(BigDecimal sale) {
            this.sale = sale;
        }

        public void setPrivilege(BigDecimal privilege) {
            this.privilege = privilege;
        }

        public void setMaling(BigDecimal maling) {
            this.maling = maling;
        }

        public void setOverFlow(BigDecimal overFlow) {
            this.overFlow = overFlow;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getCarry() {
            return carry;
        }

        public void setCarry(BigDecimal carry) {
            this.carry = carry;
        }

        public BigDecimal getDeposit() {
            return deposit;
        }

        public void setDeposit(BigDecimal deposit) {
            this.deposit = deposit;
        }
    }

    public static class SaleItem {
        public Long dishId;
        public String dishName;
        public BigDecimal price;
        public Long majorTypeId;
        public String majorTypeName;
        public Long typeId;
        public String typeName;        public String categoryName;        public BigDecimal count;
        public BigDecimal amount;
        public Boolean isTotal;

        public Long getDishId() {
            return dishId;
        }

        public String getDishName() {
            return dishName;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public Long getTypeId() {
            return typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public BigDecimal getCount() {
            return count;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setDishId(Long dishId) {
            this.dishId = dishId;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public void setTypeId(Long typeId) {
            this.typeId = typeId;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public void setCount(BigDecimal count) {
            this.count = count;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getMajorTypeName() {
            return majorTypeName;
        }

        public void setMajorTypeName(String majorTypeName) {
            this.majorTypeName = majorTypeName;
        }

        public Long getMajorTypeId() {
            return majorTypeId;
        }

        public void setMajorTypeId(Long majorTypeId) {
            this.majorTypeId = majorTypeId;
        }

        public Boolean isTotal() {
            return isTotal;
        }

        public void setTotal(Boolean total) {
            isTotal = total;
        }
    }

    public static class PrivilegeItem {
        public Long typeId;
        public String name;
        public BigDecimal amount;

        public Long getTypeId() {
            return typeId;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setTypeId(Long typeId) {
            this.typeId = typeId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class TypeListItem {
        public Long typeid;
        public String typeName;
        public BigDecimal count;
        public BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Long getTypeid() {
            return typeid;
        }

        public void setTypeid(Long typeid) {
            this.typeid = typeid;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public BigDecimal getCount() {
            return count;
        }

        public void setCount(BigDecimal count) {
            this.count = count;
        }

    }
}

