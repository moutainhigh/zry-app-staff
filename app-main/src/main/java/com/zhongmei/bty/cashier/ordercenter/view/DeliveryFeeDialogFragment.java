package com.zhongmei.bty.cashier.ordercenter.view;


import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.cashier.ordercenter.adapter.DeliveryFeeItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DeliveryFeeDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button mOkBtn;
    private ImageView mCloseBtn;
    private ListView mListView;
    private DeliveryFeeItemAdapter deliveryFeeItemAdapter;
    private OnOkListener mListener;
    public final static String ITEMS_KEY = "items_key";
    public final static String TRADEVOS_KEY = "tradevos_key";
    private List<TradeVo> tradeVos;
    private List<BatchDeliveryFee> deliveryFees;

    public static DeliveryFeeDialogFragment newInstance(List<BatchDeliveryFee> deliveryFees, List<TradeVo> tradeVos) {
        DeliveryFeeDialogFragment fragment = new DeliveryFeeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEMS_KEY, (Serializable) deliveryFees);
        bundle.putSerializable(TRADEVOS_KEY, (Serializable) tradeVos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_center_delivery_fee_dialog, null);
        mOkBtn = (Button) view.findViewById(R.id.btn_ok);
        mOkBtn.setOnClickListener(this);
        mCloseBtn = (ImageView) view.findViewById(R.id.btn_close);
        mCloseBtn.setOnClickListener(this);
        tradeVos = (List<TradeVo>) getArguments().getSerializable(TRADEVOS_KEY);
        deliveryFees = (List<BatchDeliveryFee>) getArguments().getSerializable(ITEMS_KEY);
        mListView = (ListView) view.findViewById(R.id.switch_shop_listview);
        deliveryFeeItemAdapter = new DeliveryFeeItemAdapter(getActivity());
        deliveryFeeItemAdapter.setData(createTradeAndDeliveryFees(tradeVos, deliveryFees));
        mListView.setAdapter(deliveryFeeItemAdapter);
        return view;
    }

    private List<DeliveryFeeItemAdapter.TradeAndDeliveryFee> createTradeAndDeliveryFees(List<TradeVo> tradeVos, List<BatchDeliveryFee> deliveryFees) {
        List<DeliveryFeeItemAdapter.TradeAndDeliveryFee> tradeAndDeliveryFees = new ArrayList<>();
        for (int i = 0; i < tradeVos.size(); i++) {
            TradeVo tradeVo = tradeVos.get(i);
            DeliveryFeeItemAdapter.TradeAndDeliveryFee tradeAndDeliveryFee = new DeliveryFeeItemAdapter.TradeAndDeliveryFee();
            tradeAndDeliveryFee.sourceId = tradeVo.getTrade().getSource();
            tradeAndDeliveryFee.tradeNo = tradeVo.getTrade().getTradeNo();
            tradeAndDeliveryFee.serialNo = tradeVo.getTradeExtra().getThirdSerialNo();
            // TODO: 2018/2/7  do better 可能顺序不对 导致id和fee对不上
            tradeAndDeliveryFee.fee = deliveryFees.get(i).getFee();
            tradeAndDeliveryFees.add(tradeAndDeliveryFee);
        }
        return tradeAndDeliveryFees;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (mListener != null) {
                    mListener.onOkClick();
                }
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }

    }

    public void setListener(OnOkListener listener) {
        mListener = listener;
    }

    public interface OnOkListener {
        void onOkClick();
    }
}
