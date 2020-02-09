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
package com.insiderser.android.template.core.dagger

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Qualifier

/**
 * Similarly to [ViewModelFactory], [SavedStateViewModelFactory] creates [ViewModel]
 * using dagger, but it supports [ViewModel]s with [SavedStateHandle].
 *
 * Use Assisted Inject to get [SavedStateHandle] into a dagger graph.
 * See [this post](https://proandroiddev.com/connecting-the-dots-44d8fa79f14)
 * for how to implement it.
 */
class SavedStateViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>,
        Provider<AssistedViewModelFactory<out ViewModel>>>,
    owner: SavedStateRegistryOwner,
    @DefaultArgs defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        Timber.v("Creating instance of ${modelClass.name}")

        val found = creators.entries.find { modelClass.isAssignableFrom(it.key) }
        val creator = found?.value
            ?: throw NoVMProviderError(
                "Cannot create an instance of ${modelClass.name} using Dagger. " +
                    "Did you forget to add it into Dagger?"
            )
        try {
            return creator.get().create(handle) as T
        } catch (e: Exception) {
            throw RuntimeException("Error occurred while creating ${modelClass.name}", e)
        }
    }
}

/**
 * A factory that can create [T] [ViewModel]. Should be used with AssistedInject to create
 * a [ViewModel] that uses saved state.
 *
 * Implementations should be interfaces that are located inside the target [ViewModel] class
 * and annotated with `@AssistedInject.Factory`.
 */
interface AssistedViewModelFactory<T : ViewModel> {
    /** Create [ViewModel] with the given [SavedStateHandle]. */
    fun create(savedStateHandle: SavedStateHandle): T
}

/**
 * Dagger qualifier for default arguments for saved state view model.
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultArgs

/**
 * Dagger qualifier that tells dagger that you need a [androidx.lifecycle.ViewModelProvider.Factory]
 * that can create [ViewModel] that uses saved state.
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class SavedStateFactory
