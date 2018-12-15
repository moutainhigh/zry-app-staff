package com.zhongmei.bty.mobilepay.fragment.mobilepay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.MobilePayMenuItem;
import com.zhongmei.bty.mobilepay.bean.MobliePayMenuTool;
import com.zhongmei.bty.mobilepay.views.MobilePayModeChooseView;
import com.zhongmei.bty.basemodule.commonbusiness.view.ShowBarcodeView;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;

import java.util.List;


/**
 * 移动支付二维码展示界面
 * Created by demo on 2018/12/15
 */
public class MobilePayCodeFragment extends BasicFragment implements View.OnClickListener, ShowBarcodeView.OnChickRetryListener {

    public static final String TAG = MobilePayCodeFragment.class.getSimpleName();

    protected TextView mTvPayTimeoutAlert;

    protected TextView mTvRefreshPayState;

    protected ShowBarcodeView mShowBarcodeView;

    protected LinearLayout mLlPayTypes;

    protected MobilePayModeChooseView mChooseView;

    private IPaymentInfo mPaymentInfo;

    private MobliePayMenuTool mobliePayMenuTool;

    private List<MobilePayMenuItem> menuItemList;

    private MobilePayMenuItem mCurrentMenuItem;

    private MobilePayDialog.MobilePayCallBack mobilePayCallBack;

    public static MobilePayCodeFragment newInstance(IPaymentInfo paymentInfo, MobilePayDialog.MobilePayCallBack payCallBack) {
        MobilePayCodeFragment mobilePayCodeFragment = new MobilePayCodeFragment();
        mobilePayCodeFragment.mPaymentInfo = paymentInfo;
        mobilePayCodeFragment.mobilePayCallBack = payCallBack;
        return mobilePayCodeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_mobile_pay_scanned_fragment_layout, container, false);
        mobliePayMenuTool = new MobliePayMenuTool(getContext());
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        mTvPayTimeoutAlert = (TextView) view.findViewById(R.id.paytimeoutAlert);
        mTvRefreshPayState = (TextView) view.findViewById(R.id.tv_refresh_pay_state);
        mShowBarcodeView = (ShowBarcodeView) view.findViewById(R.id.showBarcode);
        mLlPayTypes = (LinearLayout) view.findViewById(R.id.ll_loginType);
        mChooseView = (MobilePayModeChooseView) view.findViewById(R.id.choose_mode_view);
        mShowBarcodeView.setChickRetryListener(this);
        mTvRefreshPayState.setOnClickListener(this);
        menuItemList = mobliePayMenuTool.getShowCodeMenus();
        mCurrentMenuItem = null;
        if (Utils.isNotEmpty(menuItemList)) {
            boolean temp = false;
            if (menuItemList.size() == 1) {
                menuItemList.get(0).isSelected = true;
                temp = true;
            } else {
                for (MobilePayMenuItem item : menuItemList) {
                    if (item.payModeId == PayModeId.MOBILE_PAY) {
                        item.isSelected = true;
                        temp = true;
                    }
                }
            }
            if (!temp) {
                mShowBarcodeView.setShowType(ShowBarcodeView.SHOW_QR_NOT_ASSIGN);
            }
        }
        mChooseView.setMenuItemList(menuItemList);
        mChooseView.setCheckMobilePayModeListener(new MobilePayModeChooseView.OnCheckMobilePayModeListener() {
            @Override
            public void onCheck(MobilePayMenuItem menuItem) {
                if (mCurrentMenuItem != null && menuItem.payModeId == mCurrentMenuItem.payModeId) {
                    return;
                }
                mCurrentMenuItem = menuItem;
                setSupportPayTypeView();
                mobilePayCallBack.stopUpdateOutTime();
                hidPayStatusButton();
                hidTimeoutAlert();
                mobilePayCallBack.getPayBarcode(mCurrentMenuItem.payModeId);
            }
        });
    }

    private void setSupportPayTypeView() {
        if (mCurrentMenuItem == null) return;
        List<MobilePayMenuItem> menusList = mCurrentMenuItem.getChildMenus();
        ImageView imageView;
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Utils.isNotEmpty(menusList)) {
            mLlPayTypes.removeAllViews();
            for (MobilePayMenuItem item : menusList) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setPadding(DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2));
                imageView.setImageResource(item.payModeIcon);
                mLlPayTypes.addView(imageView);
            }
        } else {
            mLlPayTypes.removeAllViews();
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2));
            imageView.setImageResource(mCurrentMenuItem.payModeIcon);
            mLlPayTypes.addView(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        if (v.getId() == R.id.tv_refresh_pay_state) {
            mobilePayCallBack.getPayState();
        }
    }

    public void showPayStatusButton() {
        mTvRefreshPayState.setVisibility(View.VISIBLE);
    }

    public void hidPayStatusButton() {
        mTvRefreshPayState.setVisibility(View.INVISIBLE);
    }

    public void showBarcode(Bitmap bitmap) {
        mShowBarcodeView.setShowQR(bitmap);
    }

    public void showBarcodeType(int showType) {
        mShowBarcodeView.setShowType(showType);
    }

    public void showTimeoutAlert(SpannableStringBuilder builder) {
        if (getActivity() != null && isAdded()) {
            if (builder != null) {
                mTvPayTimeoutAlert.setText(builder);
                mTvPayTimeoutAlert.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hidTimeoutAlert() {
        if (getActivity() != null && isAdded()) {
            mTvPayTimeoutAlert.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void retry() {
        if (mCurrentMenuItem != null)
            mobilePayCallBack.getPayBarcode(mCurrentMenuItem.payModeId);
    }
}
