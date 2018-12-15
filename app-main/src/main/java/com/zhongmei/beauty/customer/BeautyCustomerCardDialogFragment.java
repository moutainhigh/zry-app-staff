package com.zhongmei.beauty.customer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.beauty.customer.adapter.BeautyCustomerCardAdapter;
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 美业登录
 */
public class BeautyCustomerCardDialogFragment extends BasicDialogFragment implements View.OnClickListener {

    public static final String TAG = BeautyCustomerCardDialogFragment.class.getSimpleName();

    private ImageButton mIbClose;

    private RecyclerView mRvContent;

    private List<CustomerCardItem> mCards = new ArrayList<>();

    private CustomerResp mCustomerNew;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mCustomerNew = (CustomerResp) getArguments().getSerializable(BeautyCustomerConstants.KEY_CUSTOMER_INFO);
            mCards.clear();
            if (mCustomerNew.cardList != null && mCustomerNew.cardList.size() > 0) {
                mCards.addAll(mCustomerNew.cardList);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beauty_customer_card_list, container, false);
        mIbClose = (ImageButton) view.findViewById(R.id.ib_close);
        mRvContent = (RecyclerView) view.findViewById(R.id.rv_content);
        mIbClose.setOnClickListener(this);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        // 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(linearLayoutManager);
        mRvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == mCards.size()) { // ji有Header不需要减一
                    outRect.bottom = 13;
                }
                outRect.top = 13;
            }
        });
        BeautyCustomerCardAdapter adapter = new BeautyCustomerCardAdapter(getActivity(), mCards, mCustomerNew);
        adapter.setOnCardItemClickListener(new BeautyCustomerCardAdapter.OnCardItemClickListener() {
            @Override
            public void onCardItemListener(@NotNull CustomerResp customerNew, @NotNull EcCardInfo item) {
                showChargingDialog(customerNew, item);
            }

            @Override
            public void onCustomerListener(@NotNull CustomerResp customerNew) {
                showChargingDialog(customerNew, null);
//                dismiss();
            }
        });
        mRvContent.setAdapter(adapter);
    }

    /**
     * 实体卡会员充值界面
     *
     * @param customer 顾客信息
     * @param ecCard   实体卡信息 , 会员充值 传 null
     */
    private void showChargingDialog(CustomerResp customer, EcCardInfo ecCard) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_CARD_BEATY_MAIN);//来自顾客界面
        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        if (ecCard != null) {
            args.putSerializable(CustomerChargingDialogFragment.KEY_ECCARD, ecCard);
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, ecCard.getRemainValue() != null ? ecCard.getRemainValue().toString() : "0");
        } else {
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, customer.remainValue != null ? customer.remainValue.toString() : "0");
        }
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(), "ecCardCharging");
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
