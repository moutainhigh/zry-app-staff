package com.zhongmei.bty.queue.ui.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

@EFragment(R.layout.keyboard_layout_queue)
public class KeyboardFragment extends Fragment implements OnClickListener {

    @ViewById
    protected TextView one;

    @ViewById
    protected TextView two;

    @ViewById
    protected TextView three;

    @ViewById
    protected TextView four;

    @ViewById
    protected TextView five;

    @ViewById
    protected TextView six;

    @ViewById
    protected TextView seven;

    @ViewById
    protected TextView eight;

    @ViewById
    protected TextView nine;

    @ViewById
    protected TextView zero;

    @ViewById
    protected ImageView clean;

    @ViewById
    protected ImageView delete;

    private OnKeyBoradListener mListener;


    @AfterViews
    protected void initViews() {
//		DisplayMetrics dm=new DisplayMetrics();
////		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //	layout1();
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        clean.setOnClickListener(this);
        delete.setOnClickListener(this);

    }


//	@Click({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
//		R.id.zero, R.id.dot, R.id.delete})
//	@Click(R.id.one)
//	Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//		String value = "";
//		switch (v.getId()) {
//		case R.id.one:
//			value = "1";
//			break;
//		case R.id.two:
//			value = "2";
//			break;
//		case R.id.three:
//			value = "3";
//			break;
//		case R.id.four:
//			value = "4";
//			break;
//		case R.id.five:
//			value = "5";
//			break;
//		case R.id.six:
//			value = "6";
//			break;
//		case R.id.seven:
//			value = "7";
//			break;
//		case R.id.eight:
//			value = "8";
//			break;
//		case R.id.nine:
//			value = "9";
//			break;
//		case R.id.zero:
//			value = "0";
//			break;
//		case R.id.dot:
//			value = "clear";
//			break;
//		case R.id.delete:
//
//			value = "delete";
//			break;
//
//		default:
//			break;
//
//		}
//		if(!TextUtils.isEmpty(value)){
//			if(mListener!=null){
//				mListener.click(value);
//			}
//		}
//	}

    public ImageView getDotImageView() {
        return clean;
    }

    public ImageView getDeleteImageView() {
        return delete;
    }

    public interface OnKeyBoradListener {
        void click(String value);
    }

    public void setOnKeyBoradListener(OnKeyBoradListener listener) {
        this.mListener = listener;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String value = "";
        switch (v.getId()) {
            case R.id.one:
                value = "1";
                break;
            case R.id.two:
                value = "2";
                break;
            case R.id.three:
                value = "3";
                break;
            case R.id.four:
                value = "4";
                break;
            case R.id.five:
                value = "5";
                break;
            case R.id.six:
                value = "6";
                break;
            case R.id.seven:
                value = "7";
                break;
            case R.id.eight:
                value = "8";
                break;
            case R.id.nine:
                value = "9";
                break;
            case R.id.zero:
                value = "0";
                break;
            case R.id.clean:
                value = "clear";
                break;
            case R.id.delete:

                value = "delete";
                break;

            default:
                break;

        }
        if (!TextUtils.isEmpty(value)) {
            if (mListener != null) {
                mListener.click(value);
            }
        }
    }


}
