package com.zhongmei.bty.settings.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.analysis.DataHandleListener;
import com.zhongmei.analysis.DataMobAgent;
import com.zhongmei.OSLog;
import com.zhongmei.yunfu.context.util.log.BaseLogAction;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.common.qiniuupload.QiniuUploadHandler;
import com.zhongmei.bty.common.util.UploadFile;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.message.content.TokenResp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EFragment(R.layout.settings_support_fragment)
public class SupportFragment extends BasicFragment {
    private static final String TAG = SupportFragment.class.getSimpleName();

    public static final int FROM_TYPE_RETAIL = 1;// 来源于零售

    @ViewById(R.id.settings_progress_extend)
    View mProgressParentView;

    @ViewById(R.id.settings_system_clear_btn)
    Button mClearButton;

    @ViewById(R.id.settings_log_submit)
    Button mSubmitButton;

    @ViewById(R.id.settings_log_extend)
    View mExtendView;

    @ViewById(R.id.settings_log_swtich)
    ToggleButton mLogSwitch;

    @ViewById(R.id.mLogButton)
    ToggleButton mPrintLogSwitch;

    @ViewById(R.id.btn_upload_print_log)
    Button mBtnUpdatePrintFile;

    @ViewById(R.id.snack_btn)
    Button mBtnSnackLog;

    @ViewById(R.id.settings_user_action_swtich)
    ToggleButton btnUserActionSwitch;
    @ViewById(R.id.settings_useraction_extend)
    ViewGroup settingsUseractionExtend;
    @ViewById(R.id.settings_useraction_submit)
    Button btnUseractionSubmit;

    @ViewById(R.id.tv_log_hint)
    TextView mTvLogHint;

    @ViewById(R.id.snack_log)
    TextView mTvLog;

    @ViewById(R.id.tv_log_detail)
    TextView mTvLogDetail;

    private static final int HIDE_PROGRESS = 0;

    ProgressTask mProgressTask;
    //保存需要上传的所有文件
    List<FileAndNewNameMapItem> listFiles = new ArrayList<>();
    private String[] uploadFileName = new String[]{
            "printer",
            "calm_printer",
            "snack"
    };
    //需要上传的文件路径
    private String[] uploadFilePaths = new String[]{
            getExternalStorageDirectory() + BaseLogAction.S_BRAND_NAME + "/logs/" + uploadFileName[0],
            getExternalStorageDirectory() + BaseLogAction.S_BRAND_NAME + "/logs/" + uploadFileName[1],
            getExternalStorageDirectory() + BaseLogAction.S_BRAND_NAME + "/logs/" + uploadFileName[2]
    };

    /*
    文件上传到服务器后的名字
    商户号 + 日期 + 业务类别 + mac地址 + 时间搓
    例如: erp/810005040_20170203_calm_printer_e076ad7671a_148123213123
     */
    private String newName = null;
    // mac地址加时间搓
    private String tempNewName = null;
    //是否所有文件都长传成功
    private Boolean isAllSuccefull = true;
    //没有上传成功的文件名字
    private StringBuilder failFiles = new StringBuilder();

    Context mContext;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_PROGRESS:
                    mProgressParentView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private int mFromType = -1;

    public void setFromType(int type) {
        mFromType = type;
    }

    @CheckedChange(R.id.settings_log_swtich)
    public void onCheckedChange(CompoundButton hello, boolean isChecked) {
        SharedPreferenceUtil.getSpUtil().putBoolean(Constant.SP_LOG_SWITCH, isChecked);
        if (isChecked) {
            mExtendView.setVisibility(View.VISIBLE);
        } else {
            mExtendView.setVisibility(View.GONE);
        }
    }

    @AfterViews
    public void initChecked() {
        boolean isChecked = SharedPreferenceUtil.getSpUtil().getBoolean(Constant.SP_LOG_SWITCH, false);
        mLogSwitch.setChecked(isChecked);
        mContext = getActivity();
        btnUserActionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserActionEvent.setSwitch(isChecked);
                settingsUseractionExtend.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        btnUserActionSwitch.setChecked(UserActionEvent.isSwitch());

        if (mFromType == FROM_TYPE_RETAIL) {
            mTvLogHint.setText(getText(R.string.retail_settings_log));
            mTvLog.setText(getText(R.string.retail_settings_log_upload));
            mTvLogDetail.setText(getText(R.string.retail_upload_remind_snack));
        }
    }

