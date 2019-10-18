package com.zhongmei.bty.dinner.ordercenter.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.trade.bean.TableStateInfo;
import com.zhongmei.bty.basemodule.trade.bean.TablesAreaVo;
import com.zhongmei.bty.basemodule.trade.bean.TablesVo;
import com.zhongmei.bty.basemodule.trade.message.ClearDinnertableResp;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.dinner.ordercenter.adapter.TablesAreaAdapter;
import com.zhongmei.bty.dinner.ordercenter.adapter.TablesVoAdapter;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EFragment(R.layout.dinner_choose_table_dialog_fragment)
public class DinnerChooseTableDialogFragment extends BasicDialogFragment {

    public static final String TAG = DinnerChooseTableDialogFragment.class.getSimpleName();

    @ViewById(R.id.booking_create_gridView_desk)
    GridView desk_gridview;

    @ViewById(R.id.booking_create_desk_hint)
    TextView tv_desk_null;

    @ViewById(R.id.tables_area_layout)
    protected ViewPager tablesAreaLayout;

    @ViewById(R.id.order_table_layout)
    LinearLayout mLlOrderTable;

    @ViewById(R.id.order_table_name)
    TextView mTvOrderTableName;

    @ViewById(R.id.order_table_status)
    TextView mTvOrderTableStatus;

    @ViewById(R.id.order_table_op_hint)
    TextView mTvOrderTableOpHint;

    @ViewById(R.id.order_table_op)
    TextView mTvOrderTableOp;

    public List<TablesAreaVo> tablesAreaVoList;

    private TablesAreaAdapter areaPagerAdapter;

    private TablesVoAdapter tablesAdapter;

    private TablesVo selectedTablesVo;

    private Tables tables;

    private OnTableSelectedConfirmListener onTableSelectedConfirmListener;

    public void setTables(Tables tables) {
        this.tables = tables;
    }

    public void setOnTableSelectedConfirmListener(OnTableSelectedConfirmListener onTableSelectedConfirmListener) {
        this.onTableSelectedConfirmListener = onTableSelectedConfirmListener;
    }

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

    @AfterViews
    protected void initView() {
        initAdapter();
        TaskContext.bindExecute(this, new SimpleAsyncTask<Object>() {
            @Override
            public Object doInBackground(Void... params) {
                initData();
                return null;
            }

            @Override
            public void onPostExecute(Object o) {
                super.onPostExecute(o);
                areaPagerAdapter.setDataSet(tablesAreaVoList);
                refreshOrderTableLayout();
            }
        });
    }


