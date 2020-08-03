plugins {
    kotlin("jvm") version "1.3.72"
    application
}

group = "ru.aleshi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClassName = "ru.aleshi.scoreboards.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.reactivex.rxjava3:rxjava:3.0.5")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")

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