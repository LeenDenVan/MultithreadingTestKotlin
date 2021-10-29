import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

group = "xyz.leendvan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "11"
    sdk = "D:\\libraries\\javafx-sdk-17.0.0.1"
    modules("javafx.controls","javafx.fxml")
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.projectlombok:lombok:1.18.12")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("org.json:json:20210307")
    implementation("org.jsoup:jsoup:1.14.3")
}


tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}