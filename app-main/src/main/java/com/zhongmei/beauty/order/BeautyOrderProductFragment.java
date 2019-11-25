package com.zhongmei.beauty.order;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.BeautyShopCartActivity;
import com.zhongmei.beauty.order.adapter.BeautyCardAdapter;
import com.zhongmei.beauty.order.event.BeautyShopCartLoadEvent;
import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.bty.basemodule.orderdish.bean.DishPageInfo;
import com.zhongmei.bty.basemodule.orderdish.event.EventDishChangedNotice;
import com.zhongmei.bty.snack.orderdish.adapter.DishTypeAdapter;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.beauty.order.view.BeautyBrandTypeView;
import com.zhongmei.beauty.order.view.BeautyBrandTypeViewEx;
import com.zhongmei.bty.dinner.orderdish.DishHomePageFragment;

import java.util.ArrayList;
import java.util.List;


public class BeautyOrderProductFragment extends DishHomePageFragment implements View.OnClickListener , AdapterView.OnItemClickListener {

    Button btn_service, btn_product;
    RelativeLayout rl_back,rl_shopCart;
    TextView tv_tableName,tv_waiterName,tv_shopcartCount;

    LinearLayout layout_typeName;
    TextView tv_curDishType;
    ListView lv_dishType;

    private ImageView iv_search;

    DishTypeAdapter mDishTypeAdapter;

    protected void initAdapter() {
        mAdapter = new BeautyOrderProductListPagerAdapter(getActivity(), new ArrayList<DishPageInfo>()) {

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

        layout_typeName=(LinearLayout)view.findViewById(R.id.layout_type_name);
        tv_curDishType=(TextView)view.findViewById(R.id.tv_cur_dish_type);
        lv_dishType=(ListView)view.findViewById(R.id.lv_dish_type);

        iv_search=(ImageView)view.findViewById(R.id.iv_search);

        btn_service.setSelected(true);
        btn_product.setSelected(false);
        btn_service.setOnClickListener(tabClickListener);
        btn_product.setOnClickListener(tabClickListener);

        rl_back.setOnClickListener(this);
        rl_shopCart.setOnClickListener(this);
        layout_typeName.setOnClickListener(this);
        lv_dishType.setOnItemClickListener(this);
        iv_search.setOnClickListener(this);

        mDishTypeAdapter=new DishTypeAdapter(getContext(),new ArrayList<DishPageInfo>());
        lv_dishType.setAdapter(mDishTypeAdapter);

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

    public void onEventMainThread(BeautyShopCartLoadEvent event) {
        //购物车加载完毕
        mAdapter.notifyDataSetChanged();
        tv_shopcartCount.setText(mShoppingCart.getShoppingCartDish().size()+"");
    }

    @Override
    protected int calculateGridHeight() {
        int height = super.calculateGridHeight();
                return height - 45;
    }

    public void setTables(List<TradeTable> tradeTables) {
        if (Utils.isEmpty(tradeTables)) {
            return;
        }
        String tablesNameTmp = "";
        for (TradeTable tb : tradeTables) {
            tablesNameTmp += tb.getTableName() + ",";
        }
        String tableName = tablesNameTmp.substring(0, tablesNameTmp.length() - 1);
        tv_tableName.setText("工作台:" + tableName);
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
                intent.putExtra(BeautyOrderConstants.ORDER_BUSINESSTYPE,businessTypeValue.value());
                startActivity(intent);
                break;
            case R.id.layout_type_name:
                //弹出所有商品品牌选项
                mDishTypeAdapter.notifyDataSetChanged();
                layout_typeName.setVisibility(View.GONE);
                lv_dishType.setVisibility(View.VISIBLE);
                mViewShadow.setVisibility(View.VISIBLE);
                break;
            case R.id.view_shadow:
                layout_typeName.setVisibility(View.VISIBLE);
                lv_dishType.setVisibility(View.GONE);
                mViewShadow.setVisibility(View.GONE);
                break;
            case R.id.iv_search:
                onSearchClick();
                break;

        }
    }

    public void setCurDishType(DishPageInfo dishPageInfo){
        if(dishPageInfo!=null){
            mDishTypeAdapter.setCurPageInfo(dishPageInfo);
            tv_curDishType.setText(dishPageInfo.dishBrand.getName());
        }
    }

    public void refreshAdapterData(List<DishPageInfo> listDishPageInfo){
        mDishTypeAdapter.setData(listDishPageInfo);
        mDishTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DishPageInfo dishPageInfo = mDishTypeAdapter.getItem(position);
        if(dishPageInfo!=null){
            mVpDishList.setCurrentItem(dishPageInfo.position,true);
        }
        setCurDishType(dishPageInfo);
        layout_typeName.setVisibility(View.VISIBLE);
        lv_dishType.setVisibility(View.GONE);
        mViewShadow.setVisibility(View.GONE);
    }

    public void refreshShopCartCount(){
        if(tv_shopcartCount!=null && mShoppingCart!=null){
            tv_shopcartCount.setText(mShoppingCart.getShoppingCartDish().size()+"");
        }
    }
}
