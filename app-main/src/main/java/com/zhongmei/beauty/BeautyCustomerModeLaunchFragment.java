package com.zhongmei.beauty;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 美业顾客模式
 */
@EFragment(R.layout.fragment_customer_launch)
public class BeautyCustomerModeLaunchFragment extends BasicFragment implements View.OnClickListener {

    @ViewById(R.id.rl_game)
    RelativeLayout rl_game;

    @ViewById(R.id.btn_open_trade)
    Button btn_openTrade;
    @ViewById(R.id.tv_table)
    TextView tv_table;
    @ViewById(R.id.tv_waiter)
    TextView tv_waiter;

    @ViewById(R.id.rl_shop_cart)
    RelativeLayout rl_shopCart;
    @ViewById(R.id.tv_shopcart_count)
    TextView tv_shopCartCount;

    private Tables mTable;

    private DinnerShoppingCart mShopCart;

    @AfterViews
    public void init(){

        btn_openTrade.setOnClickListener(this);
        rl_shopCart.setOnClickListener(this);


        setWaiterInfo(Session.getAuthUser());
        initData();
    }

    private void initData(){
        mShopCart=DinnerShoppingCart.getInstance();

        Long tableId= SharedPreferenceUtil.getSpUtil().getLong(Constant.SP_TABLE_ID,-1);
        if(tableId>0){
            mTable=new Tables();
            mTable.setId(tableId);
            mTable.setTableName(SharedPreferenceUtil.getSpUtil().getString(Constant.SP_TABLE_NAME,"--"));
        }

        setTableInfo(mTable);
    }


    public void setWaiterInfo(AuthUser authUser) {
        if (authUser != null) {
            tv_waiter.setText("服务员:"+authUser.getName());
        }else{
            tv_waiter.setText("服务员：--");
        }
    }

    public void setTableInfo(Tables table){
        if(table!=null){
            btn_openTrade.setText("开始点单");
            tv_table.setText("工作台:"+table.getTableName());
        }else{
            btn_openTrade.setText("请先设置工作台");
            tv_table.setText("工作台: -- ");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mShopCart!=null){
            updateShopCartCount(mShopCart);
        }
    }

    private void createOrderDish(Tables table) {
        if(table==null){
            return;
        }

        Intent mIntent = new Intent();
        mIntent.setClass(getActivity(), BeautyOrderActivity.class);
        mIntent.putExtra(BeautyOrderConstants.IS_ORDER_EDIT, false);
        mIntent.putExtra(BeautyOrderConstants.ORDER_EDIT_TABLE, table);
        startActivity(mIntent);
    }

    private void updateShopCartCount(DinnerShoppingCart shopCart){
        int shopCartCount=shopCart.getShoppingCartDish().size();
        tv_shopCartCount.setText(shopCartCount+"");
    }

    private void toShopCartItem(){
        if(Utils.isEmpty(mShopCart.getShoppingCartDish())){
            ToastUtil.showShortToast("消费清单为空，请先点单!");
            return;
        }
        Intent intent=new Intent(getContext(), BeautyShopCartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open_trade:
                //开单
                createOrderDish(mTable);
                break;
            case R.id.rl_shop_cart:
                toShopCartItem();
                break;
        }
    }
}
