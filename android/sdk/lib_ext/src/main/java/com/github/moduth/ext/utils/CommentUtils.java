package com.github.moduth.ext.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Abner on 16/6/24.
 * Email nimengbo@gmail.com
 * GitHub https://github.com/nimengbo
 */
public class CommentUtils {

    public static String getReplyText(String name) {
        return "@" + name + " ";
    }


    public static SpannableStringBuilder getReplyContentText(String name, String message, int color) {
        String replyName = "@" + name + " ";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(replyName);
        stringBuilder.append(message);
        stringBuilder.setSpan(new ForegroundColorSpan(color), 0, replyName.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

}
