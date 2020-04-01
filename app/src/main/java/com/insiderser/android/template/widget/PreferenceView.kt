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

package com.insiderser.android.template.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.insiderser.android.template.R
import com.insiderser.android.template.databinding.PreferenceViewBinding

/**
 * A view that represents a single user preference.
 * Similar to Preference from Android's preferences library.
 */
@Suppress("MemberVisibilityCanBePrivate")
class PreferenceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    private val binding = PreferenceViewBinding.inflate(LayoutInflater.from(context), this)

    /** Current title of the preference. */
    var title: CharSequence?
        get() = binding.title.text
        set(value) {
            binding.title.text = value
        }

    /** Short summary of the current value of the preference. */
    var summary: CharSequence?
        get() = binding.summary.text
        set(value) {
            binding.summary.text = value
            binding.summary.isVisible = !value.isNullOrEmpty()
        }

    init {
        orientation = VERTICAL

        context.withStyledAttributes(attrs, R.styleable.PreferenceView) {
            title = getText(R.styleable.PreferenceView_title)
            summary = getText(R.styleable.PreferenceView_summary)
        }
    }

    override fun setOrientation(orientation: Int) {
        require(orientation == VERTICAL) { "Preference can only be laid out vertically" }
        super.setOrientation(orientation)
    }
}
