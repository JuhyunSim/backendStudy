plugins {
    id("org.jetbrains.kotlin.plugin.jpa") version "2.0.0-RC3"
}

version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.projectlombok:lombok")

    runtimeOnly("com.mysql:mysql-connector-j")
}