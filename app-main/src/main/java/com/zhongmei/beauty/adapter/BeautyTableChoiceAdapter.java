package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.zhongmei.beauty.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BeautyTableChoiceAdapter extends RecyclerViewBaseAdapter<DinnertableModel, BeautyTableItemView> {

    private Context mContext;
    private boolean isCheckStatus=true;

    public BeautyTableChoiceAdapter(Context context,boolean isCheckStatus) {
        this.mContext = context;
        this.isCheckStatus=isCheckStatus;
    }

    public BeautyTableChoiceAdapter(Context context) {
        this.mContext = context;
    }

    private Map<Long, TableInfo> mapCheckedTable = new HashMap<>();

    private Set<Long> tableSetChecked = new HashSet<>();

    @Override
    protected BeautyTableItemView onCreateItemView(ViewGroup parent, int viewType) {
        BeautyTableItemView tableView = BeautyTableItemView_.build(mContext);
        return tableView;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<BeautyTableItemView> holder, int position) {
        DinnertableModel tableModel = (DinnertableModel) getItem(position);
        BeautyTableItemView tableView = holder.getView();

        if (tableSetChecked.contains(tableModel.getId())) {
            mapCheckedTable.put(tableModel.getId(), new TableInfo(tableModel, position));
            tableSetChecked.remove(tableModel.getId());
        }

        tableView.refreshUI(tableModel, mapCheckedTable.containsKey(tableModel.getId()), isCheckStatus,new OnRBClickListener(tableModel, position));
    }


    public void setCheckedTableSet(Set<Long> tableSet) {
        if (tableSet != null) {
            tableSetChecked.addAll(tableSet);
        }
    }

    public List<DinnertableModel> getChoicedTables() {
        List<DinnertableModel> listTableModes = new ArrayList<>();
        List<TableInfo> listTableInfo = new ArrayList<>(mapCheckedTable.values());

        for (TableInfo tableInfo : listTableInfo) {
            listTableModes.add(tableInfo.getTableMode());
        }
        return listTableModes;
    }


    class OnRBClickListener implements View.OnClickListener {

        private DinnertableModel mTableModel;

        private int mPosition;

        public OnRBClickListener(DinnertableModel model, int position) {
            this.mTableModel = model;
            this.mPosition = position;
        }


        private void clearCheckTables(Map<Long, TableInfo> mapCheckedTable) {
            if (mapCheckedTable == null || mapCheckedTable.size() <= 0) {
                return;
            }

            for (Long tableid : mapCheckedTable.keySet()) {
                TableInfo tableinfo = mapCheckedTable.get(tableid);

                if (tableinfo != null) {
                    Log.e("notifayda", "tableID:" + tableid + ":position:" + tableinfo.getPosition() + "");
                    mapCheckedTable.remove(tableid);
                }
            }
        }

        @Override
        public void onClick(View v) {
            boolean isChecked = ((CheckBox) v).isChecked();

            clearCheckTables(mapCheckedTable);

            if (isChecked) {
                TableInfo tableinfo = new TableInfo(mTableModel, mPosition);
                Log.e("checkedTable", "tableID:" + mTableModel.getId() + ":position:" + mPosition + "");
                mapCheckedTable.put(mTableModel.getId(), tableinfo);
            }

            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v,mPosition);
            }
            notifyDataSetChanged();

        }
    }


    class TableInfo {
        final private DinnertableModel mTableModel;
        final private int mPosition;

        public TableInfo(DinnertableModel model, int position) {
            this.mTableModel = model;
            this.mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }

        public DinnertableModel getTableMode() {
            return mTableModel;
        }

    }


}
