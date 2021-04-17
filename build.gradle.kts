import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    application
}

group = "me.pbeagan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.5"
        apiVersion = "1.5"
    }
}

application {
    mainClassName = "MainKt"
}