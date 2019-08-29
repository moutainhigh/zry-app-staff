package com.zhongmei.beauty.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.beauty.adapter.BeautyTableChoiceAdapter;
import com.zhongmei.beauty.adapter.TableItemDecoration;
import com.zhongmei.beauty.adapter.TableZonesChoiceAdapter;
import com.zhongmei.beauty.interfaces.ITableChoice;
import com.zhongmei.beauty.interfaces.ITableOperator;
import com.zhongmei.beauty.operates.TableFilterManager;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 */
public class BeautyTablePopWindow extends PopupWindow implements ITableOperator, CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {

    private Context mContext;

    private BusinessType mBusinessType;

    private RelativeLayout rl_parent;

    private RadioButton rb_all;

    private RadioButton rb_empty;

    private RadioButton rb_busy;

    private RecyclerView gv_tables;

    private Button btn_cancel;

    private Button btn_sure;

    private LinearLayout layout_zone;

    private TextView tv_zone;

    private ListView lv_zones;

    private ZoneModel mCurModel = null;

    private TableStatus mCurTableStatus = null;

    private UIHandler mUiHandler;

    private ITableChoice mTableChoiceListener;

    private TableZonesChoiceAdapter mZonesChoiceAdapter;

    protected BeautyTableChoiceAdapter mAdapter;

    private TableFilterManager mTableFileterManager;

    private List<ZoneModel> mListZoneModel;

    private Set<Long> tableSetTmp;//临时存放选中的tableId

    public void setiTableChoiceListener(ITableChoice tableChoiceListener) {
        this.mTableChoiceListener = tableChoiceListener;
    }

