package com.zhongmei.bty.base;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.slidemenu.listener.TradeSlideMenuListener;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.database.enums.EntranceType;

/**
 * Created by demo on 2018/12/15
 */
public class TestMainActivity extends BusinessMainActivity {

    @Override
    public EntranceType getEntranceType() {
        return EntranceType.DINNER;
    }

    @Override
    protected TradeSlideMenuListener getSlideMenuListener() {
        return new TradeSlideMenuListener() {
            @Override
            public boolean switchToTrade() {
                setLeftMenu(false);
                ToastUtil.showShortToast("switchToTrade");
                return true;
            }

            @Override
            public boolean switchToOrderCenter() {
                setLeftMenu(false);
                ToastUtil.showShortToast("switchToOrderCenter");
                return true;
            }

            @Override
            public boolean switchToShopManagement() {
                setLeftMenu(false);
                ToastUtil.showShortToast("switchToShopManagement");
                return true;
            }

            @Override
            public boolean switchToFormCenter() {
                setLeftMenu(false);
                ToastUtil.showShortToast("switchToFormCenter");
                return true;
            }

            @Override
            public boolean backHome() {
                setLeftMenu(false);
                ToastUtil.showShortToast("backHome");
                finish();
                return true;
            }
        };
    }

    @Override
    protected View getBusinessContentView() {
        LinearLayout ll = new LinearLayout(this);
        Button btnLeftMenu = new Button(this);
        btnLeftMenu.setText("slide菜单");
        btnLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLeftMenu();
            }
        });
        ll.addView(btnLeftMenu, new LinearLayout.LayoutParams(100, 50));
        Button btnNotifycenter = new Button(this);
        btnNotifycenter.setText("通知中心");
        btnNotifycenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDrawer();
            }
        });
        ll.addView(btnNotifycenter, new LinearLayout.LayoutParams(100, 50));

        return ll;
    }

    @NonNull
    @Override
    protected TextView getViewNotifyCenterTip() {
        return new TextView(this);
    }

    @NonNull
    @Override
    protected TextView getViewNotifyCenterOtherTip() {
        return new TextView(this);
    }
}
