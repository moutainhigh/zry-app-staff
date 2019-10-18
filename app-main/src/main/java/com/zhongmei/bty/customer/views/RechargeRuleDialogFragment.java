package com.zhongmei.bty.customer.views;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.customer.adapter.RechargeRuleDialogFragmentAdapter;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


@EFragment(R.layout.recharge_rule_dialog)
public class RechargeRuleDialogFragment extends BasicDialogFragment implements OnClickListener {
    @ViewById(R.id.recharge_dialog_listview)
    ListView rechargeDialogListView;

    @ViewById(R.id.close_btn)
    Button closeBtn;

    @ViewById(R.id.send_value_title_tx)
    TextView mSendValueTitleTx;

    private RechargeRuleVo ruleVo;

    BaseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        this.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return null;

    }

    @AfterViews
    public void init() {
        closeBtn.setOnClickListener(this);
        adapter = new RechargeRuleDialogFragmentAdapter(getActivity(), ruleVo);
        rechargeDialogListView.setAdapter(adapter);
        if (ruleVo != null) {
            SendType sendType = ruleVo.getSendType();
            if (sendType != null) {
                switch (sendType) {
                    case FIXED:
                        mSendValueTitleTx.setText(getResources().getString(R.string.given_money));
                        break;

                    case RATIO:
                        mSendValueTitleTx.setText(getResources().getString(R.string.given_rate));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                this.dismiss();
                break;

            default:
                break;
        }
    }

    public void setDataSource(RechargeRuleVo ruleVo) {
        this.ruleVo = ruleVo;
    }
}
