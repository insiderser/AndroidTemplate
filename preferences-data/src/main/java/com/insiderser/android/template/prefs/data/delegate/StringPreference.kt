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
package com.insiderser.android.template.prefs.data.delegate

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import timber.log.Timber
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property delegate that manages a single entry in [SharedPreferences].
 *
 * **Note**: all get operations are done synchronously on the calling thread.
 * Make sure to call it on a worker thread.
 */
internal class StringPreference(
    private val sharedPreferences: Lazy<SharedPreferences>,
    private val preferenceKey: String,
    private val defaultValue: String? = null
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        sharedPreferences.value.getString(preferenceKey, defaultValue)

    @WorkerThread
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        Timber.v("Changing value of property with key $preferenceKey to $value")
        sharedPreferences.value.edit(commit = true) {
            putString(preferenceKey, value)
        }
    }
}