    @Click({R.id.settings_log_submit, R.id.settings_useraction_submit})
    public void onLogSubmit(View v) {
        switch (v.getId()) {
            case R.id.settings_log_submit:
                executeProgressTask(getString(R.string.settings_log_submitting),
                        getString(R.string.settings_log_submit_done),
                        true);

                break;
            case R.id.settings_useraction_submit:
                DataMobAgent.writeAllData(MainApplication.getInstance(), new DataHandleListener<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        MainApplication.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadUserEvent();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        MainApplication.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadUserEvent();
                            }
                        });
                    }
                });

                break;
        }
    }

    private void uploadUserEvent() {
        final File file = DataMobAgent.getDataFormat(MainApplication.getInstance(), DateUtil.format(System.currentTimeMillis(), "yyyy-MM-dd"));
        if (file == null || !file.exists()) {
            ToastUtil.showShortToast(R.string.log_file_not_exist_in_route);
            return;
        }

        //showLoadingProgressDialog();
        btnUseractionSubmit.setEnabled(false);
        btnUseractionSubmit.setText(MainApplication.getInstance().getText(R.string.update_running));
        QiniuUploadHandler.doOneKeyUpload(file, getUserActionFileName(file), new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                //dismissLoadingProgressDialog();
                btnUseractionSubmit.setEnabled(true);
                btnUseractionSubmit.setText(MainApplication.getInstance().getText(R.string.settings_log_submit));
                if (!responseInfo.isOK()) {
                    ToastUtil.showShortToast(R.string.update_fail);
                } else {
                    file.delete();
                    ToastUtil.showShortToast(R.string.update_success);
                }
            }
        }, null);
    }

    public String getUserActionFileName(File file) {
        String newName = ShopInfoCfg.getInstance().shopId + "_"
                + SystemUtils.getMacAddress().replace(":", "") + "_"
                + file.getName() + "_"
                + DateUtil.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
        return String.format("[%s]%s", getEnvironmentName(), newName);
    }

    private String getEnvironmentName() {
        String result = ShopInfoCfg.getInstance().getServerKey();
        if (result.contains("test")) {
            result = "testerp";
        } else if (result.contains("dev")) {
            result = "deverp";
        } else if (result.contains("gld")) {
            result = "glderp";
        } else {
            result = "erp";
        }
        return result;
    }

    private void initFileNewName() {
        // 构造tempNewName
        tempNewName = SystemUtils.getMacAddress().replace(":", "") + "_" + System.currentTimeMillis();
        newName = ShopInfoCfg.getInstance().shopId + "_";
    }

    @Click(R.id.snack_btn)
    public void onSnackLogBtnClick() {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        /*LogUploader.upload(new LogUploader.Callback() {
            @Override
            public void onStart() {
                ToastUtil.showShortToast("准备上传款餐日志");
            }

            @Override
            public void onUploading() {
                ToastUtil.showLongToast("快餐日志上传中");
            }

            @Override
            public void onSuccess() {
                ToastUtil.showShortToast("快餐日志上传成功");
            }

            @Override
            public void onError(String message) {
                ToastUtil.showShortToast("失败:" + message);
            }
        });*/
    }

    @Click(R.id.btn_upload_print_log)
    public void onUpdatePrintFile() {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        setUpdateBtnClick(false);
        QiniuUploadHandler.getUploadToken(new ResponseListener<TokenResp>() {
            @Override
            public void onResponse(ResponseObject<TokenResp> response) {
                if (ResponseObject.isOk(response)
                        && response.getContent() != null
                        && (!TextUtils.isEmpty(response.getContent().getData()))) {
                    QiniuUpdateAsyncTask task = new QiniuUpdateAsyncTask();
                    AsyncTaskCompat.executeParallel(task, response.getContent().getData());
                } else {
                    ToastUtil.showLongToast(R.string.upload_fail);
                    Log.e(TAG, "请求token失败：" + response.getMessage());
                    setUpdateBtnClick(true);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                Log.e(TAG, "请求token失败：" + error.getMessage());
                setUpdateBtnClick(true);
            }
        });
    }

    private void setUpdateBtnClick(Boolean isCanClick) {
        mBtnUpdatePrintFile.setClickable(isCanClick);
        mBtnUpdatePrintFile.setEnabled(isCanClick);
    }

    private void doQiniuUpload(String token) {
        prepareFiles();
        if (listFiles.size() == 0) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showLongToast(R.string.log_file_not_exist_in_route);
                    }
                });
            }
            return;
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showLongToast(R.string.upload_start);
                }
            });
        }
        qiniuUpload(token);
    }

    private class QiniuUpdateAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            doQiniuUpload(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            resetStatue();
        }
    }


    /**
     * 七牛上传文件
     */
    private void qiniuUpload(String token) {
        String dir = getEnvironmentDir();
        for (FileAndNewNameMapItem item : listFiles) {
            QiniuUploadHandler.doUpload(item.getFile(), dir + item.getNewFilePath(), token, new UpCompletionHandler() {
                @Override
                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                    if (!responseInfo.isOK()) {
                        isAllSuccefull = false;
                        if (s != null) {
                            failFiles.append(s.substring(s.length() - 12, s.length()))
                                    .append(MainApplication.getInstance().getString(R.string.upload_fail))
                                    .append("\n");
                        }
                    }
                }
            }, null);
        }
    }

    /**
     * 全部文件上传完成后处理界面显示
     */
    private void resetStatue() {
        if (null != listFiles) {
            listFiles.clear();
        }
        /*
        是否有上传失败的文件，有则提示失败
		*/
        if (isAllSuccefull) {
            ToastUtil.showShortToast(R.string.upload_success);
        } else {
            if (failFiles.length() > 0) {
                ToastUtil.showShortToast(failFiles.toString());
                failFiles = new StringBuilder();
            } else {
                ToastUtil.showShortToast(R.string.upload_success);
            }
        }

        isAllSuccefull = true;

        setUpdateBtnClick(true);
    }

    private String getEnvironmentDir() {
        String result = ShopInfoCfg.getInstance().getServerKey();
        if (result.contains("test")) {
            result = "testerp/";
        } else if (result.contains("dev")) {
            result = "deverp/";
        } else if (result.contains("gld")) {
            result = "glderp/";
        } else {
            result = "erp/";
        }
        return result;
    }

    private void prepareFiles() {
        initFileNewName();
        /*
        准备上传的文件，放到list里
		 */
        if (null != listFiles && listFiles.size() != 0) {
            listFiles.clear();
        }
        for (int i = 0; i < uploadFilePaths.length; i++) {
            //取得print log的最新7个文件
            List<FileAndNewNameMapItem> fileList = findFileOf7(uploadFilePaths[i], uploadFileName[i]);
            if (fileList != null) {
                listFiles.addAll(fileList);
            }
        }
    }


    /**
     * 取得路径下的最新7个文件
     *
     * @param path 文件夹路径
     */
    private List<FileAndNewNameMapItem> findFileOf7(String path, String name) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            List<FileAndNewNameMapItem> fileList = new ArrayList<>();//将需要的子文件信息存入到FileInfo里面
            File[] fs = file.listFiles();
            /*
            如果路径下没有文件返回null
			 */
            if (fs == null || fs.length == 0) {
                return null;
            }

            for (File f : fs) {
                fileList.add(new FileAndNewNameMapItem(f, newName + f.getName() + "_" + name + "_" + tempNewName));
            }
            Collections.sort(fileList, new FileComparator());//通过重写Comparator的实现类

			/*
            返回最新5个文件
			 */
            if (fs.length <= 7) {
                return fileList;
            }
            return fileList.subList(0, 7);
        }
        return null;
    }

    @Click(R.id.mLogButton)
    public void onLogButtonClick() {
        mPrintLogSwitch.setChecked(mPrintLogSwitch.isChecked());
    }

    private boolean executeProgressTask(String executeString, String doneString, boolean isLog) {
        if (mProgressTask != null && mProgressTask.getStatus() != AsyncTask.Status.FINISHED) {
            ToastUtil.showShortToast("System busy please wait...");
            return false;
        }
        if (isLog) {
            mProgressTask = new uploadLogTask(mProgressParentView, executeString, doneString);
        } else {
            mProgressTask = new ProgressTask(mProgressParentView, executeString, doneString);
        }
        AsyncTaskCompat.executeParallel(mProgressTask);
        return true;
    }

    class ProgressTask extends AsyncTask<Void, Integer, Void> {
        ProgressBar mProgressBar;

        TextView mTitle;

        TextView mProgressText;

        String mExecuteString;

        String mDoneString;

        ImageView mProgressDone;

        ProgressTask(View parentView, String executeString, String doneString) {
            parentView.setVisibility(View.VISIBLE);
            mTitle = (TextView) parentView.findViewById(R.id.settings_progress_title);
            mProgressText = (TextView) parentView.findViewById(R.id.settings_progress_text);
            mProgressBar = (ProgressBar) parentView.findViewById(R.id.settings_progress_bar);
            mProgressDone = (ImageView) parentView.findViewById(R.id.settings_progress_done_image);
            mExecuteString = executeString;
            mDoneString = doneString;

        }

        @Override
        protected void onPreExecute() {
            mTitle.setText(mExecuteString);
            mProgressDone.setVisibility(View.INVISIBLE);
            mProgressText.setText("0%");

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
            mProgressText.setText(values[0] + "%");
        }

        @Override
        protected void onPostExecute(Void result) {
            mTitle.setText(mDoneString);
            mProgressBar.setProgress(100);
            mProgressText.setText(100 + "%");
            mProgressDone.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(HIDE_PROGRESS, 2000);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    ;

    class uploadLogTask extends ProgressTask {

        uploadLogTask(View parentView, String executeString, String doneString) {
            super(parentView, executeString, doneString);
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.publishProgress(60);
            /*try {
                File[] logFiles = OSLog.getLog().getLogFiles();
                if (logFiles != null) {
                    for (File f : logFiles) {
                        UploadFile.uploadLogFile(MainApplication.getInstance(),
                                ServerAddressUtil.getInstance().getLogAddApi(),
                                new File(f.getPath()));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }*/

            return null;
        }
    }

    private static String getExternalStorageDirectory() {
        String rootpath = Environment.getExternalStorageDirectory().getPath();
        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        return rootpath;
    }


    public class FileComparator implements Comparator<FileAndNewNameMapItem> {
        public int compare(FileAndNewNameMapItem file1, FileAndNewNameMapItem file2) {
            if (file1.getFile().lastModified() > file2.getFile().lastModified()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private class FileAndNewNameMapItem {
        private String newFileName;

        private File file;

        FileAndNewNameMapItem(File file, String newFilePath) {
            this.newFileName = newFilePath;
            this.file = file;
        }

        String getNewFilePath() {
            return newFileName;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }
}