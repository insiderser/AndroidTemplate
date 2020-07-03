package com.insiderser.template.init

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface InitializerEntryPoint {

    fun inject(initializer: ThemeInitializer)

    companion object {

        @JvmStatic
        fun create(context: Context): InitializerEntryPoint {
            val applicationContext = context.applicationContext
            return EntryPointAccessors.fromApplication(
                applicationContext,
                InitializerEntryPoint::class.java
            )
        }
    }
}
