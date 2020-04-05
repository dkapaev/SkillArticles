package ru.skillbranch.skillarticles.viewmodels.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelDelegate<T : ViewModel>(
    private val clazz: Class<T>,
    private val arg: Any?
) : ReadOnlyProperty<FragmentActivity, T> {
    private var value: T? = null

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) {
            if (arg == null) {
                value = ViewModelProvider(thisRef).get(clazz)
            } else {
                value = ViewModelProvider(thisRef, ViewModelFactory(arg)).get(clazz)
            }
        }
        return value!!
    }
}
