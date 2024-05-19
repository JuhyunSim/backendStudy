plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "fintech"
include("api")
include("consumer")
include("gateway")
include("css")
include("domain")
include("kafka")
