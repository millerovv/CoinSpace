package com.example.alexmelnikov.coinspace.util

import android.content.Context
import android.preference.PreferenceManager

class PreferencesHelper(private val context: Context) {

    fun saveString(key: String, value: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun loadString(key: String): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, "")
    }

    fun saveFloat(key: String, value: Float) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun loadFloat(key: String): Float {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getFloat(key, 0F)
    }

    companion object {
        val MAIN_CURRENCY = "mainCur"
    }

}