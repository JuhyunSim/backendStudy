plugins {
    kotlin("plugin.serialization") version "1.9.20"
}

version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")

}