import org.gradle.internal.impldep.org.jsoup.nodes.Document.OutputSettings.Syntax.html

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.detekt)

}
detekt {
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true

    source = files("src/main/java", "src/main/kotlin") // Android ho to neeche waala
    // source = files("app/src/main/java", "app/src/main/kotlin")

    reports {
        html.required.set(true)
        txt.required.set(true)
        xml.required.set(false)
    }
}
