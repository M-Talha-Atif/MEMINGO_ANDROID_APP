package com.umt.memingoapp.utils

import android.content.Context

object AuthState {
    private const val PREFS_NAME = "AuthPrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    private var isLoggedIn: Boolean? = null

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        isLoggedIn = loggedIn
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun isUserLoggedIn(context: Context): Boolean {
        if (isLoggedIn == null) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        }
        return isLoggedIn!!
    }
}
