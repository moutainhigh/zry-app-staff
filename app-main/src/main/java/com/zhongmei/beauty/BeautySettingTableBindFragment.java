package com.zhongmei.beauty;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.beauty.adapter.BeautyTableChoiceAdapter;
import com.zhongmei.beauty.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.beauty.adapter.TableItemDecoration;
import com.zhongmei.beauty.interfaces.ITableOperator;
import com.zhongmei.beauty.operates.BeautyTableTradeCache;
import com.zhongmei.beauty.operates.TableFilterManager;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EFragment(R.layout.fragment_tab_bind)
public class BeautySettingTableBindFragment extends BasicFragment implements ITableOperator {

    @ViewById(R.id.tvStartWaiter)
    TextView tv_waiterName;

    @ViewById(R.id.tvSelTab)
    TextView tv_tableName;

    @ViewById(R.id.tvBind)
    TextView tv_commitTable;

    @ViewById(R.id.ll_tabarea)
    LinearLayout layout_tableArea;

    @ViewById(R.id.gridTabs)
    RecyclerView gv_tables;

    Tables mTable;

    TableFilterManager mTableManager;

    private TextView mTempAreaTextView; //零时存放当前选中的Zone

    private BeautyTableChoiceAdapter mAdapter;

    @AfterViews
    protected void init(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setReverseLayout(false);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        gv_tables.setLayoutManager(gridLayoutManager);
        gv_tables.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gv_tables.addItemDecoration(new TableItemDecoration(DensityUtil.dip2px(getContext(), 22), DensityUtil.dip2px(getContext(), 13)));

        mAdapter=new BeautyTableChoiceAdapter(getContext(),false);
        gv_tables.setAdapter(mAdapter);
        initData();
    }

    private void initData(){
        Long tableId=SharedPreferenceUtil.getSpUtil().getLong(Constant.SP_TABLE_ID,-1);
        if(tableId>0){
            String tableName=SharedPreferenceUtil.getSpUtil().getString(Constant.SP_TABLE_NAME,"--");

            mTable=new Tables();
            mTable.setId(tableId);
            mTable.setTableName(tableName);
        }
        setTableName(mTable,false);

        mTableManager=new TableFilterManager();
        mTableManager.setTableListener(this);
        mTableManager.loadTables();
    }

    private void setTableName(Tables table,boolean isShowCommitBtn){
        if(mTable!=null){
            tv_tableName.setText(table.getTableName());
        }else{
            tv_tableName.setText("--");
        }

        tv_commitTable.setVisibility(isShowCommitBtn? View.VISIBLE:View.GONE);
    }

    private void setTableName(DinnertableModel model,boolean isShowCommitBtn){
        if(model!=null && model.getId()!=null){
            mTable=new Tables();
            mTable.setId(model.getId());
            mTable.setTableName(model.getName());
            setTableName(mTable,isShowCommitBtn);
        }
    }

    private void saveTableInfo(Tables table){
        if(table!=null && table.getId()!=null && table.getId()>0){
            SharedPreferenceUtil.getSpUtil().putLong(Constant.SP_TABLE_ID,table.getId());
            SharedPreferenceUtil.getSpUtil().putString(Constant.SP_TABLE_NAME,table.getTableName());
            ToastUtil.showShortToast("保存成功");
        }
        tv_commitTable.setVisibility(View.GONE);
    }

    @UiThread
    protected void initTableZone(List<ZoneModel> zoneModles){
        if(Utils.isEmpty(zoneModles)){
            return;
        }

        layout_tableArea.removeAllViews();

        for (final ZoneModel zoneModle : zoneModles) {
            TextView textView = new TextView(getContext());
            textView.setText(zoneModle.getName());
            textView.setTextColor(getContext().getResources().getColor(R.color.color_333333));
            textView.setTextSize(getContext().getResources().getDimension(R.dimen.text_16));
            textView.setBackgroundResource(R.drawable.beauty_shape_square);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadTableData(zoneModle.getId());
                    v.setBackgroundResource(R.color.customer_allowance);
                    if(mTempAreaTextView!=null){
                        mTempAreaTextView.setBackgroundResource(R.drawable.beauty_shape_square); //加入临时记录
                    }
                    mTempAreaTextView= (TextView) v;
                }
            });
            layout_tableArea.addView(textView);
            Log.e("initTableZone","addView"+zoneModle.getName());
        }

        loadTableData(zoneModles.get(0).getId());//
    }

    //加载table数据
    private void loadTableData(Long zoneId){
        List<DinnertableModel> listTableModels = mTableManager.filter(zoneId,null);

        Set<Long> checkTables=null;

        if(mTable!=null){
            checkTables=new HashSet<>();
            checkTables.add(mTable.getId());
        }

        Log.e("loadTableData","加载桌台"+listTableModels.size());

        mAdapter.setItems(listTableModels);
        mAdapter.setCheckedTableSet(checkTables);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DinnertableModel model= (DinnertableModel) mAdapter.getItem(position);
                setTableName(model,true);
            }
        });
    }




    @Click({R.id.tvBind})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tvBind:
                saveTableInfo(mTable);//绑定桌台
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTableManager.removeTableListener(this);
        mTableManager.onDestory();
    }

    @Override
    public void refreshZones(List<ZoneModel> zoneModes) {
        initTableZone(zoneModes);
    }

    @Override
    public void refreshTableTrades(List<ZoneModel> zoneModes) {

    }

    @Override
    public void refreshTables(List<DinnertableModel> tableModes) {

    }
}
