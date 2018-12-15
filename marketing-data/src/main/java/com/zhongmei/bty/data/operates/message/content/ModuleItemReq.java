package com.zhongmei.bty.data.operates.message.content;

public class ModuleItemReq {

    private String moduleName;

    private String uuid;

    public ModuleItemReq(String moduleName, String uuid) {
        this.moduleName = moduleName;
        this.uuid = uuid;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
