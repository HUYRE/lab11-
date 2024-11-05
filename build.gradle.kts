// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Repositorio de Google
        mavenCentral() // Agregar este repositorio
    }
    dependencies {
        // Aquí van las dependencias del buildscript
        classpath("com.android.tools.build:gradle:7.4.0") // Asegúrate de que la versión sea correcta
        classpath("com.google.gms:google-services:4.4.2") // Asegúrate de que la versión sea correcta
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false
}