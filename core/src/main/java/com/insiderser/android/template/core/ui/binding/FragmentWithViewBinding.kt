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
package com.insiderser.android.template.core.ui.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * [Fragment] wrapper that uses [ViewBinding] to manage its views.
 *
 * Current binding can be accessed via [binding] and [requireBinding].
 *
 * @param B [ViewBinding] class that is used.
 * @see Fragment
 */
abstract class FragmentWithViewBinding<B : ViewBinding> : Fragment() {

    /**
     * A current [ViewBinding] that is being displayed to the user.
     * @see requireBinding
     */
    var binding: B? = null
        private set

    /**
     * Non-null [binding].
     * @throws IllegalStateException if called before [onCreateBinding] has been called.
     * @see binding
     */
    fun requireBinding(): B = binding ?: error("No binding created")

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = onCreateBinding(inflater, container)
        .also { binding = it }
        .root

    /**
     * Called to inflate Fragment's [ViewBinding].
     * @see onCreateView
     */
    @MainThread
    protected abstract fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): B

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindingCreated(requireBinding(), savedInstanceState)
    }

    /**
     * Called immediately after [onCreateBinding],
     * but before any saved state has been restored in to the view.
     *
     * @param binding The [ViewBinding] returned by [onCreateBinding].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @see onViewCreated
     */
    @MainThread
    protected open fun onBindingCreated(binding: B, savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Make sure we don't leak our views
        binding = null
    }
}
