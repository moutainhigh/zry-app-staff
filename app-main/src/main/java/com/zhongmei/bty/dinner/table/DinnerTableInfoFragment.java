package com.zhongmei.bty.dinner.table;

import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.table.view.NumberAndWaiterViewTable;
import com.zhongmei.bty.dinner.table.view.NumberAndWaiterViewTable_;
import com.zhongmei.bty.dinner.table.view.ViewUtils;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;

import org.androidannotations.annotations.EFragment;



@EFragment(R.layout.dinner_table_info)
public class DinnerTableInfoFragment extends TableInfoFragment {

        NumberAndWaiterViewTable viewNumberAndWaiter;

    @Override
    public View getNumberAndWaiterView() {
        if (viewNumberAndWaiter == null) {
            viewNumberAndWaiter = ViewUtils.inflateDinnerNumberAndWaiterPanel(getActivity().getApplicationContext());
            viewNumberAndWaiter.setActivity(getActivity());
            viewNumberAndWaiter.setmBusinessType(dinnertableFragment.getBussinessType());
        }

        return viewNumberAndWaiter;
    }

    @Override
    public void setNumberAndWaiter(DinnertableTradeVo dinnertableTradeVo) {
        if (viewNumberAndWaiter != null) {
            viewNumberAndWaiter.show(dinnertableTradeVo, DinnerTableInfoFragment.this);
        }
    }

    @Override
    public boolean isShowJoinTrade() {
        if (dataManage.isUnionTrade()) {
            return true;
        }

        return false;
    }
}
