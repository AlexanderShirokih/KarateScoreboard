plugins {
    kotlin("jvm")
}

group = "scoreboards"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":core"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.5.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")

}