    protected void initData() {
        tablesAreaVoList = new ArrayList<>();
        boolean allowMultTrades = ServerSettingManager.allowMultiTradesOnTable();
        TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
        try {
            List<Tables> tablesList = tablesDal.listAllPhysicalLayoutTables();
            List<CommercialArea> areasList = tablesDal.listTableArea();
            Map<Long, List<TablesVo>> areaIdTablesListMap = new HashMap<>();
            if (Utils.isNotEmpty(tablesList) && Utils.isNotEmpty(areasList)) {
                for (Tables tables : tablesList) {
                    if (tables.getTableStatus() == TableStatus.DONE || tables.getTableStatus() == TableStatus.LOCKING) {
                                                continue;
                    } else if (tables.getTableStatus() == TableStatus.OCCUPIED && !allowMultTrades) {
                        continue;
                    }
                    TablesVo tablesVo;
                    if (areaIdTablesListMap.containsKey(tables.getAreaId())) {
                        tablesVo = new TablesVo(tables, false);
                        areaIdTablesListMap.get(tables.getAreaId()).add(tablesVo);
                    } else {
                        areaIdTablesListMap.put(tables.getAreaId(), new ArrayList<TablesVo>());
                        tablesVo = new TablesVo(tables, false);
                        areaIdTablesListMap.get(tables.getAreaId()).add(tablesVo);
                    }
                }
                TablesAreaVo areaVo;
                for (CommercialArea area : areasList) {
                    if (areaIdTablesListMap.containsKey(area.getId())) {
                        areaVo = new TablesAreaVo(area);
                        areaVo.setTablesVoList(areaIdTablesListMap.get(area.getId()));
                        tablesAreaVoList.add(areaVo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initAdapter() {
        tablesAdapter = new TablesVoAdapter(getContext(), new TablesVoAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(TablesVo tablesVo) {
                if (selectedTablesVo != null && !selectedTablesVo.getTable().getUuid().equals(tablesVo.getTable().getUuid()) && selectedTablesVo.getSelected()) {
                    selectedTablesVo.setSelected(false);
                }
                selectedTablesVo = tablesVo;
            }
        });
        desk_gridview.setAdapter(tablesAdapter);
        areaPagerAdapter = new TablesAreaAdapter(getContext()) {
            @Override
            public void selectArea(TablesAreaVo vo) {
                tablesAdapter.setData(vo.getTablesVoList());
            }
        };
        tablesAreaLayout.setAdapter(areaPagerAdapter);
    }

    private void refreshOrderTableLayout() {
        if (tables == null) {
            mLlOrderTable.setVisibility(View.GONE);
        } else {
            mTvOrderTableName.setText(tables.getTableName());
            if (tables.getTableStatus() == TableStatus.OCCUPIED) {
                mLlOrderTable.setVisibility(View.VISIBLE);
                mTvOrderTableOpHint.setVisibility(View.GONE);
                mTvOrderTableOp.setVisibility(View.GONE);
                mTvOrderTableStatus.setText(R.string.accept_order_tables_status_dinner);
            } else if (tables.getTableStatus() == TableStatus.DONE) {
                mLlOrderTable.setVisibility(View.VISIBLE);
                mTvOrderTableOpHint.setVisibility(View.VISIBLE);
                mTvOrderTableOp.setVisibility(View.VISIBLE);
                mTvOrderTableStatus.setText(R.string.accept_order_tables_status_clear);
            }
        }

    }

    @Click({R.id.ivClose, R.id.order_table_op, R.id.table_area_pre, R.id.tables_area_next, R.id.btn_table_select_ok})
    void onClick(View view) {
        if (ClickManager.getInstance().isClicked()) return;
        int current = tablesAreaLayout.getCurrentItem();
        int count = areaPagerAdapter.getCount();
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.order_table_op:
                DinnertableModel mode = createModel();
                clearTable(mode);
                break;
            case R.id.table_area_pre:
                if (current > 0) tablesAreaLayout.setCurrentItem(current - 1);
                break;
            case R.id.tables_area_next:
                if (current < count) tablesAreaLayout.setCurrentItem(current + 1);
                break;
            case R.id.btn_table_select_ok:
                if (selectedTablesVo != null) {
                    if (onTableSelectedConfirmListener != null) {
                        onTableSelectedConfirmListener.onConfirm(selectedTablesVo.getTable());
                    }
                    dismiss();
                } else {
                    ToastUtil.showShortToast(getString(R.string.please_select_table));
                }
                break;
        }
    }


    private DinnertableModel createModel() {
        TableStateInfo stateInfo = new TableStateInfo(tables, BusinessType.DINNER);
        DinnertableModel dinnertable = new DinnertableModel();
        dinnertable.setStateInfo(stateInfo, Collections.EMPTY_LIST);
        dinnertable.setId(tables.getId());
        return dinnertable;
    }

    private void clearTable(DinnertableModel mode) {
        TablesDal dal = OperatesFactory.create(TablesDal.class);

        dal.clearDinnertable(mode, LoadingResponseListener.ensure(new ResponseListener<ClearDinnertableResp>() {
            @Override
            public void onResponse(ResponseObject<ClearDinnertableResp> response) {
                if (response.getContent() != null) {
                    if (onTableSelectedConfirmListener != null) {
                        onTableSelectedConfirmListener.onConfirm(response.getContent().getTables().get(0));
                    }
                    dismiss();
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager()));
    }

    public interface OnTableSelectedConfirmListener {

        public void onConfirm(Tables tables);

    }

}
