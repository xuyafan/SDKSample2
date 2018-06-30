package com.okokkid.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.okokkid.MyApp;


/**
 * author： xuyafan
 * description:
 */

public class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = MyApp.getContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = MyApp.getContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
