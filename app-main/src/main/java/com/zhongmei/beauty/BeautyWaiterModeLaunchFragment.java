package com.zhongmei.beauty;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zhongmei.beauty.adapter.ModuleAdapter;
import com.zhongmei.beauty.entity.AppContent;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 美业服务员模式
 */
@EFragment(R.layout.fragment_waiter_launch)
public class BeautyWaiterModeLaunchFragment extends BasicFragment implements AdapterView.OnItemClickListener {

    @ViewById(R.id.gv_launcher)
    GridView gv_modules;

    private ModuleAdapter mAdapter;


    @AfterViews
    protected void initView(){
        gv_modules.setOnItemClickListener(this);

        mAdapter=new ModuleAdapter(getContext(),getApps());
        gv_modules.setAdapter(mAdapter);
    }




    private List<AppContent> getApps(){
        List<AppContent> listApps=new ArrayList<>();

        listApps.add(new AppContent("收银/开单",BeautyMainActivity.PAGE_CASHIER,R.drawable.launcher_icon));
        listApps.add(new AppContent("预约中心",BeautyMainActivity.PAGE_RESERVER,R.drawable.launcher_icon));
        listApps.add(new AppContent("订单中心",BeautyMainActivity.PAGE_TRADE_CENTER,R.drawable.launcher_icon));
        listApps.add(new AppContent("会员中心",BeautyMainActivity.PAGE_MEMBER_CENTER,R.drawable.launcher_icon));
        listApps.add(new AppContent("门店管理",BeautyMainActivity.PAGE_SHOP_MANAGE,R.drawable.launcher_icon));
        listApps.add(new AppContent("报表中心",BeautyMainActivity.PAGE_REPORT_CENTER,R.drawable.launcher_icon));
        listApps.add(new AppContent("任务中心",BeautyMainActivity.PAGE_TASK_CENTER,R.drawable.launcher_icon));

        return listApps;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //判断会员登陆信息
        //跳转
        AppContent app=mAdapter.getItem(position);
        Intent intent=new Intent();
        intent.setClassName(app.getPackageName(),app.getActivityClass());
        intent.putExtra("page_no",app.getPageNo());
        startActivity(intent);
    }
}
