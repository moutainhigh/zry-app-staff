package com.zhongmei.yunfu.bean.req;

/**
 * 新顾客上传请求
 * <p>
 * Created by demo on 2018/12/15
 */
public class CustomerCreateReq {

    public Long customerId;
    public Long userId;
    public String userName;
    public Integer source = 1;//顾客来源：1为Calm，2为手机app，3为其他系统导入，4为微信，5支付宝，6商家官网，7百度，8 后台，9百度外卖，10饿了么，11美团外卖，12大众点评，13熟客,14糯米点菜,15os mobile，16开放平台，17自助
    public String nation;//国家英文名称(为空默认中国)
    public String country;//国家中文名称(为空默认中国)
    public String nationalTelCode;//电话国际区码(为空默认中国)
    public String mobile;//顾客手机号码
    public String name;//顾客名称
    public String consumePassword;//消费密码（6位整数或者MD5加密,为空则随机密码，随机密码会短信通知顾客）
    public String faceCode;//人脸识别标志(理论上是唯一)
    public String birthday;//生日（毫秒级时间戳），为空时默认为18年前的当天
    public String sex;
    public String address;
    public Long groupId;//顾客分组ID
    public String groupName;
    public String environmentHobby;
    public String invoiceTitle;
    public String memo;
    public String cardNo;

}
