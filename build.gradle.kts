plugins {
    kotlin("jvm") version "1.3.72"
    application
}

group = "ru.aleshi"
version = "1.0.1"

repositories {
    mavenCentral()
}

application {
    mainClassName = "ru.aleshi.scoreboards.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")

    testImplementation("junit", "junit", "4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}