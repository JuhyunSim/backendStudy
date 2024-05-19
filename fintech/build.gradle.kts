import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23" apply false
}

group = "zerobase"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

allprojects {
    group = "com.zerobase"

    repositories {
        mavenCentral()
    }

}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")

        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

//project(":api") {
//    dependencies {
//        implementation(project(":kafka"))
//        implementation(project(":domain"))
//    }
//}
//
//project(":consumer") {
//    dependencies {
//        implementation(project(":kafka"))
//        implementation(project(":domain"))
//    }
//}
//
//project(":domain") {
//    val jar: Jar by tasks
//    val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
//
//    bootJar.enabled = false
//    jar.enabled = true
//}
//
//project(":kafka") {
//    val jar: Jar by tasks
//    val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
//
//    bootJar.enabled = false
//    jar.enabled = true
//}