package com.zhongmei.bty.settings.fragment;

import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.event.SpeechCallStopEvent;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.CommercialQueueConfigFile;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.data.operates.BaiduSyntheticSpeechDal;
import com.zhongmei.bty.data.operates.CommerQueueConfigFileDal;
import com.zhongmei.bty.queue.util.DownLoaderBroadVoiceTask;
import com.zhongmei.bty.settings.adapter.SexorSpeedAdapter;
import com.zhongmei.bty.settings.bean.SexOrSpeedVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 语音合成与下载设置
 */
@EFragment(R.layout.speech_broadcast_setting)
public class SpeechBroadcastSettingFragment extends BasicFragment {
    private static final String TAG = SpeechBroadcastSettingFragment.class.getSimpleName();

    /**
     * 语音包下载
     */
    @ViewById(R.id.voice_packge_download_tg)
    protected TextView voice_packge_download_bt;

    /**
     * 语音包合成
     */
    @ViewById(R.id.voice_packge_systhesis_tg)
    protected ToggleButton voicePackgeSysthesisTg;

    /**
     * 合成界面
     */
    @ViewById(R.id.voice_synthesis_layout)
    protected LinearLayout voiceSynthesidLayout;

    /**
     * 性别
     */
    @ViewById(R.id.voice_sex)
    protected Spinner voiceSex;

    /**
     * 速度
     */
    @ViewById(R.id.voice_speed)
    protected Spinner voiceSpeed;

    /**
     * 内容
     */
    @ViewById(R.id.voice_content)
    protected EditText voiceContent;

    /**
     * 内容
     */
    @ViewById(R.id.voice_name)
    protected EditText voiceName;

    /**
     * 保存
     */
    @ViewById(R.id.voice_save)
    protected TextView voiceSave;

    /**
     * 试听
     */
    @ViewById(R.id.voice_audition)
    protected TextView voiceAudition;

    public static final String BROADCAST_VOICE_SYNTHESIS = "queue_switch_broadcast_voice_synthesis";

    private Sex sex = Sex.MALE;

    private int speed = 5;

    private List<SexOrSpeedVo> sexList;

    private List<SexOrSpeedVo> speedList;

    private boolean isPlay = false;

