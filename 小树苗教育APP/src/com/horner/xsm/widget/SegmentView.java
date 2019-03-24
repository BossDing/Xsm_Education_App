package com.horner.xsm.widget;

import org.xmlpull.v1.XmlPullParser;

import com.horner.xsm.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SegmentView extends LinearLayout {
	private TextView textView1;
	private TextView textView2;
	private onSegmentViewClickListener listener;

	public SegmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SegmentView(Context context) {
		super(context);
		init();
	}

	private void init() {
		// this.setLayoutParams(new
		// LinearLayout.LayoutParams(dp2Px(getContext(), 60),
		// LinearLayout.LayoutParams.WRAP_CONTENT));
		textView1 = new TextView(getContext());
		textView2 = new TextView(getContext());
		textView1.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1));
		textView2.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1));
		textView1.setText("男");
		textView2.setText("女");
		XmlPullParser xrp = getResources().getXml(
				R.drawable.set_text_color_selector);
		try {
			@SuppressWarnings("deprecation")
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);
			textView1.setTextColor(csl);
			textView2.setTextColor(csl);
		} catch (Exception e) {
		}
		textView1.setGravity(Gravity.CENTER);
		textView2.setGravity(Gravity.CENTER);
		textView1.setPadding(12, 9, 12, 9);
		textView2.setPadding(12, 9, 12, 9);
		setSegmentTextSize(16);
		textView1.setBackgroundResource(R.drawable.seg_left);
		textView2.setBackgroundResource(R.drawable.seg_right);
		textView1.setSelected(true);
		this.removeAllViews();
		this.addView(textView1);
		this.addView(textView2);
		this.invalidate();

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView1, 0);
				}
			}
		});
		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView2.isSelected()) {
					return;
				}
				textView2.setSelected(true);
				textView1.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
				}
			}
		});
	}

	/**
	 * 设置字体大小 单位dip
	 * 
	 * @param dp
	 * @author RANDY.ZHANG
	 */
	public void setSegmentTextSize(int dp) {
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
	}

	// dp转换为px
	private static int dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public void setOnSegmentViewClickListener(
			onSegmentViewClickListener listener) {
		this.listener = listener;
	}

	
	  
    public void checkState(int position){
    	Log.i("info","position的数值为;"+position);
    	if(position == 0 ){
    		 textView1.setSelected(true); 
    		 textView2.setSelected(false);  
    	}else if(position == 1) {
    		textView1.setSelected(false); 
   		    textView2.setSelected(true); 
		}
    }
	public String getUserSex(){
		if(textView1.isSelected()){
			return "男";
		}else if(textView2.isSelected()) {
			return "女";
		}
		return "";
	}
	/**
	 * @author 作者 : sun
	 * @date 创建时间：2016-2-1 下午3:59:57
	 * @return
	 * @description SegmentView内容的设置
	 */
	public void setSegmentText(CharSequence text, int position) {
		if (position == 0) {
			textView1.setText(text);
		}
		if (position == 1) {
			textView2.setText(text);
		}
	}

	/**
	 * @author sun 定义一个接口,实现接口回调
	 */
	public static interface onSegmentViewClickListener {
		public void onSegmentViewClick(View v, int position);
	}
}