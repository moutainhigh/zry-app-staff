package com.zhongmei.yunfu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.zhongmei.beauty.BeautyMainActivity
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils
import com.zhongmei.bty.basemodule.session.core.user.UserFunc
import com.zhongmei.bty.splash.login.LoginSuccessEvent
import com.zhongmei.bty.splash.login.UserDialog
import com.zhongmei.bty.splash.login.adapter.UserGridAdapter
import com.zhongmei.yunfu.context.base.BaseApplication
import com.zhongmei.yunfu.context.session.Callback
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.yunfu.context.session.core.user.User
import com.zhongmei.yunfu.ui.base.BaseActivity
import com.zhongmei.yunfu.util.ToastUtil
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.zm_login_layout.*
import kotlinx.android.synthetic.main.zm_login_number_keyboard_layout.*

class LoginActivity : BaseActivity(), UserGridAdapter.OnUserSelectedListener {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }

        @JvmStatic
        fun logout(context: Context) {
            Session.unbind()
            BaseApplication.sInstance.finishAllActivity(null)
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zm_login_layout)

        login_user_list_bt.setOnClickListener {
            var userList = Session.getFunc(UserFunc::class.java).users
            var userDialog = UserDialog(this, userList, null, this)
            userDialog.show()
        }

        NumberKeyBoardUtils.setTouchListener(login_username_et)
        NumberKeyBoardUtils.setTouchListener(login_password_et)

        login_reset_bt.setOnClickListener {
            login_username_et.text = null
            login_password_et.text = null
        }

        login_submit_bt.setOnClickListener {
            submitLogin(login_username_et.text.toString(), login_password_et.text.toString())
        }

        number_one.setOnClickListener(numberClick)
        number_two.setOnClickListener(numberClick)
        number_three.setOnClickListener(numberClick)
        number_four.setOnClickListener(numberClick)
        number_five.setOnClickListener(numberClick)
        number_six.setOnClickListener(numberClick)
        number_seven.setOnClickListener(numberClick)
        number_eight.setOnClickListener(numberClick)
        number_nine.setOnClickListener(numberClick)
        number_zero.setOnClickListener(numberClick)
        number_clean.setOnClickListener(numberClick)
        number_delete.setOnClickListener(numberClick)
    }

    private fun setInputText(input: EditText) {
        if (input != null) {
            input.requestFocus()
            NumberKeyBoardUtils.hiddenSoftKeyboard(input)
        }
    }

    override fun onSelected(item: User, userId: Long?, userName: String?) {
        login_username_et.setText(item.account)
        login_password_et.requestFocus()
    }

    private fun submitLogin(userName: String, userPwd: String) {
        if (userName == "") {
            ToastUtil.showShortToast("请输入用户名")
            return
        }

        if (userPwd == "") {
            ToastUtil.showShortToast("请输入密码")
            return
        }

        var user = Session.getFunc(UserFunc::class.java).checkPassword(userName, userPwd)
        if (user == null) {
            ToastUtil.showShortToast("用户或密码出错")
            return
        }

        Session.bind(user, object : Callback {
            override fun onBindSuccess() {
                BeautyMainActivity.start(this@LoginActivity)
                EventBus.getDefault().post(LoginSuccessEvent())
                finish()
            }

            override fun onBindError(message: String) {
                login_password_et.setText("")
            }
        })
    }

    private val numberClick = View.OnClickListener { v ->
        when (v) {
            number_one,
            number_two,
            number_three,
            number_four,
            number_five,
            number_six,
            number_seven,
            number_eight,
            number_nine,
            number_zero -> {
                if (v is TextView) {
                    appendText(currentFocus, v.text.toString())
                }
            }
            number_clean -> cleanText(currentFocus)
            number_delete -> {
                //login_password_et.setText(delTextContent(login_password_et.text.toString()))
                delKeyCode(currentFocus)
            }
        }
    }

    private fun appendText(v: View, append: String) {
        if (v != null && v is EditText) {
            v.append(append)
        }
    }

    private fun cleanText(editText: View) {
        if (editText != null && editText is EditText) {
            editText.text = null
        }
    }

    private fun delKeyCode(editText: View) {
        if (editText != null && editText is EditText) {
            val keyCode = KeyEvent.KEYCODE_DEL
            val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, keyCode)
            editText.onKeyDown(keyCode, keyEventDown)
            editText.onKeyUp(keyCode, keyEventUp)
        }
    }

    private fun delTextContent(text: String): String {
        return if (text.isEmpty()) "" else text.substring(0, text.length - 1)
    }
}