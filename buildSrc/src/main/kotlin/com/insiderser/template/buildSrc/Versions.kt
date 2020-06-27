package com.insiderser.template.buildSrc

import org.gradle.api.JavaVersion

object Versions {
    const val versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    const val versionCode = 1

    object Sdk {
        const val target = 30
        const val compile = target
        const val min = 23
    }

    @JvmField
    val jvmTarget = JavaVersion.VERSION_1_8

    const val buildToolsVersion = "30.0.0"
}
