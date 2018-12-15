package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.SaleType;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年8月11日
 */
public interface DishDal extends IOperates {

    /**
     * 估清或取消估清菜品
     *
     * @param newValue
     * @param dishUuids
     * @param listener
     */
    void clearStatus(ClearStatus newValue, List<String> dishUuids, ResponseListener<Boolean> listener);

    /**
     * 根据排序获取第一条数据
     *
     * @param saleType saleType
     * @return DishShop
     * @throws Exception
     */
    DishShop findDishShop(SaleType saleType) throws Exception;

    /**
     * 获取最近使用的用户自定义名称和单价的商品信息
     *
     * @param skuUuid
     * @param limit
     * @return
     * @throws Exception
     */
    List<UserDefineSku> listUserDefineSku(String skuUuid, long limit) throws Exception;

    DishBrandType findDishBrandType(DishShop dishShop) throws Exception;

    /**
     * @version: 1.0
     * @date 2015年11月20日
     */
    public class UserDefineSku {

        private String name;
        private BigDecimal price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

}
