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


public class CouponsManager {
    private static final String TAG = CouponsManager.class.getSimpleName();


    private List<CouponVo> mCouponVoList;

    private CouponDal mCouponDal;

    private boolean isDinner = false;

    public CouponsManager() {
        mCouponDal = OperatesFactory.create(CouponDal.class);
    }


    public void loadData(final List<CustomerCouponResp> list) {
        if (list != null && list.size() > 0) {
            new AsyncTask<Void, Void, EventCouponVoResult>() {
                @Override
                protected EventCouponVoResult doInBackground(Void... params) {

                    EventCouponVoResult data = new EventCouponVoResult();

                    try {
                        TradeVo tradevo = null;
                        List<IShopcartItem> shopcartItems = null;
                        List<Long> promoIds = new ArrayList<>();
                        if (isDinner) {
                            tradevo = DinnerShopManager.getInstance().getShoppingCart().getOrder();

                            shopcartItems = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartDish();

                        } else {
                            tradevo = ShoppingCart.getInstance().getOrder();
                            shopcartItems = ShoppingCart.getInstance().mergeShopcartItem(ShoppingCart.getInstance().fastFootShoppingCartVo);
                        }

                        Map<Long, Long> tempPromoId = new HashMap<Long, Long>();

                        if (shopcartItems != null && !shopcartItems.isEmpty()) {
                            for (IShopcartItem shopcartItem : shopcartItems) {
                                if (shopcartItem != null && (shopcartItem.getStatusFlag() == StatusFlag.VALID) && shopcartItem.getCouponPrivilegeVo() != null && shopcartItem.getCouponPrivilegeVo().isValid()) {
                                    Long promoId = shopcartItem.getCouponPrivilegeVo().getTradePrivilege().getPromoId();
                                    tempPromoId.put(promoId, promoId);
                                }
                            }
                        }
                        mCouponVoList = mCouponDal.findCouponVoListByCouponInfos(list);

                        if (list != null && !list.isEmpty()) {


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
                                if (tempPromoId != null && tempPromoId.size() > 0) {
                                    if (tempPromoId.containsKey(vo.getCouponInfo().getCustomerCouponId())) {
                                        vo.setSelected(true);
                                        data.setSelectedCouponVo(vo);
                                    }
                                }

                                switch (vo.getCouponInfo().getCouponType()) {

                                    case DISCOUNT:
                                        if (data.getDiscountCoupons() == null) {
                                            data.setDiscountCoupons(new ArrayList<CouponVo>());
                                            data.getDiscountCoupons().add(vo);
                                        } else {
                                            data.getDiscountCoupons().add(vo);
                                        }
                                        break;
                                    case GIFT:
                                        vo.setDishId(vo.getCouponInfo().getDishId());
                                        if (data.getGiftCoupons() == null) {
                                            data.setGiftCoupons(new ArrayList<CouponVo>());
                                            data.getGiftCoupons().add(vo);
                                        } else {
                                            data.getGiftCoupons().add(vo);
                                        }
                                        break;
                                    case CASH:
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
            tradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
            tradePrivilege.setPrivilegeValue(BigDecimal.ZERO);

            cpv.setTradePrivilege(tradePrivilege);
            switch (vo.getCoupon().getCouponType()) {

                case REBATE:
                    tradePrivilege.setPrivilegeValue(vo.getCoupon().getDiscountValue());
                    break;

                case DISCOUNT:
                    tradePrivilege.setPrivilegeValue(vo.getCoupon().getDiscountValue());
                    break;
                case GIFT:
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
                case CASH:
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


    private DishVo createDishVo(DishShop dishShop) throws Exception {
        DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
        if (dishShop.getHasStandard() == Bool.YES) {
            Set<DishProperty> standards = DishManager.filterStandards(dishShop);
            return new DishVo(dishShop, standards, unit);
        } else {
            return new DishVo(dishShop, unit);
        }
    }


    public static void setCouponPrivilegeValue(CouponPrivilegeVo couponPrivilegeVo) {
        TradePrivilege tradePrivilege = couponPrivilegeVo.getTradePrivilege();
        Coupon coupon = couponPrivilegeVo.getCoupon();
        if (tradePrivilege == null || coupon == null) {
            return;
        }
        switch (coupon.getCouponType()) {

            case REBATE:
                tradePrivilege.setPrivilegeValue(coupon.getDiscountValue());
                break;

            case DISCOUNT:
                tradePrivilege.setPrivilegeValue(coupon.getDiscountValue());
                break;
            case CASH:
                tradePrivilege.setPrivilegeValue(coupon.getDiscountValue());
                break;
            default:
                break;
        }
    }
}
