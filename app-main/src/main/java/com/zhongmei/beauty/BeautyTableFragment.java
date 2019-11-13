package com.zhongmei.beauty;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.IZone;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.beauty.adapter.TableItemDecoration;
import com.zhongmei.beauty.adapter.TablesAdapter;
import com.zhongmei.beauty.interfaces.ITableOperator;
import com.zhongmei.beauty.operates.BeautyTableManager;
import com.zhongmei.beauty.operates.TableManagerBase;
import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.bty.dinner.table.view.OnControlListener;
import com.zhongmei.bty.dinner.table.view.TableFragmentBase;
import com.zhongmei.bty.dinner.table.view.TableViewBase;
import com.zhongmei.bty.dinner.table.view.ZonesVerticalIndicator;
import com.zhongmei.bty.dinner.vo.SwitchTableTradeVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;



@EFragment(R.layout.beauty_table_fragment)
public class BeautyTableFragment extends TableFragmentBase implements ITableOperator, OnControlListener {


    @ViewById(R.id.custom_zone_indicator)
    protected ZonesVerticalIndicator custom_zoneIndicator;

    @ViewById(R.id.gv_tables)
    protected RecyclerView gv_tables;

    private TableManagerBase mTableManager;

    @Bean
    protected TablesAdapter mTablesAdapter;

    private ZoneModel mCurZone;


    @AfterViews
    protected void initView() {
        mTableManager = new BeautyTableManager();
        mTableManager.setmTableOperatorListener(this);
        mTableManager.loadTableInfos();
        custom_zoneIndicator.setOnControlListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        gridLayoutManager.setReverseLayout(false);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        gv_tables.addItemDecoration(new TableItemDecoration(DensityUtil.dip2px(getContext(), 10), DensityUtil.dip2px(getContext(), 10)));
        gv_tables.setLayoutManager(gridLayoutManager);
        gv_tables.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mTablesAdapter.setControlListener(this);
        gv_tables.setAdapter(mTablesAdapter);
    }

    @Override
    protected TableInfoFragment initInfoFragment() {
        return null;
    }

    @Override
    public TableInfoFragment getInfoFragment() {
        return null;
    }

    @Override
    public void enableDinnertableClick(boolean enable) {

    }

    @Override
    public void selectTrade(SwitchTableTradeVo switchTableTradeVo) {

    }

    @Override
    public boolean hideMoreMenuPop(MotionEvent event) {
        return false;
    }

    @Override
    public boolean hideComboSelectView(MotionEvent event) {
        return false;
    }

    @Override
    public BusinessType getBussinessType() {
        return BusinessType.BEAUTY;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(mTableManager!=null){
            mTableManager.onDestory();
        }
        super.onDestroy();
    }

    @UiThread
    @Override
    public void refreshZones(List<ZoneModel> zoneModes) {        Log.e("BeautyTable", "refreshZones");
        custom_zoneIndicator.setZones(zoneModes);
        if (mCurZone == null && Utils.isNotEmpty(zoneModes)) {
            mCurZone = zoneModes.get(0);
        }

        if (mCurZone != null) {
            custom_zoneIndicator.switchZone(mCurZone);
        }
    }

    @UiThread
    @Override
    public void refreshTableTrades(List<ZoneModel> zoneModes) {
        Log.e("BeautyTable", "refreshTableTrades");
        custom_zoneIndicator.refreshZone();
        if (mCurZone == null && Utils.isNotEmpty(zoneModes)) {
            mCurZone = zoneModes.get(0);
        }

        if (mCurZone != null) {
            this.mTablesAdapter.setItems(mCurZone.getDinnertableModels());
            this.mTablesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshTables(List<DinnertableModel> tableModes) {

    }

    @Override
    public void onSwitchZone(IZone zone) {
        Log.e("BeautyTable", "onSwitchZone");
        if (zone instanceof ZoneModel) {
            ZoneModel zoneModel = (ZoneModel) zone;
            this.mCurZone = zoneModel;
            this.mTablesAdapter.setItems(mCurZone.getDinnertableModels());
            this.mTablesAdapter.notifyDataSetChanged();
            mTableManager.loadTableTrades(mCurZone.getId());
        }
    }

    @Override
    public boolean onSelect(DinnertableModel dinnertableModel, TableViewBase dinnertableView) {
        if (Utils.isNotEmpty(dinnertableModel.getDinnertableTrades())) {
                        showOrderDish(dinnertableModel.getDinnertableTrades().get(0).getTradeId());
        } else {
                        createOrderDish(createTable(dinnertableModel));
        }

        return false;
    }

    private void showOrderDish(Long tradeId) {
        Intent mIntent = new Intent();
        mIntent.setClass(getActivity(), BeautyOrderActivity.class);
        mIntent.putExtra(BeautyOrderConstants.IS_ORDER_EDIT, true);
        mIntent.putExtra(BeautyOrderConstants.ORDER_EDIT_TRADEID, tradeId);
        startActivity(mIntent);
    }

    private void createOrderDish(Tables table) {
        Intent mIntent = new Intent();
        mIntent.setClass(getActivity(), BeautyOrderActivity.class);
        mIntent.putExtra(BeautyOrderConstants.IS_ORDER_EDIT, false);
        mIntent.putExtra(BeautyOrderConstants.ORDER_EDIT_TABLE, table);
        startActivity(mIntent);
    }


    private Tables createTable(DinnertableModel dinnertableModel) {
        Tables tables = new Tables();
        tables.setId(dinnertableModel.getId());
        tables.setTableName(dinnertableModel.getName());
        tables.setTableStatus(dinnertableModel.getTableStatus());
        tables.setAreaId(dinnertableModel.getZone().getId());
        return tables;
    }

    @Override
    public void onDestroyView() {
        mTableManager.onDestory();
        mCurZone = null;
        super.onDestroyView();
    }

    @Override
    public void onTransfer(IDinnertableTrade orginal, IDinnertable dest) {

    }

    @Override
    public void onMerge(IDinnertableTrade orginal, IDinnertableTrade dest) {

    }

    @Override
    public void onDelete(IDinnertableTrade dinnertableTrade) {

    }

    @Override
    public void onShowControl(IDinnertable dinnertable) {

    }

    @Override
    public void onHideControl() {

    }

    @Override
    public void onTransferMoveDish(IDinnertableTradeMoveDish orginal, IDinnertable dest) {

    }

    @Override
    public void onMergeMoveDish(IDinnertableTradeMoveDish orginal, IDinnertableTrade dest) {

    }
}
