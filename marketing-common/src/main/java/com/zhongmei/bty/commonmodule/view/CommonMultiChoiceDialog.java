package com.zhongmei.bty.commonmodule.view;

import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多选框公共类
 *
 * @created 2017/7/3
 */
@SuppressLint("ValidFragment")
public class CommonMultiChoiceDialog<T> extends BasicDialogFragment {

    public interface OnItemClickListener<T> {
        String onItemGetName(T item);

        void OnItemClick(DialogFragment dialog, List<T> item);
    }

    TextView tvTitle;
    TextView tvSubTitle;
    ImageView ivClose;
    ListView lvItem;
    Button btnOk;

    MultiChoiceAdapter multiChoiceAdapter;
    OnItemClickListener itemClickListener;

    String title;
    String buttonText;
    List<T> titleItems;

    public static <T> CommonMultiChoiceDialog show(FragmentActivity activity, int titleResId, List<T> titleItems, int buttonTextResId, OnItemClickListener<T> listener) {
        return CommonMultiChoiceDialog.with(activity)
                .setTitle(titleResId)
                .setTitleItems(titleItems)
                .setButtonText(buttonTextResId, listener)
                .showDialog();
    }

    public static Builder with(FragmentActivity activity) {
        return new CommonMultiChoiceDialog.Builder(activity);
    }

    static public class Builder<T> {

        FragmentActivity activity;
        CommonMultiChoiceDialog multiChoiceDialog;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
            this.multiChoiceDialog = new CommonMultiChoiceDialog();
        }

        public Builder setTitle(@StringRes int titleResId) {
            return setTitle(activity.getString(titleResId));
        }

        public Builder setTitle(String title) {
            multiChoiceDialog.title = title;
            return this;
        }

        public Builder setTitleItems(List<T> titleItems) {
            multiChoiceDialog.titleItems = titleItems;
            return this;
        }

        public Builder setButtonText(int buttonTextResId, OnItemClickListener<T> listener) {
            return setButtonText(activity.getString(buttonTextResId), listener);
        }

        public Builder setButtonText(String buttonText, OnItemClickListener<T> listener) {
            multiChoiceDialog.buttonText = buttonText;
            multiChoiceDialog.itemClickListener = listener;
            return this;
        }

        public CommonMultiChoiceDialog showDialog() {
            multiChoiceDialog.show(activity.getSupportFragmentManager(), "MultiChoiceDialog");
            return multiChoiceDialog;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_multi_choice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubTitle);
        ivClose = findViewById(R.id.ivClose);
        lvItem = findViewById(R.id.lvItem);
        btnOk = findViewById(R.id.btnOk);

        tvTitle.setText(title);
        btnOk.setText(buttonText);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        lvItem.setAdapter(multiChoiceAdapter = new MultiChoiceAdapter(getActivity()));
        multiChoiceAdapter.setData(titleItems);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    List<String> checkItems = multiChoiceAdapter.getCheckItems();
                    itemClickListener.OnItemClick(CommonMultiChoiceDialog.this, checkItems);
                }
            }
        });
    }


    class MultiChoiceAdapter<T> extends BaseAdapter {

        Context context;
        List<T> objects;
        List<Boolean> checkItems = new ArrayList<>();

        public MultiChoiceAdapter(@NonNull Context context) {
            this(context, new ArrayList<T>());
        }

        public MultiChoiceAdapter(@NonNull Context context, @NonNull T[] objects) {
            this(context, Arrays.asList(objects));
        }

        public MultiChoiceAdapter(@NonNull Context context, @NonNull List<T> objects) {
            this.context = context;
            this.objects = objects;
            setCheckStatus(objects);
        }

        private void setCheckStatus(@NonNull List<T> objects) {
            checkItems.clear();
            for (T obj : objects) {
                checkItems.add(true);
            }
        }

        public List<T> getCheckItems() {
            List<T> result = new ArrayList<>();
            for (int i = 0; i < checkItems.size(); i++) {
                if (checkItems.get(i)) {
                    result.add(objects.get(i));
                }
            }

            return result;
        }

        public void setData(List<T> data) {
            objects.clear();
            objects.addAll(data);
            setCheckStatus(objects);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public T getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.dialog_multi_choice_item, parent, false);
            }

            final CheckBox cbItem = (CheckBox) convertView.findViewById(R.id.cbItem);
            if (itemClickListener != null)
                cbItem.setText(itemClickListener.onItemGetName(getItem(position)));
            else
                cbItem.setText(getItem(position).toString());
            cbItem.setChecked(checkItems.get(position));
            cbItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkItems.set(position, cbItem.isChecked());
                }
            });

            return convertView;
        }
    }

}

