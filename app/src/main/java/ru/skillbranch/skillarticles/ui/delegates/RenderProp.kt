package ru.skillbranch.skillarticles.ui.delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import ru.skillbranch.skillarticles.ui.base.Binding

class RenderProp<T>(
    var value: T,
    needInit: Boolean = true,
    private val onChange: ((T) -> Unit)? = null
) : ReadWriteProperty<Binding, T> {
    private val listeners: MutableList<() -> Unit> = mutableListOf()

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
            if (listeners.isNotEmpty()) listeners.forEach { it.invoke() }
        }
    }

    // register additional listener
    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }
}

class ObserveProp<T : Any> (
    private var value: T,
    private val onChange: ((T) -> Unit)? = null
) {
    // provide delegate (when "by" is called)
    operator fun provideDelegate(
        thisRef: Binding,
        prop: KProperty<*>
    ): ReadWriteProperty<Binding, T> {
        val delegate = RenderProp(value, true, onChange)
        registerDelegate(thisRef, prop.name, delegate)
        return delegate
    }

    // register new delegate for property in Binding
    private fun registerDelegate(
        thisRef: Binding,
        name: String,
        delegate: RenderProp<T>
    ) {
        thisRef.delegates[name] = delegate
    }
}
