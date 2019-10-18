package com.zhongmei.bty.basemodule.slidemenu.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;

import com.zhongmei.bty.basemodule.slidemenu.listener.TradeSlideMenuListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(resName = "slidemenu_trade_slide_menu")
public class TradeSlideMenuView extends SlideMenuView {

    public final static int PAGE_TRADE = 1;
    public final static int PAGE_ORDER_CENTER = 2;
    public final static int PAGE_SHOP_MANAGEMENT = 3;
    public final static int PAGE_FORM_CENTER = 4;

    private int pageTag = PAGE_TRADE;

    @ViewById(resName = "rb_trade")
    protected RadioButton rbTrade;

    @ViewById(resName = "rb_order_center")
    protected RadioButton rbOrderCenter;

    @ViewById(resName = "rb_shop_management")
    protected RadioButton rbShopManagement;

    @ViewById(resName = "rb_form_center")
    protected RadioButton rbFormCenter;

    @ViewById(resName = "tv_back_home")
    protected TextView tvBackHome;

    private int slideMenuType = SLIDE_MENU_TYPE_TRADE;

    public TradeSlideMenuView(Context context, int slideMenuType) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundResource(R.drawable.slidemenu_cashier_slide_menu_bg);
        this.slideMenuType = slideMenuType;
    }

    @AfterViews
    void init() {
        switch (slideMenuType) {
            case SlideMenuView.SLIDE_MENU_TYPE_TRADE:
                rbTrade.setText(R.string.slidemenu_dinner_tables);
                break;
            case SlideMenuView.SLIDE_MENU_TYPE_GROUP:
                rbTrade.setText(R.string.slidemenu_group_list_title);
                break;
        }

        rbTrade.setChecked(true);
    }

    @Click(resName = {"rb_trade", "rb_order_center", "rb_shop_management", "rb_form_center", "tv_back_home"})
    void click(View v) {
        TradeSlideMenuListener tradeSlideMenuListener = null;
        if (slideMenuListener != null && slideMenuListener instanceof TradeSlideMenuListener) {
            tradeSlideMenuListener = (TradeSlideMenuListener) slideMenuListener;
        }

        if (v.getId() == R.id.rb_trade) {
            if (pageTag != PAGE_TRADE) {
                if (tradeSlideMenuListener != null) {
                    tradeSlideMenuListener.switchToTrade();
                }

                pageTag = PAGE_TRADE;
            }
        } else if (v.getId() == R.id.rb_order_center) {
            if (pageTag != PAGE_ORDER_CENTER) {
                if (tradeSlideMenuListener != null) {
                    tradeSlideMenuListener.switchToOrderCenter();
                }

                pageTag = PAGE_ORDER_CENTER;
            }
        } else if (v.getId() == R.id.rb_shop_management) {
            if (pageTag != PAGE_SHOP_MANAGEMENT) {
                if (tradeSlideMenuListener != null) {
                    tradeSlideMenuListener.switchToShopManagement();
                }

                pageTag = PAGE_SHOP_MANAGEMENT;
            }
        } else if (v.getId() == R.id.rb_form_center) {
            if (pageTag != PAGE_FORM_CENTER) {
                if (tradeSlideMenuListener != null) {
                    tradeSlideMenuListener.switchToFormCenter();
                }

                pageTag = PAGE_FORM_CENTER;
            }
        } else if (v.getId() == R.id.tv_back_home) {
            if (tradeSlideMenuListener != null) {
                tradeSlideMenuListener.backHome();
            }
        }
    }

    @Override
    public void onModelClick(int pageNo) {
        pageTag = pageNo;
        switch (pageNo) {
            case PAGE_TRADE:
                rbTrade.setChecked(true);
                break;
            case PAGE_ORDER_CENTER:
                rbOrderCenter.setChecked(true);
                break;
            case PAGE_SHOP_MANAGEMENT:
                rbShopManagement.setChecked(true);
                break;
            case PAGE_FORM_CENTER:
                rbFormCenter.setChecked(true);
                break;

        }
    }

}
