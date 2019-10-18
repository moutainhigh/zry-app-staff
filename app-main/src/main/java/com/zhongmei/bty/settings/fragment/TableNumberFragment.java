package com.zhongmei.bty.settings.fragment;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.bty.commonmodule.database.enums.SetDeliveryType;
import com.zhongmei.bty.commonmodule.database.enums.SetType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.message.TableNumberSettingResp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@EFragment(R.layout.settings_table_number)
public class TableNumberFragment extends BasicFragment {

    private static final String TAG = TableNumberFragment.class.getSimpleName();

    @ViewById(R.id.rl_table)
    RelativeLayout rlTable;

    @ViewById(R.id.rl_number)
    RelativeLayout rlNumber;

    @ViewById(R.id.rl_here)
    RelativeLayout rlHere;

    @ViewById(R.id.rl_carray)
    RelativeLayout rlCarray;

    @ViewById(R.id.cb_table)
    CheckBox cbTable;

    @ViewById(R.id.cb_number)
    CheckBox cbNumber;

    @ViewById(R.id.cb_here)
    CheckBox cbHere;

    @ViewById(R.id.cb_carray)
    CheckBox cbCarray;

    @ViewById(R.id.ll_here_or_carry)
    LinearLayout llHereOrCarry;

    @ViewById(R.id.tv_number_tip)
    TextView tvNumberTip;

    private List<TableNumberSetting> mTableNumberSettingList;

    private boolean haveLimitServiceTime = false;


    private class LoadLimitServiceTimeAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return haveLimitTime();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean != null && isAdded()) {
                haveLimitServiceTime = aBoolean;
                checkNumberTip();
            }
        }
    }


    private boolean haveLimitTime() {
        return !TextUtils.isEmpty(ServerSettingCache.getInstance().getLimitServiceTime());
    }

    @AfterViews
    protected void init() {
        mTableNumberSettingList = getTableNumberSetting();
        if (Utils.isNotEmpty(mTableNumberSettingList)) {
            SetType setType = getSetType();
            if (setType != null) {
                if (setType == SetType.NUMPLATE) {
                    cbNumber.setChecked(true);
                } else if (setType == SetType.TABLE) {
                    cbTable.setChecked(true);
                }
            }

            cbHere.setChecked(isSelected(SetDeliveryType.HERE));
            cbCarray.setChecked(isSelected(SetDeliveryType.CARRY));

            checkSubSelectShow();
            new LoadLimitServiceTimeAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        cbTable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbNumber.setChecked(false);
                }
            }
        });

        cbNumber.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbTable.setChecked(false);
                }
            }
        });
    }

    private SetType getSetType() {
        List<TableNumberSetting> validTableNumberSetting = getValidTableNumberSetting();
        if (Utils.isNotEmpty(validTableNumberSetting)) {
            return validTableNumberSetting.get(0).getType();
        }

        return null;
    }

    private boolean isSelected(SetDeliveryType deliveryType) {
        List<TableNumberSetting> validTableNumberSetting = getValidTableNumberSetting();
        if (Utils.isNotEmpty(validTableNumberSetting)) {
            for (TableNumberSetting tableNumberSetting : validTableNumberSetting) {
                if (tableNumberSetting.getDeliveryType() == deliveryType) {
                    return true;
                }
            }
        }

        return false;
    }

    private void setContrary(CheckBox cb) {
        boolean contrary = !cb.isChecked();
        cb.setChecked(contrary);
    }

    @Click({R.id.rl_table, R.id.rl_number, R.id.rl_here, R.id.rl_carray, R.id.btn_ok})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_table:
                                if (cbTable.isChecked()) {
                    reset();
                } else {
                    setContrary(cbTable);
                    if (cbNumber.isChecked()) {
                        cbNumber.setChecked(false);
                    }
                }
                break;
            case R.id.rl_number:
                                if (cbNumber.isChecked()) {
                    reset();
                } else {
                    setContrary(cbNumber);
                    if (cbTable.isChecked()) {
                        cbTable.setChecked(false);
                    }
                }
                break;
            case R.id.rl_here:
                                if (cbTable.isChecked() || cbNumber.isChecked()) {
                    setContrary(cbHere);
                }
                break;
            case R.id.rl_carray:
                                if (cbTable.isChecked() || cbNumber.isChecked()) {
                    setContrary(cbCarray);
                }
                break;
            case R.id.btn_ok:
                if (!ClickManager.getInstance().isClicked()) {
                    saveTableNumberSetting();
                }
                break;
        }
        checkSubSelectShow();
        checkNumberTip();
    }

    private void saveTableNumberSetting() {
        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);

        List<TableNumberSetting> tableNumberSettings = new ArrayList<TableNumberSetting>();
        if (cbTable.isChecked() || cbNumber.isChecked()) {
            SetType setType = cbTable.isChecked() ? SetType.TABLE : SetType.NUMPLATE;

            if (Utils.isNotEmpty(mTableNumberSettingList)) {
                for (TableNumberSetting tableNumberSetting : mTableNumberSettingList) {
                    if (tableNumberSetting.getType() != setType
                            && tableNumberSetting.getStatusFlag() == StatusFlag.VALID) {
                        tableNumberSetting.setStatusFlag(StatusFlag.INVALID);
                        tableNumberSetting.validateUpdate();
                        tableNumberSettings.add(tableNumberSetting);
                    }
                }
            }

            if (!cbHere.isChecked() && !cbCarray.isChecked()) {
                TableNumberSetting noneSetting = getRecord(setType, SetDeliveryType.NONE);
                if (noneSetting == null) {
                    noneSetting = createTableNumberSettingItem(setType, SetDeliveryType.NONE);
                    tableNumberSettings.add(noneSetting);
                } else if (noneSetting.getStatusFlag() == StatusFlag.INVALID) {
                    noneSetting.setStatusFlag(StatusFlag.VALID);
                    noneSetting.validateUpdate();
                    tableNumberSettings.add(noneSetting);
                }

                TableNumberSetting hereSetting = getRecord(setType, SetDeliveryType.HERE);
                if (hereSetting != null && hereSetting.getStatusFlag() == StatusFlag.VALID) {
                    hereSetting.setStatusFlag(StatusFlag.INVALID);
                    hereSetting.validateUpdate();
                    tableNumberSettings.add(hereSetting);
                }

                TableNumberSetting carrySetting = getRecord(setType, SetDeliveryType.CARRY);
                if (carrySetting != null && carrySetting.getStatusFlag() == StatusFlag.VALID) {
                    carrySetting.setStatusFlag(StatusFlag.INVALID);
                    carrySetting.validateUpdate();
                    tableNumberSettings.add(carrySetting);
                }
            } else {
                if (cbHere.isChecked()) {
                    TableNumberSetting hereSetting = getRecord(setType, SetDeliveryType.HERE);
                    if (hereSetting == null) {
                        hereSetting = createTableNumberSettingItem(setType, SetDeliveryType.HERE);
                        tableNumberSettings.add(hereSetting);
                    } else if (hereSetting.getStatusFlag() == StatusFlag.INVALID) {
                        hereSetting.setStatusFlag(StatusFlag.VALID);
                        hereSetting.validateUpdate();
                        tableNumberSettings.add(hereSetting);
                    }
                } else {
                    TableNumberSetting hereSetting = getRecord(setType, SetDeliveryType.HERE);
                    if (hereSetting != null && hereSetting.getStatusFlag() == StatusFlag.VALID) {
                        hereSetting.setStatusFlag(StatusFlag.INVALID);
                        hereSetting.validateUpdate();
                        tableNumberSettings.add(hereSetting);
                    }
                }

                if (cbCarray.isChecked()) {
                    TableNumberSetting carraySetting = getRecord(setType, SetDeliveryType.CARRY);
                    if (carraySetting == null) {
                        carraySetting = createTableNumberSettingItem(setType, SetDeliveryType.CARRY);
                        tableNumberSettings.add(carraySetting);
                    } else if (carraySetting.getStatusFlag() == StatusFlag.INVALID) {
                        carraySetting.setStatusFlag(StatusFlag.VALID);
                        carraySetting.validateUpdate();
                        tableNumberSettings.add(carraySetting);
                    }
                } else {
                    TableNumberSetting carraySetting = getRecord(setType, SetDeliveryType.CARRY);
                    if (carraySetting != null && carraySetting.getStatusFlag() == StatusFlag.VALID) {
                        carraySetting.setStatusFlag(StatusFlag.INVALID);
                        carraySetting.validateUpdate();
                        tableNumberSettings.add(carraySetting);
                    }
                }

                TableNumberSetting noneSetting = getRecord(setType, SetDeliveryType.NONE);
                if (noneSetting != null && noneSetting.getStatusFlag() == StatusFlag.VALID) {
                    noneSetting.setStatusFlag(StatusFlag.INVALID);
                    noneSetting.validateUpdate();
                    tableNumberSettings.add(noneSetting);
                }
            }
        } else {
            if (Utils.isNotEmpty(mTableNumberSettingList)) {
                for (TableNumberSetting tableNumberSetting : mTableNumberSettingList) {
                    if (tableNumberSetting.getStatusFlag() == StatusFlag.VALID) {
                        tableNumberSetting.setStatusFlag(StatusFlag.INVALID);
                        tableNumberSetting.validateUpdate();
                        tableNumberSettings.add(tableNumberSetting);
                    }
                }
            }
        }

        ResponseListener<TableNumberSettingResp> listener = new ResponseListener<TableNumberSettingResp>() {

            @Override
            public void onResponse(ResponseObject<TableNumberSettingResp> response) {
                ToastUtil.showShortToast(response.getMessage());
                if (ResponseObject.isOk(response)) {
                    mTableNumberSettingList = getTableNumberSetting();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }

        };

        if (Utils.isNotEmpty(tableNumberSettings)) {
            systemSettingDal.setTableNumberSetting(tableNumberSettings,
                    LoadingResponseListener.ensure(listener, getFragmentManager()));
        }
    }

    private TableNumberSetting createTableNumberSettingItem(SetType setType, SetDeliveryType setDeliveryType) {
        TableNumberSetting settingItem = new TableNumberSetting();
        settingItem.setId(0L);
        settingItem.setType(setType);
        settingItem.setDeliveryType(setDeliveryType);
        settingItem.setStatusFlag(StatusFlag.VALID);
        settingItem.setClientCreateTime(System.currentTimeMillis());
        settingItem.setClientUpdateTime(settingItem.getClientCreateTime());
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            settingItem.setCreatorId(user.getId());
            settingItem.setCreatorName(user.getName());
        }
        settingItem.setUuid(SystemUtils.genOnlyIdentifier());
        return settingItem;
    }

    private List<TableNumberSetting> getTableNumberSetting() {
        try {
            SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
            return systemSettingDal.listTableNumberSetting(false);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

        return Collections.emptyList();
    }

    private List<TableNumberSetting> getValidTableNumberSetting() {
        if (Utils.isNotEmpty(mTableNumberSettingList)) {
            List<TableNumberSetting> validTableNumberSetting = new ArrayList<TableNumberSetting>();
            for (TableNumberSetting tableNumberSetting : mTableNumberSettingList) {
                if (tableNumberSetting.getStatusFlag() == StatusFlag.VALID) {
                    validTableNumberSetting.add(tableNumberSetting);
                }
            }

            return validTableNumberSetting;
        }

        return Collections.emptyList();
    }

    private TableNumberSetting getRecord(SetType setType, SetDeliveryType setDeliveryType) {
        if (Utils.isNotEmpty(mTableNumberSettingList)) {
            for (TableNumberSetting tableNumberSetting : mTableNumberSettingList) {
                if (tableNumberSetting.getType() == setType
                        && tableNumberSetting.getDeliveryType() == setDeliveryType) {
                    return tableNumberSetting;
                }
            }
        }

        return null;
    }

    private void checkSubSelectShow() {
        if (cbNumber.isChecked() || cbTable.isChecked()) {
            llHereOrCarry.setVisibility(View.VISIBLE);
        } else {
            llHereOrCarry.setVisibility(View.GONE);
        }
    }


    private void checkNumberTip() {
        if (cbNumber.isChecked() && haveLimitServiceTime) {
            tvNumberTip.setVisibility(View.VISIBLE);
        } else {
            tvNumberTip.setVisibility(View.GONE);
        }
    }

    private void reset() {
        cbNumber.setChecked(false);
        cbTable.setChecked(false);
        cbCarray.setChecked(false);
        cbHere.setChecked(false);
    }
}
