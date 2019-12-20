package com.aeropay_merchant.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PrefKeeper {

    private var prefs: SharedPreferences? = null

    var logInCount: Int
        get() = prefs!!.getInt(ConstantsStrings().loginCount, 0)
        set(loginCount) = prefs!!.edit().putInt(ConstantsStrings().loginCount, loginCount).apply()

    var pinValue: String?
        get() = prefs!!.getString(ConstantsStrings().pinValue,ConstantsStrings().noValue)
        set(pinValue) = prefs!!.edit().putString(ConstantsStrings().pinValue, pinValue).apply()

    var deviceToken: String?
        get() = prefs!!.getString(ConstantsStrings().deviceToken,ConstantsStrings().noValue)
        set(setToken) = prefs!!.edit().putString(ConstantsStrings().deviceToken, setToken).apply()

    var isPinEnabled: Boolean
        get() = prefs!!.getBoolean(ConstantsStrings().pinEnabled, false)
        set(pinEnabled) = prefs!!.edit().putBoolean(ConstantsStrings().pinEnabled, pinEnabled).apply()

    var isLoggedIn: Boolean
        get() = prefs!!.getBoolean(ConstantsStrings().isLoggedin, false)
        set(isLogin) = prefs!!.edit().putBoolean(ConstantsStrings().isLoggedin, isLogin).apply()

    var storeName: String?
        get() = prefs?.getString(ConstantsStrings().storeName, ConstantsStrings().noValue)
        set(storeName) = prefs!!.edit().putString(ConstantsStrings().storeName, storeName).apply()

    var deviceName: String?
        get() = prefs?.getString(ConstantsStrings().deviceName, ConstantsStrings().noValue)
        set(deviceName) = prefs!!.edit().putString(ConstantsStrings().deviceName, deviceName).apply()

    var merchantDeviceIdPosition : Int?
        get() = prefs?.getInt(ConstantsStrings().merchantDeviceIdPosition, 0)
        set(devicePosition) = prefs!!.edit().putInt(ConstantsStrings().merchantDeviceIdPosition, devicePosition!!).apply()

    var username: String?
        get() = prefs?.getString(ConstantsStrings().username, ConstantsStrings().noValue)
        set(username) = prefs!!.edit().putString(ConstantsStrings().username, username).apply()

    var password: String?
        get() = prefs?.getString(ConstantsStrings().password, null)
        set(token) = prefs!!.edit().putString(ConstantsStrings().password, token).apply()

    var usernameIV: String?
        get() = prefs?.getString(ConstantsStrings().usernameIv, null)
        set(usernameIv) = prefs!!.edit().putString(ConstantsStrings().usernameIv, usernameIv).apply()

    var passwordIV: String?
        get() = prefs?.getString(ConstantsStrings().passwordIv, null)
        set(passwordIv) = prefs!!.edit().putString(ConstantsStrings().passwordIv, passwordIv).apply()

    fun init(context: Context) {
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun clear() = prefs?.edit()?.clear()?.apply()

}