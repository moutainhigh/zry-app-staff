package com.zhongmei.bty.dinner.table.searchtable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.dinner.orderdish.DinnerDishSearchFragment;
import com.zhongmei.bty.dinner.orderdish.DinnerDishSearchFragment_;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment_;
import com.zhongmei.bty.basemodule.notifycenter.event.EventSelectDinnertableNotice;
import com.zhongmei.yunfu.db.enums.BusinessType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */

@EActivity(R.layout.dinner_search_table_activity)
public class SearchTableActivity extends MainBaseActivity implements DinnerDishSearchFragment.ChooseSelectedDishListener {
    @ViewById(R.id.tv_search_data_empty)
    protected TextView mEmptyView;

    @ViewById(R.id.ll_search_data_layout)
    protected LinearLayout mSearchDataLayout;

    @ViewById(R.id.tv_dish_name_label)
    protected TextView mDishNameTV;

    @ViewById(R.id.tv_dish_label)
    protected TextView mResultLabelTV;

    @ViewById(R.id.dinner_search_table_list)
    protected ListView mDataListView;

    private TableAreAdapter mTableAreAdapter;
    private SearchTableTool mSearchTableTool;
    private BusinessType mBusinessType;
    List<SearchTableTool.TableAreaVo> mSearchDataList;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("BusinessType")) {
            mBusinessType = (BusinessType) getIntent().getSerializableExtra("BusinessType");
        }
        MobclickAgentEvent.openActivityDurationTrack(false);
    }

    @AfterViews
    public void initView() {
        replaceTitleLayout();
        replaceSearchDishLayout();
        mSearchTableTool = new SearchTableTool(mBusinessType);
        mTableAreAdapter = new TableAreAdapter(this);
        if (mDataListView != null) {
            mDataListView.setAdapter(mTableAreAdapter);

            mDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mSearchDataList != null) {
                        SearchTableTool.TableAreaVo vo = mSearchDataList.get(position);
                        EventSelectDinnertableNotice event = new EventSelectDinnertableNotice(vo.table.getAreaId(), vo.table.getId(), null);
                        EventBus.getDefault().post(event);
                        finish();
                    }
                }
            });
        }
    }

    private void replaceTitleLayout() {
        TitleBarFragment titleBarFragment = new TitleBarFragment_();
        replaceFragment(R.id.dinner_title_bar_layout, titleBarFragment, titleBarFragment.getClass().getName());
    }

    @Override
    public void onBackPressed() {
        if (dinnerDishSearchFragment != null) {
            if (dinnerDishSearchFragment.keyboardIsHide()) {
                super.onBackPressed();
            } else {
                dinnerDishSearchFragment.hideKeyboard();
            }
        } else {
            super.onBackPressed();
        }
    }

    private DinnerDishSearchFragment dinnerDishSearchFragment;

    private void replaceSearchDishLayout() {
        dinnerDishSearchFragment = new DinnerDishSearchFragment_();
        dinnerDishSearchFragment.setSearchFromListener(DinnerDishSearchFragment.SEARCH_FROM_TABLE, this);
        replaceFragment(R.id.searchTableRightView, dinnerDishSearchFragment, dinnerDishSearchFragment.getClass().getName());
    }


    @Override
    public void onSelectedDish(final DishVo dishVo) {
        dishVo.setSelected(true);
        mDishNameTV.setText(dishVo.getDishShop().getName());
        new AsyncTask<Void, Void, List<SearchTableTool.TableAreaVo>>() {

            @Override
            protected List<SearchTableTool.TableAreaVo> doInBackground(Void... params) {
                List<String> dishUUids = new ArrayList<String>();
                dishUUids.add(dishVo.getDishShop().getUuid());
                //其它同类菜品
                if (dishVo.isContainProperties()) {
                    if (dishVo.getOtherDishs() != null) {
                        for (Map.Entry<String, DishShop> entry : dishVo.getOtherDishs().entrySet()) {
                            dishUUids.add(entry.getValue().getUuid());
                        }
                    }
                }
                return mSearchTableTool.findDishTables(dishUUids);
            }

            protected void onPostExecute(List<SearchTableTool.TableAreaVo> list) {
                mSearchDataList = list;
                if (mSearchDataList != null) {
                    updateSearchResultLabel(mSearchDataList.size());
                    mTableAreAdapter.reshDataList(mSearchDataList);
                } else {
                    updateSearchResultLabel(0);
                    mTableAreAdapter.reshDataList(null);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateSearchResultLabel(int count) {
        mEmptyView.setVisibility(View.GONE);
        mSearchDataLayout.setVisibility(View.VISIBLE);
        String countTxt = count + "";
        String allCount = String.format(getString(R.string.search_table_result_label), countTxt);
        SpannableStringBuilder builder = new SpannableStringBuilder(allCount);
        int startP = allCount.indexOf("(") + 2;
        int endP = startP + countTxt.length();
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_blue)), startP, endP, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mResultLabelTV.setText(builder);
        mResultLabelTV.setVisibility(View.VISIBLE);
    }

    @Click({R.id.dinner_balance_back})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.dinner_balance_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    public static class TableAreAdapter extends BaseAdapter {
        private List<SearchTableTool.TableAreaVo> dataList;

        private Context mContext;

        public TableAreAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return this.dataList == null ? 0 : dataList.size();
        }

        @Override
        public Object getItem(int i) {
            if (dataList != null)
                return dataList.get(i);
            else return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            AdapterViewHolder viewHolder = null;
            SearchTableTool.TableAreaVo vo = null;
            if (dataList != null)
                vo = dataList.get(i);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dinner_search_table_item_layout, null);
                viewHolder = new AdapterViewHolder();
                viewHolder.tv_tablename = (TextView) convertView.findViewById(R.id.tv_table_name);
                viewHolder.tv_areaname = (TextView) convertView.findViewById(R.id.tv_area_name);
                viewHolder.tv_tablestatus = (TextView) convertView.findViewById(R.id.tv_table_status);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (AdapterViewHolder) convertView.getTag();
            }
            if (vo != null) {
                viewHolder.tv_tablename.setText(vo.table.getTableName());
                viewHolder.tv_areaname.setText(vo.CommercialArea.getAreaName());
            }
            return convertView;
        }

        public void reshDataList(List<SearchTableTool.TableAreaVo> datas) {
            this.dataList = datas;
            notifyDataSetChanged();
        }
    }

    static class AdapterViewHolder {

        TextView tv_tablename;
        TextView tv_areaname;
        TextView tv_tablestatus;
    }
}
