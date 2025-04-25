import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    id("org.jetbrains.kotlin.plugin.compose")
//    kotlin("plugin.serialization") version "1.9.0"
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
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
//            implementation("net.sourceforge.tess4j:tess4j:5.8.0")  // Tesseract OCR for Java
//            implementation("org.slf4j:slf4j-simple:2.0.12")      // Required by Tess4J
            implementation("org.apache.poi:poi-ooxml:5.2.3") // For DOCX files
            implementation("org.apache.pdfbox:pdfbox:2.0.27")// For PDF files
            implementation("org.apache.commons:commons-io:1.3.2")
            implementation("com.squareup.okhttp3:okhttp:4.9.3") // For making API calls
            implementation("org.json:json:20231013")
            implementation("org.apache.logging.log4j:log4j-core:2.20.0")
            implementation("org.apache.logging.log4j:log4j-api:2.20.0")
            implementation ("com.google.code.gson:gson:2.10")
//            implementation("com.google.ai.client.generativeai:generativeai:0.6.0") // Gemini AI
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // Coroutines
            implementation("io.ktor:ktor-client-core:2.0.0") // HTTP client for API calls
            implementation("io.ktor:ktor-client-cio:2.0.0")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            implementation("com.squareup.okio:okio:3.2.0")
            implementation("org.apache.tika:tika-core:2.9.1")
            implementation("org.apache.tika:tika-parsers-standard-package:2.9.1")
        }
    }
}

android {
    namespace = "com.tanveer.kotlinproject"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.tanveer.kotlinproject"
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
        mainClass = "com.tanveer.kotlinproject.MainKt"
        jvmArgs += listOf("-Xmx2g", "-Dfile.encoding=UTF-8")

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.tanveer.kotlinproject"
            packageVersion = "1.0.0"

            // Corrected jvmArgs configuration
            modules("java.sql", "java.desktop")
            appResourcesRootDir.set(project.layout.projectDirectory.dir("lib"))
        }
    }
}