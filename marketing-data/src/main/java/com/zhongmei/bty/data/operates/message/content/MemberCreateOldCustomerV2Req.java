package com.zhongmei.bty.data.operates.message.content;

import android.text.TextUtils;

import com.zhongmei.yunfu.bean.req.CustomerCreateReq;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.util.DateTimeUtils;

/**
 * 新顾客上传请求
 * <p>
 * Created by demo on 2018/12/15
 */
public class MemberCreateOldCustomerV2Req extends BaseRequest {

    public String uniqueCode; // 预制顾客唯一标识 v8.7 旧会员导入添加字段
    public int loginType = 0; // 客户类型(0:手机注册顾客，1:微信注册顾客,2:座机注册顾客 【ps:当前只支持手机】)
    public String loginId;
    public Long userId;
    public Integer source = 1;//顾客来源：1为Calm，2为手机app，3为其他系统导入，4为微信，5支付宝，6商家官网，7百度，8 后台，9百度外卖，10饿了么，11美团外卖，12大众点评，13熟客,14糯米点菜,15os mobile，16开放平台，17自助
    public String nation;//国家英文名称(为空默认中国)
    public String country;//国家中文名称(为空默认中国)
    public String nationalTelCode;//电话国际区码(为空默认中国)
    public String mobile;//顾客手机号码
    public String name;//顾客名称
    public String consumePassword;//消费密码（6位整数或者MD5加密,为空则随机密码，随机密码会短信通知顾客）
    public String faceCode;//人脸识别标志(理论上是唯一)
    public Long birthday;//生日（毫秒级时间戳），为空时默认为18年前的当天
    public String sex;
    public String address;
    public Long groupId;//顾客分组ID
    public String environmentHobby;
    public String invoiceTitle;
    public String memo;

    public void cloneReq(CustomerCreateReq req) {
        this.name = req.name;
        this.sex = req.sex + "";
        this.address = req.address;
        this.birthday = DateTimeUtils.formatDate(req.birthday);
        this.consumePassword = req.consumePassword;
        this.environmentHobby = req.environmentHobby;
        this.groupId = req.groupId;
        this.invoiceTitle = req.invoiceTitle;
        this.memo = req.memo;
        this.mobile = req.mobile;
        this.nation = req.nation;
        this.country = req.country;
        this.nationalTelCode = req.nationalTelCode;
        this.userId = Session.getAuthUser().getId();
        if (!TextUtils.isEmpty(req.faceCode)) {
            this.faceCode = req.faceCode;
        }
        this.loginId = req.mobile;
    }
}
