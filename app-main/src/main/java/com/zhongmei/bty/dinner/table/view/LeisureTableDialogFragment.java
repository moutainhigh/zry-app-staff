package com.zhongmei.bty.dinner.table.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.dinner.adapter.EmptyTablesListAdapter;
import com.zhongmei.bty.dinner.adapter.LeisureTableDialogPagerAdapter;
import com.zhongmei.bty.dinner.table.manager.OpenTableManager;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.bty.dinner.vo.DinnertableVo;
import com.zhongmei.bty.dinner.vo.EmptyTableVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LeisureTableDialogFragment extends BasicDialogFragment
        implements View.OnClickListener {
    private ImageView mClose;

    private ViewPager mViewPager;

    private ImageView mLeft;

    private ImageView mRight;

    private ListView mListView;

    private RelativeLayout mEmptyView, mLeftLayout, mRightLayout;

    private TablesDal tablesDal;
        private List<Tables> emptTabls;

    private List<CommercialArea> tableAreas;

    private List<TableSeat> emptTableSeats;

    private ArrayList<EmptyTableVo> emptyTableData;

    private ArrayList<EmptyTableVo> tempTables;

    private LeisureTableDialogPagerAdapter mPagerAdapter;

    private EmptyTablesListAdapter mListAdapter;

    private int pagerIndex = 0;

    private BusinessType mBusinessType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tablesDal = OperatesFactory.create(TablesDal.class);
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


    public LeisureTableDialogFragment setmBusinessType(BusinessType mBusinessType) {
        this.mBusinessType = mBusinessType;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leisure_table_dialog_fragment, container, false);
        emptTabls = new ArrayList<>();
        tableAreas = new ArrayList<>();
        emptyTableData = new ArrayList<>();
        emptTableSeats = new ArrayList<>();
        tempTables = new ArrayList<EmptyTableVo>();
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        asynInitData();
    }

    private void initView(View v) {
        mClose = (ImageView) v.findViewById(R.id.btn_close);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mLeft = (ImageView) v.findViewById(R.id.left_btn);
        mRight = (ImageView) v.findViewById(R.id.right_btn);
        mListView = (ListView) v.findViewById(R.id.listview);
        mEmptyView = (RelativeLayout) v.findViewById(R.id.empty_view);
        mLeftLayout = (RelativeLayout) v.findViewById(R.id.left_layout);
        mRightLayout = (RelativeLayout) v.findViewById(R.id.right_layout);
        mListView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mClose.setOnClickListener(this);
        mLeftLayout.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);
        mViewPager.setOffscreenPageLimit(5);
    }

    private void asynInitData() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                initData();
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                tempTables.clear();
                tempTables.addAll(emptyTableData);
                if (tempTables.size() != 0) {
                    mListView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                Collections.sort(tempTables, new PeopleCountComparator());

                mListAdapter = new EmptyTablesListAdapter(getActivity(), tempTables);
                mPagerAdapter = new LeisureTableDialogPagerAdapter(getActivity(), emptTabls);
                updatePager();
                updateList();
            }
        });
    }

    private void initData() {
        try {
            List<Tables> tables = tablesDal.listDinnerEmptyTablesByStatus(TableStatus.EMPTY);
            emptTabls.clear();
            emptTabls.addAll(tables);
            emptTabls.size();
            List<CommercialArea> areas = tablesDal.listTableArea();
            tableAreas.clear();
            tableAreas.addAll(areas);
            List<TableSeat> tableSeats = tablesDal.listTableSeats();
            emptTableSeats.clear();
            emptTableSeats.addAll(tableSeats);

            getListData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePager() {
        if (pagerIndex == 0) {
            mLeft.setEnabled(false);
        } else {
            mLeft.setEnabled(true);
        }
        if (pagerIndex == mPagerAdapter.getCount() - 1) {
            mRight.setEnabled(false);
        } else {
            mRight.setEnabled(true);
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(listener);
        if (mPagerAdapter != null) {
            mPagerAdapter.setSelectContent(new LeisureTableDialogPagerAdapter.SelectContent() {
                @Override
                public void send(Integer peopleCount) {
                    tempTables.clear();
                    if (peopleCount.intValue() == 0) {
                        tempTables.addAll(emptyTableData);
                    } else {
                        for (int i = 0; i < emptyTableData.size(); i++) {
                            if (emptyTableData.get(i).getEmptyTables().getTablePersonCount().intValue() == peopleCount.intValue()) {
                                tempTables.add(emptyTableData.get(i));
                            }
                        }
                    }
                    Collections.sort(tempTables, new PeopleCountComparator());
                    mListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void getListData() {
        Map<Long, CommercialArea> mapAreas = new HashMap<>();
        for (CommercialArea tableArea : tableAreas) {
            mapAreas.put(tableArea.getId(), tableArea);
        }

        Map<Long, List<TableSeat>> mapTableSeats = new HashMap<>();
        for (TableSeat tableSeat : emptTableSeats) {
            if (!mapTableSeats.containsKey(tableSeat.getTableId())) {
                mapTableSeats.put(tableSeat.getTableId(), new ArrayList<TableSeat>());
            }
            mapTableSeats.get(tableSeat.getTableId()).add(tableSeat);
        }


        EmptyTableVo vo;
        for (Tables table : emptTabls) {
            vo = new EmptyTableVo();
            vo.setEmptyTables(table);
            vo.setTableArea(mapAreas.get(table.getAreaId()));
            vo.setTableSeats(mapTableSeats.get(table.getId()));
            emptyTableData.add(vo);
        }

    }

    private void updateList() {
        mListView.setAdapter(mListAdapter);
        mListAdapter.setListener(new EmptyTablesListAdapter.OnItemBtnClickListener() {
            @Override
            public void onClick(final EmptyTableVo vo) {
                VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CREATE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                DinnertableModel dinnertableModel = createModel(vo);
                                OpenTableManager manager = new OpenTableManager(new DinnertableVo(dinnertableModel),
                                        dinnertableModel.getNumberOfSeats(), getActivity(), mBusinessType);
                                manager.finishOpenTable();
                                dismiss();
                            }
                        });
            }
        });
    }

    private class PeopleCountComparator implements Comparator<EmptyTableVo> {

        @Override
        public int compare(EmptyTableVo o1, EmptyTableVo o2) {
            Integer t1 = o1.getEmptyTables().getTablePersonCount();
            Integer t2 = o2.getEmptyTables().getTablePersonCount();
            return t1.compareTo(t2);
        }
    }


    private DinnertableModel createModel(EmptyTableVo vo) {
        ZoneModel zoneModel = new ZoneModel();
        zoneModel.setId(vo.getTableArea().getId());
        zoneModel.setCode(vo.getTableArea().getAreaCode());
        zoneModel.setName(vo.getTableArea().getAreaName());
        DinnertableModel dinnertableModel = new DinnertableModel();
        dinnertableModel.setId(vo.getEmptyTables().getId());
        dinnertableModel.setName(vo.getEmptyTables().getTableName());
        dinnertableModel.setNumberOfSeats(vo.getEmptyTables().getTablePersonCount());
        dinnertableModel.setZone(zoneModel);
        dinnertableModel.setUuid(vo.getEmptyTables().getUuid());
        dinnertableModel.setTableSeats(vo.getTableSeats());
        return dinnertableModel;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagerIndex = position;
            if (mPagerAdapter != null) {
                if (position == 0) {
                    mLeft.setEnabled(false);
                } else {
                    mLeft.setEnabled(true);
                }
                if (position == mPagerAdapter.getCount() - 1) {
                    mRight.setEnabled(false);
                } else {
                    mRight.setEnabled(true);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.left_layout:
                mViewPager.setCurrentItem(pagerIndex - 1);
                break;
            case R.id.right_layout:
                mViewPager.setCurrentItem(pagerIndex + 1);
                break;
            default:
                break;
        }
    }
}
