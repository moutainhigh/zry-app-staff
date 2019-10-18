package com.zhongmei.bty.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.commonmodule.component.IPanelSettings;


public class Keyboard26WithClear extends LinearLayout implements OnClickListener {

    private int[][] marginNormal = {{36, 36, 18}, {36, 36, 12}, {36, 36, 12}, {36, 36, 12}};
    private int[][] marginMini = {{5, 5, 28}, {5, 5, 18}, {5, 5, 18}, {5, 5, 18}};
    private int[] itemMargin = {5, 2};
    private int[] layoutHeight = {60, 50};
    ClickEventListener listener;
    private Context mContext;

    public Keyboard26WithClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.keyboard_26_with_clear_backspace, this, true);
    }

    public Keyboard26WithClear(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard26WithClear(Context context) {
        this(context, null);
    }

    @Override
    protected void onAttachedToWindow() {
                super.onAttachedToWindow();
        findViewById(R.id.keyboard_a).setOnClickListener(this);
        findViewById(R.id.keyboard_b).setOnClickListener(this);
        findViewById(R.id.keyboard_c).setOnClickListener(this);
        findViewById(R.id.keyboard_d).setOnClickListener(this);
        findViewById(R.id.keyboard_e).setOnClickListener(this);
        findViewById(R.id.keyboard_f).setOnClickListener(this);
        findViewById(R.id.keyboard_g).setOnClickListener(this);
        findViewById(R.id.keyboard_h).setOnClickListener(this);
        findViewById(R.id.keyboard_i).setOnClickListener(this);
        findViewById(R.id.keyboard_j).setOnClickListener(this);
        findViewById(R.id.keyboard_k).setOnClickListener(this);
        findViewById(R.id.keyboard_l).setOnClickListener(this);
        findViewById(R.id.keyboard_dot).setOnClickListener(this);
        findViewById(R.id.keyboard_m).setOnClickListener(this);
        findViewById(R.id.keyboard_n).setOnClickListener(this);
        findViewById(R.id.keyboard_o).setOnClickListener(this);
        findViewById(R.id.keyboard_p).setOnClickListener(this);
        findViewById(R.id.keyboard_q).setOnClickListener(this);
        findViewById(R.id.keyboard_r).setOnClickListener(this);
        findViewById(R.id.keyboard_s).setOnClickListener(this);
        findViewById(R.id.keyboard_t).setOnClickListener(this);
        findViewById(R.id.keyboard_u).setOnClickListener(this);
        findViewById(R.id.keyboard_v).setOnClickListener(this);
        findViewById(R.id.keyboard_w).setOnClickListener(this);
        findViewById(R.id.keyboard_x).setOnClickListener(this);
        findViewById(R.id.keyboard_y).setOnClickListener(this);
        findViewById(R.id.keyboard_z).setOnClickListener(this);
        findViewById(R.id.num_1).setOnClickListener(this);
        findViewById(R.id.num_2).setOnClickListener(this);
        findViewById(R.id.num_3).setOnClickListener(this);
        findViewById(R.id.num_4).setOnClickListener(this);
        findViewById(R.id.num_5).setOnClickListener(this);
        findViewById(R.id.num_6).setOnClickListener(this);
        findViewById(R.id.num_7).setOnClickListener(this);
        findViewById(R.id.num_8).setOnClickListener(this);
        findViewById(R.id.num_9).setOnClickListener(this);
        findViewById(R.id.num_0).setOnClickListener(this);
        findViewById(R.id.keyboard_backspace).setOnClickListener(this);
        findViewById(R.id.keyboard_clear).setOnClickListener(this);
        resize();
    }

    private void resize() {
        int layoutStyle = SettingManager.getSettings(IPanelSettings.class).getPanel();
        LinearLayout numberLayout = (LinearLayout) findViewById(R.id.number_layout);
        LinearLayout q2pLayout = (LinearLayout) findViewById(R.id.q2p_layout);
        LinearLayout a2lLayout = (LinearLayout) findViewById(R.id.a2l_layout);
        LinearLayout z2mLayout = (LinearLayout) findViewById(R.id.z2m_layout);
        if (layoutStyle == IPanelSettings.PANEL_TYPE_2) {
            layoutMargin(numberLayout, marginMini[0][0], marginMini[0][1], marginMini[0][2], layoutHeight[1]);
            layoutMargin(q2pLayout, marginMini[1][0], marginMini[1][1], marginMini[1][2], layoutHeight[1]);
            layoutMargin(a2lLayout, marginMini[2][0], marginMini[2][1], marginMini[2][2], layoutHeight[1]);
            layoutMargin(z2mLayout, marginMini[3][0], marginMini[3][1], marginMini[3][2], layoutHeight[1]);
            resizeChildMargin(numberLayout, itemMargin[1]);
            resizeChildMargin(q2pLayout, itemMargin[1]);
            resizeChildMargin(a2lLayout, itemMargin[1]);
            resizeChildMargin(z2mLayout, itemMargin[1]);
        } else {
            layoutMargin(numberLayout, marginNormal[0][0], marginNormal[0][1], marginNormal[0][2], layoutHeight[0]);
            layoutMargin(q2pLayout, marginNormal[1][0], marginNormal[1][1], marginNormal[1][2], layoutHeight[0]);
            layoutMargin(a2lLayout, marginNormal[2][0], marginNormal[2][1], marginNormal[2][2], layoutHeight[0]);
            layoutMargin(z2mLayout, marginNormal[3][0], marginNormal[3][1], marginNormal[3][2], layoutHeight[0]);
            resizeChildMargin(numberLayout, itemMargin[0]);
            resizeChildMargin(q2pLayout, itemMargin[0]);
            resizeChildMargin(a2lLayout, itemMargin[0]);
            resizeChildMargin(z2mLayout, itemMargin[0]);
        }
    }

    private void layoutMargin(LinearLayout layout, int dipValue2, int dipValue3, int dipValue4, int height) {
        MarginLayoutParams params = (MarginLayoutParams) layout.getLayoutParams();
        params.height = DensityUtil.dip2px(mContext, height);
        params.leftMargin = DensityUtil.dip2px(mContext, dipValue2);
        params.rightMargin = DensityUtil.dip2px(mContext, dipValue3);
        params.topMargin = DensityUtil.dip2px(mContext, dipValue4);
    }

    private void resizeChildMargin(LinearLayout layout, int marin) {
        int childCount = layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            MarginLayoutParams params = (MarginLayoutParams) layout.getChildAt(i).getLayoutParams();
            params.leftMargin = DensityUtil.dip2px(mContext, marin);
        }
    }


    public interface ClickEventListener {
        public void onClick(View v);

        public void onClear();

        public void onBackspace();
    }

    ;

    public void setListener(ClickEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            throw new RuntimeException("please set a listener first");
                    }
        switch (v.getId()) {
            case R.id.keyboard_clear: {
                listener.onClear();
                break;
            }
            case R.id.keyboard_backspace: {
                listener.onBackspace();
                break;
            }
            default: {
                listener.onClick(v);
                break;
            }
        }

    }
}
