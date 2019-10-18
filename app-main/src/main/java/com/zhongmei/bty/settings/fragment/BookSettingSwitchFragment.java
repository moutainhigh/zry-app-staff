package com.zhongmei.bty.settings.fragment;

import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.settings_book_switch)
public class BookSettingSwitchFragment extends BasicFragment {
    private static final String TAG = BookSettingSwitchFragment.class.getSimpleName();


        public static final String BOOK_IN_SWITCH = "bookingInSwtich";


    @ViewById(R.id.book_come_in_open_switch)
    protected ToggleButton mBookInSwitch;

    @AfterViews
    void init() {

        initComInSwtich();

    }

    private void initComInSwtich() {
        mBookInSwitch.setChecked(SpHelper.getDefault().getBoolean(BOOK_IN_SWITCH, true));
        mBookInSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getDefault().putBoolean(BOOK_IN_SWITCH, isChecked);

            }
        });
    }


}
