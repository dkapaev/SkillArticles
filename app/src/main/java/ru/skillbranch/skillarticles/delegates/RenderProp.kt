package ru.skillbranch.skillarticles.delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import ru.skillbranch.skillarticles.ui.base.Binding

class RenderProp<T>(
    var value: T,
    needInit: Boolean = true,
    private val onChange: ((T) -> Unit)? = null
) : ReadWriteProperty<Binding, T> {

    init {
        if (needInit) onChange?.invoke(value)
    }

    override fun getValue(thisRef: Binding, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Binding, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            onChange?.invoke(this.value)
        }
    }
}
