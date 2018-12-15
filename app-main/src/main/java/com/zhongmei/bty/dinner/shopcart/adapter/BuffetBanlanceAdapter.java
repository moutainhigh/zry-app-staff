package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.trade.bean.OutTimeInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by dzb 2017/06/26
 */
public class BuffetBanlanceAdapter extends DinnerBanlanceAdapter {
    private Context mContext;
    private Map<Integer, List<IShopcartItem>> dishGroup = null;

    protected BigDecimal shellActumalAmount = BigDecimal.ZERO;

    /**
     * @param context
     * @Constructor
     * @Description 构造函数，
     */
    public BuffetBanlanceAdapter(Context context) {
        super(context);
        this.mContext = context;
    }


    /**
     * 获取餐标下菜品的实际价格
     *
     * @return
     */
    public BigDecimal getShellActumalAmount() {
        return shellActumalAmount;
    }


    public void updateGroupData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        this.data.clear();// 清空数据
        this.mAllDishCount = BigDecimal.ZERO;

        this.dishGroup = BuffetAdapterUtil.buildBuffetShopcartData(context, tradeVo, dataList, data, this);
        updateTrade(tradeVo, isShowInvalid);// 构建整单属性显示对象并刷新列表
//        updateOutTimeFeeItem(tradeVo);
    }


    protected void buildExtraCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.ADDITIONAL);
                    // 下个迭代使用这个name
                    // item.setName(tradePrivilege.getSurchargeName());
                    ExtraCharge extraCharge = tradeVo.getMinconExtraCharge();
                    if (extraCharge != null && extraCharge.getStatusFlag() == StatusFlag.VALID
                            && tradePrivilege.getPromoId() != null && extraCharge.getId().compareTo(tradePrivilege.getPromoId()) == 0) {
                        item.setExtraType(ExtraItemType.MIN_CONSUM);
                    } else {
                        extraCharge = ExtraManager.getExtraChargeById(tradeVo, tradePrivilege.getPromoId());
                        if (extraCharge == null || extraCharge.getStatusFlag() == StatusFlag.INVALID) {
                            continue;
                        }
                    }
                    item.setName(extraCharge.getName());
                    double value = tradePrivilege == null ? 0 : tradePrivilege.getPrivilegeAmount().doubleValue();
                    item.setValue(value);
                    item.setExtraCharge(extraCharge);
                    //if(extraCharge.getCode().equals(ExtraManager.BUFFET_MIN_CONSUM))
                    //    item.setExtraType(ExtraItemType.MIN_CONSUM);
                    data.add(item);
                }
            }
        }

    }

    @Override
    protected void showBuffetExtra(BuffetExtraHolder buffetExtraHolder, final DishDataItem item) {
        super.showBuffetExtra(buffetExtraHolder, item);
        if (item.getExtraType() == ExtraItemType.DEPOSIT && !DinnerShoppingCart.getInstance().getOrder().isPaidTradeposit()) {
            buffetExtraHolder.btn_Edit.setVisibility(View.VISIBLE);
            buffetExtraHolder.btn_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NumberInputdialog inputdialog = new NumberInputdialog(context,
                            context.getString(R.string.buffet_deposit_edit_dialog_title),
                            context.getString(R.string.buffet_deposit_edit_dialog_hint),
                            MathDecimal.toDecimalFormatString(item.getValue()),
                            10000, mInputListener);
                    inputdialog.setNumberType(2);
                    inputdialog.show();
                }
            });
        } else {
            buffetExtraHolder.btn_Edit.setVisibility(View.GONE);
        }
    }

    private NumberInputdialog.InputOverListener mInputListener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {
            DinnerShoppingCart.getInstance().modifyDeposit(Double.valueOf(inputContent));
        }
    };

    @Override
    public void updateOutTimeFeeItem(TradeVo tradeVo) {//计算超时费用
        //计算附加费
        boolean outTimeFeeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

        if (!outTimeFeeEnable) {
            if (Utils.isNotEmpty(tradeVo.getOutTimeFeePrivaleges())) {
                for (TradePrivilege tradePrivilege : tradeVo.getOutTimeFeePrivaleges()) {
                    tradePrivilege.setInValid();
                }
            }
            return;
        }

        ExtraCharge extraChargeOutTime = ServerSettingCache.getInstance().getmOutTimeRule();

        OutTimeInfo outTimeInfo = ServerSettingCache.getInstance().getBuffetOutTimeFeeRule();

        long spendMinute = (System.currentTimeMillis() - tradeVo.getTrade().getServerCreateTime()) / (60 * 1000);//就餐用时（分钟）

        long diffTime = outTimeInfo.getLimitTimeLine() - spendMinute;

        if (diffTime > 0) {//还没有超时
            return;
        }

        int outTimeCount = (int) Math.ceil(Math.abs((double) diffTime) / outTimeInfo.getOutTimeUnit());

        BigDecimal mOutTimeFee = BigDecimal.valueOf(outTimeCount).multiply(outTimeInfo.getOutTimeFee()).multiply(BigDecimal.valueOf(tradeVo.getTrade().getTradePeopleCount()));

        String hint = String.format(mContext.getResources().getString(R.string.buffet_outtime_hint), Math.abs(diffTime) + "", MathDecimal.toDecimalFormatString(outTimeInfo.getOutTimeFee()), MathDecimal.toDecimalFormatString(outTimeInfo.getOutTimeUnit()));//50/60分钟


        BigDecimal tradeOutTimeFee = BigDecimal.ZERO;
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        Map<Long, ExtraCharge> extraChargeMap = tradeVo.getExtraChargeMap();
        List<TradePrivilege> tmpOutTimePrivilege = new ArrayList<TradePrivilege>();

        if (tradeprivileges == null) {
            tradeprivileges = new ArrayList<TradePrivilege>();
            tradeVo.setTradePrivileges(tradeprivileges);
        }

        for (TradePrivilege tradePrivilege : tradeprivileges) {
            if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.getPromoId() != null && extraChargeOutTime.getId().compareTo(tradePrivilege.getPromoId()) == 0) {
                tradePrivilege.setStatusFlag(StatusFlag.VALID);//全部设置为有效
                tmpOutTimePrivilege.add(tradePrivilege);
            }
        }


        int outTimeCountDiff = outTimeCount - tmpOutTimePrivilege.size();

        Map<Long, ExtraCharge> mapExtraCharge = tradeVo.getExtraChargeMap();

        if (mapExtraCharge == null) {
            mapExtraCharge = new HashMap<Long, ExtraCharge>();
            tradeVo.setExtraChargeMap(mapExtraCharge);
        }

        for (int i = 0; i < outTimeCountDiff; i++) {
            //加入超时费
            tradeprivileges.add(buildOutTimeTradePrivilege(extraChargeOutTime, null, tradeVo));
        }

        MathShoppingCartTool.mathTotalPrice(DinnerShoppingCart.getInstance().getShoppingCartDish(), tradeVo);

    }


    private TradePrivilege buildOutTimeTradePrivilege(ExtraCharge extraCharge, TradePrivilege privilege, TradeVo tradeVo) {
        return BuildPrivilegeTool.buildExtraChargePrivilege(tradeVo,
                privilege,
                extraCharge, BigDecimal.ZERO);
    }

    /**
     * 返回菜品分组显示
     *
     * @return
     */
    public Map<Integer, List<IShopcartItem>> getGroup() {
        return dishGroup;
    }

    @Override
    protected View initDishCarteNormView(LayoutInflater inflater) {
        View ContentView = super.initDishCarteNormView(inflater);
        ContentView.findViewById(R.id.layout_parent).setPadding(DensityUtil.dip2px(mContext, 20), 0, DensityUtil.dip2px(mContext, 20), 0);
        return ContentView;
    }

    @Override
    protected View initBuffetExtraView(LayoutInflater inflater) {
        View ContentView = super.initBuffetExtraView(inflater);
        ContentView.findViewById(R.id.layout_parent).setPadding(DensityUtil.dip2px(mContext, 20), 0, DensityUtil.dip2px(mContext, 20), 0);
        return ContentView;
    }
}
