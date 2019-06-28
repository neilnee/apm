package com.tinycold.apm.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * UI适配
 */
public class UIAdapter {

    private static float sDeviceDensity;
    private static float sDeviceScaleDensity;
    private static int sDeviceDensityDpi;

    /**
     * 设置自定义的density参数
     */
    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        final DisplayMetrics appDM = application.getResources().getDisplayMetrics();
        final DisplayMetrics activityDM = activity.getResources().getDisplayMetrics();
        if (sDeviceDensity == 0 || sDeviceScaleDensity == 0) {
            sDeviceDensity = appDM.density;
            sDeviceScaleDensity = appDM.scaledDensity;
            sDeviceDensityDpi = appDM.densityDpi;
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
        // 修改application的属性后会影响全局
        appDM.density = customDensity;
        appDM.scaledDensity = customScaleDensity;
        appDM.densityDpi = customDesityDpi;
        activityDM.density = customDensity;
        activityDM.scaledDensity = customScaleDensity;
        activityDM.densityDpi = customDesityDpi;
    }

    /**
     * 重置回系统的density参数
     * 修改density等操作会影响应用全局的density相关值，因此考虑以activity为粒度进行修改适配
     */
    public static void resetDensity(@NonNull Activity activity, @NonNull final Application application) {
        if (sDeviceDensity > 0 && sDeviceDensityDpi > 0 && sDeviceScaleDensity > 0) {
            final DisplayMetrics appDM = application.getResources().getDisplayMetrics();
            final DisplayMetrics activityDM = activity.getResources().getDisplayMetrics();
            appDM.density = sDeviceDensity;
            appDM.scaledDensity = sDeviceScaleDensity;
            appDM.densityDpi = sDeviceDensityDpi;
            activityDM.density = sDeviceDensity;
            activityDM.scaledDensity = sDeviceScaleDensity;
            activityDM.densityDpi = sDeviceDensityDpi;
        }
    }

}
