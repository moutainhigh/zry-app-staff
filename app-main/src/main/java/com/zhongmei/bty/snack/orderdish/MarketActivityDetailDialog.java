package com.zhongmei.bty.snack.orderdish;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.yunfu.util.MathDecimal;

/**
 * @Date： 16/4/28
 * @Description: 活动详情
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MarketActivityDetailDialog extends BasicDialogFragment {

    private MarketRuleVo mRuleVo;

    private TextView mName, mType, mDate, mTime, mCondition, mCustomer, mTermType, mDeliveryType, mDishInfo;

    public static MarketActivityDetailDialog newInstance(MarketRuleVo vo) {
        MarketActivityDetailDialog f = new MarketActivityDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("rulevo", vo);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mRuleVo = (MarketRuleVo) bundle.getSerializable("rulevo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_market_activity_detail_layout, container);
        mName = (TextView) view.findViewById(R.id.tv_name);
        mType = (TextView) view.findViewById(R.id.tv_type);
        mDate = (TextView) view.findViewById(R.id.tv_date);
        mTime = (TextView) view.findViewById(R.id.tv_time);
        mCondition = (TextView) view.findViewById(R.id.tv_condition);
        mCustomer = (TextView) view.findViewById(R.id.tv_customer_info);
        mTermType = (TextView) view.findViewById(R.id.tv_termType);
        mDeliveryType = (TextView) view.findViewById(R.id.tv_deliveryType);
        mDishInfo = (TextView) view.findViewById(R.id.tv_dish_info);

        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (mRuleVo != null) {
            // 活动名称
            mName.setText(mRuleVo.getMarketPlan().getPlanName());
            // 类型
            String condition = "";
            switch (mRuleVo.getPromotionType()) {
                case MINUS: // 满减
                    mType.setText(R.string.full_cut);

                    if (mRuleVo.getMarketActivityRule().getPayment() != null)
                        condition += getString(R.string.full) + MathDecimal.trimZero(mRuleVo.getMarketActivityRule().getPayment());
                    if (mRuleVo.getMarketActivityRule().getReduce() != null)
                        condition += getString(R.string.cut) + MathDecimal.trimZero(mRuleVo.getMarketActivityRule().getReduce());
                    break;
                case DISCOUNT: // 折扣
                    mType.setText(R.string.discount);
                    if (mRuleVo.getMarketActivityRule().getName() != null)
                        condition = mRuleVo.getMarketActivityRule().getName();
                    break;
                case GIFT: // 赠送
                    mType.setText(R.string.give);
                    if (mRuleVo.getMarketActivityRule().getName() != null)
                        condition = mRuleVo.getMarketActivityRule().getName();
                    break;
                case SPECAILPRICE: // 特价
                    mType.setText(R.string.special_price);
                    if (mRuleVo.getMarketActivityRule().getName() != null)
                        condition = mRuleVo.getMarketActivityRule().getName();
                    break;
                default:
                    break;
            }
            mCondition.setText(condition);
            // 活动日期
            mDate.setText(mRuleVo.getMarketPlan().getPlanStartDay() + "-" + mRuleVo.getMarketPlan().getPlanEndDay());
        }
        // 时间段
        String time = "";
        if (mRuleVo.getMarketActivityRule().getPeriodStart() != null)
            time += mRuleVo.getMarketActivityRule().getPeriodStart();
        if (mRuleVo.getMarketActivityRule().getPeriodEnd() != null)
            time += "-" + mRuleVo.getMarketActivityRule().getPeriodEnd();

        mTime.setText(time);

        // 参与人群
        String customer = "";
        if (mRuleVo.isContainsUserType(UserType.MEMBER)) {
            customer += getString(R.string.customer_member);
        }
        if (mRuleVo.isContainsUserType(UserType.MEMBERNON)) {
            if (TextUtils.isEmpty(customer))
                customer += getString(R.string.customer_not_menber);
            else customer += "、" + getString(R.string.customer_not_menber) + "    ";
        }

        mCustomer.setText(customer);
        // 单据类型
        String deliveryType = "";
        if (mRuleVo.isContainsDeliveryType(DeliveryType.HERE)) {
            deliveryType += getString(R.string.order_here);
        }
        if (mRuleVo.isContainsDeliveryType(DeliveryType.SEND)) {
            if (TextUtils.isEmpty(deliveryType))
                deliveryType += getString(R.string.order_send);
            else deliveryType += "、" + getString(R.string.order_send);
        }
        if (mRuleVo.isContainsDeliveryType(DeliveryType.TAKE)) {
            if (TextUtils.isEmpty(deliveryType))
                deliveryType += getString(R.string.order_take);
            else deliveryType += "、" + getString(R.string.order_take);
        }
        if (mRuleVo.isContainsDeliveryType(DeliveryType.CARRY)) {
            if (TextUtils.isEmpty(deliveryType))
                deliveryType += getString(R.string.packup);
            else deliveryType += "、" + getString(R.string.packup);
        }
        mDeliveryType.setText(deliveryType);
        // 活动终端
        List<Integer> pos = mRuleVo.getActivityPos();
        String activityPos = "";
        if (pos != null) {
            if (pos.contains(1)) {
                activityPos += "POS";
            }
            if (pos.contains(2)) {
                if (TextUtils.isEmpty(activityPos))
                    activityPos += getString(R.string.dinner_wechat);
                else activityPos += "、" + getString(R.string.dinner_wechat);
            }

            if (pos.contains(3)) {
                if (TextUtils.isEmpty(activityPos))
                    activityPos += getString(R.string.order_kiosk);
                else activityPos += "、" + getString(R.string.order_kiosk);
            }
        } else {
            activityPos = getString(R.string.pos_cash);
        }

        mTermType.setText(activityPos);
        if (Utils.isNotEmpty(mRuleVo.getMarketActivityDishList())) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < mRuleVo.getMarketActivityDishList().size(); i++) {
                if (i == mRuleVo.getMarketActivityDishList().size() - 1) {
                    buffer.append(mRuleVo.getMarketActivityDishList().get(i).getDishName());
                    break;
                }
                buffer.append(mRuleVo.getMarketActivityDishList().get(i).getDishName()).append(",");
            }
            mDishInfo.setText(buffer.toString());
        } else {
            mDishInfo.setText(R.string.dinner_formcenter_goods_all);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogWidthAndHeight(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
}
