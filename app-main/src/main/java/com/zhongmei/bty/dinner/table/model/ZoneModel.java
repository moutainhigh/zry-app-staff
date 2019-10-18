package com.zhongmei.bty.dinner.table.model;

import java.util.List;

import com.zhongmei.bty.basemodule.trade.bean.IZone;


public class ZoneModel implements IZone {

    private Long id;
    private String code;
    private String name;
    private int width;
    private int height;
    private List<DinnertableModel> dinnertableModels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<DinnertableModel> getDinnertableModels() {
        return dinnertableModels;
    }

    public void setDinnertableModels(List<DinnertableModel> models) {
        this.dinnertableModels = models;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ZoneModel other = (ZoneModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
