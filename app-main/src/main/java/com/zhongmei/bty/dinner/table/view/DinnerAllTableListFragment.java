package com.zhongmei.bty.dinner.table.view;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.booking.ui.ConditionPopuWindow;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.table.adapter.BatchOpterationTablesListAdapter;
import com.zhongmei.bty.dinner.table.adapter.TablesAreaPopupAdapter;
import com.zhongmei.bty.dinner.table.adapter.TablesSeatingAreaPagerAdapter;
import com.zhongmei.bty.dinner.table.manager.DinnerTableBatchOperationManager;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesAreaVo;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EFragment(R.layout.dinner_all_table_list_fragment)
public class DinnerAllTableListFragment extends BasicFragment {

    public static final String TAG = DinnerAllTableListFragment.class.getName();

    @ViewById(R.id.tv_table_area)
    TextView tvTableArea;

    @ViewById(R.id.tv_all_table)
    TextView tvAllTable;

    @ViewById(R.id.tv_empty_table)
    TextView tvEmptyTable;

    @ViewById(R.id.tv_occupy_table)
    TextView tvOccupyTable;

    @ViewById(R.id.tv_connect_table)
    TextView tvConnectTable;

    @ViewById(R.id.left_btn)
    ImageView ivLeftBtn;

    @ViewById(R.id.right_btn)
    ImageView ivRightBtn;

    @ViewById(R.id.viewpager)
    ViewPager vpTablesSeat;

    @ViewById(R.id.listview)
    ListView lvTablesList;

    @ViewById(R.id.empty_view)
    RelativeLayout emptyView;

    ConditionPopuWindow popupWindow;

    DinnerTableBatchOperationManager manager;

    TablesSeatingAreaPagerAdapter seatingAdapter;

    BatchOpterationTablesListAdapter listAdapter;

    List<DinnerConnectTablesAreaVo> tablesAreaVoList;

    DinnerConnectTablesAreaVo dinnerTablesAreaVo;

    Map<Long, List<DinnerConnectTablesVo>> tablesVoMap;

    Map<Long, DinnerConnectTablesVo> selectedTablesVoMap;

    private int pagerIndex = 0;

    private int areaVoStatus = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL;

    private OnItemSelectedListener onItemSelectedListener;

    private BusinessType mBusinessType;

    Map<Long, TradeExtra> mainTradeExtraMap;

    @AfterViews
    public void initEmpty() {
        mBusinessType = ((BatchOperationTableDialogFragment) getParentFragment()).getmBusinessType();
        initObject();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.unregister();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }


    private void initObject() {

        manager = new DinnerTableBatchOperationManager();
                manager.register(new DinnerTableBatchOperationManager.OnDataChangedListener() {
            @Override
            public void onChanged() {
                asynAreaData();
            }
        });
        tablesAreaVoList = new ArrayList<>();
        tablesVoMap = new HashMap<>();
        selectedTablesVoMap = new HashMap<>();
    }

