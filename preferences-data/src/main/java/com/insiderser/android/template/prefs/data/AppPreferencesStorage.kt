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
package com.insiderser.android.template.prefs.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Storage for app's and user's preferences.
 */
interface AppPreferencesStorage {

    /**
     * Theme storage key that is currently selected.
     * @see com.insiderser.android.template.model.Theme
     */
    @get:WorkerThread
    var selectedTheme: String?

    /**
     * [LiveData] with up-to-date theme storage key that is currently selected.
     * @see com.insiderser.android.template.model.Theme
     */
    val selectedThemeObservable: LiveData<String>
}

/**
 * Implementation of [AppPreferencesStorage] that uses [SharedPreferences] to store the data.
 */
@Singleton
class AppPreferencesStorageImpl @Inject constructor(context: Context) : AppPreferencesStorage {

    // Lazy to prevent IO on the main thread
    private val sharedPreferences: Lazy<SharedPreferences> = lazy {
        context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).apply {
            registerOnSharedPreferenceChangeListener(onChangeListener)
        }
    }

    // SharedPreferences doesn't store a strong reference to listeners,
    // so make this a field to make sure it isn't GCed.
    private val onChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            KEY_THEME -> _selectedThemeLiveData.value = selectedTheme
        }
    }

    override var selectedTheme: String? by StringPreference(sharedPreferences, KEY_THEME)

    private val _selectedThemeLiveData = MutableLiveData<String>()
    override val selectedThemeObservable: LiveData<String> get() = _selectedThemeLiveData

    companion object {

        private const val PREFS_NAME = "preferences"

        private const val KEY_THEME = "selected_theme"
    }
}

/**
 * Property delegate that manages a single entry in [SharedPreferences].
 */
@VisibleForTesting
class StringPreference(
    private val sharedPreferences: Lazy<SharedPreferences>,
    private val preferenceKey: String,
    private val defaultValue: String? = null
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        sharedPreferences.value.getString(preferenceKey, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        sharedPreferences.value.edit {
            putString(preferenceKey, value)
        }
    }
}
