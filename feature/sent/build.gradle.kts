@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.susu.android.feature.compose)
}

android {
    namespace = "com.susu.feature.sent"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
