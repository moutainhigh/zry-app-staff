package com.zhongmei.bty.basemodule.shopmanager.message;

import java.util.Arrays;
import java.util.List;

/**
 * 封装调用开始营业与歇业接口的请求数据
 *
 * @Date：2015-8-7 上午10:20:13
 * @Description: TODO
 * @Version: 1.0
 */
public class OpenAndCloseReq {

    private Integer status;

    private List<Item> items;

    public OpenAndCloseReq() {
        items = Arrays.asList(new Item());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @Date：2015-8-7 上午10:18:52
     * @Description: TODO
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
    public static class Item {

        private Integer menuId = 2;

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }
    }

}
