plugins {
    kotlin("jvm") version "1.5.21"
    application
}

group = "ru.aleshi"
version = "1.0.3"

repositories {
    mavenCentral()
}

application {
    mainClassName = "ru.aleshi.scoreboards.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    
    implementation(project(":core"))
    implementation(project(":swing-ui"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

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