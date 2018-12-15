package com.zhongmei.bty.entity.bean.customer;//package com.zhongmei.bty.entity.bean.customer;
//
//import android.text.TextUtils;
//
//import com.zhongmei.bty.basemodule.customer.bean.CustomerListBean;
//import com.zhongmei.bty.basemodule.customer.bean.ICustomerListBean;
//import com.zhongmei.bty.basemodule.customer.bean.ICustomerStatistic;
//import com.zhongmei.bty.customer.card.entity.EcCard;
//import com.zhongmei.bty.data.db.common.CrmCustomerLevelRights;
//import com.zhongmei.bty.basemodule.customer.entity.CustomerLevel;
//import com.zhongmei.bty.data.db.util.DataBaseDescription;
//import com.zhongmei.bty.data.db.util.FieldType;
//import com.zhongmei.bty.baseservice.data.type.RealInterface;
//import com.zhongmei.bty.data.operates.message.content.CustomerInfoResp;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
//
//public class CustomerNew2 implements Serializable  , ICustomerListBean , ICustomerStatistic {
//
//    private static final long serialVersionUID = 2319959790769528162L;
//    public static final int SEX_FEMALE = 0; //女
//    public static final int SEX_MALE = 1; //男
//
//    public Long brandId; //	品牌ID
//    public Long memberId; //	memberId
//    public Long customerId; //	顾客Id
//    public Long customerMainId; //	顾客主Id
//    public String customerName; //	顾客名字
//    public String mobile; //	手机号码
//    public String tel;
//    public Integer sex; //	性别Code
//    public Long levelId; //	等级Id
//    public Long level; //	等级数（1~5）
//    public String levelName; //	等级名称
//    public String memo; //	备注
//    public String interest; //	口味喜好
//    public String invoice; //	发票开头
//    public String address; //	地址
//    public String entityCard; //	会员卡号(非实体卡)
//    public Long groupId; //	分组ID
//    public String groupName; //	分组名称
//    public String birthday; //	生日
//    public Integer isDisable; //	会员停用：1.是停用; 2.否
//    public String openId; //	微信openID
//    public double remainValue; //	当前虚拟会员储值余额
//    public Long integral; //	当前积分
//    public Integer cardCount; //	实体卡数量
//    public Integer coupCount; //	优惠券数量
//    public double creditableValue; //	可挂账总额度
//    public double remainCreditValue; //	可挂账余额
//    public double usedCreditValue; //	已挂账金额
//    public String password;//会员密码，创建会员接口用
//
//    /** 同步唯一标示 老Customer的Customer 的Uuid 和 serverId*/
//    public String synFlag;
//
//    public Long localModifyDateTime;
//
//    public Integer source = 1;
//    /** 客户类型  0 手机注册顾客，1,微信注册顾客,2,座机注册顾客 */
//    public Integer loginType;
//    /** 注册标示 对应loginType，0 手机；1 openId；2 座机 */
//    public String loginId;
//    public Long modifyDateTime; // 最后数据更新时间
//    public Long upgradeTime; // 会员创建时间
//
//    ////以下是扩展字段用于业务后期会删除，此属性接口不给予返回////
//    private CustomerLevel customerLevel;// 会员等级名称信息
//    private CrmCustomerLevelRights customerLevelRights;// 会员等级权限信息
//    private List<Long> customerLevelRightsDishs;// 享受会员折扣的菜品ids
//
//    public Double valuecard = null; // 缓存储值，供DinnerPriviligeItemsFragment使用
//    public EcCard card; //实体卡，如果不为空表示实体卡登录，否则为虚拟会员
//    public boolean needRefresh = false; //是否需要刷新会员信息，结账界面充值后，返回结算界面时需要刷新
//    public List<CustomerInfoResp.Card> otherCardList; //会员下其他实体卡列表，在会员切换功能中使用
//
//    public boolean isMember() {
//        return levelId != null;
//    }
//
//    public boolean isDisabled() {
//        return isDisable != null && isDisable == 1;
//    }
//
//    @Override
//    public CustomerListBean getCustomerListBean() {
//        CustomerListBean bean = new CustomerListBean();
//        bean.customerId = customerId;
//        bean.groupId = groupId;
//        bean.isDisable = isDisable == null ? 2 : Integer.valueOf(isDisable);
//        bean.levelId = levelId;
//        bean.name = customerName;
//        bean.mobile = mobile;
//        return bean;
//    }
//
//    @Override
//    public CustomerStatistic getCustomerStatistic() {
//        CustomerStatistic statistic = new CustomerStatistic();
//        statistic.set(CustomerStatistic.MEMBER_BALANCE_KEY , remainValue);
//        statistic.set(CustomerStatistic.MEMBER_INTEGRAL_KEY,integral);
//        statistic.set(CustomerStatistic.MEMBER_COUPONS_KEY,coupCount);
//        return statistic;
//    }
//
//    public JSONObject toJson() throws JSONException {
//        JSONObject json = new JSONObject();
//        json.put("address", address);
//
//        json.put("bindFlag", 1);
//        json.put("ctype", 0);
//        if (!TextUtils.isEmpty(birthday)) {
//            json.put("birthday", birthday);
//        }
//        json.put("id", customerId);
//        json.put("customerMainId", customerMainId);
//        if (upgradeTime == null) {
//            json.put("cMemberCreateDateTime", upgradeTime);
//        }
//        json.put("groupID", groupId == null ? 999999 : groupId);
//        json.put("groupId", groupId == null ? "999999" : groupId + "");
//        json.put("interest", interest);
//        json.put("hobby", interest);
//        json.put("invoice", invoice);
//        json.put("isDisable", isDisable);
//        json.put("isNeedConfirm", 0);
//        json.put("modifyDateTime", modifyDateTime);
//        if (modifyDateTime != null) {
//            json.put("lastSyncMarker", modifyDateTime / 1000);
//        }
//        json.put("levelId", levelId);
//        json.put("loginId", loginId);
//        json.put("loginType", loginType);
//        json.put("memo", memo);
//        json.put("mobile", mobile);
//        json.put("name", customerName);
//        json.put("serverId", synFlag);
//        json.put("sex", sex);
//        json.put("source", source);
//        json.put("localCreateDateTime", upgradeTime);
//        json.put("uuid", synFlag);
//        json.put("birthdate", birthday + "");
//        return json;
//    }
//}
