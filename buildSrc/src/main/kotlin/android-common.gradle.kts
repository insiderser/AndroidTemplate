import com.android.build.gradle.BaseExtension
import com.insiderser.template.buildSrc.Versions

fun android(action: BaseExtension.() -> Unit) {
    val androidExtension = extensions.findByType(BaseExtension::class)
        ?: throw Error("Should be called after applying android plugin")
    androidExtension.action()
}

android {
    compileSdkVersion(Versions.Sdk.compile)
    buildToolsVersion(Versions.buildToolsVersion)

    defaultConfig {
        targetSdkVersion(Versions.Sdk.target)
        minSdkVersion(Versions.Sdk.min)

        versionName = Versions.versionName
        versionCode = Versions.versionCode

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        targetCompatibility = Versions.jvmTarget
        sourceCompatibility = Versions.jvmTarget
    }

    sourceSets {
        named("test") {
            java.srcDir("src/sharedTest/java")
        }
        named("androidTest") {
            java.srcDir("src/sharedTest/java")
        }
    }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }

    // FIXME: Android Studio bug?
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/*.kotlin_module")
    }
}
