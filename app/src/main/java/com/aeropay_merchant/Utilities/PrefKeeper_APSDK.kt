package com.aeropay_merchant.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PrefKeeper_APSDK {

    private var prefs: SharedPreferences? = null

    var logInCount: Int
        get() = prefs!!.getInt(ConstantsStrings_APSDK().loginCount, 0)
        set(loginCount) = prefs!!.edit().putInt(ConstantsStrings_APSDK().loginCount, loginCount).apply()

    var pinValue: String?
        get() = prefs!!.getString(ConstantsStrings_APSDK().pinValue,ConstantsStrings_APSDK().noValue)
        set(pinValue) = prefs!!.edit().putString(ConstantsStrings_APSDK().pinValue, pinValue).apply()

    var deviceToken: String?
        get() = prefs!!.getString(ConstantsStrings_APSDK().deviceToken,ConstantsStrings_APSDK().noValue)
        set(setToken) = prefs!!.edit().putString(ConstantsStrings_APSDK().deviceToken, setToken).apply()

    var isPinEnabled: Boolean
        get() = prefs!!.getBoolean(ConstantsStrings_APSDK().pinEnabled, false)
        set(pinEnabled) = prefs!!.edit().putBoolean(ConstantsStrings_APSDK().pinEnabled, pinEnabled).apply()

    var isLoggedIn: Boolean
        get() = prefs!!.getBoolean(ConstantsStrings_APSDK().isLoggedin, false)
        set(isLogin) = prefs!!.edit().putBoolean(ConstantsStrings_APSDK().isLoggedin, isLogin).apply()

    var storeName: String?
        get() = prefs?.getString(ConstantsStrings_APSDK().storeName, ConstantsStrings_APSDK().noValue)
        set(storeName) = prefs!!.edit().putString(ConstantsStrings_APSDK().storeName, storeName).apply()

    var deviceName: String?
        get() = prefs?.getString(ConstantsStrings_APSDK().deviceName, ConstantsStrings_APSDK().noValue)
        set(deviceName) = prefs!!.edit().putString(ConstantsStrings_APSDK().deviceName, deviceName).apply()

    var merchantDeviceIdPosition : Int?
        get() = prefs?.getInt(ConstantsStrings_APSDK().merchantDeviceIdPosition, 0)
        set(devicePosition) = prefs!!.edit().putInt(ConstantsStrings_APSDK().merchantDeviceIdPosition, devicePosition!!).apply()

    var username: String?
        get() = prefs?.getString(ConstantsStrings_APSDK().username, ConstantsStrings_APSDK().noValue)
        set(username) = prefs!!.edit().putString(ConstantsStrings_APSDK().username, username).apply()

    var password: String?
        get() = prefs?.getString(ConstantsStrings_APSDK().password, null)
        set(token) = prefs!!.edit().putString(ConstantsStrings_APSDK().password, token).apply()

    var usernameIV: String?
        get() = prefs?.getString(ConstantsStrings_APSDK().usernameIv, null)
        set(usernameIv) = prefs!!.edit().putString(ConstantsStrings_APSDK().usernameIv, usernameIv).apply()

    var passwordIV: String?
        get() = prefs?.getString(ConstantsStrings_APSDK().passwordIv, null)
        set(passwordIv) = prefs!!.edit().putString(ConstantsStrings_APSDK().passwordIv, passwordIv).apply()

    fun init(context: Context) {
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun clear() = prefs?.edit()?.clear()?.apply()

}