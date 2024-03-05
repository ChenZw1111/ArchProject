package com.example.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class HiViewUtil {
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
}
