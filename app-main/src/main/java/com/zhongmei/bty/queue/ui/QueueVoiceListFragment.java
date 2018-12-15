package com.zhongmei.bty.queue.ui;

import com.zhongmei.yunfu.MainApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.bty.data.operates.BaiduSyntheticSpeechDal;
import com.zhongmei.bty.data.operates.CommerQueueConfigFileDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.queue.adapter.QueueVoiceListAdapter;
import com.zhongmei.bty.queue.event.QueueVoiceSelectEvent;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;
import com.zhongmei.bty.queue.util.QueueFileUtil;
import com.zhongmei.bty.basemodule.commonbusiness.bean.QueueVoiceVo;

/**
 * 语音列表
 */
@EFragment(R.layout.queue_voice_list_frangment)
public class QueueVoiceListFragment extends QueueBaseFragment {

    private final static String TAG = QueueVoiceListFragment.class.getSimpleName();

    @ViewById(R.id.queue_voice_listview)
    protected ListView queueVoiceListView;

    @ViewById(R.id.queue_voice_play)
    protected ImageView voicePlay;

    @ViewById(R.id.queue_voice_play_pre)
    protected ImageView voicePlayPre;

    @ViewById(R.id.queue_voice_play_next)
    protected ImageView voicePlayNext;

    @ViewById(R.id.queue_voice_list_bg)
    protected ImageView voiceListView;

    @ViewById(R.id.queue_voice_rote_bg)
    protected ImageView voiceRoteView;

    private List<QueueVoiceVo> voiceList;

    private QueueVoiceListAdapter queueVoiceListAdapter;

    private Boolean isPlay = false;

    private Animation animation;

    private AnimationDrawable animDrawable;

