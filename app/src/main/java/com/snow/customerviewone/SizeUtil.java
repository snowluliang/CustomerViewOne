package com.snow.customerviewone;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by snow on 2016/11/2.
 */

public class SizeUtil {

    private Context context;

    public SizeUtil(Context context) {
        this.context = context;
    }

    public static float DptoPx(Context context, int value) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static float SptoPx(Context context, int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
                context.getResources().getDisplayMetrics());
    }
}
