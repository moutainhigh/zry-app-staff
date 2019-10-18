package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;
import com.zhongmei.bty.commonmodule.database.enums.ArriveStatus;
import com.zhongmei.bty.commonmodule.database.enums.ArriveWay;

import java.util.List;


public interface CustomerArrivalShopDal extends IOperates {

    CustomerArrivalShop save(CustomerArrivalShop customerArrivalShop);

    CustomerArrivalShop getLastByCustomerId(Long customerId);

    List<CustomerArrivalShop> find() throws Exception;

    List<CustomerArrivalShop> find(List<ArriveStatus> arriveStatusList, List<Long> tableIds,
                                   List<ArriveWay> arriveWayList) throws Exception;

    boolean update(CustomerArrivalShop customer) throws Exception;

    List<CustomerArrivalShop> findNotOperated();

    void deleteAll();
}
