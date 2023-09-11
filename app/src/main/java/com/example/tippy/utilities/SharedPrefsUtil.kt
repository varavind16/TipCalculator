package com.example.tippy.utilities

import android.content.Context
import com.example.tippy.constants.Constants

//Acts as a manager to save and retrieve values in the specified shared preferences file
object SharedPrefsUtil {
    private const val PREFS_FILE_NAME = Constants.SHARED_PREFS_FILE_NAME

    //SharePReferences Init Method
    fun sharedPrefsInit(context: Context) {
        val temp = getValueFromPreferences(context, Constants.APP_FIRST_TIME_LAUNCHED)
        if (temp == Constants.SHARED_PREFS_DEFAULT_VAL) {
            saveValueToPreferences(
                context,
                Constants.SHARED_PREFS_SELECTED_CURRENCY,
                Constants.US_DOLLAR
            )

            saveValueToPreferences(
                context,
                Constants.APP_FIRST_TIME_LAUNCHED,
                Constants.TRUE
            )
        }
    }

    fun saveValueToPreferences(context: Context, key: String, value: String) {
        try {
            val sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, 0)
            val editor = sharedPreference.edit()
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getValueFromPreferences(context: Context, key: String): String {
        val sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, 0)
        return try {
            sharedPreference.getString(key, Constants.SHARED_PREFS_DEFAULT_VAL)!!
        } catch (e: Exception) {
            Constants.SHARED_PREFS_DEFAULT_VAL
        }
    }
}