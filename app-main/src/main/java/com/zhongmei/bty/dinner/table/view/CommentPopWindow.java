package com.zhongmei.bty.dinner.table.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;

/**
 * @Date：2015-9-9
 * @Description:用户列表弹出框
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CommentPopWindow extends PopupWindow implements View.OnTouchListener, TextWatcher {
    private EditText contentEditText;// 输入内容

    private CommentsView commentsView;// 备注控件

    private View parentView;

    private Activity activity;

    private String[] commentsList;

    private TextChangedLisener lisener;
    private String commentContent;

    public CommentPopWindow(Activity activity, View parentView, String[] commentsList) {
        super(activity);
        this.activity = activity;
        this.parentView = parentView;
        this.commentsList = commentsList;
        initialView(activity);

    }

    private void initialView(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.dinner_table_info_commentwindow, null);
        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        contentEditText = (EditText) view.findViewById(R.id.content_edit);
        contentEditText.requestFocus();
        contentEditText.setOnClickListener(clickListener);
        contentEditText.addTextChangedListener(this);

        commentsView = (CommentsView) view.findViewById(R.id.commentsview);
        commentsView.setItemClickListener(itemClickListener);// 评论点击添加
        commentsView.setAdatper(commentsList);
        commentsView.initialView(activity, DensityUtil.dip2px(activity, 300));

        setTouchable(true);
        setTouchInterceptor(this);
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(R.style.dinner_table_info_waiterwindow_style);

    }

    public void show() {
        if (parentView != null) {
            if (!isShowing()) {
                showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
            }

        }
    }

    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (inRangeOfView(getContentView(), event)) {
            return false;
        } else {
            hide();
            return true;
        }
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        View contentView = getContentView();
        int x = contentView.getPaddingLeft();
        int y = contentView.getPaddingTop();
        View contentView2 = contentView.findViewById(R.id.content_ll);
        int width = contentView2.getWidth();
        int height = contentView.getHeight();
        if (ev.getX() < x || ev.getX() > (x + width) || ev.getY() < y || ev.getY() > (y + height)) {
            return false;
        }
        return true;
    }

    private CommentsView.ItemClickListener itemClickListener = new CommentsView.ItemClickListener() {

        @Override
        public void onClick(String content) {
            if (TextUtils.isEmpty(contentEditText.getText().toString())) {
                contentEditText.append(content);
            } else {
                contentEditText.append("," + content);
            }

        }
    };

    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        lisener.onTextChanged(s.toString());

    }

    public void setListener(TextChangedLisener lisener) {
        this.lisener = lisener;
    }

    public interface TextChangedLisener {
        void onTextChanged(String content);
    }

    public void setCommentContent(String content) {
        commentContent = content;
        contentEditText.setText(commentContent);
    }

}
