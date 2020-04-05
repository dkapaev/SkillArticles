package ru.skillbranch.skillarticles.data.delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import ru.skillbranch.skillarticles.data.local.PrefManager

class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {
    private var storedValue: T? = null

    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        if (storedValue == null) {
            storedValue = when (defaultValue) {
                is Int -> thisRef.preferences.getInt(property.name, defaultValue as Int) as T
                is Long -> thisRef.preferences.getLong(property.name, defaultValue as Long) as T
                is Float -> thisRef.preferences.getFloat(property.name, defaultValue as Float) as T
                is String -> thisRef.preferences.getString(property.name, defaultValue as String) as T
                is Boolean -> thisRef.preferences.getBoolean(property.name, defaultValue as Boolean) as T
                else -> throw IllegalArgumentException("Cannot read value from preferences, unexpected value type")
            }
        }
        return storedValue
    }

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        when (value) {
            is Int -> thisRef.preferences.edit().putInt(property.name, value as Int).apply()
            is Long -> thisRef.preferences.edit().putLong(property.name, value as Long).apply()
            is Float -> thisRef.preferences.edit().putFloat(property.name, value as Float).apply()
            is String -> thisRef.preferences.edit().putString(property.name, value as String).apply()
            is Boolean -> thisRef.preferences.edit().putBoolean(property.name, value as Boolean).apply()
            else -> throw IllegalArgumentException("Cannot write value to preferences, unexpected value type")
        }
        storedValue = value
    }
}
