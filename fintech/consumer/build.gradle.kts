plugins {}
version = "0.0.1-SNAPSHOT"

dependencies {

    //kafka
    implementation(project(":kafka"))
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    //data
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    implementation(project(":domain"))

    //web
    implementation("org.springframework.boot:spring-boot-starter-web")

}
