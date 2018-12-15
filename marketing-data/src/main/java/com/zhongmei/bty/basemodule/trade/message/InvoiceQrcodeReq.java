package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.enums.InvoiceSource;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电子发票获取开票二维码请求
 */

public class InvoiceQrcodeReq {
    /**
     * 品牌id
     */
    private Long brandIdenty;

    /**
     * 商户id
     */
    private Long shopIdenty;

    /**
     * 消费类型
     */
    private Integer type;

    /**
     * 来源
     */
    private Integer source;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 消费订单id，挂单开票必传，就餐对应的trade.id，储值对应的是储值记录的id
     */
    private Long orderId;

    /**
     * POS操作人编号
     */
    private String drawerNo;

    /**
     * 开票人/POS操作人名称
     */
    private String drawer;

    /**
     * 价税合计金额，=合计金额+合计税额
     */
    private BigDecimal totalAmount;

    /**
     * 纳税人名称默认取自mind，若自定义则须输入
     */
    private String taxpayerName;

    /**
     * 纳税人识别号默认取自mind，若自定义则须输入
     */
    private String taxpayerNo;

    /**
     * 纳税人地址
     */
    private String taxpayerAddr;

    /**
     * 纳税人电话
     */
    private String taxpayerTel;

    /**
     * 开户行及账号
     */
    private String taxpayerBankAccount;

    /**
     * 发票明细，至少1条，参考goodsInfo
     */
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
        /**
         * 货品名称
         */
        private String goodsName;

        /**
         * 货品编号
         */
        private String goodsNo;

        /**
         * 规格型号
         */
        private String standard;

        /**
         * 数量
         */
        private Integer count;

        /**
         * 单位
         */
        private String unit;

        /**
         * 单价，单位元，精确到分
         */
        private BigDecimal unitPrice;

        /**
         * 金额
         */
        private BigDecimal amount;

        /**
         * 税率，0标识免税，2位小数
         */
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
