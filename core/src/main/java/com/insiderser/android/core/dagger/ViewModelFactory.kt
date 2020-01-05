/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insiderser.android.core.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import timber.log.Timber

/**
 * [ViewModelProvider.Factory] that uses Dagger to create [ViewModel]s.
 */
class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    /**
     * Create [T] using Dagger.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("Creating instance of ${modelClass.name}")

        val found = creators.entries.find { modelClass.isAssignableFrom(it.key) }
        val creator = found?.value
            ?: throw NoVMProviderError(
                "Cannot create an instance of ${modelClass.name} using Dagger. " +
                    "Did you forget to add it into Dagger?"
            )
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException("Error occurred while creating ${modelClass.name}", e)
        }
    }
}

/**
 * An [Error] that indicates that no [Provider] for the given [ViewModel]
 * had been added into a Dagger graph.
 */
class NoVMProviderError(message: String) : Error(message)
