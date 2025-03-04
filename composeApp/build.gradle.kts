import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.ksp)
//    alias(libs.plugins.ktorfit)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
//            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)


            implementation(project(":RtText"))
//            implementation(libs.androidx.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.runtime.compose)
            // Add KMP dependencies here
//            implementation(libs.kotlinx.datetime)
//            implementation(libs.kotlinx.coroutines.core)
//
//            // toast
//            implementation(libs.sonner)
//
//            // Navigator
//            implementation(libs.bundles.vovager)
//
//            implementation(project.dependencies.platform(libs.koin.bom))
//            implementation(libs.koin.core)
//            implementation(libs.koin.compose)
//            // Koin Annotations
//            api(libs.koin.annotations)
//
//
//            //ktorfit
//            implementation(libs.ktorfit.lib)
//            implementation(libs.ktorfit.converters.response)
////            // Only needed when you want to use Kotlin Serialization
//            implementation(libs.bundles.kotlinx.serialization)
//            implementation(libs.ktor.client.serialization)
//            implementation(libs.ktor.client.content.negotiation)
//            implementation(libs.ktor.serialization.kotlinx.json)
//
//            // datastore
//            implementation(libs.androidx.datastore.core.okio)
//            implementation(libs.okio)
//            implementation(libs.kotlin.stdlib)
//
//            implementation(libs.composeIcons.feather)
//
//            implementation(libs.paging.common)
//            implementation(libs.paging.compose.common)
//
//            implementation(libs.composeSettings.ui)
//            implementation(libs.composeSettings.ui.extended)
//
//
//            // 只解析html 字符串
//            implementation(libs.ksoup)
//
//            implementation(libs.compose.webview.multiplatform)
//
//            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.5.1")
//
//            implementation("com.github.skydoves:landscapist-coil3:2.4.7")

//            implementation("io.github.ltttttttttttt:ComposeViews:1.7.0")//this,比如1.6.11.2
        }
        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.kotlinx.coroutines.swing)
        }
        iosMain.dependencies {
//            implementation("io.ktor:ktor-client-ios:$ktorVersion")
            // Coroutines Native
//            implementation(libs.kotlinx.coroutines.core.ios)

//            implementation("app.cash.paging:paging-runtime-uikit:3.3.0-alpha02-0.5.1")
        }
    }

//    // KSP Common sourceSet
//    sourceSets.named("commonMain").configure {
//        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
//    }
}


// KSP Tasks
dependencies {
//    kspCommonMainMetadata(libs.koin.ksp.compiler)
//    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
//    add("kspAndroid", libs.koin.ksp.compiler)
//    add("kspIosX64", libs.koin.ksp.compiler)
//    add("kspIosArm64", libs.koin.ksp.compiler)
//    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

// Trigger Common Metadata Generation from Native tasks
//project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
//    if(name != "kspCommonMainKotlinMetadata") {
//        dependsOn("kspCommonMainKotlinMetadata")
//    }
//}

android {
    namespace = "cn.changjiahong.sample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    defaultConfig {
        applicationId = "cn.changjiahong.sample"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "cn.changjiahong.sample.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cn.changjiahong.sample"
            packageVersion = "1.0.0"
        }
    }
}
