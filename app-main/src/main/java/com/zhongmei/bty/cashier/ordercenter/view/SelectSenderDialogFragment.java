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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.cashier.ordercenter.adapter.SelectItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SelectSenderDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button mOkBtn;
    private ImageView mCloseBtn;
    private ListView mListView;
    private SelectItemAdapter selectSenderAdapter;
    private OnSelectAuthUserListener mListener;
    private List<User> authUsers;
    public final static String ITEMS_KEY = "items_key";
    public final static String DEFAULT_SELECTED_AUTH_USER_ID = "default_selected_auth_user_id";//默认选中的配送员
    private String defaultSelectedAuthUserId;

    public static SelectSenderDialogFragment newInstance(List<User> authUsers, String defaultSelectedAuthUserId) {
        SelectSenderDialogFragment fragment = new SelectSenderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEMS_KEY, (Serializable) authUsers);
        bundle.putString(DEFAULT_SELECTED_AUTH_USER_ID, defaultSelectedAuthUserId);
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
        View view = inflater.inflate(R.layout.select_sender_dialog_layout, null);
        mOkBtn = (Button) view.findViewById(R.id.btn_ok);
        mOkBtn.setOnClickListener(this);
        mCloseBtn = (ImageView) view.findViewById(R.id.btn_close);
        mCloseBtn.setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.switch_shop_listview);
        mListView.setOnItemClickListener(this);
        authUsers = (List<User>) getArguments().getSerializable(ITEMS_KEY);
        defaultSelectedAuthUserId = getArguments().getString(DEFAULT_SELECTED_AUTH_USER_ID);
        if (authUsers != null) {
            ArrayList<String> strings = new ArrayList<>();
            for (User user : authUsers) {
                strings.add(user.getName());
            }
            int defaultSelectedPosition = 0;
            for (int i = 0; i < authUsers.size(); i++) {
                if (defaultSelectedAuthUserId != null) {
                    if (String.valueOf(authUsers.get(i).getId()).equals(defaultSelectedAuthUserId)) {
                        defaultSelectedPosition = i;
                    }
                }
            }
            selectSenderAdapter = new SelectItemAdapter(getActivity());
            selectSenderAdapter.setData(strings);
            selectSenderAdapter.setSelectedPosition(defaultSelectedPosition);
            mListView.setAdapter(selectSenderAdapter);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (selectSenderAdapter != null) {
                    int position = selectSenderAdapter.getSelectedPosition();
                    if (mListener != null && position >= 0) {
                        mListener.onSelect(authUsers.get(position));
                    }
                }
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }

    }

    public void setListener(OnSelectAuthUserListener listener) {
        mListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectSenderAdapter.setSelectedPosition(position);
    }

    public interface OnSelectAuthUserListener {
        void onSelect(User authUser);
    }
}
