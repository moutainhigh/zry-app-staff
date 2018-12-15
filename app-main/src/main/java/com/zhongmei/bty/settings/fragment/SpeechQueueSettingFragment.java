package com.zhongmei.bty.settings.fragment;

import android.os.Looper;
import android.support.v4.os.AsyncTaskCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.event.SpeechCallStopEvent;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.db.common.queue.QueueImage;
import com.zhongmei.bty.data.operates.CommerQueueConfigFileDal;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.util.DownLoaderBroadVoiceTask;
import com.zhongmei.bty.queue.util.DownLoaderCallVoiceTask;
import com.zhongmei.bty.queue.util.QueueFileUtil;
import com.zhongmei.bty.queue.util.ZipExtractorTask;
import com.zhongmei.bty.settings.adapter.SexorSpeedAdapter;
import com.zhongmei.bty.settings.bean.SexOrSpeedVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 语音合成与下载设置
 */
@EFragment(R.layout.speech_queue_setting)
public class SpeechQueueSettingFragment extends BasicFragment {
    private static final String TAG = SpeechQueueSettingFragment.class.getSimpleName();

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

    @ViewById(R.id.open_voice_tg)
    protected ToggleButton openVoiceTg;

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
    @ViewById(R.id.call_voice_content)
    protected EditText voiceContent;

    /**
     * 号数
     */
    @ViewById(R.id.call_voice_number)
    protected TextView voiceNumber;

    /**
     * 试听
     */
    @ViewById(R.id.call_voice_audition)
    protected TextView voiceAudition;

    /**
     * 保存
     */
    @ViewById(R.id.call_voice_save)
    protected TextView voiceSave;

    /**
     * 内容
     */
    @ViewById(R.id.in_voice_content)
    protected EditText inVoiceContent;

    /**
     * 内容
     */
    @ViewById(R.id.next_voice_content)
    protected EditText nextVoiceContent;

    /**
     * 号数
     */
    @ViewById(R.id.in_voice_number)
    protected TextView inVoiceNumber;

    /**
     * 号数
     */
    @ViewById(R.id.next_voice_number)
    protected TextView nextVoiceNumber;

    /**
     * 试听
     */
    @ViewById(R.id.in_voice_audition)
    protected TextView inVoiceAudition;

    /**
     * 保存
     */
    @ViewById(R.id.in_voice_save)
    protected TextView inVoiceSave;

    public static final String CALL_VOICE_SYNTHESIS = "queue_switch_call_voice_synthesis";

    public static final String CALL_OPEN_VOICE = "queue_call_open_voice";

    private Sex sex = Sex.MALE;

    private int speed = 5;

    private List<SexOrSpeedVo> sexList;

    private List<SexOrSpeedVo> speedList;

    private Map<Integer, BaiduSyntheticSpeech> speechMap;

    private boolean isPlayCall = false;

    private boolean isPlayIn = false;

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

