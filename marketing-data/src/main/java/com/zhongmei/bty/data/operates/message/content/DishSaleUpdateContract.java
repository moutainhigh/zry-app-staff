package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.resp.data.SupplyTransferRespBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class DishSaleUpdateContract {

    public static class DishSaleUpdateReq {
        public Long brandIdenty;
        public Long shopIdenty;
        public List<DishSaleUpdateReq.Item> dishes2Update;

        public static class Item {
            public String uuid;
            public BigDecimal saleTotal;
        }
    }

    public static class DishSaleUpdateResp extends SupplyTransferRespBase {

        public List<DishSaleUpdateResp.Item> data;

        public static class Item {
            public String uuid;
            public Boolean success;
        }
    }
}
