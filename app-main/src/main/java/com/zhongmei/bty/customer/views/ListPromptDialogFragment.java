package com.zhongmei.bty.customer.views;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.DialogInterface.OnKeyListener;
import android.view.View.OnClickListener;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 可以显示多行内容的确定框.
 */
@EFragment(R.layout.customer_list_dialog_layout)
public class ListPromptDialogFragment extends BasicDialogFragment implements OnClickListener, OnKeyListener {

    private String title;


    private int iconType;
    protected List<String> mContentList;

    /*public static final int ICON_ERROR = R.drawable.commonmodule_dialog_icon_error;

    public static final int ICON_SUCCESS = R.drawable.commonmodule_dialog_icon_success;

    public static final int ICON_WARNING = R.drawable.common_dialog_icon_warning;

    public static final int ICON_HINT = R.drawable.commonmodule_dialog_icon_hint;*/

    @ViewById(R.id.common_dialog_title)
    protected TextView mTitle;

    @ViewById(R.id.positive_button)
    protected Button mPositiveButton;

    @ViewById(R.id.icon)
    protected ImageView mIconView;


    @ViewById(R.id.listview)
    protected ListView mlistView;

    private StringListAdapter mAdapter;

    private boolean mCancelWithHomeKey = true;

    OnClickListener mpositiveLinstner;

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    public void show(FragmentManager manager, String tag) {
        if (manager != null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
        getDialog().setOnKeyListener(this);
        if (iconType > 0)
            mIconView.setBackgroundResource(iconType);
        mPositiveButton.setOnClickListener(this);
        if (mContentList != null) {
            mAdapter = new StringListAdapter(mContentList, getActivity().getApplicationContext());
            mlistView.setAdapter(mAdapter);
        }
    }

    public ListPromptDialogFragment setIconType(int iconType) {
        this.iconType = iconType;
        return this;
    }

    public ListPromptDialogFragment setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public ListPromptDialogFragment setContentList(List<String> contentList) {
        this.mContentList = contentList;
        return this;
    }

    public ListPromptDialogFragment setPositiveListener(OnClickListener listener) {
        mpositiveLinstner = listener;
        return this;
    }

    /**
     * 刷新ListView的数据
     */
    public void refreshListData(List<String> printerStatusInfoList) {
        mContentList = printerStatusInfoList;
        if (mAdapter != null) {
            mAdapter.reshDataList(mContentList);
        }

    }

    public void setCancelWithHomeKey(boolean mCancelWithHomeKey) {
        this.mCancelWithHomeKey = mCancelWithHomeKey;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mPositiveButton)) {
            if (mpositiveLinstner != null) {
                mpositiveLinstner.onClick(view);
            }
        }
        this.dismissAllowingStateLoss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            if (mCancelWithHomeKey) {
                dismiss();
            }
            getActivity().onKeyDown(keyCode, event);
            return false;
        }
        return false;
    }


    private class StringListAdapter extends BaseAdapter {
        private List<String> mDataInfoList;

        private Context mContext;

        public StringListAdapter(List<String> dataInfoList, Context context) {
            this.mDataInfoList = dataInfoList;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mDataInfoList == null ? 0 : mDataInfoList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.printer_staus_adapter, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_name.setGravity(Gravity.LEFT);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_name.setText(mDataInfoList.get(i));

            return convertView;

        }

        public void reshDataList(List<String> dataInfoList) {
            mDataInfoList = dataInfoList;
            notifyDataSetChanged();
        }

        class ViewHolder {
            TextView tv_name;
        }
    }
}

