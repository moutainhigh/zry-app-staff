package com.zhongmei.bty.basemodule.customer.bean;

public class CustomerBaseBean {
        private String uuid;
    private String id;
    private String name;
    private String localCreateDateTime;
    private String status;
    private String dirty;
    private String localModifyDateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



    public String getLocalCreateDateTime() {
        return localCreateDateTime;
    }

    public void setLocalCreateDateTime(String localCreateDateTime) {
        this.localCreateDateTime = localCreateDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirty() {
        return dirty;
    }

    public void setDirty(String dirty) {
        this.dirty = dirty;
    }

    public String getLocalModifyDateTime() {
        return localModifyDateTime;
    }

    public void setLocalModifyDateTime(String localModifyDateTime) {
        this.localModifyDateTime = localModifyDateTime;
    }
}
