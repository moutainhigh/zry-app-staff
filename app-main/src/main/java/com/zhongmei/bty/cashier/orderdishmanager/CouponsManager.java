package com.zhongmei.bty.cashier.orderdishmanager;

import android.os.AsyncTask;
import android.util.Log;

import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.customer.operates.CouponDal;
import com.zhongmei.bty.snack.event.EventCouponVoResult;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * 异步加载优惠劵
 *
 * @Date：2015-8-18 上午10:19:29
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CouponsManager {
    private static final String TAG = CouponsManager.class.getSimpleName();

    // private TradeVo mTradeVo;

    private List<CouponVo> mCouponVoList;

    private CouponDal mCouponDal;

    private boolean isDinner = false;

    public CouponsManager() {
        mCouponDal = OperatesFactory.create(CouponDal.class);
    }

    /*
     * public void setTradeVo(TradeVo tradeVo) { mTradeVo =
     * tradeVo; }
     */

    public void loadData(final List<CustomerCouponResp> list) {
        if (list != null && list.size() > 0) {
            new AsyncTask<Void, Void, EventCouponVoResult>() {
                @Override
                protected EventCouponVoResult doInBackground(Void... params) {

                    EventCouponVoResult data = new EventCouponVoResult();

                    try {
                        TradeVo tradevo = null;
                        //modify 20170303  begin 解决正餐删除菜品券还选择问题
                        List<IShopcartItem> shopcartItems = null;
                        //单菜礼品券
                        List<Long> promoIds = new ArrayList<>();
                        if (isDinner) {
                            tradevo = DinnerShopManager.getInstance().getShoppingCart().getOrder();
                            //正餐取已用券,已经使用的单菜礼品券 add 20161021

                            shopcartItems = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartDish();

                        } else {
                            tradevo = ShoppingCart.getInstance().getOrder();
                            //快餐取已用券,已经使用的单菜礼品券 add 20161021
                            shopcartItems = ShoppingCart.getInstance().mergeShopcartItem(ShoppingCart.getInstance().fastFootShoppingCartVo);
                        }

                        if (shopcartItems != null && !shopcartItems.isEmpty()) {
                            for (IShopcartItem shopcartItem : shopcartItems) {
                                if (shopcartItem != null && (shopcartItem.getStatusFlag() == StatusFlag.VALID) && shopcartItem.getCouponPrivilegeVo() != null && shopcartItem.getCouponPrivilegeVo().isValid()) {
                                    promoIds.add(shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId());
                                }
                            }
                        }
                        //modify 20170303  end 解决删除菜品券还选择问题
                        mCouponVoList = mCouponDal.findCouponVoListByCouponInfos(list);

                        if (list != null && !list.isEmpty()) {

                            Map<Long, Long> tempPromoId = new HashMap<Long, Long>();

//                           //设置购物车中已经有的整单优惠券
                            if (tradevo != null) {
                                List<CouponPrivilegeVo> couponPrivilegeVoList = tradevo.getCouponPrivilegeVoList();
                                if (couponPrivilegeVoList != null) {
                                    for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                                        if (couponPrivilegeVo != null
                                                && couponPrivilegeVo.getTradePrivilege() != null
                                                && couponPrivilegeVo.getTradePrivilege().getStatusFlag() == StatusFlag.VALID) {
                                            Long promoId = couponPrivilegeVo.getTradePrivilege().getPromoId();
                                            tempPromoId.put(promoId, promoId);
                                        }
                                    }
                                }
                            }

                            for (CouponVo vo : mCouponVoList) {
                                // 验证购物车当前的整单优惠劵
                                if (tempPromoId != null && tempPromoId.size() > 0) {
                                    if (tempPromoId.containsKey(vo.getCouponInfo().getId())) {
                                        vo.setSelected(true);
                                        data.setSelectedCouponVo(vo);
                                    }
                                }
                                // 优惠劵分类

                                switch (vo.getCouponInfo().getCouponType()) {

                                    case DISCOUNT:// 折扣券
                                        if (data.getDiscountCoupons() == null) {
                                            data.setDiscountCoupons(new ArrayList<CouponVo>());
                                            data.getDiscountCoupons().add(vo);
                                        } else {
                                            data.getDiscountCoupons().add(vo);
                                        }
                                        break;
                                    case GIFT:// 礼品券
                                        vo.setDishId(vo.getCouponInfo().getDishId());
                                        if (data.getGiftCoupons() == null) {
                                            data.setGiftCoupons(new ArrayList<CouponVo>());
                                            data.getGiftCoupons().add(vo);
                                        } else {
                                            data.getGiftCoupons().add(vo);
                                        }
                                        break;
                                    case CASH:// 现金券
                                        if (data.getCashCoupons() == null) {
                                            data.setCashCoupons(new ArrayList<CouponVo>());
                                            data.getCashCoupons().add(vo);
                                        } else {
                                            data.getCashCoupons().add(vo);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                vo.setEnabled(true);
                            }
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    return data;
                }

                protected void onPostExecute(EventCouponVoResult data) {
                    // 通知ui更新
                    EventBus.getDefault().post(data);
                }

                ;
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            EventBus.getDefault().post(new EventCouponVoResult());
        }
    }

    public boolean isIntergralToCashUsed() {
        TradeVo tradevo = null;
        if (isDinner) {
            tradevo = SeparateShoppingCart.getInstance().getOrder();

        } else {
            tradevo = ShoppingCart.getInstance().getOrder();
        }
        if (tradevo.getIntegralCashPrivilegeVo() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Title: getCouponPrivilegeVo
     * @Description: CouponVo 转换 CouponPrivilegeVo
     * @Param @param vo
     * @Param @return TODO
     * @Return CouponPrivilegeVo 返回类型
     */
    public CouponPrivilegeVo getCouponPrivilegeVo(CouponVo vo) {
        if (vo == null) {
            return null;
        } else {
            CouponPrivilegeVo cpv = new CouponPrivilegeVo();
            cpv.setCoupon(vo.getCoupon());
            cpv.setCouponInfoId(vo.getCouponInfo().getId());

            TradePrivilege tradePrivilege = new TradePrivilege();
            tradePrivilege.setPrivilegeType(PrivilegeType.COUPON);
            tradePrivilege.setPromoId(vo.getCouponInfo().getCustomerCouponId());
            tradePrivilege.setCouponId(vo.getCouponInfo().getId());
            tradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);// 默认优惠金额0
            tradePrivilege.setPrivilegeValue(BigDecimal.ZERO);

            cpv.setTradePrivilege(tradePrivilege);
            switch (vo.getCoupon().getCouponType()) {

                case REBATE:// 满减券
                    tradePrivilege.setPrivilegeValue(vo.getCoupon().getDiscountValue());
                    break;

                case DISCOUNT:// 折扣券
                    tradePrivilege.setPrivilegeValue(vo.getCoupon().getDiscountValue());
                    break;
                case GIFT:// 礼品券
                    BigDecimal price = BigDecimal.ZERO;
                    BigDecimal count = BigDecimal.ONE;
                    ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(vo.getDishId());
                    if (shopcartItem != null) {
                        shopcartItem.changeQty(BigDecimal.ONE);
                        price = shopcartItem.getPrice();
                    }
                    if (price != null && count != null) {
                        tradePrivilege.setPrivilegeAmount(price.multiply(count));
                    }
                    cpv.setShopcartItem(shopcartItem);
                    break;
                case CASH:// 代金卷
                    tradePrivilege.setPrivilegeValue(vo.getCoupon().getDiscountValue());
                    break;
                default:
                    break;
            }

            return cpv;
        }
    }

    public boolean isDinner() {
        return isDinner;
    }

    public void setDinner(boolean isDinner) {
        this.isDinner = isDinner;
    }

    /*private List<CoupDish> findCoupDishById(Long couponId) {
        DatabaseHelper helper = DBHelper.getHelper();
        try {
            Dao<CoupDish, String> dao = helper.getDao(CoupDish.class);
            QueryBuilder<CoupDish, String> qb = dao.queryBuilder();
            qb.where().eq(CoupDish.$.couponId, couponId);
            return qb.orderBy(CoupDish.$.id, true).query();
        } catch (Exception e) {
            return null;
        } finally {
            DBHelper.releaseHelper(helper);
        }
    }*///modify 20170303 统一把数据库操作放到dal

    private DishVo createDishVo(DishShop dishShop) throws Exception {
        DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
        // 没有规格的与有规格的不合成一个
        if (dishShop.getHasStandard() == Bool.YES) {
            Set<DishProperty> standards = DishManager.filterStandards(dishShop);
            return new DishVo(dishShop, standards, unit);
        } else {
            return new DishVo(dishShop, unit);
        }
    }

    /**
     * 通过优惠劵规则设置tradeprivilege的privilegeValue的值
     */
    public static void setCouponPrivilegeValue(CouponPrivilegeVo couponPrivilegeVo) {
        TradePrivilege tradePrivilege = couponPrivilegeVo.getTradePrivilege();
        Coupon coupon = couponPrivilegeVo.getCoupon();
//        List<CoupRule> coupRuleList = couponPrivilegeVo.getCoupRuleList();
        if (tradePrivilege == null || coupon == null) {
            return;
        }
        switch (coupon.getCouponType()) {

            case REBATE:// 满减券
                tradePrivilege.setPrivilegeValue(coupon.getDiscountValue());
                break;

            case DISCOUNT:// 折扣券
                tradePrivilege.setPrivilegeValue(coupon.getDiscountValue());
                break;
            case CASH:// 代金卷
                tradePrivilege.setPrivilegeValue(coupon.getDiscountValue());
                break;
            default:
                break;
        }
    }
}
