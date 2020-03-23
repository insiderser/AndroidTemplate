/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.insiderser.android.template.core.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Returns a property delegate that automatically sets backing property to `null`
 * after this [Fragment]'s view gets destroyed.
 */
fun <T : Any> Fragment.viewLifecycleScoped(): ReadWriteProperty<Any, T> =
    ViewLifecycleScopedProperty(this)

private class ViewLifecycleScopedProperty<T : Any>(owner: Fragment) :
    ReadWriteProperty<Any, T>, LifecycleObserver {

    private var value: T? = null

    init {
        owner.viewLifecycleOwnerLiveData
            .observeForever { viewLifecycleOwner ->
                viewLifecycleOwner?.lifecycle?.addObserver(this)
            }
    }

    @Suppress("unused")
    @OnLifecycleEvent(ON_DESTROY)
    fun onDestroyView() {
        value = null
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        value ?: error(
            "${property.name} property isn't initialized. Probably called before onCreateView or after onDestroyView"
        )

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}
