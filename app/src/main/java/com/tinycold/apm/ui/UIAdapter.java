package com.tinycold.apm.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class UIAdapter {

    private static float sDeviceDensity;
    private static float sDeviceScaleDensity;

    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        final DisplayMetrics appDM = application.getResources().getDisplayMetrics();
        if (sDeviceDensity == 0 || sDeviceScaleDensity == 0) {
            sDeviceDensity = appDM.density;
            sDeviceScaleDensity = appDM.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration configuration) {
                    if (configuration.fontScale > 0) {
                        sDeviceScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {}
            });
        }
        // 可以选择以高度或者以宽度适配屏幕
        final float customDensity = appDM.heightPixels / 640F;
        final float customScaleDensity = customDensity * (sDeviceScaleDensity / sDeviceDensity);
        final int customDesityDpi = (int) (customDensity * 160);

        appDM.density = customDensity;
        appDM.scaledDensity = customScaleDensity;
        appDM.densityDpi = customDesityDpi;

        final DisplayMetrics activityDM = activity.getResources().getDisplayMetrics();
        activityDM.density = customDensity;
        activityDM.scaledDensity = customScaleDensity;
        activityDM.densityDpi = customDesityDpi;
    }

}
