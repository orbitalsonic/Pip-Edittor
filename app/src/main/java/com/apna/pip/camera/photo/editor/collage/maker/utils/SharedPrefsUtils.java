package com.apna.pip.camera.photo.editor.collage.maker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsUtils {


    public static void privacy_setter(Context context, Boolean mboolean) {
        SharedPreferences    prefs = context.getSharedPreferences("prefs_privacy", MODE_PRIVATE);
        SharedPreferences.Editor  editor = prefs.edit();
        editor.putBoolean("firstTime_privacy", mboolean);
        editor.apply();

    }

    public static Boolean pivacy_getter(Context context) {
        SharedPreferences  prefs = context.getSharedPreferences("prefs_privacy", MODE_PRIVATE);
        boolean firstTime = prefs.getBoolean("firstTime_privacy", true);

        return firstTime;
    }


    public static Boolean getBanner(Context context) {
        SharedPreferences remoteConfigPref = context.getSharedPreferences("BannerPrefs", MODE_PRIVATE);
        Boolean adsBanner = remoteConfigPref.getBoolean("BannerValue", true);

        return adsBanner;
    }

    public static void setBanner(Context context, Boolean nativeBanner) {
        SharedPreferences   remoteConfigPref = context.getSharedPreferences("BannerPrefs", MODE_PRIVATE);
        SharedPreferences.Editor  remoteConfigEdditor = remoteConfigPref.edit();
        remoteConfigEdditor.putBoolean("BannerValue", nativeBanner);
        remoteConfigEdditor.apply();
    }


    public static Boolean getMediumRect(Context context) {
        SharedPreferences  remoteConfigPref = context.getSharedPreferences("MediumRectPrefs", MODE_PRIVATE);
        Boolean     adsMediumRect = remoteConfigPref.getBoolean("MediumRectValue", true);
        return adsMediumRect;
    }

    public static void setMediumRect(Context context, Boolean nativeMediumRect) {
        SharedPreferences  remoteConfigPref = context.getSharedPreferences("MediumRectPrefs", MODE_PRIVATE);
        SharedPreferences.Editor   remoteConfigEdditor = remoteConfigPref.edit();
        remoteConfigEdditor.putBoolean("MediumRectValue", nativeMediumRect);
        remoteConfigEdditor.apply();
    }

    public static boolean  getRemoveAds(Context context) {
        SharedPreferences remoteConfigPref = context.getSharedPreferences("removeAdsPrefs", Context.MODE_PRIVATE);
        return remoteConfigPref.getBoolean("removeAdsValue", true);
    }

    public static void setRemoveAds(Context context, Boolean mBoolean) {
        SharedPreferences remoteConfigPref = context.getSharedPreferences("removeAdsPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor remoteConfigEdditor = remoteConfigPref.edit();
        remoteConfigEdditor.putBoolean("removeAdsValue", mBoolean);
        remoteConfigEdditor.apply();
    }

}
