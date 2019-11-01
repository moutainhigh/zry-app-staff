package com.zhongmei.beauty.order;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.BeautyShopCartActivity;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.beauty.order.view.BeautyBrandTypeView;
import com.zhongmei.beauty.order.view.BeautyBrandTypeViewEx;
import com.zhongmei.bty.dinner.orderdish.DishHomePageFragment;

import java.util.ArrayList;



public class BeautyOrderProductFragment extends DishHomePageFragment implements View.OnClickListener {

    Button btn_service, btn_product;
    RelativeLayout rl_back,rl_shopCart;
    TextView tv_tableName,tv_waiterName,tv_shopcartCount;

    protected void initAdapter() {
        mAdapter = new BeautyOrderProductListPagerAdapter(getActivity(), new ArrayList<DishVo>()) {

            @Override
            public void doItemTouch(DishVo dishVo) {
                onTouch(dishVo);
            }

            @Override
            public void doItemLongClick(DishVo dishVo) {
            }

        };
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.beauty_order_home_page, null);
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        btn_service = (Button) view.findViewById(R.id.beauty_tab_service);
        btn_product = (Button) view.findViewById(R.id.beauty_tab_product);

        rl_back=(RelativeLayout)view.findViewById(R.id.rl_back);
        rl_shopCart=(RelativeLayout)view.findViewById(R.id.rl_shop_cart);

        tv_tableName=(TextView)view.findViewById(R.id.tv_table);
        tv_waiterName=(TextView)view.findViewById(R.id.tv_waiter);
        tv_shopcartCount=(TextView)view.findViewById(R.id.tv_shopcart_count);

        btn_service.setSelected(true);
        btn_product.setSelected(false);
        btn_service.setOnClickListener(tabClickListener);
        btn_product.setOnClickListener(tabClickListener);

        rl_back.setOnClickListener(this);
        rl_shopCart.setOnClickListener(this);

    }

    View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.beauty_tab_service:
                    changeSelected(btn_service, btn_product);
                    break;
                case R.id.beauty_tab_product:
                    changeSelected(btn_product, btn_service);
                    break;
            }

        }
    };

    private void changeSelected(View btn1, View btn2) {
        if (btn1.isSelected()) {
            btn1.setSelected(false);
            btn2.setSelected(true);
        } else {
            btn1.setSelected(true);
            btn2.setSelected(false);
        }
    }

    @Override
    protected int calculateGridHeight() {
        int height = super.calculateGridHeight();
                return height - 45;
    }

    protected int getQuanPopLeftWidth() {
        return DensityUtil.dip2px(getContext(), 520);
    }

    protected int getIndexBg() {
        return R.drawable.beauty_list_index_bg_selector;
    }

    protected int getIndexTextColor() {
        return R.color.beauty_label_text_selector;
    }

    @Override
    public View getBrandTypeView() {
        hideControlBtn();
        int level = SharedPreferenceUtil.getSpUtil().getInt("dinner_meun_level", 2);
        if (level == 2 || isBuyServerBusiness()) {
            return new BeautyBrandTypeView(getActivity(), mDishManager, this,isBuyServerBusiness());
        } else {
            return new BeautyBrandTypeViewEx(getActivity(), mDishManager, this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.rl_back:
                this.getActivity().finish();
                break;
            case R.id.rl_shop_cart:
                Intent intent=new Intent(getContext(), BeautyShopCartActivity.class);
                startActivity(intent);
                break;
        }
    }
}
