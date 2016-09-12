package com.github.moduth.ext.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Abner on 16/6/24.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class KeyBoardUtils {

    public static void hideInputMethod(Context context, View editText) {
        if (context != null) {
            if (editText != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    public static void showInputMethod(Context context, View editText) {
        if (context != null && editText != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            editText.requestFocus();
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

}
