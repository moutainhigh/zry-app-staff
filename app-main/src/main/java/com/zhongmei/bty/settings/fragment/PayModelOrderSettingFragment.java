package com.zhongmei.bty.settings.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.mobilepay.bean.PayMethodItem;
import com.zhongmei.bty.basemodule.commonbusiness.adapter.BaseListAdapter;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.settings.view.DragGridView;
import com.zhongmei.bty.settings.view.DragGridView.OnItemChangerListener;
import com.zhongmei.bty.commonmodule.database.entity.local.PayMenuOrder;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.basemodule.pay.operates.PayMenuOrderdal;
import com.zhongmei.bty.mobilepay.bean.PaymentMenuTool;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;




@EFragment(R.layout.setting_paymodel_order_fragment_layout)
public class PayModelOrderSettingFragment extends Fragment {
    private final String TAG = "PayModelOrderSetting";
    @ViewById(R.id.myDragGridView)
    DragGridView mDragGridView;    List<PayMethodItem> methodList = new ArrayList<PayMethodItem>();
    PayMethodAdapter adapter;

    @AfterViews
    void init() {
        adapter = new PayMethodAdapter(this.getActivity());
                PaymentMenuTool menuTool = new PaymentMenuTool(getActivity(), BusinessType.DINNER);
        menuTool.isBuildEmpty(false);
        methodList.addAll(menuTool.initMethodList());
        adapter.addList(methodList);
        mDragGridView.setAdapter(adapter);
        mDragGridView.setOnItemChangeListener(mOnItemChangerListener);

    }

    @UiThread
    @Click(R.id.save_paymode_order)
    public void savePayModelOrder() {
        if (!ClickManager.getInstance().isClicked() && !mDragGridView.isDrag()) {
            if (!Utils.isEmpty(methodList)) {
                int count = methodList.size();
                List<PayMenuOrder> list = new ArrayList<PayMenuOrder>(count);
                PayMethodItem item = null;
                PayMenuOrder order = null;
                for (int i = 0; i < count; i++) {
                    item = methodList.get(i);
                    order = new PayMenuOrder();
                    order.setOrder(i);
                    order.setPayMenuId(Long.valueOf(item.methodId));
                    list.add(order);
                }
                PaySettingCache.refreshPayMenuOrder(list);
                saveOrder(list);
                ToastUtil.showLongToast(getString(R.string.save_success));
            }
        }
    }

    @Background
    protected void saveOrder(final List<PayMenuOrder> list) {
        if (!Utils.isEmpty(list)) {
            PayMenuOrderdal dal = new PayMenuOrderdal();
            dal.saveOrder(list);
        }
    }

    private OnItemChangerListener mOnItemChangerListener = new OnItemChangerListener() {
        @Override
        public void onChange(int from, int to) {
            if (from < 0) {
                return;
            }
            if (from < to) {
                for (int i = from; i < to; i++) {
                    PayMethodItem it1 = methodList.get(i);
                    PayMethodItem it2 = methodList.get(i + 1);
                    methodList.set(i, it2);
                    methodList.set(i + 1, it1);
                }
            } else if (from > to) {
                for (int i = from; i > to; i--) {
                    PayMethodItem it1 = methodList.get(i);
                    PayMethodItem it2 = methodList.get(i - 1);
                    methodList.set(i, it2);
                    methodList.set(i - 1, it1);
                }
            }
            adapter.clear();
            adapter.addList(methodList);
        }
    };

    private static class ViewHolder {
        LinearLayout linearLayout;
        ImageView methodIconImg;
        TextView methodNameTxv;
    }

    private static class PayMethodAdapter extends BaseListAdapter<ViewHolder, PayMethodItem> {


        public PayMethodAdapter(Context context) {
            super(context, R.layout.setting_pay_method_item);
        }


        @Override
        public ViewHolder initViewHodler(View convertView) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.pay_method_item_layout);
            viewHolder.methodIconImg = (ImageView) convertView.findViewById(R.id.pay_method_item_img);
            viewHolder.methodNameTxv = (TextView) convertView.findViewById(R.id.pay_method_item_txv);
            return viewHolder;
        }

        @Override
        public void setViewHodler(ViewHolder viewHolder, int position) {
            PayMethodItem item = getItem(position);
            viewHolder.linearLayout.setFocusable(false);
            viewHolder.linearLayout.setEnabled(false);
            viewHolder.linearLayout.setSelected(false);
                        viewHolder.methodIconImg.setSelected(false);
            viewHolder.methodNameTxv.setSelected(item.isSelected);
            viewHolder.methodNameTxv.setText(item.methodName);
            viewHolder.methodIconImg.setImageResource(item.methodResId);
        }
    }
}

