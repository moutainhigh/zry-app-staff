package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.zhongmei.yunfu.R;

/**
 * @version: 1.0
 * @date 2015年9月8日
 */
public final class ViewUtils {
    private ViewUtils() {
    }

    /**
     * 空单颜色
     */
    static final int COLOR_TRADE_EMPTY = Color.parseColor("#6C747D");

    /**
     * 未出单单据颜色
     */
    static final int COLOR_TRADE_UNISSUED = Color.parseColor("#FBB305");

    /**
     * 已出单单据颜色
     */
    static final int COLOR_TRADE_ISSUED = Color.parseColor("#2CB8B1");

    /**
     * 已上菜单据颜色
     */
    static final int COLOR_TRADE_SERVING = Color.parseColor("#3AB3FB");

    /**
     * 半透明的空单颜色
     */
    static final int COLOR_TRADE_EMPTY_TRANSPARENT = Color.parseColor("#806C747D");

    /**
     * 半透明的未出单单据颜色
     */
    static final int COLOR_TRADE_UNISSUED_TRANSPARENT = Color.parseColor("#80FBB305");

    /**
     * 半透明的已出单单据颜色
     */
    static final int COLOR_TRADE_ISSUED_TRANSPARENT = Color.parseColor("#802CB8B1");

    /**
     * 半透明的已上菜单据颜色
     */
    static final int COLOR_TRADE_SERVING_TRANSPARENT = Color.parseColor("#803AB3FB");

    /**
     *
     */
    static final int COLOR_BEAUTY_ZONE_INDICATOR = Color.parseColor("#EE5E1F");

    static final int COLOR_ZONE_INDICATOR = Color.parseColor("#838C94");

    static final int COLOR_ZONE_INDICATOR_SELECTED = Color.parseColor("#2D3337");

    static final int COLOR_BEAUTY_ZONE_INDICATOR_SELECTED = Color.parseColor("#FFFFFF");

    static final int COLOR_TRADES_SELECTOR_BACKGROUND = Color.parseColor("#00000000");

    static final int COLOR_TRADES_SELECTOR_BACKGROUND_ENTERED = Color.parseColor("#DDE2E5");

    public static DinnertableView inflateDinnertableView(Context context) {
        return inflateView(context, R.layout.dinnertable_square);
    }


    /*public static BuffetTableView inflateBuffettableView(Context context) {
        return inflateView(context, R.layout.buffettable_square);
    }*/

    public static DinnertableTradeView inflateTradeView(Context context) {
        return inflateView(context, R.layout.dinnertable_trade);
    }

    /*public static BuffetTableTradeView inflateBuffetTradeView(Context context) {
        return inflateView(context, R.layout.buffettable_trade);
    }*/

    public static ViewGroup inflateZoneIndicatorItem(Context context) {
        return inflateView(context, R.layout.dinnertable_zone_indicator_item);
    }

    public static ViewGroup inflateZoneIndicatorVerticalItem(Context context) {
        return inflateView(context, R.layout.beautytable_zone_indicator_item);
    }


    public static NumberAndWaiterViewTable inflateDinnerNumberAndWaiterPanel(Context context) {
        return inflateView(context, R.layout.view_dinner_number_waiter);
    }

    /*public static BuffetNumberAndWaiterViewTable inflateBuffetNumberAndWaiterPanel(Context context) {
        return inflateView(context, R.layout.buffet_number_and_waiter_table);
    }*/

    @SuppressWarnings("unchecked")
    public static <V> V inflateView(Context context, int id) {
        return (V) LayoutInflater.from(context).inflate(id, null);
    }

    public static int getListWidth(Context context, BaseAdapter adapter) {
        int maxWidth = 0;
        View view = null;
        FrameLayout fakeParent = new FrameLayout(context);
        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            view = adapter.getView(i, view, fakeParent);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int width = view.getMeasuredWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

}
