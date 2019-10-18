package com.zhongmei.bty.basemodule.shopmanager.message;

import java.util.Arrays;
import java.util.List;


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