        voiceSex.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sexList.get(position).getValue() == 1) {
                    sex = Sex.MALE;
                } else {
                    sex = Sex.FEMALE;
                }
                save();
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
                save();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }

    void save() {
        if (speechMap == null) return;
        List<BaiduSyntheticSpeech> speechList = new ArrayList<>(speechMap.values());
        if (Utils.isNotEmpty(speechList)) {
            for (BaiduSyntheticSpeech speech : speechList) {
                speech.setSex(sex);
                speech.setSpeed(speed);
                speech.setClientUpdateTime(new Date().getTime());
            }
            QueueOpManager.getInstance().saveBaiduSyntheticSpeechs(speechList);
        }
    }

    @CheckedChange({R.id.voice_packge_systhesis_tg, R.id.open_voice_tg})
    void onCheckChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.voice_packge_systhesis_tg:
                SpHelper.getDefault().putBoolean(CALL_VOICE_SYNTHESIS, isChecked);
                if (isChecked) {
                    voiceSynthesidLayout.setVisibility(View.VISIBLE);
                    refershInfo();
                } else {
                    voiceSynthesidLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.open_voice_tg:
                SpHelper.getDefault().putBoolean(CALL_OPEN_VOICE, isChecked);
                break;
            default:
                break;
        }

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

    @Click({R.id.voice_packge_download_tg, R.id.call_voice_save, R.id.call_voice_number, R.id.call_voice_audition, R.id.in_voice_save, R.id.in_voice_number, R.id.in_voice_audition, R.id.next_voice_number})
    void initListener(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        switch (v.getId()) {
            case R.id.voice_packge_download_tg:
                doDownLoad();
                break;
            case R.id.call_voice_number:
                doAddNumber(voiceContent);
                break;
            case R.id.call_voice_audition:
                doAuditionCall();
                break;
            case R.id.call_voice_save:
                doSave(R.id.call_voice_save);
                break;
            case R.id.in_voice_number:
                doAddNumber(inVoiceContent);
                break;
            case R.id.next_voice_number:
                doAddNumber(nextVoiceContent);
                break;
            case R.id.in_voice_audition:
                doAuditionIn();
                break;
            case R.id.in_voice_save:
                doSave(R.id.in_voice_save);
                break;
            default:
                break;
        }
    }

    /**
     * 增加号数
     */
    private void doAddNumber(EditText editText) {
        String format = getResources().getString(R.string.voice_number_toast);
        String content = editText.getText().toString().trim();
        editText.setSelection(content.length());
        if (content.contains(format)) {
            ToastUtil.showShortToast(getString(R.string.setting_queue_insert_number_limit));
            return;
        }
        int index = editText.getSelectionStart();
        Editable editable = editText.getText();
        editable.insert(index, format);
    }

    /**
     * 刷新界面
     */
    private void refershView() {
        boolean isShow = SpHelper.getDefault().getBoolean(CALL_VOICE_SYNTHESIS, false);
        if (isShow) {
            voicePackgeSysthesisTg.setChecked(true);
            voiceSynthesidLayout.setVisibility(View.VISIBLE);
        } else {
            voicePackgeSysthesisTg.setChecked(false);
            voiceSynthesidLayout.setVisibility(View.INVISIBLE);
        }
        boolean isOpen = SpHelper.getDefault().getBoolean(CALL_OPEN_VOICE, true);
        if (isOpen) {
            openVoiceTg.setChecked(true);
        } else {
            openVoiceTg.setChecked(false);
        }

    }

    /**
     * 刷新合成信息
     */
    private void refershInfo() {

        boolean isShow = SpHelper.getDefault().getBoolean(CALL_VOICE_SYNTHESIS, false);
        if (isShow) {
            if (speechMap != null && speechMap.size() == 3) {
                resetSpinner();
                resetQueueVoiceContent();
            } else {
                TaskContext.bindExecute(this, new SimpleAsyncTask<Object>() {
                    @Override
                    public Object doInBackground(Void... params) {
                        speechMap = QueueOpManager.getInstance().getQueueVoiceMap();
                        return null;
                    }

                    @Override
                    public void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        resetSpinner();
                        resetQueueVoiceContent();
                    }
                });
            }

        }


    }

    /**
     * 选中性别和速度
     */

    private void resetSpinner() {
        BaiduSyntheticSpeech speech = speechMap.get(0);
        sex = speech.getSex();
        speed = speech.getSpeed();
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

    private void resetQueueVoiceContent() {
        voiceContent.setText(speechMap.get(0).getContent());
        inVoiceContent.setText(speechMap.get(1).getContent());
        nextVoiceContent.setText(speechMap.get(2).getContent());
    }

    /**
     * 试听
     */
    private void doAuditionCall() {
        if (isPlayCall) {
            QueuePlayServiceManager.stopAudition();
            isPlayCall = false;
            switchImage();
            return;
        }
        String content = voiceContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showShortToast(R.string.please_input_content);
            return;
        }
        isPlayCall = true;
        switchImage();
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech.setType(1);
        speech.setContent(content);
        speech.setSex(sex);
        speech.setSpeed(speed);
        QueuePlayServiceManager.playAudition(speech);
    }

    /**
     * 试听
     */
    private void doAuditionIn() {
        if (isPlayIn) {
            QueuePlayServiceManager.stopAudition();
            isPlayIn = false;
            switchImage();
            return;
        }
        String content = inVoiceContent.getText().toString() + "," + nextVoiceContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showShortToast(R.string.please_input_content);
            return;
        }
        isPlayIn = true;
        switchImage();
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech.setType(1);
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
        isPlayCall = false;
        isPlayIn = false;
        switchImage();
    }

    /**
     * 保存数据
     */
    private void doSave(int btnId) {
        if (btnId == R.id.call_voice_save) {
            String content = voiceContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                ToastUtil.showShortToast(R.string.input_content_empty_toast);
                return;
            }
            String format = getActivity().getResources().getString(R.string.voice_number_toast);
            if (content.indexOf(format) == -1) {
                ToastUtil.showShortToast(R.string.content_format_error_toast);
                return;
            }
            BaiduSyntheticSpeech speech = speechMap.get(0);
            speech.setContent(content);
            speech.setSex(sex);
            speech.setSpeed(speed);
            speech.setClientUpdateTime(new Date().getTime());
            QueueOpManager.getInstance().saveBaiduSyntheticSpeechs(speech);
            ToastUtil.showShortToast(R.string.save_success);
        } else if (btnId == R.id.in_voice_save) {
            String content1 = inVoiceContent.getText().toString();
            String content2 = nextVoiceContent.getText().toString();
            if (TextUtils.isEmpty(content1) && TextUtils.isEmpty(content2)) {
                ToastUtil.showShortToast(R.string.input_content_empty_toast);
                return;
            }
            String format = getActivity().getResources().getString(R.string.voice_number_toast);
            if (!TextUtils.isEmpty(content1) && !content1.contains(format)) {
                ToastUtil.showShortToast(R.string.content_format_error_toast);
                return;
            }
            if (!TextUtils.isEmpty(content2) && !content2.contains(format)) {
                ToastUtil.showShortToast(R.string.content_format_error_toast);
                return;
            }
            BaiduSyntheticSpeech speech1 = speechMap.get(1);
            speech1.setContent(content1);
            speech1.setSex(sex);
            speech1.setSpeed(speed);
            speech1.setClientUpdateTime(new Date().getTime());
            BaiduSyntheticSpeech speech2 = speechMap.get(2);
            speech2.setContent(content2);
            speech2.setSex(sex);
            speech2.setSpeed(speed);
            speech2.setClientUpdateTime(new Date().getTime());
            QueueOpManager.getInstance().saveBaiduSyntheticSpeechs(speech1, speech2);
            ToastUtil.showShortToast(R.string.save_success);
        }
    }

    /**
     * 切换图片
     */
    @UiThread
    public void switchImage() {
        if (isPlayCall) {
            voiceAudition.setSelected(true);
        } else {
            voiceAudition.setSelected(false);
        }

        if (isPlayIn) {
            inVoiceAudition.setSelected(true);
        } else {
            inVoiceAudition.setSelected(false);
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
            QueueImage zip = dal.getQueueCallZip();
            if (zip != null && zip.getVoice() != null) {
                //看看是否正在下载
                if (isDownLoadIng || checkIsDownLoadIng()) {
                    ToastUtil.showShortToast(R.string.downloading_and_waiting);
                    return;
                }
                isDownLoadIng = true;
                DownLoaderCallVoiceTask task = (DownLoaderCallVoiceTask) AsyncTaskCompat.executeParallel(new DownLoaderCallVoiceTask(QueueFileUtil.convertUtf(zip.getVoice()), this.getActivity()) {
                    @Override
                    protected void onDownLoadFinished(File file) {
                        super.onDownLoadFinished(file);
                        // 删除原来的文件
                        File oldPath = new File(QueueFileUtil.getPath() + "call");
                        if (oldPath.exists()) {
                            delteFile(oldPath);
                        }
                        doZipExtractorWork(file);
                    }

                    @Override
                    protected void onDownLoadException() {
                        super.onDownLoadException();
                        ToastUtil.showShortToast(R.string.download_fail);
                        isDownLoadIng = false;
                    }
                });
            } else {
                isDownLoadIng = false;
                ToastUtil.showShortToast(R.string.no_resource_can_download);
            }
        } catch (Exception e) {
            ToastUtil.showShortToast(R.string.download_suit_not_correct);
            isDownLoadIng = false;
            Log.e(TAG, "", e);
        }
    }

    /**
     * 验证是否正在下载
     */
    private boolean checkIsDownLoadIng() {
        // 删除原来的文件
        File dbPath = new File(QueueFileUtil.getPath());
        if (dbPath.exists()) {
            File[] oldFiles = dbPath.listFiles();
            if (oldFiles != null && oldFiles.length > 0) {
                for (File oldFile : oldFiles) {
                    if (!oldFile.isDirectory()) {
                        try {
                            oldFile.delete();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            isDownLoadIng = true;
                        }

                    }
                }
            }
        }
        return isDownLoadIng;
    }

    /**
     * 解压
     */
    public void doZipExtractorWork(final File file) {
        AsyncTaskCompat.executeParallel(new ZipExtractorTask(file.getPath(), QueueFileUtil.getPath() + "/call", this.getActivity(), true) {
            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                // 删除下载的zip文件
                file.delete();
                // 判断有没有audio文件加
                File dest = new File(QueueFileUtil.getPath() + "call/audio");
                if (!dest.exists()) {
                    File src = new File(QueueFileUtil.getPath() + "call");
                    try {
                        copyFolder(src, dest);
                    } catch (IOException e) {
                        Log.e(TAG, "", e);
                    }
                }
                checkFile();
                isDownLoadIng = false;
            }

            @Override
            protected void onZipExtractorException() {
                super.onZipExtractorException();
                Looper.prepareMainLooper();
                ToastUtil.showShortToast(R.string.unzip_fail);
                Looper.loop();
            }
        });
    }

    /**
     * 删除文件
     *
     * @param file
     */
    private void delteFile(File file) {
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (File subFile : files) {
                delteFile(subFile);
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    /**
     * 复制一个目录到另外一个目录
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    private void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            String files[] = src.list();
            if (!dest.exists()) {
                dest.mkdir();
            }
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            src.delete();
            in.close();
            out.close();
        }
    }

    /**
     * 验证文件下载是否完整可用
     */
    private void checkFile() {
        StringBuffer sb = new StringBuffer();
        String path = QueueFileUtil.getPath() + "call/audio/";
        File file0 = new File(path, "audio_0.mp3");
        File file1 = new File(path, "audio_1.mp3");
        File file2 = new File(path, "audio_2.mp3");
        File file3 = new File(path, "audio_3.mp3");
        File file4 = new File(path, "audio_4.mp3");
        File file5 = new File(path, "audio_5.mp3");
        File file6 = new File(path, "audio_6.mp3");
        File file7 = new File(path, "audio_7.mp3");
        File file8 = new File(path, "audio_8.mp3");
        File file9 = new File(path, "audio_9.mp3");
        File fileA = new File(path, "audio_A.mp3");
        File fileB = new File(path, "audio_B.mp3");
        File fileC = new File(path, "audio_C.mp3");
        File fileD = new File(path, "audio_D.mp3");
        File fileE = new File(path, "audio_E.mp3");
        File fileF = new File(path, "audio_F.mp3");
        File fileN = new File(path, "audio_N.mp3");
        File filePrefix = new File(path, "queue_prefix.mp3");
        File fileSuffix = new File(path, "queue_suffix.mp3");

        if (!file0.exists()) {
            sb.append(file0.getName());
            sb.append(",");
        }
        if (!file1.exists()) {
            sb.append(file1.getName());
            sb.append(",");
        }
        if (!file2.exists()) {
            sb.append(file2.getName());
            sb.append(",");
        }
        if (!file3.exists()) {
            sb.append(file3.getName());
            sb.append(",");
        }
        if (!file4.exists()) {
            sb.append(file4.getName());
            sb.append(",");
        }
        if (!file5.exists()) {
            sb.append(file5.getName());
            sb.append(",");
        }
        if (!file6.exists()) {
            sb.append(file6.getName());
            sb.append(",");
        }
        if (!file7.exists()) {
            sb.append(file7.getName());
            sb.append(",");
        }
        if (!file8.exists()) {
            sb.append(file8.getName());
            sb.append(",");
        }
        if (!file9.exists()) {
            sb.append(file9.getName());
            sb.append(",");
        }
        if (!fileN.exists()) {
            sb.append(fileN.getName());
            sb.append(",");
        }
        if (!fileA.exists()) {
            sb.append(fileA.getName());
            sb.append(",");
        }
        if (!fileB.exists()) {
            sb.append(fileB.getName());
            sb.append(",");
        }
        if (!fileC.exists()) {
            sb.append(fileC.getName());
            sb.append(",");
        }
        if (!fileD.exists()) {
            sb.append(fileD.getName());
            sb.append(",");
        }
        if (!fileE.exists()) {
            sb.append(fileE.getName());
            sb.append(",");
        }
        if (!fileF.exists()) {
            sb.append(fileF.getName());
            sb.append(",");
        }
        if (!filePrefix.exists()) {
            sb.append(filePrefix.getName());
            sb.append(",");
        }
        if (!fileSuffix.exists()) {
            sb.append(fileSuffix.getName());
            sb.append(",");
        }
        if (sb != null && !"".equals(sb.toString().trim())) {
            String fileNmae = sb.substring(0, sb.length() - 1);
            ToastUtil.showLongToast(fileNmae + getResources().getString(R.string.file_not_exist_unable_call));
        }
    }

}
