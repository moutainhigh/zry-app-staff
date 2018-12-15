package com.zhongmei.bty.basemodule.auth.permission.dialog;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.input.NumberClickListener;
import com.zhongmei.bty.basemodule.input.NumberKeyBoard;
import com.zhongmei.bty.basemodule.input.Password;
import com.zhongmei.bty.basemodule.input.Password.PassVerify;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.support.IAuthUserProxy;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.context.util.peony.land.Extractable;
import com.zhongmei.bty.commonmodule.adapter.AbstractSpinerAdapter;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.database.enums.AuthType;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.commonmodule.view.SpinerPopWindow;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @Date：2015年9月29日 下午3:22:32
 * @Description: 权限校验对话框
 * @Version: 1.0
 */

@EFragment(resName = "auth_permission_verify_permission_dialog")
public class VerifyPermissionsDialogFragment extends BasicDialogFragment implements
        AbstractSpinerAdapter.IOnItemSelectListener, NumberClickListener, PassVerify, OnKeyListener {

    @ViewById(resName = "close_btn")
    Button mCloseBtn;

    @ViewById(resName = "input_select_btn")
    Button mInputSelectBtn;

    @ViewById(resName = "software_disk_layout")
    NumberKeyBoard keyboard;

    @ViewById(resName = "password_edt")
    Password password;

    @ViewById(resName = "password_error_hint")
    TextView passwordErrorHint;

    private List<User> userList = new ArrayList<>();

    private SpinerPopWindow mSpinerPopWindow;

    private PermissionVerify listener;

    private CloseListener closeListener;

    private User currentAuthUser;

    private String tag;
    private MyHandler handler;
    private AuthType authType;
    private String authDesc;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    @AfterViews
    public void init() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);
        password.setListener(this);
        mSpinerPopWindow = new SpinerPopWindow(getActivity());
        mSpinerPopWindow.setItemListener(this);
        setDefaultUser();
        handler = new MyHandler();
        keyboard.setShowClean();
        keyboard.setListener(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void setDefaultUser() {
        setHero(0);
        mSpinerPopWindow.refreshData(ArgsUtils.sucker(userList, new Extractable<IAuthUser, User>() {
            @Override
            public IAuthUser extract(User value) {
                return new IAuthUserProxy(value.getId(), value.getName());
            }
        }), 0);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @Title: setSpinnerData
     * @Description: TODO 设置下拉框数据
     * @Param @param list TODO
     * @Return void 返回类型
     */
    public void setSpinnerData(List<User> list) {
        userList = list;
    }

    /**
     * 设置授权操作点信息
     *
     * @param authType
     * @param authDesc
     */
    public void setAuthLogData(AuthType authType, String authDesc) {
        this.authType = authType;
        this.authDesc = authDesc;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDialog().setOnKeyListener(this);
        return null;

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }

    private void setHero(int pos) {
        if (pos >= 0 && userList != null && pos < userList.size() && userList.size() > 0) {
            mInputSelectBtn.setText(userList.get(pos).getName());
            currentAuthUser = userList.get(pos);
            password.deleteText(true);
            showError(false);
        }
    }

    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(mInputSelectBtn.getWidth());
        mSpinerPopWindow.showAsDropDown(mInputSelectBtn, 0, DensityUtil.dip2px(BaseApplication.sInstance, 14));
    }

    @Click(resName = "close_btn")
    public void closeClick() {
        this.dismiss();
        if (null != closeListener) {
            closeListener.close(tag);
        }
        if (null != listener) {
            listener.verify(tag, false);
        }
    }

    @Click(resName = "input_select_btn")
    public void inputClick() {
        showSpinWindow();
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputSelectBtn.getWindowToken(), 0);
    }

    public void confirmClick() {
        if (TextUtils.isEmpty(password.getInputPassword())) {
            ToastUtil.showShortToast(R.string.commonmodule_password_empty_hint);
        } else {
            if (Session.getFunc(UserFunc.class)
                    .checkPassword(currentAuthUser, password.getInputPassword())) {
                // 密码校验成功，回调加对话框消失
                saveAuthCache();
                if (null != listener) {
                    if (listener instanceof PermissionVerify2) {
                        ((PermissionVerify2) listener).verify(currentAuthUser, tag, true);
                    } else {
                        listener.verify(tag, true);
                    }
                }
                this.dismiss();
            } else {
                // 密码校验失败
                password.startAnimation(Utils.shakeAnimation(3));
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessageDelayed(message, 600);

                password.deleteText(true);
                if (null != listener) {
                    listener.verify(tag, false);
                }
            }
        }

    }

    /**
     * 保存auth到缓存
     */
    private void saveAuthCache() {
        if (authType == null || authType == AuthType.__UNKNOWN__ || currentAuthUser == null) {
            return;
        }
        AuthLogManager.getInstance().putAuthLog(authType, authDesc, currentAuthUser);
    }


    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            showError(true);
        }
    }

    @Override
    public void onItemClick(int pos) {
        setHero(pos);
    }

    public void setListener(PermissionVerify listener) {
        this.listener = listener;
    }

    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public interface PermissionVerify {
        void verify(String permission, boolean success);
    }

    public interface PermissionVerify2 extends PermissionVerify {
        void verify(User user, String permission, boolean success);
    }

    public interface CloseListener {
        void close(String permission);
    }

    @Override
    public void numberClicked(String number) {
        password.setText(number);
        showError(false);
    }

    @Override
    public void clearClicked() {
        password.deleteText(true);
        showError(false);
    }

    @Override
    public void deleteClicked() {
        password.deleteText(false);
        showError(false);

    }

    public void onEventMainThread(ActionClosePermissionDialog event) {
        this.dismiss();
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void verifyPass() {
        confirmClick();
    }

    private void showError(boolean show) {
        if (show) {
            passwordErrorHint.setText(R.string.commonmodule_password_error_hint);
        } else {
            passwordErrorHint.setText("");
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME && event.getRepeatCount() == 0) {
            this.dismiss();
            if (null != listener) {
                listener.verify(tag, false);
            }
            this.getActivity().onKeyDown(keyCode, event);
            return false;
        }
        //add 20170321 begin 添加外接键盘输入
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_NUMPAD_DOT) {//删除键
            password.deleteText(false);
            showError(false);
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && ClickManager.isNumber(keyCode)) {//数字键
            password.setText(event.getNumber() + "");
            showError(false);
            return true;
        }
        //消费回车键
        if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
            return true;
        }
        //add 20170321 end 添加外接键盘输入
        return false;
    }

    //add v8.16  start人脸授权
	/*@Click(resName = "btn_face_pick")
	public void startFace() {
		boolean available = BaiduFaceRecognition.getInstance().checkFaceServer();
		if (!available) {
			if (Session.getAuthUser() != null) {
				FacecognitionActivity.showFaceServerWarmDialog(getContext(), getChildFragmentManager());
			} else {
				DialogUtil.showHintConfirmDialog(getChildFragmentManager(), getString(R.string.talent_open_face_alter),
						R.string.talent_know_alter, null, false, "confirm_band_face");
			}
			return;
		}
		startActivityForResult(BaiduFaceRecognition.getInstance().getRecognitionFaceIntent(), RC_TALENT_FACE_PERMISSION);
	}*/

	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_TALENT_FACE_PERMISSION && resultCode == Activity.RESULT_OK) {
			String faceCode = data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE);
			facePermissionCheck(faceCode);
		}
	}*/


    private void facePermissionCheck(final String faceCode) {
        if (!TextUtils.isEmpty(faceCode)) {
            new AsyncTask<Void, Void, User>() {
                @Override
                protected User doInBackground(Void... params) {
                    try {
                        List<User> list = Session.getFunc(UserFunc.class).getUsersByFaceCode(faceCode);
                        if (list != null && list.size() > 0) {
                            return list.get(0);
                        }
                        return null;
                    } catch (Exception e) {

                    }
                    return null;
                }

                protected void onPostExecute(User user) {
                    if (user != null) {
                        if (userList != null && userList.size() > 0) {
                            for (User loginer : userList) {
                                if (loginer.equals(user)) {//配当前有登录权限的用户
                                    permissionUser(user);
                                    return;
                                }
                            }
                            ToastUtil.showLongToast(R.string.talent_alter_not_promisson);
                        } else {
                            ToastUtil.showShortToast(R.string.talent_alter_not_promisson);
                        }

                    } else {
                        DialogUtil.showHintConfirmDialog(getChildFragmentManager(), getString(R.string.talent_alter_bind_face_text),
                                com.zhongmei.yunfu.basemodule.R.string.ok_button, null, false, "confirm_band_face");
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            //人脸认证失败

        }
    }

    private static int RC_TALENT_FACE_PERMISSION = 0X004;//人脸授权

    //绑定user 到 session
    private void permissionUser(final User user) {
        saveAuthCache();
        if (null != listener) {
            if (listener instanceof PermissionVerify2) {
                ((PermissionVerify2) listener).verify(user, tag, true);
            } else {
                listener.verify(tag, true);
            }
        }
        this.dismiss();
    }
    //add v8.16  end人脸授权
}