    public BeautyTablePopWindow(Context context, BusinessType businessType) {
        super(context);
        //初始化popuwindow
        this.mContext = context;
        this.mBusinessType = businessType;
        this.mUiHandler = new UIHandler(context.getMainLooper());
        initView(context);
        initData();
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popuwindow_beauty_orderdish_table, null);
        setContentView(view);
        setWidth(DensityUtil.dip2px(MainApplication.getInstance(), 500f));
        setHeight(DensityUtil.dip2px(MainApplication.getInstance(), 550f));
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.beauty_bg_dialog));
        setFocusable(false);
        setOutsideTouchable(true);


        rl_parent = (RelativeLayout) view.findViewById(R.id.rl_parent);
        rb_all = (RadioButton) view.findViewById(R.id.rb_all);
        rb_empty = (RadioButton) view.findViewById(R.id.rb_empty);
        rb_busy = (RadioButton) view.findViewById(R.id.rb_busy);
        layout_zone = (LinearLayout) view.findViewById(R.id.layout_zone);
        tv_zone = (TextView) view.findViewById(R.id.tv_zone);
        gv_tables = (RecyclerView) view.findViewById(R.id.gv_tables);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        lv_zones = (ListView) view.findViewById(R.id.lv_zones);

        rb_all.setChecked(true);

        rl_parent.setOnTouchListener(this);
        gv_tables.setOnTouchListener(this);
        rb_all.setOnCheckedChangeListener(this);
        rb_empty.setOnCheckedChangeListener(this);
        rb_busy.setOnCheckedChangeListener(this);

        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        layout_zone.setOnClickListener(this);
        lv_zones.setOnItemClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        gridLayoutManager.setReverseLayout(false);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        gv_tables.setLayoutManager(gridLayoutManager);
        gv_tables.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gv_tables.addItemDecoration(new TableItemDecoration(DensityUtil.dip2px(context, 22), DensityUtil.dip2px(context, 13)));

        mAdapter = new BeautyTableChoiceAdapter(context);
        gv_tables.setAdapter(mAdapter);
    }

    private void initData() {
        mTableFileterManager = new TableFilterManager();//默认加载桌台信息，异步
        mTableFileterManager.setTableListener(this);
        mTableFileterManager.loadTables();
    }


    public void settables(List<TradeTable> tradeTables) {
        if (Utils.isEmpty(tradeTables)) {
            return;
        }

        Set<Long> tableSet = new HashSet<>();
        for (TradeTable tradeTable : tradeTables) {
            tableSet.add(tradeTable.getTableId());
        }

        if (mAdapter != null) {
            mAdapter.setCheckedTableSet(tableSet);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    @Override
    public void refreshZones(List<ZoneModel> zoneModes) {

    }

    @Override
    public void refreshTableTrades(List<ZoneModel> zoneModes) {

    }

    @Override
    public void refreshTables(List<DinnertableModel> tableModes) {
        Message msg = mUiHandler.obtainMessage(1);
        msg.obj = tableModes;
        mUiHandler.sendMessage(msg);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_all:
                if (isChecked) {
                    mCurTableStatus = null;
                    refreshTableListView();
                }
                break;
            case R.id.rb_empty:
                if (isChecked) {
                    mCurTableStatus = TableStatus.EMPTY;
                    refreshTableListView();
                }
                break;
            case R.id.rb_busy:
                if (isChecked) {
                    mCurTableStatus = TableStatus.OCCUPIED;
                    refreshTableListView();
                }
                break;
        }


    }

    private void refreshTableListView() {
        Long zoneId = null;

        if (mCurModel != null) {
            zoneId = mCurModel.getId();
        }
        List<DinnertableModel> tableModess = mTableFileterManager.filter(zoneId, mCurTableStatus);
        mAdapter.setItems(tableModess);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                this.dismiss();
                break;
            case R.id.btn_sure:
                sure();
                break;
            case R.id.layout_zone:
                if (lv_zones.getVisibility() == View.VISIBLE) {
                    lv_zones.setVisibility(View.GONE);
                } else {
                    lv_zones.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void sure() {
        List<DinnertableModel> listTableMode = mAdapter.getChoicedTables();
        if (mTableChoiceListener != null) {
            mTableChoiceListener.choiceTables(transferTables(listTableMode));
        }
        this.dismiss();
    }

    private List<Tables> transferTables(List<DinnertableModel> tableModes) {
        if (Utils.isEmpty(tableModes)) {
            return null;
        }

        List<Tables> choiceTables = new ArrayList<>();

        for (DinnertableModel tableMode : tableModes) {
            Tables table = new Tables();
            table.setAreaId(tableMode.getZone().getId());
            table.setTableName(tableMode.getName());
            table.setTableStatus(tableMode.getPhysicsTableStatus());
            table.setStatusFlag(StatusFlag.VALID);
            table.setId(tableMode.getId());
            table.setUuid(tableMode.getUuid());
            table.setModifyDateTime(tableMode.getServerUpdateTime());

            choiceTables.add(table);
        }

        return choiceTables;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (lv_zones.getVisibility() == View.VISIBLE) {
            if (!DensityUtil.isRangeOfView(lv_zones, event)) {
                lv_zones.setVisibility(View.GONE);
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //设置返回只
        mCurModel = mListZoneModel.get(position);
        mZonesChoiceAdapter.setCheckZoneModel(mCurModel);
        mZonesChoiceAdapter.notifyDataSetChanged();
        tv_zone.setText(mCurModel.getName());

        refreshTableListView();
        lv_zones.setVisibility(View.GONE);
    }

    class UIHandler extends Handler {

        public UIHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mAdapter.setItems((List<DinnertableModel>) msg.obj);
                    mAdapter.notifyDataSetChanged();

                    mListZoneModel = mTableFileterManager.getZoneModels();

                    ZoneModel allZoneModel = new ZoneModel();
                    allZoneModel.setName("全部");
                    allZoneModel.setId(-1L);
                    mListZoneModel.add(0, allZoneModel);
                    mCurModel = allZoneModel;

                    mZonesChoiceAdapter = new TableZonesChoiceAdapter(mContext, mListZoneModel);
                    mZonesChoiceAdapter.setCheckZoneModel(allZoneModel);
                    lv_zones.setAdapter(mZonesChoiceAdapter);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void onDestoery() {
        if (mTableFileterManager != null) {
            mTableFileterManager.onDestory();
        }
    }
}
