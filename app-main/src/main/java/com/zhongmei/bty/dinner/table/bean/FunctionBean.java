package com.zhongmei.bty.dinner.table.bean;

/**
 * Created by demo on 2018/12/15
 */
public class FunctionBean {
    public int functionCode;//功能代码
    public String functionName;//功能名称

    public FunctionBean() {
    }

    public FunctionBean(int code, String name) {
        this.functionCode = code;
        this.functionName = name;
    }
}
