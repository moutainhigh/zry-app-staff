package com.zhongmei.bty.dinner.cash;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.bty.basemodule.input.NumberKeyBoard;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.commonbusiness.view.ScanPopupWindow;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;
import com.zhongmei.bty.data.operates.CouponsOperates;
import com.zhongmei.bty.basemodule.discount.enums.WeiXinCardType;
import com.zhongmei.bty.data.operates.message.content.WxCouponsInfoResp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 微信卡劵界面
 */
@EFragment(R.layout.dinner_weixin_coupon_code)
public class DinnerWeixinCouponCodeFragment extends BasicDialogFragment {
    @ViewById(R.id.scan_btn)
    ImageButton mScanBtn;

    @ViewById(R.id.value)
    EditText mValue;

    @ViewById(R.id.coupon_code_hint)
    TextView tv_hint;

    @ViewById(R.id.keyboard)
    NumberKeyBoard mKeyBoard;

    @ViewById(R.id.customer_verification)
    Button mVerification;

    private ScanPopupWindow scanPopupWindow;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setCancelable(false);
        return dialog;
    }

    private void setDialogWidthAndHeight(View view) {
        Window window = getDialog().getWindow();
        view.measure(0, 0);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;
    }

    @AfterViews
    void initView() {
        setDialogWidthAndHeight(getView());
        mKeyBoard.setShowClean();
        mKeyBoard.setEditView(mValue);
        NumberKeyBoardUtils.hiddenSoftKeyboard(mValue);
    }

    @Click({R.id.btn_close, R.id.scan_btn, R.id.customer_verification})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.scan_btn:
                startScan();
                break;
            case R.id.customer_verification:
                tv_hint.setVisibility(View.GONE);
                doVerifyCode(mValue.getText().toString().trim(), tv_hint, getActivity());
                break;
        }
    }

    /**
     * 开启扫描
     */
    private void startScan() {
        scanPopupWindow = new ScanPopupWindow(getActivity());
        scanPopupWindow.showAtLocation(mScanBtn, Gravity.NO_GRAVITY, 0, 0);
        scanPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        scanPopupWindow.setOnScanBarcodeCallback(new ScanPopupWindow.ScanBarcodeCallback() {
            @Override
            public void onScanBarcode(String barcode) {
                mValue.setText(barcode);
            }
        });
    }


    /**
     * 请求服务器验劵
     *
     * @param ticketNo
     */
    public void doVerifyCode(String ticketNo, final TextView tvHint, final FragmentActivity activity) {
        if (activity == null || activity.isDestroyed()) {
            return;
        }
        if (TextUtils.isEmpty(ticketNo)) {
            ToastUtil.showLongToast(R.string.dinner_Wechat_code_not_null);
            return;
        }

        if (DinnerShopManager.getInstance().getShoppingCart().checkIsHaveWXC(ticketNo)) {
            ToastUtil.showLongToast(R.string.dinner_wechat_code_repeat);
            return;
        }
        CouponsOperates operates = OperatesFactory.create(CouponsOperates.class);
        EventResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>> listener = new EventResponseListener<LoyaltyTransferResp<WxCouponsInfoResp>>() {
            @Override
            public void onResponse(ResponseObject<LoyaltyTransferResp<WxCouponsInfoResp>> response) {
                if (isDestory(activity)) {
                    return;
                }
                if (ResponseObject.isOk(response)) {
                    if (LoyaltyTransferResp.isOk(response.getContent())) {
                        WeiXinCouponsInfo info = response.getContent().getResult().getWeixinCard();
                        WeiXinCouponsInfo.WeiXinCodeInfo codeInfo = info.getCode_info();
                        //判断是否是代金券,暂时只支持代金券
                        if (info.getCard_type() != WeiXinCardType.CASH) {
                            if (tvHint != null) {
                                tv_hint.setVisibility(View.VISIBLE);
                                tvHint.setText(getString(R.string.not_support_coupons));
                            }
                            return;
                        }
                        if (info.getCode_info().isCanConsume()) {
                            if (tvHint != null) {
                                tv_hint.setVisibility(View.VISIBLE);
                                tvHint.setText(response.getMessage());
                            }

                            Long id = response.getContent().getResult().getId();
                            if (id == null || DinnerShopManager.getInstance().getShoppingCart()
                                    .isAllowAddCoupon(DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo(), id)) {
                                addToShopcart(info);
                            } else {
                                ToastUtil.showShortToast(R.string.coupons_has_existed);
                            }
                        } else {
                            if (!TextUtils.isEmpty(codeInfo.getStatus())) {
                                if (tvHint != null) {
                                    tv_hint.setVisibility(View.VISIBLE);
                                    tvHint.setText(codeInfo.getStatus());
                                }
                            } else {
                                if (tvHint != null) {
                                    tv_hint.setVisibility(View.VISIBLE);
                                    tvHint.setText(codeInfo.getErrmsg());
                                }
                            }
                        }
                    } else {
                        if (tvHint != null) {
                            tv_hint.setVisibility(View.VISIBLE);
                            tvHint.setText(response.getContent().getErrorMessage());
                        }
                    }
                } else {
                    if (tvHint != null) {
                        tv_hint.setVisibility(View.VISIBLE);
                        tvHint.setText(response.getMessage());
                    }
                }
                UserActionEvent.end(UserActionEvent.DINNER_PAY_WECHAT_CARD_COUPON);
            }

            @Override
            public void onError(VolleyError error) {
                if (!isDestory(activity)) {
                    ToastUtil.showLongToast(error.getMessage());
                }
            }
        };
        operates.getWeiXinCouponsDetail(ticketNo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
    }

    private static boolean isDestory(Activity activity) {
        if (activity != null && !activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    /**
     * 加入购物车
     */
    private static void addToShopcart(WeiXinCouponsInfo info) {
        //特殊处理微信卡号
        String wxCardNumber = info.getCode();
        info.setCode(BaseShoppingCart.getNewWxCode(wxCardNumber));

        DinnerShopManager.getInstance().getShoppingCart().addWeiXinCouponsPrivilege(info);
    }

}
