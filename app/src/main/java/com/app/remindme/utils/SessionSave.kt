package com.app.remindme.utils

import android.content.Context
import com.app.remindme.R


fun Context.saveSessionData(key: String, value: Any) {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    val editor = sharedPreference.edit()
    when (value) {
        is String -> {
            editor.putString(key, value)
        }
        is Int -> {
            editor.putInt(key, value)
        }
        is Boolean -> {
            editor.putBoolean(key, value)
        }
        is Float -> {
            editor.putFloat(key, value)
        }
        is Long -> {
            editor.putLong(key, value)
        }
        else -> {
            editor.putString(key, value.toString())
        }
    }
    editor.apply()
}


fun Context.getSessionData(key: String, defaultValue: String): String {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    return sharedPreference.getString(key, defaultValue) ?: defaultValue
}

fun Context.getSessionData(key: String, defaultValue: Int): Int {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    return sharedPreference.getInt(key, defaultValue)
}

fun Context.getSessionData(key: String, defaultValue: Boolean): Boolean {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    return sharedPreference.getBoolean(key, defaultValue)
}

fun Context.getSessionData(key: String, defaultValue: Float): Float {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    return sharedPreference.getFloat(key, defaultValue)
}

fun Context.getSessionData(key: String, defaultValue: Long): Long {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    return sharedPreference.getLong(key, defaultValue)
}

fun Context.getSessionData(key: String, defaultValue: MutableSet<String>?): MutableSet<String>? {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    return sharedPreference.getStringSet(key, defaultValue)
}

fun Context.deleteSessionData(key: String) {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    val editor = sharedPreference.edit()
    editor.remove(key)
    editor.apply()
}

fun Context.deleteAllSessionData() {
    val sharedPreference = getSharedPreferences(getString(R.string.app_name), 0)
    val editor = sharedPreference.edit()
    editor.clear()
    editor.apply()
}


fun logAllSharedPreferences(context: Context) {
    val appName = context.getString(R.string.app_name)
    val sharedPreferences =
        context.getSharedPreferences(appName, Context.MODE_PRIVATE)
    val allEntries = sharedPreferences.all
    logThis("SHARED PREFERENCES FOR $appName")
    for ((key, value) in allEntries) {
        logThis("| $key : $value |")
    }
}