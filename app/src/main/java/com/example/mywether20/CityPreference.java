package com.example.mywether20;

import android.content.Context;
import android.content.SharedPreferences;

public class CityPreference {
    private static final String PREFS_NAME = "CityPrefs";
    private static final String KEY_CITY_NAME = "cityName";

    public static String getCity(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CITY_NAME, ""); // Повертаємо пустий рядок якщо місто не знайдено
    }

    public static void setCity(Context context, String cityName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_CITY_NAME, cityName);
        editor.apply();
    }
}