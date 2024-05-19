plugins {
}

version = "0.0.1-SNAPSHOT"

dependencies {

    //spring-boot-web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    runtimeOnly("com.mysql:mysql-connector-j")
    implementation(project(":domain"))
    implementation(project(":kafka"))

    //test
    testImplementation("io.mockk:mockk:1.13.11")
    runtimeOnly("com.h2database:h2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.+")


    //AOP
    implementation("org.springframework.boot:spring-boot-starter-aop")

    //logger
    implementation("io.github.microutils:kotlin-logging:3.0.4")

    //redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

}
