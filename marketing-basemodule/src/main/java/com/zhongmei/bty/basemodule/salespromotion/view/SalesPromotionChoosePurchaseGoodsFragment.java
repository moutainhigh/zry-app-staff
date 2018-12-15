package com.zhongmei.bty.basemodule.salespromotion.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionPolicyDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class SalesPromotionChoosePurchaseGoodsFragment extends BasicDialogFragment implements View.OnClickListener {

    private static String SALE_PROMOTION_RULE_VO = "salesPromotionRuleVo";

    private ImageView mBtnClose;
    private RecyclerView mRvChooseGoodsList;
    private Button mBtnCancel;
    private Button mBtnChoose;

    private ChoosePurchaseGoodsAdapter choosePurchaseGoodsAdapter;

    private List<DishVoChoiceWrap> dishVos = new ArrayList<>();

    List<SalesPromotionPolicyDish> salesPromotionPolicyDishes1;
    private SalesPromotionRuleVo salesPromotionRuleVo;

    private ISwitchDishListener switchDishListener;
    private DishManager dishManager = new DishManager();
    private BigDecimal shopNumber;

    public void setSwitchDishListener(ISwitchDishListener switchDishListener) {
        this.switchDishListener = switchDishListener;
    }

    public static SalesPromotionChoosePurchaseGoodsFragment newInstance(SalesPromotionRuleVo salesPromotionRuleVo, BigDecimal shopNumber) {
        SalesPromotionChoosePurchaseGoodsFragment dialog = new SalesPromotionChoosePurchaseGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SALE_PROMOTION_RULE_VO, salesPromotionRuleVo);
        bundle.putSerializable("shopNumber", shopNumber);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_purchase_goods, null, false);
        initView(rootView);
        if (getArguments() != null) {
            salesPromotionRuleVo = (SalesPromotionRuleVo) getArguments().getSerializable(SALE_PROMOTION_RULE_VO);
            shopNumber = (BigDecimal) getArguments().getSerializable("shopNumber");
            if (salesPromotionRuleVo != null && salesPromotionRuleVo.getPolicyDishs() != null) {
                for (SalesPromotionPolicyDish salesPromotionPolicyDish : salesPromotionRuleVo.getPolicyDishs()) {
                    if (salesPromotionPolicyDish.getType() == 1) {
                        DishVo dishVo = dishManager.getDishVoByBrandDishId(salesPromotionPolicyDish.getRelateId());
                        if (dishVo != null && dishVo.getDishShop() != null) {
                            dishVo.getDishShop().setDishQty(shopNumber);
                            dishVos.add(new DishVoChoiceWrap(dishVo));
                        }


                    } else {
                        DishBrandType type = new DishBrandType();
                        type.setId(salesPromotionPolicyDish.getRelateId());
                        DishInfo dishInfo = dishManager.switchType(type);
                        if (dishInfo != null && Utils.isNotEmpty(dishInfo.dishList)) {
                            for (DishVo dishVo : dishInfo.dishList) {
                                if (dishVo != null && dishVo.getDishShop() != null) {
                                    dishVo.getDishShop().setDishQty(shopNumber);
                                    dishVos.add(new DishVoChoiceWrap(dishVo));
                                }

                            }
                        }
                    }
                }
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvChooseGoodsList.setLayoutManager(layoutManager);
        mRvChooseGoodsList.setItemAnimator(new DefaultItemAnimator());
        mRvChooseGoodsList.addItemDecoration(new DividerDecoration(getActivity()));
        choosePurchaseGoodsAdapter = new ChoosePurchaseGoodsAdapter(getActivity(), dishVos);
        mRvChooseGoodsList.setAdapter(choosePurchaseGoodsAdapter);


        return rootView;
    }

    private void initView(View rootView) {
        mBtnClose = (ImageView) rootView.findViewById(R.id.btn_goods_close);
        mRvChooseGoodsList = (RecyclerView) rootView.findViewById(R.id.rv_choose_goods_list);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        mBtnChoose = (Button) rootView.findViewById(R.id.btn_choose);

        mBtnClose.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnChoose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel || id == R.id.btn_goods_close) {
            dismissAllowingStateLoss();
        } else if (id == R.id.btn_choose) {
            if (getSelectDishShop() == null) {
                ToastUtil.showShortToast("请选择一个加价购的菜品");
                return;
            }
            if (switchDishListener != null) {
                switchDishListener.switchDishShop(getSelectDishShop());
                dismissAllowingStateLoss();
            }
        }
    }

    private static class DishVoChoiceWrap {
        public DishVo dishVo;
        public boolean isSelected;

        public DishVoChoiceWrap(DishVo dishVo) {
            this.dishVo = dishVo;
        }
    }

    private static class ChoosePurchaseGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public Context mContext;
        List<DishVoChoiceWrap> dishVos;

        public ChoosePurchaseGoodsAdapter(Context context, List<DishVoChoiceWrap> dishVos) {
            this.mContext = context;
            this.dishVos = dishVos;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sale_promotion_choose_purchase_goods_item, parent, false);
            return new CommonRecyclerlViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CommonRecyclerlViewHolder commonHolder = (CommonRecyclerlViewHolder) holder;
            final DishVoChoiceWrap dishShop = dishVos.get(position);
            commonHolder.setText(R.id.tv_goods_name, dishShop.dishVo.getDishShop().getName());
            commonHolder.setText(R.id.tv_goods_number, "x" + dishShop.dishVo.getDishShop().getDishQty() + "");
            commonHolder.setText(R.id.tv_goods_price, formatPrice(dishShop.dishVo.getDishShop().getMarketPrice().doubleValue()));
            commonHolder.setSelected(R.id.cb_select_label_device, dishShop.isSelected);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean currentSelectStatus = dishShop.isSelected;
                    for (DishVoChoiceWrap dishShopChoiceWrap : dishVos) {
                        dishShopChoiceWrap.isSelected = false;
                    }
                    dishShop.isSelected = !currentSelectStatus;
                    notifyDataSetChanged();
                }
            });
            commonHolder.setOnClickListener(R.id.cb_select_label_device, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean currentSelectStatus = dishShop.isSelected;
                    for (DishVoChoiceWrap dishShopChoiceWrap : dishVos) {
                        dishShopChoiceWrap.isSelected = false;
                    }
                    dishShop.isSelected = !currentSelectStatus;
                    notifyDataSetChanged();
                }
            });


        }


        @Override
        public int getItemCount() {
            return dishVos.size();
        }

        /**
         * "￥######.00"格式 将金额格式化
         */
        public static String formatPrice(double value) {
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                if (value >= 0) {

                    return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(value);

                } else {

                    return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(Math.abs(value));
                }
            } catch (Exception e) {

//                Log.e(TAG,"",e);

                return value + "";
            }
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

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
        setCancelable(false);
        return dialog;
    }

    public DishVo getSelectDishShop() {
        for (DishVoChoiceWrap dishShop : dishVos) {
            if (dishShop.isSelected) {
                return dishShop.dishVo;
            }
        }
        return null;
    }

    public interface ISwitchDishListener {
        void switchDishShop(DishVo dishVo);
    }
}
