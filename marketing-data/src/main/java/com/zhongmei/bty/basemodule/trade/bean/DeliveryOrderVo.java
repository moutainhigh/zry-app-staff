package com.zhongmei.bty.basemodule.trade.bean;

import android.util.Log;

import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class DeliveryOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String TAG = DeliveryOrderVo.class.getSimpleName();

    private DeliveryOrder deliveryOrder;

    private List<DeliveryOrderRecord> deliveryOrderRecords;

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public List<DeliveryOrderRecord> getDeliveryOrderRecords() {
        return deliveryOrderRecords;
    }

    public void setDeliveryOrderRecords(List<DeliveryOrderRecord> deliveryOrderRecords) {
        this.deliveryOrderRecords = deliveryOrderRecords;
    }

    @Override
    protected DeliveryOrderVo clone() throws CloneNotSupportedException {
        DeliveryOrderVo deliveryOrderVo = new DeliveryOrderVo();
        try {
            if (deliveryOrder != null) {
                deliveryOrderVo.setDeliveryOrder(copyEntity(deliveryOrder, new DeliveryOrder()));
            }

            if (deliveryOrderRecords != null) {
                List<DeliveryOrderRecord> newList = new ArrayList<DeliveryOrderRecord>();
                for (DeliveryOrderRecord deliveryOrderRecord : deliveryOrderRecords) {
                    newList.add(copyEntity(deliveryOrderRecord, new DeliveryOrderRecord()));
                }
                deliveryOrderVo.setDeliveryOrderRecords(newList);
            }

            return deliveryOrderVo;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error!", e);
        }
        return null;
    }

    /**
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    private static <T> T copyEntity(T source, T target) throws Exception {
        Beans.copyProperties(source, target);
        return target;
    }
}
