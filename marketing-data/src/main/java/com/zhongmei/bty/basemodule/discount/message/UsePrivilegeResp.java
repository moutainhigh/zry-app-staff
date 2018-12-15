package com.zhongmei.bty.basemodule.discount.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口响应体
 */

public class UsePrivilegeResp {
    private List<Item> list;//结果列表


    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    /**
     * 获取校验失败的优惠项
     */
    public List<Item> getCheckFailedItems() {
        if (list != null) {
            List<Item> checkFailedItems = new ArrayList<Item>();
            for (Item item : list) {
                if (item.getCheckStatus() != null && item.getCheckStatus() != 1000) {
                    checkFailedItems.add(item);
                }
            }

            return checkFailedItems;
        }

        return null;
    }

    /**
     * 获取核销失败的优惠项
     */
    public List<Item> getUsageFailedItems() {
        if (list != null) {
            List<Item> usageFailedItems = new ArrayList<Item>();
            for (Item item : list) {
                if (item.getUsageStatus() != null && item.getUsageStatus() != 1000) {
                    usageFailedItems.add(item);
                }
            }

            return usageFailedItems;
        }

        return null;
    }

    public class Item {
        private Integer checkStatus;//校验结果,为1000表成功
        private Integer usageStatus;//核销结果，为1000表成功
        private String uuid;//优惠uuid
        private String resultMsg;//结果消息

        public Integer getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(Integer checkStatus) {
            this.checkStatus = checkStatus;
        }

        public Integer getUsageStatus() {
            return usageStatus;
        }

        public void setUsageStatus(Integer usageStatus) {
            this.usageStatus = usageStatus;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }
}
