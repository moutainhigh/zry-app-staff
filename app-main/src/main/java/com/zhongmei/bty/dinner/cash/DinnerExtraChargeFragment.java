package com.zhongmei.bty.dinner.cash;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.view.WrapGridView;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.bty.dinner.adapter.DinnerExtraAdapter;
import com.zhongmei.bty.basemodule.discount.bean.DinnerExtraChargeVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import de.greenrobot.event.EventBus;

/**
 * 附加费界面
 *
 * @date:2016年3月10日上午9:22:20
 */
@EFragment(R.layout.dinner_extra)
public class DinnerExtraChargeFragment extends BasicFragment implements OnItemClickListener, DinnerExtraAdapter.OnStatusChangeListner {

    private static final String TAG = "DinnerExtraChargeFragment";

    @ViewById(R.id.dinner_extra_gridview)
    WrapGridView mGridView;

    private List<DinnerExtraChargeVo> extraVoList = new ArrayList<DinnerExtraChargeVo>();

    private DinnerExtraAdapter extraAdapter;

    ExtraManager extraManager;

    @AfterViews
    void init() {
        initExraData();
        extraAdapter = new DinnerExtraAdapter(getActivity(), extraVoList);
        extraAdapter.setmStatusChangeListener(this);
        mGridView.setAdapter(extraAdapter);
//		mGridView.setOnItemClickListener(this);
        DinnerShopManager.getInstance().getShoppingCart().registerListener(ShoppingCartListerTag.DINNER_EXTRA, listener);
    }

    ShoppingCartListener listener = new ShoppingCartListener() {
        public void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId) {
            extraManager.resetSelectById(extraVoList, extraChargeId);
            extraAdapter.notifyDataSetChanged();
        }

        ;

    };

    @Click({R.id.btn_close})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;
        }
    }

    /**
     * @Title: loadExraData
     * @Description: 加载附加费数据
     * @Param TODO
     * @Return void 返回类型
     */
    private void initExraData() {
        extraManager = new ExtraManager();
        extraVoList = extraManager.getExtraListVoByCommercialArea(DinnerShopManager.getInstance().getShoppingCart().getOrder());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        DinnerShopManager.getInstance().getShoppingCart().unRegisterListenerByTag(ShoppingCartListerTag.DINNER_EXTRA);
    }

    @Override
    public void onStatusChange(int position, boolean isCheck) {
        DinnerExtraChargeVo extraChagreVo = extraAdapter.getItem(position);
        ExtraCharge extraCharge = extraChagreVo.getExtrageCharge();
        if (!isCheck) {
            // 从购物车移除附加费
            DinnerShopManager.getInstance().getShoppingCart().removeExtraCharge(extraCharge.getId());
        } else {
            // 添加附加费入购物车
            List<ExtraCharge> exCharges = new ArrayList<ExtraCharge>();
            exCharges.add(extraCharge);
            DinnerShopManager.getInstance().getShoppingCart().addExtraCharge(exCharges, true, true);
        }
    }
}
