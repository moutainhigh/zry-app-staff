package com.zhongmei.yunfu.bean.req;

import android.content.Context;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.customer.bean.CustomerStatistic;
import com.zhongmei.bty.basemodule.customer.bean.ICustomerListBean;
import com.zhongmei.bty.basemodule.customer.bean.ICustomerStatistic;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.customer.entity.CrmCustomerLevelRightsDish;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.face.FaceFeatureVo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerResp /*extends DataBaseInfo*/ implements Serializable, ICustomerListBean, ICustomerStatistic {

    private static final long serialVersionUID = 2319959790769528162L;
    public static final int SEX_FEMALE = 0; //女
    public static final int SEX_MALE = 1; //男

    public Long brandId; //	品牌ID
    public Long memberId; //	memberId
    public Long customerId; //	顾客Id
    public Long customerMainId; //	顾客主Id
    public String customerName; //	顾客名字
    public String mobile; //	手机号码
    public String tel;
    public Integer sex; //	性别Code
    public Long levelId; //	等级Id
    public Long level; //	等级数（1~5）
    public String levelName; //	等级名称
    public String memo; //	备注
    public String interest; //	口味喜好
    public String invoice; //	发票开头
    public String invoiceTitle;
    public String address; //	地址
    public String entityCard; //	会员卡号(非实体卡)
    public Long groupId; //	分组ID
    public String groupName; //	分组名称
    public String birthday; //	生日
    public Integer isDisable; //	会员停用：1.是停用; 2.否
    public String openId; //	微信openID
    public Double remainValue; //	当前虚拟会员储值余额
    public Long integral; //	当前积分
    public Integer cardCount; //	实体卡数量
    public Integer coupCount; //	优惠券数量
    public Double creditableValue; //	可挂账总额度
    public Double remainCreditValue; //	可挂账余额
    public Double usedCreditValue; //	已挂账金额
    public String password;//会员密码，创建会员接口用
    public Long commercialId;//顾客所属门店id
    public String commercialName;//顾客所属门店名称
    public List<CustomerCardItem> cardList;
    public Integer hasFaceCode;//是否有人脸识别码
    public String faceCode;//人脸识别码，只有详情页才下行
    public String peopleId;//第三方人脸识别服务器返回字段、只在详情才下行

    /**
     * 颜值得分
     */
    public int faceGrade;


    /**
     * 国家英文名称(为空默认中国) = countryEN
     */
    public String nation;
    /**
     * 国家中文名称(为空默认中国) = countryZH
     */
    public String country;
    /**
     * 电话国际区码(为空默认中国) = AreaCode
     */
    public String nationalTelCode;


    public Long localModifyDateTime;
    /**
     * 同步唯一标示 老Customer的Customer 的Uuid 和 serverId
     */
    public String synFlag;

    public Integer source = 1;
    /**
     * 客户类型  0 手机注册顾客，1,微信注册顾客,2,座机注册顾客 , 6，为权益卡号顾客
     */
    public Integer loginType;
    /**
     * 注册标示 对应loginType，0 手机；1 openId；2 座机
     */
    public String loginId;
    public Long modifyDateTime; // 最后数据更新时间
    public Long upgradeTime; // 会员创建时间

    ////以下是扩展字段用于业务后期会删除，此属性接口不给予返回////
    public CustomerGroupLevel customerLevel;// 会员等级名称信息
    public CrmCustomerLevelRights customerLevelRights;// 会员等级权限信息
    public List<Long> customerLevelRightsDishs;// 享受会员折扣的菜品ids

    public EcCard card; //实体卡，如果不为空表示实体卡登录，否则为虚拟会员
    public boolean needRefresh = false; //是否需要刷新会员信息(重新登录)
    public List<CustomerInfoResp.Card> otherCardList; //会员下其他实体卡列表，在会员切换功能中使用
    public boolean isLoginByCard;// 是否用登录卡登录

    //冗余字段，用来处理编辑接口下行数据
    public Long id;
    public String name;

    public CustomerLoginType customerLoginType; // 本地loginType
    //人脸打分vo
    public FaceFeatureVo mFaceFeatureVo;

    public boolean isMember() {
        return true; //levelId != null;
    }

    public boolean isDisabled() {
        return isDisable != null && isDisable == 1;
    }

    /**
     * 获取顾客姓名
     *
     * @param c
     * @return
     */
    public String getCustomerName(Context c) {
        return TextUtils.isEmpty(customerName) ? c.getString(R.string.customer_name_null) : customerName;
    }

    /**
     * 查询会员等级，积分抵现规则及会员折扣的菜品
     */
    public void queryLevelRightInfos() {
        try {
            if (levelId != null) {
//                CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
//                CustomerGroupLevel levelObj = customerDal.findCustomerGroupLevelByLevelId(levelId);// 查询等级
//                setCustomerLevel(levelObj);
//                customerLevelRights = customerDal.findCustomerLevelRightsByLevelId(levelId);// 查询等级权限
//                if (customerLevelRights != null) {
//                    List<CrmCustomerLevelRightsDish> list =
//                            customerDal.findCustomerLevelRightsDishsByLevelRightId(customerLevelRights.getId());
//                    setCustomerLevelRightsDishs(list);// 查询等级享受折扣菜品列表
//                }
//                setCustomerLevelRights(customerLevelRights);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomerGroupLevel getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(CustomerGroupLevel customerLevel) {
        this.customerLevel = customerLevel;
    }

    /**
     * @Title: setCustomerLevelRightsDishs
     * @Description: 设置会员享受折扣的菜品列表
     * @Return void 返回类型
     */
    public void setCustomerLevelRightsDishs(List<CrmCustomerLevelRightsDish> dishs) {
        if (dishs != null && !dishs.isEmpty()) {
            customerLevelRightsDishs = new ArrayList<>(10);

            for (CrmCustomerLevelRightsDish dish : dishs) {
                if (!customerLevelRightsDishs.contains(dish.getDishBrandId())) {
                    customerLevelRightsDishs.add(dish.getDishBrandId());
                }
            }
        }
    }

    @Override
    public CustomerListResp getCustomerListBean() {
        CustomerListResp bean = new CustomerListResp();
        bean.customerId = customerId;
        bean.groupId = groupId;
        bean.isDisable = isDisable == null ? 2 : Integer.valueOf(isDisable);
        bean.levelId = levelId;
        bean.name = customerName;
        bean.mobile = mobile;
        bean.hasFaceCode = hasFaceCode;
        return bean;
    }

    @Override
    public CustomerStatistic getCustomerStatistic() {
        CustomerStatistic statistic = new CustomerStatistic();
        statistic.memberBalance = remainValue == null ? 0 : remainValue;
        statistic.memberIntegral = integral;
        statistic.memberCoupons = coupCount;
        return statistic;
    }

    /**
     * 顾客编辑时的上传参数封装
     *
     * @return
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("address", address);

        json.put("bindFlag", 1);
        json.put("ctype", 0);
        if (!TextUtils.isEmpty(birthday)) {
            json.put("birthday", birthday);
        }
        json.put("id", customerId);
        json.put("customerMainId", customerMainId);
        if (upgradeTime == null) {
            json.put("cMemberCreateDateTime", upgradeTime);
        }

        json.put("groupId", groupId == null ? "999999" : groupId + "");
        json.put("interest", interest);
        json.put("hobby", interest);
        json.put("invoice", invoice);
        json.put("isDisable", isDisable);
        json.put("isNeedConfirm", 0);
        json.put("modifyDateTime", modifyDateTime);
        if (modifyDateTime != null) {
            json.put("lastSyncMarker", modifyDateTime / 1000);
        }
        json.put("levelId", levelId);
        json.put("loginId", loginId);
        json.put("loginType", loginType);
        json.put("memo", memo);
        json.put("mobile", mobile);
        json.put("name", customerName);
        json.put("serverId", synFlag);
        json.put("sex", sex);
        json.put("source", source);
        json.put("localCreateDateTime", upgradeTime);
        json.put("uuid", synFlag);
        json.put("birthdate", birthday + "");
        return json;
    }

    /**
     * 对会员积分、余额、实体卡数目和优惠券数目设置初始值
     */
    public void setInitialValue() {
        if (integral == null) {
            integral = 0L;
        }
        if (remainValue == null) {
            remainValue = 0D;
        }
        if (cardCount == null) {
            cardCount = 0;
        }
        if (coupCount == null) {
            coupCount = 0;
        }
    }

    /**
     * 判断是否有会员卡
     *
     * @return false 无  true 有
     */
    public boolean hasCustomerEntityCard() {
        if (cardList == null || cardList.size() == 0) {
            return false;
        } else {
            for (CustomerCardItem item : cardList) {
                if (EntityCardType.GENERAL_CUSTOMER_CARD.value().equals(item.cardType)) {
                    return true;
                }
            }
        }
        return false;
    }

    //add start 20170913 for card pwd
    private boolean isLoginCardNeedPwd() {
        if (otherCardList == null || otherCardList.size() == 0) {
            return false;
        } else {
            for (CustomerInfoResp.Card card : otherCardList) {
                if (EntityCardType.GENERAL_CUSTOMER_CARD.equals(card.getCardType())) {
                    return card.getIsNeedPwd() == 1;
                }
            }
        }
        return false;
    }

    public boolean isNeedPassword(BigDecimal amount) {
        //会员卡登陆走会员卡的免密支付逻辑
        if (this.isLoginByCard) {
            return isLoginCardNeedPwd();
        }
        //add v8.5 人脸登录和微信免密支付不需要密码
        if (this.customerLoginType == CustomerLoginType.FACE_CODE || this.customerLoginType == CustomerLoginType.WECHAT_MEMBERCARD_ID) {
            return false;
        }
        //手机号，会员id等登录走会员密码支付逻辑 add v8.13
        return !PaySettingCache.isCanPayNoPwd(amount);//使用免密支付开关
    }
    //add end 20170913 for card pwd

    public CustomerListResp.LoginType getLoginType() {
        return ValueEnums.toEnum(CustomerListResp.LoginType.class, loginType);
    }

    /**
     * 是否有人脸
     *
     * @return
     */
    public boolean hasFaceCode() {
        if (hasFaceCode == null) {
            return false;
        } else {
            if (hasFaceCode == 0) {
                return false;
            } else {
                return true;
            }
        }
    }
}
