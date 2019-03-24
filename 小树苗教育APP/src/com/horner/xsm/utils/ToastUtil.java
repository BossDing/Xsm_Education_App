package com.horner.xsm.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static Toast mToast;

	public static void showLongToast(Context context, CharSequence msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		}
		mToast.setText(msg);
		mToast.show();

	}

	public static void showShortToast(Context context, CharSequence msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		}
		mToast.setText(msg);
		mToast.show();
	}

	public static void cancelToastShow() {
		mToast.cancel();
	}
}
