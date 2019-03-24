package com.horner.xsm.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.horner.xsm.R;

/**
 * @author Horner 正在加载dialog
 */
public class LoadingDialog {
	private static Dialog dialog;
	private static AnimationDrawable background;

	public static void isLoading(Context context) {
		dialog = new Dialog(context, R.style.Translucent_Dialog);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading_layout, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		imageView.setBackgroundResource(R.drawable.loadingani);
		background = (AnimationDrawable) imageView
				.getBackground();
		background.start();
		dialog.setContentView(view);
		dialog.show();
	}

	public static void finishLoading() {
		try {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (null != background) {
				background = null;
			}
		} catch (Exception e) {
		}

	}
}
