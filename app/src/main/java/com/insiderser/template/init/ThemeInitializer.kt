package com.insiderser.template.init

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.startup.Initializer
import com.insiderser.template.core.domain.prefs.theme.ObservableThemeUseCase
import com.insiderser.template.core.domain.prefs.theme.Theme
import com.insiderser.template.core.domain.prefs.theme.toAppCompatNightMode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ThemeInitializer : Initializer<Unit> {

    @Inject
    lateinit var observableThemeUseCase: ObservableThemeUseCase

    override fun create(context: Context) {
        val entryPoint = InitializerEntryPoint.create(context)
        entryPoint.inject(this)

        GlobalScope.launch {
            observableThemeUseCase().collect { setAppTheme(it) }
        }
    }

    private fun setAppTheme(newTheme: Theme) {
        Timber.d("Setting app theme to $newTheme")
        AppCompatDelegate.setDefaultNightMode(newTheme.toAppCompatNightMode())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        HiltInitializer::class.java
    )
}
