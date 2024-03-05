package org.devio.as.proj.ability.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class HiDisplayUtil {
    public static int dp2px(float dp, Resources resources){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    public static int dp2px(float dp, Context context){
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static int getDisplayWidthInPx(@NonNull Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(wm != null){
            Display display = wm.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            return point.x;
        }
        return 0;
    }

    public static int getDisplayHeightInPx(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(wm != null){
            Display display = wm.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            return point.y;
        }
        return 0;
    }
}
