package com.zhongmei.bty.settings.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.settings.view.DishTypeSelectDialog;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date： 17/2/28
 * @Description:
 * @Version: 1.0
 */
public class DinnerMenuSettingFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private DishManager mDishManager;

    private RadioGroup mRgMenuSetting;
    private RadioButton mRbTwolevel;
    private RadioButton mRbTreelevel;
    private TextView mSelectCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDishManager = new DishManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dinner_menu_setting_fragment, container, false);
        mRgMenuSetting = (RadioGroup) view.findViewById(R.id.rg_menu_setting);
        mRbTwolevel = (RadioButton) view.findViewById(R.id.rb_twolevel);
        mRbTreelevel = (RadioButton) view.findViewById(R.id.rb_treelevel);
        mSelectCount = (TextView) view.findViewById(R.id.tv_selected_count);
        view.findViewById(R.id.ll_dishtype_set).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int level = SharedPreferenceUtil.getSpUtil().getInt("dinner_meun_level", 2);
        if (level == 2) {
            mRbTwolevel.setChecked(true);
        } else {
            mRbTreelevel.setChecked(true);
        }
        mRgMenuSetting.setOnCheckedChangeListener(this);
        setSelectCountText();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == mRbTwolevel.getId()) {
            mRbTwolevel.setChecked(true);
            SharedPreferenceUtil.getSpUtil().putInt("dinner_meun_level", 2);
        } else {
            mRbTreelevel.setChecked(true);
            SharedPreferenceUtil.getSpUtil().putInt("dinner_meun_level", 3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_dishtype_set:
                DishTypeSelectDialog.show(getActivity(), mDishManager.loadData(), new OnCloseListener() {
                    @Override
                    public void onClose(boolean isEnsure, Object obj) {
                        setSelectCountText();
                    }
                });
                break;
        }
    }


    private void setSelectCountText() {
        int selected = 0;
        Set unselectUUids = new HashSet();
        List<DishBrandType> dishBrandTypes = mDishManager.loadData().dishTypeList;
        for (DishBrandType type : dishBrandTypes) {
            if (type.isShow() == Bool.YES.value()) {
                selected++;
            } else
                unselectUUids.add(type.getUuid());
        }

        mSelectCount.setText(selected + "/" + dishBrandTypes.size());
        SharedPreferenceUtil.getSpUtil().putStringSet("unselect_dish_types", unselectUUids);
    }
}
