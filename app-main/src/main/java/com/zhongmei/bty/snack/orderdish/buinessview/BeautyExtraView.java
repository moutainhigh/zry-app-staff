package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishListenerImp;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@EViewGroup(R.layout.add_material_view)
public class BeautyExtraView extends LinearLayout {

    private final static String TAG = BeautyExtraView.class.getSimpleName();

    public BeautyExtraView(Context context) {
        super(context);
    }

    public BeautyExtraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeautyExtraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private List<ExtraInfo> mList = null;

    private OrderDishListenerImp mListener;

    private boolean isBatchMode = false;

    public OrderDishListenerImp getListener() {
        return mListener;
    }

    public void setListener(OrderDishListenerImp mListener) {
        this.mListener = mListener;
    }

    private List<LinearLayout> rootViews;

    @ViewById(R.id.add_material_layout)
    LinearLayout addMaterialLayout;

    @AfterViews
    public void initView() {
        setOrientation(VERTICAL);
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);

    }


    public void setList(List<OrderExtra> list) {
        mList = toExtraInfos(list);
        inflateAddtion();
    }

    public void setListForBatchOperation(List<ExtraInfo> extraInfoList) {
        mList = extraInfoList;
        inflateAddtion();
    }

    private void inflateAddtion() {
        addMaterialLayout.removeAllViews();
        if (rootViews != null) {
            rootViews.clear();
            rootViews = null;
        }


        if (Utils.isEmpty(mList)) {
            TextView tv = new TextView(getContext());
            tv.setText(R.string.dish_no_extra);
            tv.setTextSize(13);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.but_gray));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 150);
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
            params.leftMargin = DensityUtil.dip2px(MainApplication.getInstance(), 26);
            params.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 26);

            Drawable drawable = getContext().getResources().getDrawable(R.drawable.beauty_icon_empty_status);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tv.setCompoundDrawables(null, drawable, null, null);
            tv.setCompoundDrawablePadding(DensityUtil.dip2px(MainApplication.getInstance(), 5));
            addMaterialLayout.addView(tv, params);
            return;
        }

        for (int i = 0; i < mList.size(); i++) {

            View root_view = LayoutInflater.from(getContext()).inflate(R.layout.add_material_item_view, null);
            TextView text_name = (TextView) root_view.findViewById(R.id.text_name);
            TextView text_price = (TextView) root_view.findViewById(R.id.text_price);
            LinearLayout ll = (LinearLayout) root_view.findViewById(R.id.count_ll0);
            TextView count = (TextView) root_view.findViewById(R.id.text_count);
            ImageView plus = (ImageView) root_view.findViewById(R.id.text_plus);
            ImageView minus = (ImageView) root_view.findViewById(R.id.text_minus);


            ExtraInfo extraInfo = mList.get(i);
            text_name.setText(extraInfo.getName());
            final LinearLayout parentView = (LinearLayout) (text_name.getParent());
            parentView.setId(i);
            if (extraInfo.getQty() != null && extraInfo.getQty().compareTo(BigDecimal.ZERO) == 1) {
                count.setText(MathDecimal.toTrimZeroString(extraInfo.getQty()));
                text_name.setMaxHeight(8);
                text_price.setText(formatPrice(MathDecimal.trimZero(extraInfo.getAmount())));
                parentView.setSelected(true);
            } else {
                text_price.setText(formatPrice(MathDecimal.trimZero(extraInfo.getPrice())));
                ll.setVisibility(View.GONE);
                parentView.setSelected(false);
            }

            ((View) text_name.getParent()).setTag(i);
            parentView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mList.get(v.getId()).getQty() == null
                            || mList.get(v.getId()).getQty().compareTo(BigDecimal.ZERO) == 0) {
                        LinearLayout llLayout = (LinearLayout) ((LinearLayout) v).getChildAt(2);
                        v.setSelected(true);
                        llLayout.setVisibility(View.VISIBLE);
                        TextView tvCount = (TextView) llLayout.getChildAt(1);
                        tvCount.setText("1");
                        mList.get(v.getId()).setQty(BigDecimal.ONE);
                        mListener.onAddMaterial(mList.get(v.getId()), BigDecimal.valueOf(1));
                    }
                                    }
            });
            minus.setId(i);
            minus.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = v.getId();
                    BigDecimal singleQty = mList.get(position).getQty();
                    if (singleQty.compareTo(BigDecimal.ZERO) > 0) {
                        int count = singleQty.intValue() - 1;
                        mList.get(position).setQty(BigDecimal.valueOf(count));
                        if (count == 0) {
                            hiddenCount(position);
                            parentView.setSelected(false);
                        } else {
                            findTextView(position).setText(count + "");
                            ((TextView) (rootViews.get(position)).getChildAt(1)).setText(
                                    formatPrice(MathDecimal.trimZero(mList.get(position).getPrice().multiply(mList.get(position).getQty()))));
                        }
                        if (mListener != null) {
                            mListener.onAddMaterial(mList.get(v.getId()), BigDecimal.valueOf(count));
                        }
                    } else {
                        hiddenCount(position);
                        parentView.setSelected(false);
                    }
                }

            });
            plus.setId(i);
            plus.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = v.getId();
                    int count = mList.get(position).getQty().intValue() + 1;
                    mList.get(position).setQty(BigDecimal.valueOf(count));
                    findTextView(position).setText(count + "");
                    ((TextView) (rootViews.get(position)).getChildAt(1)).setText(formatPrice(MathDecimal.trimZero(mList.get(position).getAmount())));
                    if (mListener != null) {
                        mListener.onAddMaterial(mList.get(v.getId()), BigDecimal.valueOf(count));
                    }
                }

            });
            if (rootViews == null)
                rootViews = new ArrayList<LinearLayout>();
            rootViews.add(parentView);
            addMaterialLayout.addView(root_view);
        }
    }

    private TextView findTextView(int position) {
        LinearLayout llLayout = (LinearLayout) (rootViews.get(position)).getChildAt(2);

        return (TextView) llLayout.getChildAt(1);
    }

    private void hiddenCount(int position) {
        LinearLayout llLayout = (LinearLayout) (rootViews.get(position)).getChildAt(2);
        llLayout.setVisibility(View.GONE);
    }

    public static String formatPrice(BigDecimal value) {
        try {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return ShopInfoCfg.getInstance().getCurrencySymbol() + value;
            } else {
                return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.negate(value);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return value + "";
        }
    }


    private static List<ExtraInfo> toExtraInfos(List<OrderExtra> list) {
        ArrayList<ExtraInfo> resultList = new ArrayList<>();
        for (OrderExtra orderExtra : list) {
            resultList.add(new ExtraInfo(orderExtra, true));
        }
        return resultList;
    }

    private static List<ExtraInfo> toExtraInfosForBatchOperation(List<OrderExtra> list) {
        ArrayList<ExtraInfo> resultList = new ArrayList<>();
        for (OrderExtra orderExtra : list) {
            ExtraInfo extraInfo = new ExtraInfo(orderExtra, false);
            extraInfo.qty = BigDecimal.ZERO;
            resultList.add(extraInfo);
        }
        return resultList;
    }

}
