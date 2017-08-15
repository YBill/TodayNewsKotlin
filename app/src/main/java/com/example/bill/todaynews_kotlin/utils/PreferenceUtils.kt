package com.example.bill.todaynews_kotlin.utils

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Bill on 2017/8/15.
 */
class PreferenceUtils<T>(val context: Context, val name: String, val default: T) : ReadWriteProperty<Any?, T> {

    val preference by lazy {
        context.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T = with(preference) {
        val res: Any = when (default) {
            is Int -> getInt(name, default)
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Float -> getFloat(name, default)
            is Boolean -> getBoolean(name, default)
            else -> {
                throw IllegalArgumentException("Not have is Type be saved preference")
            }
        }
        res as T
    }

    private fun putPreference(name: String, value: T) = with(preference.edit()) {
        when (value) {
            is Int -> putInt(name, value)
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Float -> putFloat(name, value)
            is Boolean -> putBoolean(name, value)
            else -> {
                throw IllegalArgumentException("Not have is Type be saved preference")
            }
        }.apply()
    }

}