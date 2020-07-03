package com.insiderser.template.init

import android.content.Context
import androidx.startup.Initializer

class HiltInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // This will lazily initialize ApplicationComponent.
        InitializerEntryPoint.create(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        StrictModeInitializer::class.java,
        TimberInitializer::class.java,
    )
}
