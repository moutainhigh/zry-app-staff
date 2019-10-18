package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.zhongmei.yunfu.R;


public final class ViewUtils {
    private ViewUtils() {
    }


    static final int COLOR_TRADE_EMPTY = Color.parseColor("#6C747D");


    static final int COLOR_TRADE_UNISSUED = Color.parseColor("#FBB305");


    static final int COLOR_TRADE_ISSUED = Color.parseColor("#2CB8B1");


    static final int COLOR_TRADE_SERVING = Color.parseColor("#3AB3FB");


    static final int COLOR_TRADE_EMPTY_TRANSPARENT = Color.parseColor("#806C747D");


    static final int COLOR_TRADE_UNISSUED_TRANSPARENT = Color.parseColor("#80FBB305");


    static final int COLOR_TRADE_ISSUED_TRANSPARENT = Color.parseColor("#802CB8B1");


    static final int COLOR_TRADE_SERVING_TRANSPARENT = Color.parseColor("#803AB3FB");


    static final int COLOR_BEAUTY_ZONE_INDICATOR = Color.parseColor("#EE5E1F");

    static final int COLOR_ZONE_INDICATOR = Color.parseColor("#838C94");

    static final int COLOR_ZONE_INDICATOR_SELECTED = Color.parseColor("#2D3337");

    static final int COLOR_BEAUTY_ZONE_INDICATOR_SELECTED = Color.parseColor("#FFFFFF");

    static final int COLOR_TRADES_SELECTOR_BACKGROUND = Color.parseColor("#00000000");

    static final int COLOR_TRADES_SELECTOR_BACKGROUND_ENTERED = Color.parseColor("#DDE2E5");

    public static DinnertableView inflateDinnertableView(Context context) {
        return inflateView(context, R.layout.dinnertable_square);
    }




    public static DinnertableTradeView inflateTradeView(Context context) {
        return inflateView(context, R.layout.dinnertable_trade);
    }



    public static ViewGroup inflateZoneIndicatorItem(Context context) {
        return inflateView(context, R.layout.dinnertable_zone_indicator_item);
    }

    public static ViewGroup inflateZoneIndicatorVerticalItem(Context context) {
        return inflateView(context, R.layout.beautytable_zone_indicator_item);
    }


    public static NumberAndWaiterViewTable inflateDinnerNumberAndWaiterPanel(Context context) {
        return inflateView(context, R.layout.view_dinner_number_waiter);
    }



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
