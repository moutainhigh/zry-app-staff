package com.zhongmei.yunfu.init.sync.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年11月23日
 */
public class ModulesBody implements Serializable {

    private List<String> modules;

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "ModulesBody{" +
                "modules=" + modules +
                '}';
    }
}
