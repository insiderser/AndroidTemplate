package com.insiderser.template.init

import android.content.Context
import android.os.StrictMode
import androidx.startup.Initializer
import com.insiderser.template.BuildConfig

class StrictModeInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    // Doing anything expensive on the main thread is bad, so enforce that.
                    .penaltyDeath()
                    .build()
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
