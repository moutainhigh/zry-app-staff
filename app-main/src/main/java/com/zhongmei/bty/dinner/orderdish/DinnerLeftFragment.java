package com.zhongmei.bty.dinner.orderdish;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.dinner.orderdish.view.DinnerNumberAndWaiterView;
import com.zhongmei.bty.dinner.orderdish.view.NumberAndWaiterView;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * @Date： 17/6/19
 * @Description:
 * @Version: 1.0
 */

@EFragment(R.layout.dinner_left)
public class DinnerLeftFragment extends DishLeftFragment {

    @Override
    public NumberAndWaiterView getViewNumberAndWaiter() {
        return new DinnerNumberAndWaiterView(getActivity());
    }

    @Override
    public void refreshShopCart() {

    }

    @Override
    protected BusinessType getBusinessType() {
        return BusinessType.DINNER;
    }

    @Override
    protected boolean isDinner() {
        return true;
    }

    @Override
    protected boolean isOnlySingleAddDish(List<String> uuids) {
        return true;
    }

    @Override
    protected boolean isPrintLabel() {
        return true;
    }
}