    public void asynAreaData() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                try {

                    tablesAreaVoList = manager.createDinnerTablesAreaVo();
                    getMainTradeExtraMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                initView();
            }
        });
    }

    void getMainTradeExtraMap() throws SQLException {
        if (Utils.isNotEmpty(tablesAreaVoList)) {
            DinnerConnectTablesAreaVo tablesAreaVo = tablesAreaVoList.get(0);
            List<Long> mainTradeIdList = new ArrayList<>(tablesAreaVo.connectTablesVoMap.keySet());
            if (Utils.isNotEmpty(mainTradeIdList)) {
                TradeDal dal = OperatesFactory.create(TradeDal.class);
                List<TradeExtra> mainTradeExtraList = dal.getTradeExtra(mainTradeIdList);
                mainTradeExtraMap = new HashMap<>();
                if (Utils.isNotEmpty(mainTradeExtraList)) {
                    for (TradeExtra extra : mainTradeExtraList) {
                        mainTradeExtraMap.put(extra.getTradeId(), extra);
                    }
                }
            }
        }
    }

    private void initView() {
        if (Utils.isEmpty(tablesAreaVoList)) return;
        dinnerTablesAreaVo = tablesAreaVoList.get(0);
        dinnerTablesAreaVo.status = areaVoStatus;
        if (selectedTablesVoMap.size() > 0) {
            for (DinnerConnectTablesVo tablesVo : dinnerTablesAreaVo.allTablesVoMap.get(0L)) {
                if (selectedTablesVoMap.containsKey(tablesVo.tables.getId())) {
                    tablesVo.isSelected = true;
                    selectedTablesVoMap.put(tablesVo.tables.getId(), tablesVo);
                }
            }
            onItemSelectedListener.onSelectedList(new ArrayList<>(selectedTablesVoMap.values()));
        }
        if (tablesAreaVoList.get(0).connectTablesVoMap != null) {
            tvConnectTable.setText(String.format(getContext().getString(R.string.batch_operation_table_connect), tablesAreaVoList.get(0).connectTablesVoMap.size()));
        } else {
            tvConnectTable.setText(String.format(getContext().getString(R.string.batch_operation_table_connect), 0));
        }
        initAdapter();
        refreshView();
    }

        public void removeSelectedTable(DinnerConnectTablesVo vo) {
        if (vo != null) {
            selectedTablesVoMap.remove(vo.tables.getId());
            refreshView();
        }
    }

    private void initAdapter() {
        if (seatingAdapter == null) {
            seatingAdapter = new TablesSeatingAreaPagerAdapter(getContext()) {
                @Override
                public void selectArea(Long data) {
                    listAdapter.setData(tablesVoMap.get(data));
                }
            };
            vpTablesSeat.setAdapter(seatingAdapter);
            vpTablesSeat.setOnPageChangeListener(listener);
            seatingAdapter.setMainTradeExtraMap(mainTradeExtraMap);
        }
        if (listAdapter == null) {
            listAdapter = new BatchOpterationTablesListAdapter(getContext());
            lvTablesList.setAdapter(listAdapter);
            lvTablesList.setEmptyView(emptyView);
            listAdapter.setListener(new BatchOpterationTablesListAdapter.OnItemBtnClickListener() {
                @Override
                public void onClick(DinnerConnectTablesVo vo) {
                    if (selectedTablesVoMap.containsKey(vo.tables.getId())) {
                        selectedTablesVoMap.remove(vo.tables.getId());
                    } else {
                        selectedTablesVoMap.put(vo.tables.getId(), vo);
                    }
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onSelected(vo);
                    }
                }
            });
        }
    }

    public void refreshView() {
        refreshMap();
        refreshTitleView();
        pagerIndex = 0;
        vpTablesSeat.setCurrentItem(pagerIndex);
        if (dinnerTablesAreaVo.status == DinnerConnectTablesAreaVo.TABLES_CONNECT) {
            seatingAdapter.setStyle(TablesSeatingAreaPagerAdapter.STYLE_CONNECT);
        } else {
            seatingAdapter.setStyle(TablesSeatingAreaPagerAdapter.STYLE_SEATING);
        }
        seatingAdapter.setStyle(dinnerTablesAreaVo.status == DinnerConnectTablesAreaVo.TABLES_CONNECT ?
                TablesSeatingAreaPagerAdapter.STYLE_CONNECT : TablesSeatingAreaPagerAdapter.STYLE_SEATING);
        seatingAdapter.setDataSet(tablesVoMap.keySet());
        listAdapter.setData(tablesVoMap.get(seatingAdapter.getFirstData()));
        updatePager();
    }

    public void refreshMap() {
        switch (dinnerTablesAreaVo.status) {
            case DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL:
                tablesVoMap.clear();
                tablesVoMap.putAll(dinnerTablesAreaVo.allTablesVoMap);
                break;
            case DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_EMPTY:
                tablesVoMap.clear();
                tablesVoMap.putAll(dinnerTablesAreaVo.EmptyTablesVoMap);
                break;
            case DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_OCCUPY:
                tablesVoMap.clear();
                tablesVoMap.putAll(dinnerTablesAreaVo.occupyTablesVoMap);
                break;
            case DinnerConnectTablesAreaVo.TABLES_CONNECT:
                tablesVoMap.clear();

                tablesVoMap.putAll(dinnerTablesAreaVo.connectTablesVoMap);
                break;
            default:
                tablesVoMap.clear();
                break;
        }
    }

    public void refreshTitleView() {
        tvTableArea.setText(dinnerTablesAreaVo.area.getAreaName());
        switch (dinnerTablesAreaVo.status) {
            case DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL:
                tvAllTable.setTextColor(getContext().getResources().getColor(R.color.color_32ADF6));
                tvEmptyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvOccupyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvConnectTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvTableArea.setClickable(true);
                break;
            case DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_EMPTY:
                tvAllTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvEmptyTable.setTextColor(getContext().getResources().getColor(R.color.color_32ADF6));
                tvOccupyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvConnectTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvTableArea.setClickable(true);
                break;
            case DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_OCCUPY:
                tvAllTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvEmptyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvOccupyTable.setTextColor(getContext().getResources().getColor(R.color.color_32ADF6));
                tvConnectTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvTableArea.setClickable(true);
                break;
            case DinnerConnectTablesAreaVo.TABLES_CONNECT:
                tvAllTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvEmptyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvOccupyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvConnectTable.setTextColor(getContext().getResources().getColor(R.color.color_32ADF6));
                tvTableArea.setClickable(false);
                break;
            default:
                tvAllTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvEmptyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                tvOccupyTable.setTextColor(getContext().getResources().getColor(R.color.color_666666));
                break;
        }
    }

    private void updatePager() {
        if (pagerIndex == 0) {
            ivLeftBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_left_unenable));
        } else {
            ivLeftBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_left_normal));
        }
        if (pagerIndex == seatingAdapter.getCount() - 1) {
            ivRightBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_right_unenable));
        } else {
            ivRightBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_right_normal));
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagerIndex = position;
            if (seatingAdapter != null) {
                if (position == 0) {
                    ivLeftBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_left_unenable));
                } else {
                    ivLeftBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_left_normal));
                }
                if (position == seatingAdapter.getCount() - 1) {
                    ivRightBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_right_unenable));
                } else {
                    ivRightBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pay_method_indicator_right_normal));
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Click({R.id.tv_table_area, R.id.tv_all_table, R.id.tv_empty_table, R.id.tv_occupy_table, R.id.tv_connect_table, R.id.left_btn, R.id.right_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_table_area:
                initAreaPopupWindow();
                break;
            case R.id.tv_all_table:
                if (dinnerTablesAreaVo == null) break;
                dinnerTablesAreaVo.status = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL;
                areaVoStatus = dinnerTablesAreaVo.status;
                refreshView();
                break;
            case R.id.tv_empty_table:
                if (dinnerTablesAreaVo == null) break;
                dinnerTablesAreaVo.status = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_EMPTY;
                areaVoStatus = dinnerTablesAreaVo.status;
                refreshView();
                break;
            case R.id.tv_occupy_table:
                if (dinnerTablesAreaVo == null) break;
                dinnerTablesAreaVo.status = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_OCCUPY;
                areaVoStatus = dinnerTablesAreaVo.status;
                refreshView();
                break;
            case R.id.tv_connect_table:
                if (dinnerTablesAreaVo == null) break;
                dinnerTablesAreaVo.isSelected = false;
                dinnerTablesAreaVo = tablesAreaVoList.get(0);
                dinnerTablesAreaVo.isSelected = true;
                dinnerTablesAreaVo.status = DinnerConnectTablesAreaVo.TABLES_CONNECT;
                areaVoStatus = dinnerTablesAreaVo.status;
                refreshView();
                break;
            case R.id.left_btn:
                vpTablesSeat.setCurrentItem(pagerIndex - 1);
                break;
            case R.id.right_btn:
                vpTablesSeat.setCurrentItem(pagerIndex + 1);
                break;
        }
    }


    @SuppressLint("InflateParams")
    private void initAreaPopupWindow() {
        TablesAreaPopupAdapter adapter = new TablesAreaPopupAdapter(this.getActivity(), tablesAreaVoList);
        ConditionPopuWindow.PopupItemClick itemClik = new ConditionPopuWindow.PopupItemClick() {

            @Override
            public void click(int position) {
                for (DinnerConnectTablesAreaVo areaVo : tablesAreaVoList) {
                    if (areaVo.area.getAreaCode().equals(tablesAreaVoList.get(position).area.getAreaCode())) {
                        areaVo.status = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL;
                        dinnerTablesAreaVo = areaVo;
                        areaVo.isSelected = true;
                    } else {
                        areaVo.isSelected = false;
                    }
                }
                refreshView();
            }
        };
        dismissPopupWindow();
        popupWindow =
                new ConditionPopuWindow(this.getActivity(), tvTableArea, itemClik, true);
        popupWindow.setAdapter(adapter);
    }

    private void dismissPopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public interface OnItemSelectedListener {
        void onSelected(DinnerConnectTablesVo vo);

        void onSelectedList(List<DinnerConnectTablesVo> tablesVoList);
    }
}
