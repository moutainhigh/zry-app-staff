package com.zhongmei.beauty;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhongmei.yunfu.R;
import com.zhongmei.beauty.adapter.SchedulingAdapter;
import com.zhongmei.beauty.entity.SchedulingVo;
import com.zhongmei.beauty.widgets.ItemDivider;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.beauty_fragment_employee_manage)
public class BeautyEmployeeFragment extends BasicFragment {

    @ViewById(R.id.lv_employee)
    protected RecyclerView lv_scheduling;

    @Bean
    protected SchedulingAdapter mSchedulingAdapter;


    @AfterViews
    protected void init() {
        LinearLayoutManager manager = new RecyclerLinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_scheduling.setLayoutManager(manager);

        lv_scheduling.addItemDecoration(new ItemDivider(getContext(), R.drawable.beauty_line_gray_horizontal));

        List<SchedulingVo> items = new ArrayList<>();
        items.add(new SchedulingVo());
        items.add(new SchedulingVo());
        items.add(new SchedulingVo());
        items.add(new SchedulingVo());

        mSchedulingAdapter.setItems(items);

        lv_scheduling.setAdapter(mSchedulingAdapter);
    }
}
