package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.SaleType;

import java.math.BigDecimal;
import java.util.List;


public interface DishDal extends IOperates {


    void clearStatus(ClearStatus newValue, List<String> dishUuids, ResponseListener<Boolean> listener);


    DishShop findDishShop(SaleType saleType) throws Exception;


    List<UserDefineSku> listUserDefineSku(String skuUuid, long limit) throws Exception;

    DishBrandType findDishBrandType(DishShop dishShop) throws Exception;


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
