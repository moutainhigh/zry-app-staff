package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;
import com.zhongmei.bty.basemodule.discount.enums.WeiXinCardType;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.operates.CouponsOperates;
import com.zhongmei.bty.data.operates.message.content.WxCouponsInfoResp;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.coupons_view_layout)
public class CouponsView extends LinearLayout implements View.OnClickListener, CheckCouponView.CouponsListener {
    @ViewById(R.id.checkcouponview)
    public TextView checkCouponTV;

    @ViewById(R.id.customercouponview)
    public TextView customerCouponTV;

    @ViewById(R.id.check_coupon_view)
    CheckCouponView mCheckCouponView;

    @ViewById(R.id.customer_coupon_view)
    CustomerCouponView mCustomerCouponView;

    //@ViewById(R.id.keyat_yaz_coupons)
    //YazCouponView mKeyAtYazCoupons;

    private FragmentActivity mActivity;

    private ShoppingCart mShoppingCart;

    private CouponsOperates mCouponsOperates;

    public CouponsView(Context context) {
        super(context);
        mActivity = (FragmentActivity) context;
    }

    @AfterViews
    public void initView() {
        mShoppingCart = ShoppingCart.getInstance();
        mCouponsOperates = OperatesFactory.create(CouponsOperates.class);
        checkCouponTV.setOnClickListener(this);
        customerCouponTV.setOnClickListener(this);
        mCheckCouponView.setCouponsListener(this);
        mCheckCouponView.setKeyBoardValue(true);
        updateView(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkcouponview:
                updateView(1);
                break;
            case R.id.customercouponview:
                updateView(2);
                break;
            default:
                break;
        }
    }

    private void updateView(int type) {
        if (type == 1) {
            if (checkCouponTV.isSelected()) {
                return;
            }
            checkCouponTV.setSelected(true);
            customerCouponTV.setSelected(false);
            mCheckCouponView.setVisibility(View.VISIBLE);
            hideCouponView();
        } else {
            if (customerCouponTV.isSelected()) {
                return;
            }
            checkCouponTV.setSelected(false);
            customerCouponTV.setSelected(true);
            mCheckCouponView.setVisibility(View.GONE);
            showCouponView();
        }
    }

    // 显示优惠券
    private void showCouponView() {
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
            mKeyAtYazCoupons.setVisibility(View.VISIBLE);
            mKeyAtYazCoupons.loadYazCoupons();
        } else {*/
        mCustomerCouponView.setVisibility(View.VISIBLE);
        mCustomerCouponView.lodata();
        //}
    }

    // 隐藏优惠券
    private void hideCouponView() {
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
            mKeyAtYazCoupons.setVisibility(View.GONE);
        } else {*/
        mCustomerCouponView.setVisibility(View.GONE);
        //}
    }

    @Override
    public void getCouponsNo(String ticketNo) {
        if (!TextUtils.isEmpty(ticketNo)) {

            if (mShoppingCart.getIsSalesReturn()) {
                ToastUtil.showShortToast(R.string.refeund_cannot_user_coupon);
                return;
            }
            if (mShoppingCart.checkIsHaveWXC(ticketNo)) {
                ToastUtil.showLongToast(R.string.dinner_wechat_code_repeat);
                return;
            }

            mCouponsOperates.getWeiXinCouponsDetail(ticketNo, LoadingResponseListener.ensure(listener, mActivity.getSupportFragmentManager()));
        } else {
            ToastUtil.showLongToast(R.string.coupons_empty);
        }
    }

    /**
     * 微信卡券Resp Listener
     */
    ResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>> listener = new ResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>>() {

        @Override
        public void onResponse(ResponseObject<LoyaltyTransferResp<WxCouponsInfoResp>> response) {
            if (ResponseObject.isOk(response)) {
                if (LoyaltyTransferResp.isOk(response.getContent())) {
                    WeiXinCouponsInfo info = response.getContent().getResult().getWeixinCard();
                    //判断是否是代金券
                    if (info.getCard_type() != WeiXinCardType.CASH) {
                        ToastUtil.showShortToast(mActivity.getString(R.string.not_support_coupons));
                        return;
                    }

                    if (info.getCode_info().isCanConsume()) {
                        Long id = response.getContent().getResult().getId();
                        if (id == null || mShoppingCart.isAllowAddCoupon(mShoppingCart.fastFootShoppingCartVo, id)) {
                            //特殊处理微信卡号
                            String wxCardNumber = info.getCode();
                            info.setCode(BaseShoppingCart.getNewWxCode(wxCardNumber));

                            mShoppingCart.addWeiXinCouponsPrivilege(info);
                            mCheckCouponView.cleanEditText();
                        } else {
                            ToastUtil.showShortToast(R.string.coupons_has_existed);
                        }
                    } else {
                        WeiXinCouponsInfo.WeiXinCodeInfo codeInfo = info.getCode_info();
                        if (!TextUtils.isEmpty(codeInfo.getStatus())) {
                            ToastUtil.showShortToast(codeInfo.getStatus());
                        } else {
                            ToastUtil.showShortToast(codeInfo.getErrmsg());
                        }
                    }
                } else {
                    ToastUtil.showShortToast(response.getContent().getErrorMessage());
                }
            } else {
                ToastUtil.showShortToast(response.getMessage());
            }
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.showLongToast(error.getMessage());
        }
    };
}