    @AfterViews
    void init() {
        registerEventBus();
        String[] sexs = getActivity().getResources().getStringArray(R.array.voice_sex);
        String[] speeds = getActivity().getResources().getStringArray(R.array.voice_speed);
        sexList = convertList(sexs, 1);
        SexorSpeedAdapter sexAdapter = new SexorSpeedAdapter(this.getActivity(), sexList, getString(R.string.sex));
        speedList = convertList(speeds, 2);
        SexorSpeedAdapter speedAdapter = new SexorSpeedAdapter(this.getActivity(), speedList, getString(R.string.setting_queue_voice_speed));
        voiceSex.setAdapter(sexAdapter);
        voiceSpeed.setAdapter(speedAdapter);
        refershView();
        refershInfo();
        // 增加监听
        voicePackgeSysthesisTg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getDefault().putBoolean(BROADCAST_VOICE_SYNTHESIS, isChecked);
                if (isChecked) {
                    voiceSynthesidLayout.setVisibility(View.VISIBLE);
                    refershInfo();
                } else {
                    voiceSynthesidLayout.setVisibility(View.GONE);
                }
            }

        });
        voiceSex.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sexList.get(position).getValue() == 1) {
                    sex = Sex.MALE;
                } else {
                    sex = Sex.FEMALE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        voiceSpeed.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speed = speedList.get(position).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        voiceAudition.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                doAudition();

            }

        });
    }

    @Override
    public void onDestroyView() {
        unregisterEventBus();
        super.onDestroyView();
    }

    /**
     * @param array
     * @param type  1性别 2速度
     * @return
     */
    private List<SexOrSpeedVo> convertList(String[] array, int type) {
        List<SexOrSpeedVo> list = new ArrayList<SexOrSpeedVo>();
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                SexOrSpeedVo vo = new SexOrSpeedVo();
                vo.setSelectedState(false);
                vo.setName(array[i]);
                if (type == 1) {// 性别
                    if (i == 0) {
                        vo.setValue(1);
                    } else {
                        vo.setValue(0);
                    }
                } else {// 速度
                    switch (i) {
                        case 0:
                            vo.setValue(1);
                            break;
                        case 1:
                            vo.setValue(3);
                            break;
                        case 2:
                            vo.setValue(5);
                            break;
                        case 3:
                            vo.setValue(7);
                            break;
                        case 4:
                            vo.setValue(9);
                            break;

                        default:
                            vo.setValue(3);
                            break;
                    }
                }
                list.add(vo);
            }

        }
        return list;
    }

    @Click({R.id.voice_save, R.id.voice_packge_download_tg})
    void initListener(View v) {
        switch (v.getId()) {
            case R.id.voice_save:
                doSave();
                break;

            case R.id.voice_packge_download_tg:
                doDownLoad();
                break;
            default:
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refershView() {
        boolean isShow = SpHelper.getDefault().getBoolean(BROADCAST_VOICE_SYNTHESIS, false);
        if (isShow) {
            voicePackgeSysthesisTg.setChecked(true);
            voiceSynthesidLayout.setVisibility(View.VISIBLE);
        } else {
            voicePackgeSysthesisTg.setChecked(false);
            voiceSynthesidLayout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新合成信息
     */
    private void refershInfo() {
        voiceName.setText(getTitle());
        voiceContent.setText("");
        resetSpinner();
    }

    /**
     * 标题
     *
     * @return
     */
    private String getTitle() {
        int count = 1;
        BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
        try {
            List<BaiduSyntheticSpeech> list = dal.listSyntherticSpeech();

            if (list != null && list.size() > 0) {
                count += list.size();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getResources().getString(R.string.broadcast_file) + count;
    }

    /**
     * 选中性别和速度
     */

    private void resetSpinner() {
        for (int i = 0; i < sexList.size(); i++) {
            if (sexList.get(i).getValue() == sex.value()) {
                voiceSex.setSelection(i);
            }
        }
        for (int i = 0; i < speedList.size(); i++) {
            if (speedList.get(i).getValue() == speed) {
                voiceSpeed.setSelection(i);
            }
        }
    }

    /**
     * 试听
     */
    private void doAudition() {
        if (isPlay) {
            QueuePlayServiceManager.stopAudition();
            isPlay = false;
            switchImage();
            return;
        }
        String content = voiceContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showShortToast(R.string.please_input_content);
            return;
        }
        isPlay = true;
        switchImage();
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech.setType(2);
        speech.setContent(content);
        speech.setSex(sex);
        speech.setSpeed(speed);
        QueuePlayServiceManager.playAudition(speech);
    }

    /**
     * 试听停止
     *
     * @param selectEvent
     */
    public void onEventMainThread(SpeechCallStopEvent selectEvent) {
        isPlay = false;
        switchImage();
    }

    /**
     * 保存数据
     */
    private void doSave() {
        String content = voiceContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showShortToast(R.string.input_content_empty_toast);
            return;
        }
        BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
        Long time = new Date().getTime();
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        try {

            String title = voiceName.getText().toString();
            if (TextUtils.isEmpty(title)) {
                ToastUtil.showShortToast(R.string.input_title_toast);
                return;
            }
            speech.setName(title);
            speech.setType(2);
            speech.setClientCreateTime(time);

            if (speech.getUuid() == null) {
                speech.setUuid(SystemUtils.genOnlyIdentifier());
                speech.setStatusFlag(StatusFlag.VALID);
            }
            speech.setContent(content);
            speech.setSex(sex);
            speech.setSpeed(speed);
            speech.setClientUpdateTime(time);
            dal.saveQueue(speech);
            ToastUtil.showShortToast(R.string.save_success);
            refershInfo();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    /**
     * 切换图片
     */
    @UiThread
    public void switchImage() {
        if (isPlay) {
            voiceAudition.setSelected(true);
        } else {
            voiceAudition.setSelected(false);
        }
    }

    private DownLoaderBroadVoiceTask task;

    private boolean isDownLoadIng = false;

    /**
     * 下载文件
     */
    private void doDownLoad() {
        CommerQueueConfigFileDal dal = OperatesFactory.create(CommerQueueConfigFileDal.class);
        try {
            List<CommercialQueueConfigFile> fileList = dal.listSysFileBroadCast();
            if (fileList != null && fileList.size() > 0) {

                if (isDownLoadIng) {
                    task.showDialog();
                    ToastUtil.showShortToast(R.string.downloading_and_waiting);
                    return;
                }
                isDownLoadIng = true;
                task = (DownLoaderBroadVoiceTask) AsyncTaskCompat.executeParallel(new DownLoaderBroadVoiceTask(fileList, this.getActivity()) {
                    @Override
                    protected void onPostExecute(Long result) {
                        super.onPostExecute(result);
                        if (voice_packge_download_bt != null) {
                            isDownLoadIng = false;
                        }
                        ToastUtil.showShortToast(R.string.download_complete);
                    }

                    @Override
                    protected void onCancelDownload() {
                    }

                });
            } else {
                isDownLoadIng = false;
                ToastUtil.showShortToast(R.string.no_resource_can_download);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

}
