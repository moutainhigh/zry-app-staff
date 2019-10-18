package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.enums.InvoiceSource;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.List;



public class InvoiceQrcodeReq {

    private Long brandIdenty;


    private Long shopIdenty;


    private Integer type;


    private Integer source;


    private String uuid;


    private Long orderId;


    private String drawerNo;


    private String drawer;


    private BigDecimal totalAmount;


    private String taxpayerName;


    private String taxpayerNo;


    private String taxpayerAddr;


    private String taxpayerTel;


    private String taxpayerBankAccount;


    private List<GoodsInfo> goods;

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public InvoiceType getType() {
        return ValueEnums.toEnum(InvoiceType.class, type);
    }

    public void setType(InvoiceType type) {
        this.type = ValueEnums.toValue(type);
    }

    public InvoiceSource getSource() {
        return ValueEnums.toEnum(InvoiceSource.class, source);
    }

    public void setSource(InvoiceSource source) {
        this.source = ValueEnums.toValue(source);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDrawerNo() {
        return drawerNo;
    }

    public void setDrawerNo(String drawerNo) {
        this.drawerNo = drawerNo;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getTaxpayerNo() {
        return taxpayerNo;
    }

    public void setTaxpayerNo(String taxpayerNo) {
        this.taxpayerNo = taxpayerNo;
    }

    public String getTaxpayerAddr() {
        return taxpayerAddr;
    }

    public void setTaxpayerAddr(String taxpayerAddr) {
        this.taxpayerAddr = taxpayerAddr;
    }

    public String getTaxpayerTel() {
        return taxpayerTel;
    }

    public void setTaxpayerTel(String taxpayerTel) {
        this.taxpayerTel = taxpayerTel;
    }

    public String getTaxpayerBankAccount() {
        return taxpayerBankAccount;
    }

    public void setTaxpayerBankAccount(String taxpayerBankAccount) {
        this.taxpayerBankAccount = taxpayerBankAccount;
    }

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }

    public class GoodsInfo {

        private String goodsName;


        private String goodsNo;


        private String standard;


        private Integer count;


        private String unit;


        private BigDecimal unitPrice;


        private BigDecimal amount;


        private BigDecimal taxRate;

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }
    }
}