    @AfterViews
    void init() {
        registerEventBus();
        voiceList = new ArrayList<QueueVoiceVo>();
        BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
        try {
            String vlicePath = QueueFileUtil.getPath() + "broadcast";
            readfile(vlicePath);// 读取播音文件
            List<BaiduSyntheticSpeech> speechList = dal.listSyntherticSpeech();
            if (speechList != null && speechList.size() > 0) {
                for (BaiduSyntheticSpeech speech : speechList) {
                    QueueVoiceVo vo = new QueueVoiceVo();
                    vo.setType(1);
                    vo.setSelected(false);
                    vo.setSpeech(speech);
                    vo.setName(speech.getName());
                    voiceList.add(vo);
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        queueVoiceListAdapter = new QueueVoiceListAdapter(this.getActivity(), voiceList);
        queueVoiceListView.setAdapter(queueVoiceListAdapter);
        animation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.queue_voice_rote_animation);
        isPlay = QueuePlayServiceManager.isPlay();
        if (isPlay != null && isPlay) {//全局播放需要更改播放状态
            switchImage();
            if (QueuePlayServiceManager.getSelectIndex() != -1) {
                doSelect(QueuePlayServiceManager.getSelectIndex(), false);
            }
        }
    }


    /**
     * 读取某个文件夹下的所有文件
     */
    private boolean readfile(String filepath) {
        File file = new File(filepath);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            CommerQueueConfigFileDal dal = OperatesFactory.create(CommerQueueConfigFileDal.class);
            for (String aFilelist : filelist) {
                QueueVoiceVo vo = new QueueVoiceVo();
                String fileName = aFilelist;
                File readfile = new File(filepath, fileName);
                try {
                    if (dal.validFile(fileName)) {
                        vo.setType(2);
                        vo.setSelected(false);
                        if (fileName.lastIndexOf(".") != -1) {
                            fileName = fileName.substring(0, fileName.lastIndexOf("."));
                        }
                        vo.setName(fileName);
                        vo.setPath(readfile.getPath());
                        voiceList.add(vo);
                    } else {
                        readfile.delete();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }
        return true;
    }

    @Click({R.id.queue_voice_play, R.id.queue_voice_play_pre, R.id.queue_voice_play_next})
    public void clic(View v) {
        switch (v.getId()) {
            case R.id.queue_voice_play:
                if (isPlay) {
                    stop();
                } else {
                    doPlay();
                }
                break;
            case R.id.queue_voice_play_pre:
                doPreOrNext(true);
                break;
            case R.id.queue_voice_play_next:
                doPreOrNext(false);
                break;
            default:
                break;
        }
    }

    /**
     * 选中
     *
     * @param vo
     */
    @ItemClick(R.id.queue_voice_listview)
    public void queueVoiceListViewClick(final int position) {
        doSelect(position, true);
    }

    /**
     * 选中
     *
     * @param position
     * @param play     初始话不播放
     */
    private void doSelect(int position, boolean play) {
        int size = voiceList.size();
        for (int i = 0; i < size; i++) {
            if (i == position) {
                voiceList.get(i).setSelected(true);
            } else {
                voiceList.get(i).setSelected(false);
            }
        }
        queueVoiceListAdapter.notifyDataSetChanged();
        if (isPlay && play) {
            doPlay();
        }
    }

    /**
     * 长按删除
     *
     * @param vo
     */
    @ItemLongClick(R.id.queue_voice_listview)
    public void queueVoiceListViewLongClick(final int position) {
        new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(R.string.queue_voice_delete_info)
                .iconType(R.drawable.commonmodule_dialog_icon_warning)
                .negativeText(R.string.common_cancel)
                .negativeLisnter(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {// 取消
                    }
                })
                .positiveText(R.string.common_submit)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {// 确定
                        QueueVoiceVo vo = voiceList.get(position);
                        if (vo.getType() == 1) {// 合成
                            BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
                            try {
                                dal.delete(vo.getSpeech());
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        } else {// 下载
                            String path = vo.getPath();
                            File file = new File(path);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        voiceList.remove(position);
                        queueVoiceListAdapter.notifyDataSetChanged();
                        if (isPlay && position == selectIndex) {
                            stop();
                        }
                    }
                })
                .build()
                .show(this.getFragmentManager(), "booking");
    }

    /**
     * 选中项
     */
    private int selectIndex = -1;

    /**
     * 上一条或下一条
     *
     * @param isPre true上一条 false下一条
     */
    private void doPreOrNext(boolean isPre) {
        if (voiceList != null && voiceList.size() > 0) {
            resetSelectIndex();
            if (isPre) {
                if (selectIndex == 0) {
                    ToastUtil.showShortToast(getString(R.string.queue_voice_first));
                    return;
                } else if (selectIndex - 1 >= 0) {
                    selectIndex--;
                }

            } else {
                if (selectIndex == voiceList.size() - 1) {
                    ToastUtil.showShortToast(getString(R.string.queue_voice_last));
                    return;
                } else if (selectIndex < voiceList.size() - 1) {
                    selectIndex++;
                }
            }
            // 选中
            doSelect(selectIndex, true);
            // 播放
            if (isPlay) {
                doPlay();
            }
        }
    }

    /**
     * 重置选中项
     */
    private void resetSelectIndex() {
        int size = voiceList.size();
        for (int i = 0; i < size; i++) {
            if (voiceList.get(i).isSelected()) {
                selectIndex = i;
                break;
            }
        }
    }

    /**
     * 停止
     */
    private void stop() {
        QueuePlayServiceManager.stop();
        isPlay = false;
        switchImage();
    }

    /**
     * 播放
     */
    private void doPlay() {
        // 播放选中的
        resetSelectIndex();
        if (selectIndex == -1) {
            ToastUtil.showShortToast(getString(R.string.queue_file_selection));
            return;
        }
        QueueVoiceVo selectedVo = voiceList.get(selectIndex);
        if (selectedVo != null) {
            isPlay = true;
            QueuePlayServiceManager.play(selectedVo, selectIndex);
            switchImage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    /**
     * 切换正在播放图片
     */
    @UiThread
    public void switchImage() {
        if (isPlay) {
            voicePlay.setBackgroundResource(R.drawable.queue_voice_pause_selector);
            voiceRoteView.startAnimation(animation);
            voiceListView.setBackgroundResource(R.drawable.queue_voice_list_anim);
            animDrawable = (AnimationDrawable) voiceListView.getBackground();
            animDrawable.start();
        } else {
            voiceRoteView.clearAnimation();
            voicePlay.setBackgroundResource(R.drawable.queue_voice_play_selector);
            if (animDrawable != null && animDrawable.isRunning()) {
                animDrawable.stop();
            }
            voiceRoteView.setBackgroundResource(R.drawable.queue_voice_list_play_bg3);
        }

    }


    /**
     * 选中播放
     *
     * @param selectEvent
     */
    public void onEventMainThread(QueueVoiceSelectEvent selectEvent) {
        if (isPlay) {
            doPlay();
        }
    }
}
