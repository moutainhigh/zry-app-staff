package com.zhongmei.bty.dinner.ordercenter.view;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.yunfu.context.util.Utils;

@EFragment(R.layout.dinner_order_dish_choose_table)
public class DinnerChooseTableDialog extends BasicDialogFragment {

    private final static int PAGE_SIZE = 3;

    @ViewById(R.id.viewPager)
    ViewPager viewPager;

    @ViewById(R.id.gridView)
    GridView gridView;

    @ViewById(R.id.tv_empty)
    TextView tvEmpty;

    private List<LinearLayout> groupViewPageList = new ArrayList<LinearLayout>();

    private View selectedGroupView;

    private List<TableGroup> groupList = new ArrayList<TableGroup>();

    private GroupPagerAdapter groupPagerAdapter;

    private TableAdapter tableAdapter;

    private OnTableSelectedConfirmListener onTableSelectedConfirmListener;

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    public void setOnTableSelectedConfirmListener(OnTableSelectedConfirmListener onTableSelectedConfirmListener) {
        this.onTableSelectedConfirmListener = onTableSelectedConfirmListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    @AfterViews
    void init() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);

        initGroupViews();
        groupPagerAdapter = new GroupPagerAdapter(groupViewPageList);
        viewPager.setAdapter(groupPagerAdapter);
        tableAdapter = new TableAdapter(getActivity());
        gridView.setAdapter(tableAdapter);
        if (groupViewPageList.size() > 0 && groupViewPageList.get(0).getChildCount() > 0) {
            selectGroup(groupViewPageList.get(0).getChildAt(0));
        }
    }

    @Click({R.id.btn_left, R.id.btn_right, R.id.btn_close, R.id.btn_ok})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                int currentItem = viewPager.getCurrentItem();
                if (currentItem > 0) {
                    viewPager.setCurrentItem(currentItem - 1, true);
                }
                break;
            case R.id.btn_right:
                currentItem = viewPager.getCurrentItem();
                if (currentItem < viewPager.getAdapter().getCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                }
                break;
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_ok:
                if (tableAdapter.selectedTable == null) {
                    ToastUtil.showShortToast(R.string.please_select_table);
                    return;
                }
                if (onTableSelectedConfirmListener != null) {
                    onTableSelectedConfirmListener.onConfirm(tableAdapter.selectedTable);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    @ItemClick({R.id.gridView})
    void itemClick(Tables table) {
        tableAdapter.setSelectedTable(table);
    }

    private void initDatas() {
        TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        try {
            boolean allowMultTrades = ServerSettingManager.allowMultiTradesOnTable();
            List<Tables> tables = tablesDal.listAllPhysicalLayoutTables();
                        for (int i = tables.size() - 1; i >= 0; i--) {
                if (tables.get(i).getTableStatus() == TableStatus.EMPTY) {
                                        continue;
                } else if (allowMultTrades && tables.get(i).getTableStatus() == TableStatus.OCCUPIED) {
                    continue;
                }
                tables.remove(i);
            }
            List<CommercialArea> areas = tablesDal.listTableArea();
            for (CommercialArea area : areas) {
                TableGroup tableGroup = new TableGroup(area, new ArrayList<Tables>());
                for (int i = tables.size() - 1; i >= 0; i--) {
                    if (tables.get(i).getAreaId().equals(area.getId())) {
                        Tables table = tables.remove(i);
                        tableGroup.getTableList().add(table);
                    }
                }
                if (Utils.isNotEmpty(tableGroup.getTableList())) {
                    groupList.add(tableGroup);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGroupViews() {
        Context context = getActivity();

        List<LinearLayout> singleGroupViewList = new ArrayList<LinearLayout>();
        for (int i = 0; i < groupList.size(); i++) {
            TableGroup group = groupList.get(i);
            LinearLayout singleGroupView = makeSingleGroupView(context, group.getCommercialArea().getAreaName(), i);
            singleGroupViewList.add(singleGroupView);
        }

        int pageCount = 0;
        if (singleGroupViewList.size() % PAGE_SIZE == 0) {
            pageCount = singleGroupViewList.size() / 3;
        } else {
            pageCount = singleGroupViewList.size() / 3 + 1;
        }
        for (int i = 0; i < pageCount; i++) {
            LayoutParams groupViewPageLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            groupViewPageLp.weight = 1;
            groupViewPageLp.gravity = Gravity.CENTER;

            LinearLayout groupViewPage = new LinearLayout(context);
            groupViewPage.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < PAGE_SIZE; j++) {
                int groupViewIndex = i * PAGE_SIZE + j;
                if (groupViewIndex < singleGroupViewList.size()) {
                    groupViewPage.addView(singleGroupViewList.get(groupViewIndex), groupViewPageLp);
                } else {
                    groupViewPage.addView(new View(context), groupViewPageLp);
                }
            }
            groupViewPageList.add(groupViewPage);
        }
    }

    private LinearLayout makeSingleGroupView(Context context, String groupName, int groupViewIndex) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setId(groupViewIndex);
        View singleGroupView = LayoutInflater.from(context).inflate(R.layout.dinner_choose_table_dialog_area_layout, linearLayout, false);
        TextView textView = (TextView) singleGroupView.findViewById(R.id.tv_area_name);
        textView.setText(groupName);
        View view = singleGroupView.findViewById(R.id.v_area_line);
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroup(v);
            }
        });
        linearLayout.addView(singleGroupView);
        return linearLayout;
    }

    private void selectGroup(View v) {
        if (v.isSelected()) {
            return;
        }
        List<Tables> tables = groupList.get(v.getId()).getTableList();
        tableAdapter.setData(tables);
        if (tables == null || tables.isEmpty()) {
            gridView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }

        if (selectedGroupView != null) {
            selectedGroupView.setSelected(false);
        }
        v.setSelected(true);
        selectedGroupView = v;
    }

    private class TableAdapter extends BaseAdapter {
        private Context context;

        private List<Tables> listData;

        private Tables selectedTable;

        public TableAdapter(Context context) {
            this.context = context;
        }

        public void setData(List<Tables> listData) {
            this.listData = listData;
            this.notifyDataSetChanged();
        }

        public void setSelectedTable(Tables selectedTable) {
            this.selectedTable = selectedTable;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listData == null ? 0 : listData.size();
        }

        @Override
        public Tables getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.dinner_table_list_item, null);
                holder = new ViewHolder();
                holder.table_name = (TextView) convertView.findViewById(R.id.table_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Tables table = (Tables) getItem(position);
            holder.table_name.setText(table.getTableName());
            if (selectedTable != null && selectedTable.getId().equals(table.getId())) {
                                holder.table_name.setTextColor(context.getResources().getColor(R.color.text_white));
                holder.table_name.setBackgroundResource(R.drawable.dinner_table_press);
            } else {
                holder.table_name.setBackgroundResource(R.drawable.dinner_table_normal);
                holder.table_name.setTextColor(context.getResources().getColor(R.color.settings_normalword));
            }
            return convertView;
        }

        class ViewHolder {
            TextView table_name;
        }
    }

    private class GroupPagerAdapter extends PagerAdapter {

        private int page;

        private List<LinearLayout> groupViewPageList;

        private GroupPagerAdapter(List<LinearLayout> groupViewPageList) {
            this.page = groupViewPageList.size();
            this.groupViewPageList = groupViewPageList;
        }

        @Override
        public int getCount() {
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = groupViewPageList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    public static class TableGroup {

        private CommercialArea commercialArea;

        private List<Tables> tableList;

        private TableGroup(CommercialArea commercialArea, List<Tables> tableList) {
            this.commercialArea = commercialArea;
            this.tableList = tableList;
        }

        public CommercialArea getCommercialArea() {
            return commercialArea;
        }

        public void setCommercialArea(CommercialArea commercialArea) {
            this.commercialArea = commercialArea;
        }

        public List<Tables> getTableList() {
            return tableList;
        }

        public void setTableList(List<Tables> tableList) {
            this.tableList = tableList;
        }
    }

    public interface OnTableSelectedConfirmListener {

        public void onConfirm(Tables table);

    }

}
