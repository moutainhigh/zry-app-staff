package com.zhongmei.yunfu.bean.req;

/**
 * 新顾客上传请求
 * <p>
 * Created by demo on 2018/12/15
 */
public class CustomerCreateImportReq extends CustomerCreateReq {

    public String uniqueCode; // 预制顾客唯一标识 v8.7 旧会员导入添加字段
    public String loginId;

}
